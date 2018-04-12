package Client.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

import Client.Forms.Gossip_friend_chat_form;
import Client.Threads.Gossip_send_file_thread;
import Client.Threads.Gossip_send_message_thread;
import Server.Structures.Gossip_user;

/**
 * Controller della finestra di chat tra amici
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_friend_chat_listener extends Gossip_listener {
	
	private Gossip_friend_chat_form window;
	private String friend;
	private Gossip_friend_chat_listener this_friend_chat_listener;
	private Gossip_main_listener main;
	
	public Gossip_friend_chat_listener(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener m, Gossip_user u, String f) {
		super(i, o, s, u);
		
		if (u == null || f == null || m == null)
			throw new NullPointerException();
		
		friend = f;
		this_listener = this;
		this_friend_chat_listener = this;
		main = m;
		window = new Gossip_friend_chat_form(new Gossip_user(friend));
		window.setVisible(true);
		this.setFrame(window.getFrame());
	}
	
	/**
	 * Per richerche
	 * @param f
	 */
	public Gossip_friend_chat_listener(String f) {
		super();
		friend = f;
	}
	
	public void listen() {
		super.listen();
		
		//listener della chiura della finestra
		window.getFrame().addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				//rimuovo il listener dalla lista
				main.removeFriendChatListener(this_friend_chat_listener);
			}
		});
		
		//listener del bottone per inviare un mesasggio sulla chat
		window.getSendMsgButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//avvio thread per spedire il messaggio
				new Gossip_send_message_thread(input, output, socket, this_friend_chat_listener, user, friend, window.getMsgBox().getText()).start();
				window.getMsgBox().setText("");
			}
		});
		
		//listener del bottone per inviare un file sulla chat
		window.getSendFileButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//avvio thread per spedire il file
				new Gossip_send_file_thread(input, output, socket, this_friend_chat_listener, user, friend, window.getFileBox().getText()).start();
				window.getFileBox().setText("");
			}
		});
	}
	
	public String getFriend() {
		return friend;
	}
	
	public JTextArea getTextArea() {
		return window.getTextArea();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_friend_chat_listener)) {
			return false;
		}
		Gossip_friend_chat_listener listener = (Gossip_friend_chat_listener)o;
		return listener.getFriend().equals(this.getFriend());
	}
}
