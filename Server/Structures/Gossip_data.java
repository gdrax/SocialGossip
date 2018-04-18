package Server.Structures;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Messages.RMI.Gossip_RMI_client_interface;
import Server.Gossip_config;
import Server.PortNotFoundException;
import Server.Graph.Gossip_graph;
import Server.Graph.NodeAlreadyException;
import Server.Graph.NodeNotFoundException;

/**
 * Struttura dati del server
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_data {
	
	private Gossip_graph<Gossip_user> graph; //grafo degli utenti
	private ArrayList<Gossip_chat> chats; //lista delle chatroom
	private String multicast_address; //prossimo indirizzo multicast disponibile
	
	public Gossip_data() {
		graph = new Gossip_graph<Gossip_user>();
		chats = new ArrayList<Gossip_chat>();
		multicast_address = Gossip_config.MULTICAST_FIRST;
	}
	
	/**
	 * Aggiunge una nuova chat
	 * @param chatname: nome della chat
	 * @param username: proprietario della chat
	 * @throws NodeNotFoundException: proprietario non registrato
	 */
	public void addChat(String chatname, String username) throws NodeNotFoundException, UnknownHostException, PortNotFoundException, IOException, ChatAlreadyException {
		synchronized (chats) {
			if (chats.contains(new Gossip_chat(chatname)))
					throw new ChatAlreadyException();
		}
		Gossip_chat chat;
		chat = new Gossip_chat(chatname, graph.getNode(new Gossip_user(username)), InetAddress.getByName(multicast_address), InetAddress.getByName("localhost"));
		//divido l'indirizzo
		String[] splitted = multicast_address.split("\\.");
		//aumento di 1 l'indirizzo
		Integer i = Integer.parseInt(splitted[3]) + 1;
		//indirizzi terminati
		if (i == 256)
			return;
		//aggiorno l'indirizzo
		multicast_address = splitted[0]+"."+splitted[1]+"."+splitted[2]+"."+i.toString();
		synchronized (chat) {
			chats.add(chat);
		}

		//notifico tutti gli utenti della presenza di una nuova chat
		for (Gossip_user user: graph.getNodes()) {
			if (user.getRMIChannel() != null)
				user.getRMIChannel().addChat(chat);
		}
	}

	/**
	 * Rimuove una chat
	 * @param chatname: nome della chat da rimuovere
	 * @param username: utente che richiede l'operazione
	 * @return true: chat rimossa, false: la richiesta non proveniva dal proprietario (chat non rimossa)
	 * @throws RemoteException: Errore RMI
	 */
	public boolean removeChat(String chatname, String username) throws RemoteException {
		synchronized (chats) {
			int index = chats.indexOf(new Gossip_chat(chatname));
			//solo l'owner della chat può eliminarla
			if (chats.get(index).getOwner().equals(new Gossip_user(username))) {
				for (Gossip_user user: chats.get(index).getMembers()) {
					user.removeChat(new Gossip_chat(chatname));
				}
				chats.remove(index);
				//notifico tutti gli utenti della cancellazione della chat
				for (Gossip_user user: graph.getNodes()) {
					if (user.getRMIChannel() != null)
						user.getRMIChannel().removeChat(new Gossip_chat(chatname));;
				}
				return true;
			}
			else
				return false;
		}
	}
	
	/**
	 * Aggiunge un utente ad una chat
	 * @param chatname: chat alla quale aggiungere l'utente
	 * @param username: utente da aggiungere
	 * @return true: successo, false: utente già membro della chat
	 * @throws NodeNotFoundException: utente non registrato
	 * @throws RemoteException: Errore RMI
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 * @throws ChatNotFoundExcpetion: chat inesistente
	 */
	public boolean addMember(String chatname, String username) throws NodeNotFoundException, ChatNotFoundException, RemoteException, SocketException, UnknownHostException {
		boolean ret = false;
		synchronized (chats) {
			try {
				Gossip_chat chat = chats.get(chats.indexOf(new Gossip_chat(chatname)));
				Gossip_user user = graph.getNode(new Gossip_user(username));
				ret = chat.addMember(user);
				user.addChat(chat);
				return ret;
			} catch (IndexOutOfBoundsException e) {
				//chat inesistente
				throw new ChatNotFoundException();
			}
		}
	}
	
	/**
	 * Rimuove un utente da una chat
	 * @param chatname: chat dalla quale rimuovere l'utente
	 * @param username: utente da rimuovere
	 * @throws NodeNotFoundException: utente non trovato
	 * @throws RemoteException: errore RMI
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 * @throws ChatNotFoundExcpetion: chat inesistente
	 */
	public void removeMember(String chatname, String username) throws NodeNotFoundException, ChatNotFoundException, RemoteException, SocketException, UnknownHostException {
		synchronized (chats) {
			try {
				Gossip_chat chat = chats.get(chats.indexOf(new Gossip_chat(chatname)));
				Gossip_user user = graph.getNode(new Gossip_user(username));
				user.removeChat(chat);
				chat.removeMember(user);
				//se la chat è vuota la elimino
				if (chat.isEmpty())
					removeChat(chat.getName(), chat.getOwner().getName());
			} catch (IndexOutOfBoundsException e) {
				//chat inesistente
				throw new ChatNotFoundException();
			}
		}
	}
	
	/**
	 * Aggiunge un nuovo utente
	 * 
	 * @param nickname: nomde utente
	 * @param password
	 * @param language: lingua preferita dall'utente
	 * @throws NodeAlreadyException: username già in uso
	 */
	public void addUser(String nickname, String password, String language) throws NodeAlreadyException {
		Gossip_user user = new Gossip_user(nickname, language, password);
		graph.addNode(user);
	}
	
	/**
	 * Rimuove un utente
	 * 
	 * @param username: nome dell'utente da rimuovere
	 * @return true: successo, false: fallimento
	 * @throws NodeNotFoundException 
	 * @throws RemoteException 
	 */
	public boolean removeUser(String username) throws NodeNotFoundException, RemoteException {
		synchronized (chats) {
			//elimino l'utente dalle liste di membri delle chat
			for (Gossip_chat chat: chats) {
				chat.removeMember(new Gossip_user(username));
			}
			//notifico gli amici della rimozione
			for (Gossip_user friend: graph.getNode(new Gossip_user(username)).getFriends())
				if (friend.getRMIChannel() != null)
					friend.getRMIChannel().removeFriend(graph.getNode(new Gossip_user(username)));
				
			//elimino il nodo dal grafo
			return graph.removeNode(new Gossip_user(username));
		}
	}
	
	/**
	 * Imposta lo stato di un utente e notifica gli amici
	 * @param username: nome dell'utente
	 * @param online: true se vuole connettersi, false se vuole disconnettersi
	 * @param s: socket su cui l'utente è connesso, null se si vuole disconnettersi
	 * @throws NodeNotFoundException
	 * @throws RemoteException: errore RMI
	 */
	public void setStatus(String username, boolean online) throws NodeNotFoundException, RemoteException {
			Gossip_user user = graph.getNode(new Gossip_user(username));
			if (online == true)
				graph.getNode(user).setOnline();
				
			else {
				graph.getNode(user).setOffline();
				graph.getNode(user).setRMIChannel(null);
			}
			for (Gossip_user friend: user.getFriends()) {
				if (friend.getRMIChannel() != null)
					friend.getRMIChannel().updateFriendStatus(user);
			}
	}
	
	/**
	 * Rimuove una relazione di amicizia
	 * 
	 * @param user1
	 * @param user2
	 * @return true: successo, false: fallimento
	 * @throws RemoteException 
	 */
	public boolean removeFriendship(String user1, String user2) throws RemoteException {
		Gossip_user u1 = new Gossip_user(user1);
		Gossip_user u2 = new Gossip_user(user2);
		try {
			if (graph.removeEdge(u1, u2)) {
				graph.getNode(u1).removeFriend(u2);
				graph.getNode(u2).removeFriend(u1);
				graph.getNode(u2).getRMIChannel().removeFriend(graph.getNode(u1));
				graph.getNode(u1).getRMIChannel().removeFriend(graph.getNode(u2));
				return true;
			}
			else
				return false;
		} catch (NodeNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Aggiunge una nuova relazione di amicizia, se è online avverte l'utente interessato
	 * @param u1 
	 * @param u2
	 * @return true: successo, false: relazione di amicizia già presente
	 * @throws NodeNotFoundException: user1 o user2 non registrati
	 * @throws RemoteException: errore RMI
	 */
	public boolean addFriendship(String u1, String u2) throws NodeNotFoundException, RemoteException{
		Gossip_user user1 = graph.getNode(new Gossip_user(u1));
		Gossip_user user2 = graph.getNode(new Gossip_user(u2));
		if (user1.equals(user2))
			return false;
		//aggiungo arco al grafo
		if (!graph.addEdge(user1, user2))
			return false;
		user1.addFriend(user2);
		user2.addFriend(user1);
		//se l'utente è online gli notifico la richiesta tramite RMI
		if (user2.getStatus())
			user2.getRMIChannel().newFriend(user1);
		if (user1.getStatus())
			user1.getRMIChannel().newFriend(user2);
		return true;
	}
	
	/**
	 * @param username
	 * @return la lista degli amici dell'utente
	 * @throws NodeNotFoundException
	 */
	public ArrayList<Gossip_user> getFriends(String username) throws NodeNotFoundException {
		return graph.getNode(new Gossip_user(username)).getFriends();
	}
	
	/**
	 * @return Lista delle chat presenti sul sevrer
	 */
	public ArrayList<Gossip_chat> getChats() {
		return chats;
	}
	
	/**
	 * @param username: nome da cercare
	 * @return true: utente trovato, false: utente non trovato
	 * @throws NodeNotFoundException: utente non trovato
	 */
	public boolean searchUser(String username) throws NodeNotFoundException {
		return graph.checkNode(new Gossip_user(username));
	}
	
	/**
	 * @param username: nome dell'utente
	 * @return Socket sul quale è connesso l'utente
	 * @throws NodeNotFoundException: utente non registrato
	 */
	public boolean isOnline(String username) throws NodeNotFoundException{
		return graph.getNode(new Gossip_user(username)).getStatus();
	}
	
	/**
	 * Imposta il canale RMI per le notifiche ad un client
	 * @param username
	 * @param channel
	 * @throws NodeNotFoundException: utente non registrato
	 */
	public void setRMI(String username, Gossip_RMI_client_interface channel) throws NodeNotFoundException {
		graph.getNode(new Gossip_user(username)).setRMIChannel(channel);
	}
	
	/**
	 * @param username
	 * @return: struttura dati di un utente
	 * @throws NodeNotFoundException: utente non trovato
	 */
	public Gossip_user getUser(String username) throws NodeNotFoundException {
		return graph.getNode(new Gossip_user(username));
	}
	
	/**
	 * @param chatname
	 * @return: struttura dati di una chat
	 */
	public Gossip_chat getChat(String chatname) {
		return chats.get(chats.indexOf(new Gossip_chat(chatname)));
	}	
}
