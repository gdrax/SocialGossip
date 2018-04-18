package Server.Structures;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import Messages.RMI.Gossip_RMI_client_interface;
import Server.Threads.FriendOfflineException;
/**
 * Struttura di un utente
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_user implements Serializable {

	private static final long serialVersionUID = 4633680942029050701L;
	
	private String nickname;
	private String language;
	private String password;
	private boolean online; //status dell'utente
	private ArrayList<Gossip_chat> chats; //lista delle chat di cui fa parte
	private ArrayList<Gossip_user> friends; //lista degli amici
	private transient Socket messageSocket; //socket dove riceve le notifiche dal server
	private Gossip_RMI_client_interface RMI_channel; //interfaccia RMI
	
	//campi per la serializzazione
	public static final String NAME = "name";
	public static final String STATUS = "status";
	public static final String LANGUAGE = "language";
	
	/**
	 * @param n: nickname dell'utente
	 * @param l: lingua preferita dell'utente
	 * @param p: password per l'accesso
	 * @param s: socket su cui l'utente è connesso
	 */
	public Gossip_user(String n, String l, String p) {
		nickname = n;
		language = l;
		password = p;
		online = true;
		chats = new ArrayList<Gossip_chat>();
		friends = new ArrayList<Gossip_user>();
		messageSocket = null;
	}
	
	/**
	 * Utilizzato dal client per memorizare i dati degli amici
	 * @param n: nickbname dell'utente
	 * @param s: status dell'utente (true = online, false = offline)
	 * @param l: lingua preferita dell'utente
	 */
	public Gossip_user(String n, boolean s, String l) {
		nickname = n;
		language = l;
		online = s;
		password = null;
		chats = null;
		friends = null;
		messageSocket = null;
	}
	
	/**
	 * Utilizzato per eseguire ricerche
	 * @param n: nickname
	 */
	public Gossip_user(String n) {
		nickname = n;
		language = null;
		password = null;
		online = false;
		chats = null;
		friends = null;
		messageSocket = null;
	}
	
	/**
	 * @return il nickname dell'utente
	 */
	public synchronized String getName() {
		return nickname;
	}
	
	/**
	 * @return la password dell'utente
	 */
	public synchronized String getPassword() {
		return password;
	}

	/**
	 * Contrassegna l'utente come online
	 */
	public synchronized void setOnline() {
		online = true;
	}
	
	/**
	 * Contrassegna l'utente come offline
	 */
	public synchronized void setOffline() {
		//tolgo il socket per le notifiche
		messageSocket = null;
		online = false;
	}

	/**
	 * @return stato dell'utente (true = online, false = offline)
	 */
	public synchronized boolean getStatus() {
		return online;
	}

	/**
	 * Cambia la lingua preferita dall'utente
	 * 
	 * @param l: nuova lingua
	 */
	public synchronized void changeLanguage(String l) {
			language = l;
	}

	/**
	 * @return lingua preferita dall'utente
	 */
	public synchronized String getLanguage() {
		return language;
	}
	
	/**
	 * Aggiunge una nuova chat a cui l'utente è iscritto
	 * 
	 * @param chat
	 */
	public synchronized void addChat(Gossip_chat chat) {
		if (!chats.contains(chat))
			chats.add(chat);
	}
	
	/**
	 * Rimuove una nuova chat a cui l'utente è iscritto
	 * 
	 * @param chat
	 */
	public synchronized void removeChat(Gossip_chat chat) {
		chats.remove(chat);
	}
	
	/**
	 * @return lista dei nomi delle chat a cui l'utente è iscritto
	 */
	public synchronized ArrayList<Gossip_chat> getChats() {
		return chats;
	}
	
	/**
	 * Aggiunge un altro utente alla lista degli amici
	 * 
	 * @param user: amico da aggiungere
	 */
	public synchronized void addFriend(Gossip_user user) {
		if (!friends.contains(user))
			friends.add(user);
	}
	
	/**
	 * Rimuove un utente alla lista degli amici
	 * 
	 * @param usern: amico da rimuovere
	 */
	public synchronized void removeFriend(Gossip_user user) {
		friends.remove(user);
	}
	
	
	public synchronized boolean isFriendOf(Gossip_user user) {
		return friends.contains(user);
	}
	
	/**
	 * @return lista degli amici dell'utente
	 */
	public synchronized ArrayList<Gossip_user> getFriends() {
		return friends;
	}
	
	/**
	 * @return socket su cui l'utente riceve le notifiche, null se è offline
	 */
	public synchronized Socket getMessageSocket() throws FriendOfflineException{
		if (messageSocket == null)
			throw new FriendOfflineException();
		return messageSocket;
	}
	
	/**
	 * Imposta il socket sul quale il client riceve le notifiche
	 * @param socket
	 */
	public synchronized void setMessageSocket(Socket socket) {
		if (socket == null)
			throw new NullPointerException();
		messageSocket = socket;
	}
	
	/**
	 * Imposta un nuovo canale RMI per le notifiche al client
	 * @param c: interfaccial coi metodi del client
	 */
	public synchronized void setRMIChannel(Gossip_RMI_client_interface c) {
		RMI_channel = c;
	}
	
	/**
	 * @return canale RMI per le notifiche
	 */
	public synchronized Gossip_RMI_client_interface getRMIChannel() {
		return RMI_channel;
	}
	
	@Override
	//due Gossip_user sono uguali se hanno lo stesso nickname
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_user)) {
			return false;
		}
		Gossip_user user = (Gossip_user)o;
		return user.getName().equals(this.getName());
	}
	
	@Override
	public int hashCode() {
		return nickname.hashCode();
	}
	
	/**
	 * Serializza l'utente
	 * @return un JSONObject contenente nome, status e lingua dell'utente
	 */
	@SuppressWarnings("unchecked")
	public synchronized JSONObject toJSONObject() {
		JSONObject user = new JSONObject();
		user.put(Gossip_user.NAME,  this.nickname);
		user.put(Gossip_user.STATUS,  this.online);
		user.put(Gossip_user.LANGUAGE,  this.language);
		return user;
	}

}
