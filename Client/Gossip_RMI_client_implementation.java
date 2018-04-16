package Client;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.server.RemoteObject;

import Client.Listeners.Gossip_main_listener;
import Messages.RMI.Gossip_RMI_client_interface;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Imlementazione dell'interfaccia remota del client
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_RMI_client_implementation extends RemoteObject implements Gossip_RMI_client_interface {
	
	private static final long serialVersionUID = -4355764862846461175L;
	private Gossip_main_listener main;
	
	public Gossip_RMI_client_implementation(Gossip_main_listener m) {
		super();
		if (m == null)
			throw new NullPointerException();
		main = m;
	}

	@Override
	public void newFriend(Gossip_user friend) {
		if (friend != null) {
			main.infoMessage("Tu e "+friend.getName()+" ora siete amici");
			//aggiungo il nome dell'amico alla lista
			main.getFriendListModel().addElement(friend.getName());
		}
	}
	
	@Override
	public void removeFriend(Gossip_user friend) {
		if (friend != null)
			//rimuovo il nome dell'amico dalla lista
			main.getFriendListModel().removeElement(friend.getName());
	}

	@Override
	public void updateFriendStatus(Gossip_user friend) {
		//avviso l'utente del cambiamento di stato
		if (friend.getStatus())
			main.infoMessage(friend.getName()+" è online");
		else
			main.infoMessage(friend.getName()+" è offline");
	}

	@Override
	public void addChat(Gossip_chat chat) throws SocketException, UnknownHostException {
		if (chat != null) {
			//aggiungo chat alla lista
			main.getChatListModel().addElement(chat.getName());
			main.infoMessage("Nuova chatroom: "+chat.getName());
		}
	}

	@Override
	public void removeChat(Gossip_chat chat) {
		//rimuovo listener dalla lista
		main.removeChatroom(chat);
		//rimuovo chat dalla finestra
		main.getChatListModel().removeElement(chat.getName());
	}
	
	//TODO: Aggiornare serial version ID

}
