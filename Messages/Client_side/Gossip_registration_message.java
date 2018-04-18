package Messages.Client_side;

/**
 * Messaggio di registrazione nuovo utente
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_registration_message extends Gossip_client_message{

	public static final String LANGUAGE = "language";
	public static final String PASSWORD = "password";
		
	@SuppressWarnings("unchecked")
	public Gossip_registration_message(String sender, String language, String password) {
		super(Gossip_client_message.Op.REGISTER_OP, sender);
		
		jsonMsg.put(Gossip_registration_message.LANGUAGE,  language);
		jsonMsg.put(Gossip_registration_message.PASSWORD,  password);
	}
}
