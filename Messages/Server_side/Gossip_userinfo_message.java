package Messages.Server_side;

import Server.Structures.Gossip_user;

/**
 * Messaggio di informazioni su un utente
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_userinfo_message extends Gossip_success_message {
	
	public static final String USERINFO = "userinfo"; 

	@SuppressWarnings("unchecked")
	public Gossip_userinfo_message(Gossip_success_message.successMsg m, Gossip_user u) {
		super(m);
		
		jsonMsg.put(Gossip_userinfo_message.USERINFO,  u.toJSONObject());
	}


}