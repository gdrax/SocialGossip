package Messages.Server_side.Notification_messages;

/**
 * Messaggio di notifica nuovo messaggio testuale
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_text_notification_message extends Gossip_notification_message {
	public static final String TEXT = "text";
	
	@SuppressWarnings("unchecked")
	public Gossip_text_notification_message(String sender, String t) {
		super(Gossip_notification_message.notificationType.NEWMSG, sender);
		
		jsonMsg.put(Gossip_text_notification_message.TEXT,  t);
	}

}