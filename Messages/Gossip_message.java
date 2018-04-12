package Messages;

import org.json.simple.JSONObject;

/**
 * Formato JSON di un messaggio di Social_gossip
 * 
 * CLIENT_MESSAGE: Messaggio inviato da un client (vedi Gossip_client_message.java)
 * SERVER_MESSAGE: Messaggio inviato dal server (vedi Gossip_server_message.java)
 * 
 * @author Gioele Bertoncini
 */

public class Gossip_message {
	public enum Type {CLIENT_MESSAGE, SERVER_MESSAGE}
	public static final String TYPE = "type";
	
	protected Gossip_message.Type msgtype;
	protected JSONObject jsonMsg = null;
	
	@SuppressWarnings("unchecked")
	public Gossip_message(Gossip_message.Type type) {
		this.msgtype = type;
		jsonMsg = new JSONObject();
		jsonMsg.put(Gossip_message.TYPE, type.name());
	}
	
	/**
	 * @return oggeto JSON come String
	 */
	public String getJsonString() {
		return jsonMsg.toJSONString();
	}
}
