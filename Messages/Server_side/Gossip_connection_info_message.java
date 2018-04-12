package Messages.Server_side;

import Messages.Server_side.Gossip_success_message;

/**
 * Informazioni per lo scambio di file
 * 
 * Utilizzato solo dai client verso altri client
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_connection_info_message extends Gossip_success_message {
	
	public static final String HOSTNAME = "hostname";
	public static final String PORT = "port";
	public static final String INFOSENDER = "infoseder";

	@SuppressWarnings("unchecked")
	public Gossip_connection_info_message(String hostname, int port, String sender) {
		super(Gossip_success_message.successMsg.CONNECTIONINFO);
		
		jsonMsg.put(Gossip_connection_info_message.HOSTNAME,  hostname);
		jsonMsg.put(Gossip_connection_info_message.PORT,  port);
		jsonMsg.put(Gossip_connection_info_message.INFOSENDER,  sender);
	}

}
