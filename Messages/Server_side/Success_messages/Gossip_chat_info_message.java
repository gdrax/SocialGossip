package Messages.Server_side.Success_messages;


/**
 * Messaggio di informazioni relative ad una chatroom
 *  
 * @author Gioele Bertoncini
 *
 */
import Server.Structures.Gossip_chat;

public class Gossip_chat_info_message extends Gossip_success_message {
	
	public static final String CHATINFO = "chatinfo";

	@SuppressWarnings("unchecked")
	public Gossip_chat_info_message(Gossip_chat chat) {
		super(Gossip_success_message.successMsg.CHATINFO);
		
		jsonMsg.put(Gossip_chat_info_message.CHATINFO, chat.toJSONObject());
		
	}

}
