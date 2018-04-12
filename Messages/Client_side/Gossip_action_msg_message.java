package Messages.Client_side;

/**
 * Messaggio di testo tra amici
 * 
 * @author Gioele Bertoncini
 */
public class Gossip_action_msg_message extends Gossip_action_message {
	
	public static final String TEXT = "text";
	
	@SuppressWarnings("unchecked")
	public Gossip_action_msg_message(String text, String sender, String receiver) {
		super(Gossip_action_message.Action.MSG_FRIEND_OP, sender, receiver);
		
		jsonMsg.put(Gossip_action_msg_message.TEXT,  text);
	}
}