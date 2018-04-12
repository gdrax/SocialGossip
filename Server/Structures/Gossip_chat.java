package Server.Structures;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Server.Gossip_config;
import Server.PortNotFoundException;
import Server.Threads.Gossip_chat_dispatcher;

/**
 * Struttura di una chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_chat implements Serializable{

	private static final long serialVersionUID = -2407937145602305921L;
	
	private String chatname;
	private Gossip_user owner;
	private ArrayList<Gossip_user> members;
	private transient MulticastSocket multicastSocket;
	private InetAddress multicastAddress;
	private InetAddress chatAddress;
	private int multicastPort;
	private int chatPort;
	private transient Gossip_chat_dispatcher dispatcher;
	
	public static final String CHATNAME = "chatname";
	public static final String MEMBERS = "members";
	public static final String MULTICASTADDRESS = "multicastaddress";
	public static final String MULTICASTPORT = "multicastport";
	public static final String CHATADDRESS = "chataddress";
	public static final String CHATPORT = "chatport";
	
	/**
	 * Usato per le ricerche
	 * @param cn: nome della chat
	 */
	public Gossip_chat(String cn) {
		chatname = cn;
		members = null;
	}
	
	/**
	 * Usato dal client quando memorizza una chat
	 * @param cn: nome della chat
	 * @param ma: indirizzo multicast
	 * @param mp: porta multicast
	 * @param ca: indirizzo del server
	 * @param cp: porta su cui è in ascolto il server
	 * @param m: lista dei membri della chat
	 */
	public Gossip_chat(String cn, InetAddress ma, int mp, InetAddress ca, int cp, ArrayList<Gossip_user> m) {
		chatname = cn;
		multicastAddress = ma;
		multicastPort = mp;
		chatAddress = ca;
		chatPort = cp;
		members = m;
	}
	
	/**
	 * @param cn: nome della chat
	 * @param o: proprietario della chat (l'unico che può eliminarla)
	 * @param ca: indirizzo multicast
	 * @param sa: indirizzo del server
	 * @throws PortNotFoundException: nessuna porta libera
	 * @throws IOException: errore nella creazione del socket
	 */
	public Gossip_chat(String cn, Gossip_user o, InetAddress ca, InetAddress sa) throws PortNotFoundException, IOException {
		chatname = cn;
		owner = o;
		members = new ArrayList<Gossip_user>();
		members.add(o);
		multicastAddress = ca;
		chatAddress = sa;
		
		//trovo una porta per il socket multicast
		multicastPort = Gossip_config.findPort();

		//creo il socket multicast
		multicastSocket = new MulticastSocket(multicastPort);
		
		//creo il thread che smisterà i messaggi della chat
		try {
			dispatcher = new Gossip_chat_dispatcher(multicastAddress, multicastSocket);
		} catch (SocketException e) {
			e.printStackTrace();
		};
		chatPort = dispatcher.getChatPort();
		//avvio il thread
		dispatcher.start();
	}
	
	/**
	 * Aggiunge utente alla chat
	 * 
	 * @param m: utente da aggiungere
	 * @return true: successo, false: utente già presente
	 * @throws NullPointerException
	 */
	public boolean addMember(Gossip_user m) throws NullPointerException {
		if (m == null)
			throw new NullPointerException();
		
		if (!members.contains(m)) {
			members.add(m);
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Rimuove utente dalla chat
	 * 
	 * @param m: utente da rimuovere
	 * @throws NullPointerException
	 */
	public void removeMember(Gossip_user m) throws NullPointerException {
		if (m == null)
			throw new NullPointerException();
		if (m.equals(owner)) {
			members.remove(m);	
			if (!members.isEmpty())
				//se ho rimosso il proprietario e la chat non è vuota assegno un nuovo proprietario
				owner = members.get(0);
		}
		else
			members.remove(m);
	}
	
	/**
	 * @return nome della chat
	 */
	public String getName() {
		return chatname;
	}
	
	/**
	 * @return: proprietario della chat
	 */
	public Gossip_user getOwner(){
		return owner;
	}
	
	/**
	 * @return lista dei membri
	 */
	public ArrayList<Gossip_user> getMembers() {
		return members;
	}
	
	/**
	 * @return: indirizzo del thread listener
	 */
	public String getChatAddress() {
		return chatAddress.toString().replaceAll("[^\\d.]", "");
	}
	
	/**
	 * @return: indirizzo multicast della chat
	 */
	public String getMulticastAddress() {
		return multicastAddress.toString().replaceAll("[^\\d.]", "");
	}
	
	/**
	 * @return: porta di ascolto del thread listener
	 */
	public int getChatPort() {
		return chatPort;
	}
	
	/**
	 * @return: porta per il multicast
	 */
	public int getMulticastPort() {
		return multicastPort;
	}
	
	/**
	 * @return true se è vuota, false se non lo è
	 */
	public boolean isEmpty() {
		return members.isEmpty();
	}
	
	/**
	 * Interrompe il thread dispatcher
	 */
	public void close() {
		dispatcher.interrupt();
	}
	
	@Override
	public int hashCode() {
		return chatname.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_chat)) {
			return false;
		}
		Gossip_chat chat = (Gossip_chat)o;
		return chat.getName().equals(this.getName());
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject chat = new JSONObject();
		JSONArray membersList = new JSONArray();
		
		for (Gossip_user member: members) {
			membersList.add(member.toJSONObject());
		}

		chat.put(Gossip_chat.CHATNAME,  this.chatname);
		chat.put(Gossip_chat.MEMBERS, membersList);
		chat.put(Gossip_chat.CHATADDRESS,  this.getChatAddress());
		chat.put(Gossip_chat.CHATPORT,  this.chatPort);
		chat.put(Gossip_chat.MULTICASTADDRESS, this.getMulticastAddress());
		chat.put(Gossip_chat.MULTICASTPORT,  this.multicastPort);
		return chat;
	}
}
