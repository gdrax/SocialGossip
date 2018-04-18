package Messages.Client_side;

/**
 * Messaggio usato per segnalare al server, che il socket è destinato a ricevere solo notifiche
 *  
 * @author Gioele Bertoncini
 *
 */
public class Gossip_listening_message extends Gossip_client_message {

	public static final String PASSWORD = "password";
	@SuppressWarnings("unchecked")
	public Gossip_listening_message(String sender, String password) {
		super(Gossip_client_message.Op.LISTENING_OP, sender);
		
		jsonMsg.put(Gossip_listening_message.PASSWORD,  password);
	}
}
