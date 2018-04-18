package Client.Threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import javax.swing.JTextArea;

import Client.Listeners.Gossip_main_listener;
import Server.Structures.Gossip_chat;

/**
 * Thread che si occupa di ricevere i messaggi dall'indirizzo multicast di una chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_chat_receiver_thread extends Thread {
	
	private InetAddress address; //indirizzo multicast della chatroom
	private Gossip_main_listener main; //riferimento al conroller principale del server
	private Gossip_chat chat; //dati della chatroom relativa al thread
	private MulticastSocket chatSocket; //socket multicast da cui si ricevono i messaggi

	public Gossip_chat_receiver_thread(Gossip_chat c, Gossip_main_listener m) throws IOException {
		if (c == null || m == null)
			throw new NullPointerException();
		
		main = m;
		chat = c;
		
		//recupero porta e indirizzo del multicast e creo il socket
		chatSocket = new MulticastSocket(chat.getMulticastPort());
		address = InetAddress.getByName(chat.getMulticastAddress());
		
		//entro nel gruppo multicast
		chatSocket.joinGroup(address);
		//imposto il timeout del socket
		chatSocket.setSoTimeout(500);
	}
	
	public void run() {
		
		//preparo pacchetto per ricevere
		byte[] bytes = new byte[1024];
		DatagramPacket received = new DatagramPacket(bytes, bytes.length);
		
		while(!Thread.interrupted()) {
			try {
				
				//aspetto di ricevere un messaggio
				chatSocket.receive(received);
				
				//creo stringa da array di byte
				String text = new String(received.getData(), 0, received.getLength(), "UTF-8");
				
				//aggiungo messaggio alla conversazione	
				JTextArea text_area = main.getChatMessageArea(chat);
				if (text_area != null)
					text_area.append(text+"\n");
				
			} catch (SocketTimeoutException e) {
				//timeout
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//il thread è stato interrotto, chiudo la connessione
		chatSocket.close();
	}
}
