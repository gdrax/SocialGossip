package Messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Messages.Client_side.*;
import Messages.Client_side.Action_messages.*;
import Messages.Server_side.Gossip_server_message;
import Messages.Server_side.Fail_messages.Gossip_fail_message;
import Messages.Server_side.Notification_messages.*;
import Messages.Server_side.Success_messages.*;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Parser per tradurre i messaggi
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_parser {

	
	/**
	 * Ricava il JSONObject da una stringa
	 * 
	 * @param message: Stringa da tradurre
	 * @return JSONObject derivato dal parsing della stringa
	 * @throws ParseException: oggetto sconoscoiuto
	 */
	public static JSONObject getJsonObject(String message) throws ParseException {
		if (message == null) {
			throw new NullPointerException();
		}
		JSONParser parser = new JSONParser();
		
		return (JSONObject)parser.parse(message);
	}
	
	/*				CLIENT SIDE				*/
	
	/**
	 * Restituisce il tipo del messaggio
	 * 
	 * @param obj
	 * @return tipo del messaggio
	 */
	public static Gossip_message.Type getType(JSONObject obj) {

		if (obj == null) {
			throw new NullPointerException();
		}
		
		String type = (String)obj.get(Gossip_message.TYPE);
		if (type == null)
			return null;
		if (type.equals(Gossip_message.Type.CLIENT_MESSAGE.name()))
			return Gossip_message.Type.CLIENT_MESSAGE;
		if (type.equals(Gossip_message.Type.SERVER_MESSAGE.name()))
			return Gossip_message.Type.SERVER_MESSAGE;
		return null;
	}
	
	/**
	 * Restituisce l'operazione richiesta dal client
	 * 
	 * @param obj
	 * @return tipo di operazione del client
	 */
	public static Gossip_client_message.Op getClientOp(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		
		String op = (String)obj.get(Gossip_client_message.OP);
		if (op == null)
			return null;
		if (op.equals(Gossip_client_message.Op.REGISTER_OP.name()))
			return Gossip_client_message.Op.REGISTER_OP;
		if (op.equals(Gossip_client_message.Op.LOGIN_OP.name()))
			return Gossip_client_message.Op.LOGIN_OP;
		if (op.equals(Gossip_client_message.Op.LOGOUT_OP.name()))
			return Gossip_client_message.Op.LOGOUT_OP;
		if (op.equals(Gossip_client_message.Op.LISTFRIEND_OP.name()))
			return Gossip_client_message.Op.LISTFRIEND_OP;
		if (op.equals(Gossip_client_message.Op.ACTION_OP.name()))
			return Gossip_client_message.Op.ACTION_OP;
		if (op.equals(Gossip_client_message.Op.CHATLIST_OP.name()))
			return Gossip_client_message.Op.CHATLIST_OP;
		if (op.equals(Gossip_client_message.Op.CHAT_OP.name()))
			return Gossip_client_message.Op.CHAT_OP;
		if (op.equals(Gossip_client_message.Op.LISTENING_OP.name()))
			return Gossip_client_message.Op.LISTENING_OP;	
		return null;
	}
	
	/**
	 * @param obj
	 * @return nome dell'utente che ha inviato la richiesta
	 */
	public static String getClientName(JSONObject obj) {
		if (obj == null) 
			throw new NullPointerException();
		
		return (String)obj.get(Gossip_client_message.NICKNAME);
	}
	
	/**
	 * @param obj
	 * @return lingua preferita dall'utente
	 */
	public static String getLanguage(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		
		return (String)obj.get(Gossip_registration_message.LANGUAGE);
	}
	
	/**
	 * @param obj
	 * @return password dell'utente
	 */
	public static String getPassword(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		
		return (String)obj.get(Gossip_registration_message.PASSWORD);
	}
	
	/**
	 * Restituisce l'azione che il client vorrebbe compiere
	 * 
	 * @param obj
	 * @return tipo di azione
	 */
	public static Gossip_action_message.Action getClientAction(JSONObject obj) {
		if (obj == null)
			return null;
		
		String action = (String)obj.get(Gossip_action_message.ACTION);
		if (action.equals(Gossip_action_message.Action.LOOKUP_OP.name()))
			return Gossip_action_message.Action.LOOKUP_OP;
		if (action.equals(Gossip_action_message.Action.FRIENDSHIP_OP.name()))
			return Gossip_action_message.Action.FRIENDSHIP_OP;
		if (action.equals(Gossip_action_message.Action.RM_FRIENDSHIP_OP.name()))
			return Gossip_action_message.Action.RM_FRIENDSHIP_OP;
		if (action.equals(Gossip_action_message.Action.MSG_FRIEND_OP.name()))
			return Gossip_action_message.Action.MSG_FRIEND_OP;
		if (action.equals(Gossip_action_message.Action.FILE_FRIEND_OP.name()))
			return Gossip_action_message.Action.FILE_FRIEND_OP;
		return null;
	}
	
	/**
	 * @param obj
	 * @return nome dell'utente destinatario
	 */
	public static String getClientReceiver(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_action_message.RECEIVER);
	}
	
	/**
	 * @param obj
	 * @return testo del messaggio
	 */
	public static String getText(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_action_msg_message.TEXT);
	}
	
	/**
	 * @param obj
	 * @return nome di file
	 */
	public static String getFilename(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_action_file_message.FILENAME);
	}
	
	/**
	 * Restituisce l'azione su una chat che il client vorrebbe compiere
	 * 
	 * @param obj
	 * @return tipo di azione su chat
	 */
	public static Gossip_chat_message.ChatOp getChatAction(JSONObject obj) {
		if (obj == null)
			return null;
		
		String action = (String)obj.get(Gossip_chat_message.CHAT_ACTION);
		if (action.equals(Gossip_chat_message.ChatOp.CREATE_CHAT_OP.name()))
			return Gossip_chat_message.ChatOp.CREATE_CHAT_OP;
		if (action.equals(Gossip_chat_message.ChatOp.ADDME_OP.name()))
			return Gossip_chat_message.ChatOp.ADDME_OP;
		if (action.equals(Gossip_chat_message.ChatOp.REMOVEME_OP.name()))
			return Gossip_chat_message.ChatOp.REMOVEME_OP;
		if (action.equals(Gossip_chat_message.ChatOp.CLOSE_CHAT_OP.name()))
			return Gossip_chat_message.ChatOp.CLOSE_CHAT_OP;
		return null;
	}
	
	/**
	 * @param obj
	 * @return nome della chat
	 */
	public static String getChatName(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_chat_message.CHAT);
	}
	
	
	/*				SERVER SIDE				*/

	/**
	 * Restituisce la risposta del server
	 * 
	 * @param obj
	 * @return tipo di risposta del server
	 */
	public static Gossip_server_message.Op getServerReplyType(JSONObject obj) {
		if (obj == null)
			return null;
		
		String type = (String)obj.get(Gossip_server_message.OP);
		if (type.equals(Gossip_server_message.Op.SUCCESS_OP.name()))
			return Gossip_server_message.Op.SUCCESS_OP;
		if (type.equals(Gossip_server_message.Op.FAIL_OP.name()))
			return Gossip_server_message.Op.FAIL_OP;
		if (type.equals(Gossip_server_message.Op.NOTIFICATION_OP.name()))
			return Gossip_server_message.Op.NOTIFICATION_OP;
		return null;
	}
	
	/**
	 * Restituisce il messaggio di fallimento del server
	 * 
	 * @param obj
	 * @return tipo di messaggio di fallimento
	 */
	public static Gossip_fail_message.failMsg getFailType(JSONObject obj) {
		if (obj == null)
			return null;
		
		String type = (String)obj.get(Gossip_fail_message.FAILMSG);
		if (type.equals(Gossip_fail_message.failMsg.NICKUNKNOWN.name()))
			return Gossip_fail_message.failMsg.NICKUNKNOWN;
		if (type.equals(Gossip_fail_message.failMsg.NICKALREADY.name()))
			return Gossip_fail_message.failMsg.NICKALREADY;
		if (type.equals(Gossip_fail_message.failMsg.CHATALREADY.name()))
			return Gossip_fail_message.failMsg.CHATALREADY;
		if (type.equals(Gossip_fail_message.failMsg.CHATUNKNOWN.name()))
			return Gossip_fail_message.failMsg.CHATUNKNOWN;
		if (type.equals(Gossip_fail_message.failMsg.UNKNOWN_REQUEST.name()))
			return Gossip_fail_message.failMsg.UNKNOWN_REQUEST;
		if (type.equals(Gossip_fail_message.failMsg.OPNOTALLOWED.name()))
			return Gossip_fail_message.failMsg.OPNOTALLOWED;
		if (type.equals(Gossip_fail_message.failMsg.FRIENDOFFLINE.name()))
			return Gossip_fail_message.failMsg.FRIENDOFFLINE;
		if (type.equals(Gossip_fail_message.failMsg.FRIENDALREADY.name()))
			return Gossip_fail_message.failMsg.FRIENDALREADY;
		if (type.equals(Gossip_fail_message.failMsg.WRONGPASSWORD.name()))
			return Gossip_fail_message.failMsg.WRONGPASSWORD;
		return null;
	}
	
	/**
	 * Restituisce messaggio di successo del server
	 * 
	 * @param obj
	 * @return tipo di messaggio di successo
	 */
	public static Gossip_success_message.successMsg getSuccessType(JSONObject obj) {
		if (obj == null)
			return null;
		
		String type = (String)obj.get(Gossip_success_message.SUCCESSMSG);
		if (type.equals(Gossip_success_message.successMsg.LOGIN.name()))
			return Gossip_success_message.successMsg.LOGIN;
		if (type.equals(Gossip_success_message.successMsg.REGISTRATION.name()))
			return Gossip_success_message.successMsg.REGISTRATION;
		if (type.equals(Gossip_success_message.successMsg.USERINFO.name()))
			return Gossip_success_message.successMsg.USERINFO;
		if (type.equals(Gossip_success_message.successMsg.CONNECTIONINFO.name()))
			return Gossip_success_message.successMsg.CONNECTIONINFO;
		if (type.equals(Gossip_success_message.successMsg.CHATINFO.name()))
			return Gossip_success_message.successMsg.CHATINFO;
		return null;
	}
	
	/**
	 * Estrae la lista degli amici
	 * @param obj
	 * @return lista degli amici, null se non ce ne sono
	 */
	public static ArrayList<Gossip_user> getFriendList(JSONObject obj) {
		if (obj == null)
			return null;
		JSONArray list = (JSONArray) obj.get(Gossip_info_login_message.FRIENDLIST);
		if (list == null)
			return null;
		ArrayList<Gossip_user> friendlist = new ArrayList<Gossip_user>();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = list.iterator();
		while(iterator.hasNext()) {
			friendlist.add(buildUser(iterator.next()));
		}
		return friendlist;
	}
	
	/**
	 * Estrae i dati di un utente
	 * @param obj
	 * @return struttura di utente inizializzata
	 */
	private static Gossip_user buildUser(JSONObject obj) {
		String username = (String) obj.get(Gossip_user.NAME);
		boolean status = (boolean) obj.get(Gossip_user.STATUS);
		String language = (String) obj.get(Gossip_user.LANGUAGE);
		return new Gossip_user(username, status, language);
	}
	
	/**
	 * @param obj
	 * @return lista delle chatroom, null se non ce ne sono
	 */
	public static ArrayList<Gossip_chat> getChatList(JSONObject obj) {
		if (obj == null)
			return null;
		JSONArray list = (JSONArray) obj.get(Gossip_info_registration_message.CHATLIST);
		if (list == null)
				return null;
		ArrayList<Gossip_chat> chatlist = new ArrayList<Gossip_chat>();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = list.iterator();
		while(iterator.hasNext()) {
			chatlist.add(buildChat(iterator.next()));
		}
		return chatlist;
	}
	
	/**
	 * Estrae i dati di una chat
	 * @param obj
	 * @return struttura di una chat inizializzata
	 */
	private static Gossip_chat buildChat(JSONObject obj) {
		String chatname = (String) obj.get(Gossip_chat.CHATNAME);
		String multicastAddress = (String) obj.get(Gossip_chat.MULTICASTADDRESS);
		int multicastPort = (int)(long) obj.get(Gossip_chat.MULTICASTPORT);
		String chatAddress = (String) obj.get(Gossip_chat.CHATADDRESS);
		int chatPort = (int)(long) obj.get(Gossip_chat.CHATPORT);
		
		ArrayList<Gossip_user> members = new ArrayList<Gossip_user>();
		JSONArray list = (JSONArray) obj.get(Gossip_chat.MEMBERS);
		
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = list.iterator();
		while(iterator.hasNext()) {
			members.add(buildUser(iterator.next()));
			
		}
		try {
			return new Gossip_chat(chatname, InetAddress.getByName(multicastAddress), multicastPort, InetAddress.getByName(chatAddress), chatPort, members);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Restituisce i dati dell'utente ricercato
	 * @param obj
	 * @return struttura di un utente
	 */
	public static Gossip_user getUserFound(JSONObject obj) {
		JSONObject user = (JSONObject) obj.get(Gossip_userinfo_message.USERINFO);
		if (user == null)
			return null;
		return buildUser(user);
	}
	
	/**
	 * @param obj
	 * @return dati dell'utente appena registrato
	 */
	public static Gossip_user getUser(JSONObject obj) {
		JSONObject user = (JSONObject) obj.get(Gossip_info_registration_message.MYINFO);
		if (user == null)
			return null;
		return buildUser(user);
	}
	
	/**
	 * @param obj
	 * @return dati della chat appena creata
	 */
	public static Gossip_chat getChat(JSONObject obj) {
		JSONObject chat = (JSONObject)obj.get(Gossip_chat_info_message.CHATINFO);
		if (chat == null)
			return null;
		return buildChat(chat);
	}
	
	/**
	 * Restituisce messaggio di notifica dal server
	 * 
	 * @param obj
	 * @return tipo di messaggio di notifica
	 */
	public static Gossip_notification_message.notificationType getNotificationType(JSONObject obj) {
		if (obj == null)
			return null;
		
		String type = (String)obj.get(Gossip_notification_message.NOTIFICATION_TYPE);
		if (type.equals(Gossip_notification_message.notificationType.NEWFILE.name()))
			return Gossip_notification_message.notificationType.NEWFILE;
		if (type.equals(Gossip_notification_message.notificationType.NEWMSG.name()))
			return Gossip_notification_message.notificationType.NEWMSG;
		return null;
	}
	
	/**
	 * @param obj
	 * @return Mittente della notifica
	 */
	public static String getNotificationSender(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_notification_message.SENDER);
	}
	
	/**
	 * @param obj
	 * @return Nome del file in arrivo
	 */
	public static String getNotificationFile(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_file_notification_message.FILENAME);
	}
	
	/**
	 * @param obj
	 * @return Testo del messaggio in arrivo
	 */
	public static String getNotificationText(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_text_notification_message.TEXT);
	}
	
	/**
	 * @param obj
	 * @return: hostname di un client
	 */
	public static String getHostname(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_connection_info_message.HOSTNAME);
	}
	
	/**
	 * @param obj
	 * @return porta per connessione tra client
	 */
	public static int getPort(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		long port = (long) obj.get(Gossip_connection_info_message.PORT);
		return (int)port;
	}
	
	/**
	 * @param obj
	 * @return: nickname del mittente delle informazioni
	 */
	public static String getInfoSender(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		return (String)obj.get(Gossip_connection_info_message.INFOSENDER);
	}
	
	/**
	 * @param obj
	 * @return Testo tradotto, null in caso di oggetto malformato
	 */
	public static String getTransatedText(JSONObject obj) {
		if (obj == null)
			throw new NullPointerException();
		JSONObject obj2 = (JSONObject)obj.get("responseData");
		if (obj2 == null)
			return null;
		else
			return (String)obj2.get("translatedText");
	}
	
	/**
	 * @param language: nome inglese della lingua
	 * @return: lingua in valore ISO639
	 */
	public static String getLang(String language) {
		switch(language) {
			case "English":
				return "en";
			case "French":
				return "fr";
			case "German":
				return "de";
			case "Italian":
				return "it";
			case "Latin":
				return "la";
			case "Portoguese":
				return "pt";
			case "Spanish":
				return "es";
			default:
				return null;
		}
	}
}
