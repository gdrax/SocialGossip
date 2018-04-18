package Server.Threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import Server.Gossip_config;

/**
 * Thread che si occupa di ricevere e spedire i mesasggi all'indirizzo multicast di una chatroom
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_chat_dispatcher extends Thread {

	private int port; //porta per la ricezione di questa chat
	private MulticastSocket multicastSocket; //socket multicast della chat
	private DatagramSocket chatSocket; //socket di ricezione del thread
	private InetAddress multicastAddress; //indirizzo multicast
	
	public Gossip_chat_dispatcher(InetAddress ma, MulticastSocket ms) throws SocketException {
		multicastAddress = ma;
		multicastSocket = ms;
		try {
			port = Gossip_config.findPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//creo socket
		chatSocket = new DatagramSocket(port);
		//imposto timeout
		chatSocket.setSoTimeout(600);
	}
	
	@Override
	public void run() {
		
		byte[] buffer = new byte[1024];
		DatagramPacket received = new DatagramPacket(buffer, buffer.length);
		
		while (!Thread.interrupted()) {

			try {
				//ricevo pacchetto
				chatSocket.receive(received);
				byte[] send_buffer = new byte[received.getLength()];
				//copio pacchetto nell'array di byte
				System.arraycopy(received.getData(), received.getOffset(), send_buffer, 0, received.getLength());
				//spedisco pacchetto all'indirizzo multicast
				sendMulticastMessage(send_buffer);	
				
			} catch (SocketTimeoutException e) {}
			  catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Invia dati ad indirizzo multicast
	 * @param message: byte da inviare
	 * @throws IOException
	 */
	private void sendMulticastMessage(byte[] message) throws IOException {
		DatagramPacket msg = new DatagramPacket(message, message.length, multicastAddress, multicastSocket.getLocalPort());
		multicastSocket.send(msg);
	}
	
	/**
	 * @return numero di porta sulla quale è in ascolto il server
	 */
	public int getChatPort() {
		return port;
	}
}
