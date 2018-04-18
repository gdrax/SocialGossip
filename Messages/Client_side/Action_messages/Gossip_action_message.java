package Messages.Client_side.Action_messages;

import Messages.Client_side.Gossip_client_message;

/**
 * Azione di interazione con altri utenti:
 * 
 * LOOKUP: ricerca utente
 * FRIENDSHIP: richiesta di amicizia
 * RM_FRINEDSHIP: richiesta di rimozione amicizia
 * MSG_FRIEND: messaggio testuale a amico (vedi Gossip_action_msg_message.java)
 * FILE_FRIEND: messaggio con nome di file a amico (vedi Gossip_action_file_message.java)
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_action_message extends Gossip_client_message {
	public enum Action {LOOKUP_OP, FRIENDSHIP_OP, RM_FRIENDSHIP_OP, MSG_FRIEND_OP, FILE_FRIEND_OP};
	public static final String ACTION = "action";
	public static final String RECEIVER = "receiver";
		
	@SuppressWarnings("unchecked")
	public Gossip_action_message(Gossip_action_message.Action action, String sender, String receiver) {
		super(Gossip_client_message.Op.ACTION_OP, sender);
		
		jsonMsg.put(Gossip_action_message.ACTION,  action.name());
		jsonMsg.put(Gossip_action_message.RECEIVER,  receiver);
		}
}
