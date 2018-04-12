package Messages.Client_side;

/**
 * Azione di interazione con una chat
 * 
 * CREATE_CHAT: richiesta di creazione di una nuova chat
 * ADDME: richiesta di iscrizione ad una chat
 * REMOVEME: richiesta di disiscrizione ad una chat
 * CLOSE_CHAT: richiesta di eliminazione di una chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_chat_message extends Gossip_client_message {

	public enum ChatOp {CREATE_CHAT_OP, ADDME_OP, REMOVEME_OP, CLOSE_CHAT_OP};
	public static final String CHAT_ACTION = "chat_op";
	public static final String CHAT = "chatname";
	
	@SuppressWarnings("unchecked")
	public Gossip_chat_message(Gossip_chat_message.ChatOp chatop, String chatname, String sender) {
		
		super(Gossip_client_message.Op.CHAT_OP, sender);
		jsonMsg.put(Gossip_chat_message.CHAT_ACTION,  chatop.name());
		jsonMsg.put(Gossip_chat_message.CHAT,  chatname);
	}
}
