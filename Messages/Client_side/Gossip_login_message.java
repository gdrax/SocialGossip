package Messages.Client_side;

/**
 * Messaggio di login
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_login_message extends Gossip_client_message {
	
	public static final String PASSWORD = "password";

	@SuppressWarnings("unchecked")
	public Gossip_login_message(String nickname, char[] password) {
		super(Gossip_client_message.Op.LOGIN_OP, nickname);
		
		jsonMsg.put(Gossip_login_message.PASSWORD, new String(password));
	}

}
