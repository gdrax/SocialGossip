package Messages.RMI;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Interfaccia remota verso il client
 * 
 * @author Gioele Bertoncini
 *
 */
public interface Gossip_RMI_client_interface extends Remote {

	/**
	 * Notifica di nuova amicizia
	 * @param friend: il nuovo amico
	 */
	public void newFriend(Gossip_user friend) throws RemoteException;
	
	/**
	 * Notifica di rimozione di un amico (offline/online)
	 * @param friend: amico
	 */
	public void removeFriend(Gossip_user friend) throws RemoteException;
	
	/**
	 * Notifica di aggiornamento stato di un amico
	 * @param friend: amico
	 */
	public void updateFriendStatus(Gossip_user friend) throws RemoteException;
	
	/**
	 * Notifica di nuova chat
	 * @param chat: la nuova chat
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public void addChat(Gossip_chat chat) throws RemoteException, SocketException, UnknownHostException;
	
	/**
	 * Notifica di eliminazione di una chat
	 * @param chat: chat da eliminare
	 */
	public void removeChat(Gossip_chat chat) throws RemoteException;
}
