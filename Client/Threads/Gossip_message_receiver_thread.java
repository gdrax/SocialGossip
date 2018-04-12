		package Client.Threads;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

import javax.swing.JTextArea;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_client_message;
import Messages.Server_side.Gossip_connection_info_message;
import Messages.Server_side.Gossip_server_message;
import Server.Gossip_config;
import Server.PortNotFoundException;
import Server.Structures.Gossip_user;

/**
 * Thread che si occupa di ricevere le notifiche dal server
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_message_receiver_thread extends Thread {
	
	private InetAddress address;
	private Gossip_user user;
	private int port;
	private Gossip_main_listener main;
	private Socket messageSocket;
	private DataInputStream input;
	private DataOutputStream output;
	
	public Gossip_message_receiver_thread(InetAddress a, int p, Gossip_main_listener m, Gossip_user u) {
		super();
		
		if (a == null || m == null || u == null)
			throw new NullPointerException();
		
		address = a;
		user = u;
		port = p;
		main = m;
		
		try {
			messageSocket = new Socket(address, port);
			input = new DataInputStream(new BufferedInputStream(messageSocket.getInputStream()));
			output = new DataOutputStream(messageSocket.getOutputStream());
			
			//segnalo al server che sono un therad che riceve solo notifiche
			output.writeUTF(new Gossip_client_message(Gossip_client_message.Op.LISTENING_OP, user.getName()).getJsonString());
			
			//leggo risposta e verifico che sia di successo
			String reply = input.readUTF();
			JSONObject JSONreply = Gossip_parser.getJsonObject(reply);
			switch(Gossip_parser.getServerReplyType(JSONreply)) {
			
				case SUCCESS_OP:
					break;
					
				default:
					main.errorMessage("Risposta del server sconosciuta");
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
		
	public void run() {
		while (true) {
			try {
				
				//leggo notifica
				String notification = input.readUTF();
				
				//interpreto notifica
				parseNotification(notification);
				
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	private void parseNotification(String notification) throws IOException {
		
		try {
			JSONObject JSONReply = Gossip_parser.getJsonObject(notification);
			
			if (Gossip_parser.getServerReplyType(JSONReply) == Gossip_server_message.Op.NOTIFICATION_OP)
				switch(Gossip_parser.getNotificationType(JSONReply)) {
					
				//notifica di messaggio in arrivo
					case NEWMSG:
						String friend = Gossip_parser.getNotificationSender(JSONReply);
						JTextArea textArea = main.getMessageArea(friend);
						if (textArea != null) {
							String receiver = Gossip_parser.getNotificationSender(JSONReply);
							String text = Gossip_parser.getNotificationText(JSONReply);
							//lo stampo nell'area della chat
							if (receiver != null && text != null)
								textArea.append("["+friend+"]: "+text+"\n");
							else
								main.errorMessage("Errore lettura nuovo messaggio");
						}
						break;
					
					//notifica di file in arrivo
					case NEWFILE:
						//recupero info dalla notifica
						String sender = Gossip_parser.getNotificationSender(JSONReply);
						String filename = Gossip_parser.getFilename(JSONReply);
						if (filename == null)
							return;
						//apro socket
						ServerSocketChannel newSocket = ServerSocketChannel.open();
						
						//trovo una porta libera
						int port = Gossip_config.findPort();
						
						//binding
						newSocket.bind(new InetSocketAddress(port));
						
						//informo l'altro client che sono pronto ricevere
						Gossip_connection_info_message reply = new Gossip_connection_info_message("localhost", port, user.getName());
						output.writeUTF(reply.getJsonString());
						
						//avvio thrad per ricevere il file
						new Gossip_file_receiver(newSocket, filename).start();
						
						main.getMessageArea(sender).append("["+sender+"]: [FILE] "+filename+"\n");;
						break;
				}
			else
				main.errorMessage("Risposta del server sconosciuta");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (PortNotFoundException e) {
			//invio messaggio di errore
			output.writeUTF(new Gossip_server_message(Gossip_server_message.Op.FAIL_OP).getJsonString());
			e.printStackTrace();
		}
	}

}
