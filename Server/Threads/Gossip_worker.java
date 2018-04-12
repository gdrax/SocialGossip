package Server.Threads;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Messages.Gossip_message;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_action_message;
import Messages.Client_side.Gossip_chat_message;
import Messages.Client_side.Gossip_client_message;
import Messages.Server_side.Gossip_fail_message;
import Messages.Server_side.Gossip_file_notification_message;
import Messages.Server_side.Gossip_info_login_message;
import Messages.Server_side.Gossip_info_registration_message;
import Messages.Server_side.Gossip_server_message;
import Messages.Server_side.Gossip_success_message;
import Messages.Server_side.Gossip_text_notification_message;
import Messages.Server_side.Gossip_userinfo_message;
import Server.Gossip_translator;
import Server.PortNotFoundException;
import Server.Graph.NodeAlreadyException;
import Server.Graph.NodeNotFoundException;
import Server.Structures.ChatAlreadyException;
import Server.Structures.ChatNotFoundException;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_data;
import Server.Structures.Gossip_user;

/**
 * Thread del pool che si occupa di servire i client
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_worker implements Runnable {
	
	private Socket clientSocket;
	private Gossip_data data;
	private boolean closeThread;
	
	public Gossip_worker(Socket s, Gossip_data d) {
		clientSocket = s;
		data = d;
		closeThread = false;
	}
	
	/**
	 * Invia una risposta sul socket del client
	 * 
	 * @param reply: risposta da inviare
	 * @param client: stream di output verso il client
	 */
	private void sendReply(Gossip_message reply, DataOutputStream clientStream) {
		try {
			System.out.println("Invio risposta: "+reply.getJsonString());
			clientStream.writeUTF(reply.getJsonString());
		} catch (IOException e) {}
	}
	
	/**
	 * Fa il parsing di una stringa
	 * 
	 * @param request: stringa letta dal socket
	 * @param client: stream di output verso l'utente
	 * @return richiesta sotto forma di JSONObject, null in caso di errore
	 */
	private JSONObject parseRequest(String request, DataOutputStream clientStream) {
		try {
			//restitiuisco il JSONObject
			return Gossip_parser.getJsonObject(request);
		} catch (ParseException e) {
			//se la richiesta non è un JSONObject rispondo con messaggio di errore
			e.printStackTrace();
			sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.UNKNOWN_REQUEST), clientStream);
		}
		return null;
	}
	
	
	/**
	 * Esegue un'azione tra client
	 * 
	 * @param request: oggetto JSON contenente la richiesta
	 * @param client: stream dove scrivere la risposta
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void makeAction(JSONObject request, DataOutputStream clientStream) throws IOException, ParseException {
		Gossip_action_message.Action action = Gossip_parser.getClientAction(request);
		String sender = Gossip_parser.getClientName(request);
		String receiver = Gossip_parser.getClientReceiver(request);

		try {
			switch(action) {
			
				case LOOKUP_OP: {
					if (data.searchUser(receiver) == true)
						sendReply(new Gossip_userinfo_message(Gossip_success_message.successMsg.USERINFO, data.getUser(receiver)), clientStream);
				} break;
				
				case FRIENDSHIP_OP: {
					if (!data.addFriendship(sender, receiver))
						sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.FRIENDALREADY), clientStream);
					else
						sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
				} break;
				
				case RM_FRIENDSHIP_OP: {
					if (!data.removeFriendship(sender,  receiver))
						sendReply(new Gossip_server_message(Gossip_server_message.Op.FAIL_OP), clientStream);
					else
						sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
				} break;
				
				case MSG_FRIEND_OP: {
					//non è permesso scambiare messaggi tra utenti non amici
					if (!data.getFriends(sender).contains(data.getUser(receiver)))
						sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.OPNOTALLOWED), clientStream);
					else {
						Gossip_text_notification_message notification;
						String text = Gossip_parser.getText(request);
						String senderLang = Gossip_parser.getLang(data.getUser(sender).getLanguage());
						String receiverLang = Gossip_parser.getLang(data.getUser(receiver).getLanguage());
						//traduco testo
						String translatedText = Gossip_translator.translate(text, senderLang, receiverLang);
						Socket receiver_socket;
						//se la traduzione mi restituisce null, invio il testo non tradotto
						if (translatedText == null)
							notification = new Gossip_text_notification_message(sender, text);
						else
							notification = new Gossip_text_notification_message(sender, translatedText);
						try {
							//recupero il socket per le notifiche al client
							receiver_socket = data.getUser(receiver).getMessageSocket();
							synchronized(receiver_socket) {
							
								//invio notifica
								sendReply(notification, new DataOutputStream(receiver_socket.getOutputStream()));
								//invio risposta
								sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
							}
						} catch (FriendOfflineException e) {
							//amico offline
							sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.FRIENDOFFLINE), clientStream);
						}
					}
				} break;
				
				case FILE_FRIEND_OP: {
					//non è permesso inviare file a utenti che non sono amici
					if (!data.getFriends(sender).contains(data.getUser(receiver)))
						sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.OPNOTALLOWED), clientStream);
					else {
						String filename = Gossip_parser.getFilename(request);
						Socket receiver_socket;
						Gossip_file_notification_message notification = new Gossip_file_notification_message(sender, filename);
						try {
							//recupero socket per le notificihe
							receiver_socket = data.getUser(receiver).getMessageSocket();
							synchronized (receiver_socket) {
								sendReply(notification, new DataOutputStream(receiver_socket.getOutputStream()));
							
								//ridirigo quello che scrive il receiver verso il mittente (hostname e porta per ricevere il file)
								clientStream.writeUTF(new DataInputStream(new BufferedInputStream(receiver_socket.getInputStream())).readUTF());
							}
						} catch (FriendOfflineException e) {
							//friend offline
							sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.FRIENDOFFLINE), clientStream);
						}
					}
				} break;
			}
		} catch (NodeNotFoundException e) {
			//utente non registrato
			sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.NICKUNKNOWN), clientStream);
		}
	}
	
	/**
	 * Esegue un'azione su chat
	 * 
	 * @param request: oggetto JSON contenente la richiesta
	 * @param client: stream dove scrivere la risposta
	 * @throws IOException
	 */
	private void makeChatOp(JSONObject request, DataOutputStream clientStream) throws IOException {
		Gossip_chat_message.ChatOp op = Gossip_parser.getChatAction(request);
		String sender = Gossip_parser.getClientName(request);
		String chatname = Gossip_parser.getChatName(request);
		try {
			switch(op) {
				case CREATE_CHAT_OP: {
					data.addChat(chatname, sender);
					sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
				} break;
				
				case ADDME_OP: {
					boolean ret = data.addMember(chatname,  sender);
					if (ret == false)
						sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.NICKALREADY), clientStream);
					else
						sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
				} break;
				
				case REMOVEME_OP: {
					data.removeMember(chatname,  sender);
					sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
				} break;
				
				case CLOSE_CHAT_OP: {
					if (data.removeChat(chatname,  sender) == true)
						sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
					else
						sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.OPNOTALLOWED), clientStream);
				} break;
			}
		}//utente non registrato
		 catch (NodeNotFoundException e) {
			sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.NICKUNKNOWN), clientStream);
		 }
		 //nome chat già in uso
		 catch (ChatAlreadyException e) {
			 sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.CHATALREADY), clientStream);
		 }
		 //chat non esistente
		 catch (ChatNotFoundException e) {
			 sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.CHATUNKNOWN), clientStream);
		 }
		catch (UnknownHostException e) {
			e.printStackTrace();
			sendReply(new Gossip_server_message(Gossip_server_message.Op.FAIL_OP), clientStream);
		}
		//errore nella creazione di una chat
		catch (PortNotFoundException e) {
			sendReply(new Gossip_server_message(Gossip_server_message.Op.FAIL_OP), clientStream);
		}
	}
	
	/**
	 * Esegue una richiesta del client
	 * 
	 * @param request: oggetto JSON contenente la richiesta
	 * @param client: stream dove scrivere la risposta
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private void makeReply(JSONObject request, DataOutputStream clientStream) throws ParseException, IOException {
		Gossip_client_message.Op op = Gossip_parser.getClientOp(request);
		String sender = Gossip_parser.getClientName(request);
		try {
				switch(op) {
			
				case REGISTER_OP: {
					String password = Gossip_parser.getPassword(request);
					String language = Gossip_parser.getLanguage(request);
					data.addUser(sender, password,  language);
					//recupero la lista delle chat
					ArrayList<Gossip_chat> chatlist = data.getChats();
					//invio dati di registrazione
					sendReply(new Gossip_info_registration_message(chatlist, data.getUser(sender)), clientStream);
				} break;
				
				case LOGIN_OP: {
					String password = Gossip_parser.getPassword(request);
					if (data.getUser(sender).getPassword().equals(password)) {
						//imposto l'utente come online
						data.setStatus(sender, true);
						//recupero la lista degli amici e quella delle chat
						ArrayList<Gossip_user> friendlist = data.getFriends(sender);
						ArrayList<Gossip_chat> chatlist = data.getChats();
						//invio dati di login
						sendReply(new Gossip_info_login_message(friendlist, chatlist, data.getUser(sender)), clientStream);
					}
					else
						//password errata
						sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.WRONGPASSWORD), clientStream);
				} break;
					
				case LOGOUT_OP: {
					data.setStatus(sender, false);
					sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
				} break;
					
				case ACTION_OP:
					makeAction(request, clientStream);
					break;
					
				case LISTENING_OP:
					data.getUser(sender).setMessageSocket(clientSocket);
					//è un socket solo per le notifiche, faccio chiudere il worker
					closeThread = true;
					sendReply(new Gossip_server_message(Gossip_server_message.Op.SUCCESS_OP), clientStream);
					break;
					
				case CHAT_OP:
					//operazione di interazione con chatroom
					makeChatOp(request, clientStream);
					break;
					
				default:
					sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.UNKNOWN_REQUEST), clientStream);
					break;
			}
		} catch (NodeAlreadyException e) {
			//username già in uso
			sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.NICKALREADY), clientStream);
		} catch (NodeNotFoundException e) {
			//utente non registrato
			sendReply(new Gossip_fail_message(Gossip_fail_message.failMsg.NICKUNKNOWN), clientStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		JSONObject JSONrequest = null;
		
		try {
			//recupero stream di input e output dal socket
			DataInputStream input = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
	
			while(true) {
				
				System.out.println("Attendo richiesta");
				//leggo una richiesta
				String request = input.readUTF();
				
				System.out.println("Richiesta letta: "+request);
				
				//parsing della richiesta
				JSONrequest = parseRequest(request, output);
				if (JSONrequest == null)
					continue;
				//eseguo la richiesta
				makeReply(JSONrequest, output);
								
				//se è un socket per le notifiche, chiudo il thread
				if (closeThread)
					break;
				
			}
		} catch (IOException e) {
			System.out.println("Utente disconnesso");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
