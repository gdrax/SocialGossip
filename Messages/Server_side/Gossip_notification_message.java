package Messages.Server_side;

/**
 * Messaggio di notifica dal server
 * 
 * NEWMSG: notifica di messaggio testuale
 * NEWFILE: notifica di file
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_notification_message extends Gossip_server_message {
	public enum notificationType{NEWMSG, NEWFILE}
	public static final String NOTIFICATION_TYPE = "notification_type";
	public static final String SENDER = "sender";
	
	protected Gossip_notification_message.notificationType type;
	
	@SuppressWarnings("unchecked")
	public Gossip_notification_message(Gossip_notification_message.notificationType t, String s) {
		super(Gossip_server_message.Op.NOTIFICATION_OP);
		
		this.type = t;
		
		jsonMsg.put(Gossip_notification_message.NOTIFICATION_TYPE,  t.name());
		jsonMsg.put(Gossip_notification_message.SENDER,  s);
	}

}
