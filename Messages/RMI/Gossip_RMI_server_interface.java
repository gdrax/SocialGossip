package Messages.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Server.Graph.NodeNotFoundException;

/**
 * Interfaccia remota del server
 * 
 * @author Gioele Bertoncini
 *
 */
public interface Gossip_RMI_server_interface extends Remote{
	

	/**
	 * Registra l'interfaccia remota di un client
	 * @param username: nome dell'utente
	 * @param channel
	 * @throws NodeNotFoundException: utente non registrato
	 */
	public void registerForCallback(String username, Gossip_RMI_client_interface callback) throws RemoteException, NodeNotFoundException;
	
	/**
	 * Deregistra l'interfaccia remota di un client
	 * @param username: nome dell'utente
	 * @throws NodeNotFoundException: utente non registrato
	 */
	public void unregisterForCallback(String username) throws RemoteException, NodeNotFoundException;
}
