package Messages.Client_side.Action_messages;

/**
* Messaggio con nome di file tra amici
* 
* @author Gioele Bertoncini
*/
public class Gossip_action_file_message extends Gossip_action_message{
	
	public static final String FILENAME = "filename";
	
	@SuppressWarnings("unchecked")
	public Gossip_action_file_message(String filename, String sender, String receiver) {
		super(Gossip_action_message.Action.FILE_FRIEND_OP, sender, receiver);
		
		jsonMsg.put(Gossip_action_file_message.FILENAME,  filename);
	}
}