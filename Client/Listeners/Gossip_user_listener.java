package Client.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Forms.Gossip_user_form;
import Client.Threads.Request_threads.Gossip_add_friend_thread;
import Client.Threads.Request_threads.Gossip_remove_friend_thread;
import Server.Structures.Gossip_user;

/**
 * Controller della finestra di informazioni su un utente
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_user_listener extends Gossip_listener {

	private Gossip_main_listener main; //riferimento al controller pricipale del client
	private Gossip_user displayedUser; //dati dell'utente visualizzato
	private Gossip_user owner; //dati dell'utente che sta usando il client
	private Gossip_user_form window;
	private Gossip_user_listener this_user_listener; //riferimento a questo oggetto
	
	public Gossip_user_listener(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener m, Gossip_user ow, Gossip_user du) {
		super(i, o, s, ow);
		
		if (ow == null || m == null || du == null)
			throw new NullPointerException();
		this_listener = this;
		this_user_listener = this;
		main = m;
		displayedUser = du;
		owner = ow;
		window = new Gossip_user_form(displayedUser, owner);
		window.setVisible(true);
		this.setFrame(window.getFrame());
		
	}
	
	/**
	 * Per ricerche
	 * @param du
	 */
	public Gossip_user_listener(Gossip_user du) {
		super();
		displayedUser = du;
	}
	
	public void listen() {
		super.listen();
		
		//listener per la chiusura della finestra
		window.getFrame().addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				//rimuovo il listener dalla lista
				main.removeUserListener(this_user_listener);
			}
		});
		
		//se sto visualizzando un utente diverso da quello che sta usando il client imposto i listener per i bottoni
		if (!owner.equals(displayedUser)) {
			//listener del bottone per aggiungere agli amici
			window.getAddFriendButton().addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//avvio thread per richiedere aggiunta amico
					new Gossip_add_friend_thread(input, output, socket, this_listener, user, window.getUser()).start();;
				}
			});
			
			//listener del bottone per rimuovere dagli amici
			window.getRemoveFriendButton().addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//avvio thread per richedere rimozione amico
					new Gossip_remove_friend_thread(input, output, socket, this_listener, user, window.getUser()).start();		
				}
			});
		}
	}
	
	public Gossip_user getDisplayedUser() {
		return displayedUser;
	}
	
	@Override
	//due Gossip_user_listener sono uguali se contengono lo stesso dispayedUser
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_user_listener)) {
			return false;
		}
		Gossip_user_listener listener = (Gossip_user_listener)o;
		return listener.getDisplayedUser().equals(this.getDisplayedUser());
	}
}
