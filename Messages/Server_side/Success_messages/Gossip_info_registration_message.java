package Messages.Server_side.Success_messages;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Messaggio di informazioni per l'utente appena registrato
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_info_registration_message extends Gossip_success_message {

	public static final String MYINFO = "myinfo";
	public static final String CHATLIST = "chatlist";
	
	@SuppressWarnings("unchecked")
	public Gossip_info_registration_message(ArrayList<Gossip_chat> c, Gossip_user u) {
		super(Gossip_success_message.successMsg.REGISTRATION);

		if (u == null)
			throw new NullPointerException();
		
		jsonMsg.put(Gossip_info_registration_message.MYINFO,  u.toJSONObject());
		
		//inserisco la lista delle chat disponibili
		if (c != null) {
			JSONArray chatList = new JSONArray();
			for (Gossip_chat chat: c) {
				chatList.add(chat.toJSONObject());
			}
			jsonMsg.put(Gossip_info_login_message.CHATLIST,  chatList);
		}
	}


}
