package Messages.Server_side.Notification_messages;

/**
 * Messaggio di notifica nuovo file
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_file_notification_message extends Gossip_notification_message {
	public static final String FILENAME = "filename";
	
	@SuppressWarnings("unchecked")
	public Gossip_file_notification_message(String sender, String f) {
		super(Gossip_notification_message.notificationType.NEWFILE, sender);
		
		jsonMsg.put(Gossip_file_notification_message.FILENAME,  f);
	}
}
