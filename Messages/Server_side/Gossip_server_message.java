package Messages.Server_side;

import Messages.Gossip_message;
import Messages.Client_side.Gossip_client_message;

/**
 * Messaggio inviato dal server
 * 
 * FAIL: Operazione fallita (vedi Fail_messages/Gossip_fail_message.java)
 * SUCCESS: Operazione avvenuta con successo (vedi Success_message/Gossip_success_message.java)
 * NOTIFICATION: Notifica (vedi Notification_messages/Gossip_notification_message.java)
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_server_message extends Gossip_message {
	public enum Op {FAIL_OP, SUCCESS_OP, NOTIFICATION_OP};
	public static final String OP = "op";
	
	protected Gossip_server_message.Op op;
	
	@SuppressWarnings("unchecked")
	public Gossip_server_message(Gossip_server_message.Op o) {
		super(Gossip_message.Type.SERVER_MESSAGE);
		
		this.op = o;
		
		jsonMsg.put(Gossip_client_message.OP,  o.name());
	}
}
