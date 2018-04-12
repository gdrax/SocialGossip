package Messages.Client_side;

import Messages.Gossip_message;

/**
 * Messaggio inviato da un client
 * 
 * REGISTER: richiesta di registrazione nuovo utente
 * LOGIN: richiesta di accesso
 * LOGOUT: richiesta di disconnessione
 * LISTFRIEND: richiesta di lista dei propri amici
 * ACTION: azione di interazione con altri utenti (vedi Gossip_action_message.java)
 * CHATLIST: richiesta della lista delle chat attive
 * CHAT: interazione con una chat (vedi Gossip_chat_message.java)
 * LISTENING: inviato dal socket per le notifiche per farlo salvare al server
 *  
 * @author Gioele Bertoncini
 *
 */
public class Gossip_client_message extends Gossip_message {
	public enum Op {REGISTER_OP, LOGIN_OP, LOGOUT_OP, LISTFRIEND_OP, ACTION_OP, CHATLIST_OP, CHAT_OP, LISTENING_OP};
	public static final String OP = "op";
	public static final String NICKNAME = "nickname";
	
	protected Gossip_client_message.Op op;
	
	@SuppressWarnings("unchecked")
	public Gossip_client_message(Gossip_client_message.Op o, String nickname) {
		super(Gossip_message.Type.CLIENT_MESSAGE);
		
		this.op = o;
		
		jsonMsg.put(Gossip_client_message.OP,  o.toString());
		jsonMsg.put(Gossip_client_message.NICKNAME,  nickname);
	}
}
