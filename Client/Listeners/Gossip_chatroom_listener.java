package Client.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

import Client.Forms.Gossip_chatroom_form;
import Client.Threads.Gossip_chat_receiver_thread;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Controller della finestra di chatroom
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_chatroom_listener extends Gossip_listener {
	
	private Gossip_chatroom_form window;
	private Gossip_chat chat;
	private Gossip_main_listener main;
	private DatagramSocket chatSocket;
	private InetAddress dispatcherAddress;
	private int port;
	private Gossip_chat_receiver_thread chat_receiver;
	
	public Gossip_chatroom_listener(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener m, Gossip_user u, Gossip_chat c) throws SocketException, UnknownHostException {
		super(i, o, s, u);
		
		if (u == null || c == null || m == null)
			throw new NullPointerException();
		
		chat = c;
		main = m;
		this_listener = this;
		window = new Gossip_chatroom_form(chat);
		this.setFrame(window.getFrame());
		
		initConnection();
		try {
			//avvio thread che riceve i messaggi
			chat_receiver = new Gossip_chat_receiver_thread(chat, main);
			chat_receiver.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.setVisible(true);

	}
	
	/**
	 * Per le ricerche
	 * @param c
	 */
	public Gossip_chatroom_listener(Gossip_chat c) {
		super();
		chat = c;
	}
	
	public void listen() {
		super.listen();
		
		//listener per la chiusura della finestra
		window.getFrame().addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosing(WindowEvent e) {
				//rimuovo il listener dalla lista
				main.removeChatroomListener(chat);
				chat_receiver.interrupt();
			}
		});
		
		//listener del bottone per inviare un mesasggio sulla chat
		window.getSendMsgButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//invio messaggio sul multicast
				sendChatroomText(window.getMsgBox().getText());
				window.getMsgBox().setText("");
			}
		});
	}
	
	/**
	 * Invia un messaggio al gruppo multicast della chat
	 * 
	 * @param text: contenuto del mesasggio
	 */
	private void sendChatroomText(String text) {
		if (text.isEmpty())
			infoMessage("Scrivi qualcosa");
		
		else {
			System.out.println("MESSAGGIO SU MULTICAST");
			String toSendText = "[ "+user.getName()+" ]: "+text;
			window.getMsgBox().setText("");
			
			byte[] bytes = new byte[1024];
			try {
				
				//ottengo bytes
				bytes = toSendText.getBytes("UTF-8");
				
				//contruisco pacchetto
				DatagramPacket packet = new DatagramPacket(bytes, bytes.length, dispatcherAddress, port);
				
				//spedisco pacchetto
				chatSocket.send(packet);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Inizializza connessione
	 * 
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	private void initConnection() throws SocketException, UnknownHostException {
		chatSocket = new DatagramSocket();
		dispatcherAddress = InetAddress.getByName(chat.getChatAddress());
		port = chat.getChatPort();
	}
	
	public void stopReceiverThread() {
		chat_receiver.interrupt();
	}
	
	public Gossip_chat getChat() {
		return chat;
	}
	
	public JTextArea getTextArea() {
		return window.getTextArea();
	}
	
	/**
	 * Aggiorna  la chat
	 * @param c
	 */
	public void updateChat(Gossip_chat c) {
		if (c == null)
			throw new NullPointerException();
		chat = c;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_chatroom_listener)) {
			return false;
		}
		Gossip_chatroom_listener listener = (Gossip_chatroom_listener)o;
		return listener.getChat().equals(this.getChat());
	}
	//TODO: Se chiusa e riaperta scrive due volte il primo messaggio
}
