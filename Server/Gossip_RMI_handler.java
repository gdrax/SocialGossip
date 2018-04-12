package Server;

import java.rmi.server.RemoteObject;

import Messages.RMI.Gossip_RMI_client_interface;
import Messages.RMI.Gossip_RMI_server_interface;
import Server.Graph.NodeNotFoundException;
import Server.Structures.Gossip_data;

/**
 * Implementazione dell'interfaccia remota del server
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_RMI_handler extends RemoteObject implements Gossip_RMI_server_interface {
	
	private static final long serialVersionUID = 1L;
	private Gossip_data data;
	
	public Gossip_RMI_handler(Gossip_data d) {
		super();
		data = d;
	}
 
	public synchronized void registerForCallback(String username, Gossip_RMI_client_interface channel) throws NodeNotFoundException {
		if (username == null || channel == null)
			throw new NullPointerException();
		data.setRMI(username,  channel);
	}
	
	public synchronized void unregisterForCallback(String username) throws NodeNotFoundException {
		if (username == null)
			throw new NullPointerException();
		data.setRMI(username, null);
	}
}
