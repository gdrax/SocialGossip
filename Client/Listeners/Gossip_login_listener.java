package Client.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Forms.Gossip_login_form;
import Client.Threads.Gossip_login_thread;
import Server.Structures.Gossip_user;

/**
 * Controller della finestra di login
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_login_listener extends Gossip_listener {
	
	private Gossip_login_form window;
	
	public Gossip_login_listener (DataInputStream i, DataOutputStream o, Socket s) {
		super(i, o, s, null);
		this_listener = this;
		window = new Gossip_login_form();
		this.setFrame(window.getFrame());
		window.setVisible(true);
	}
	
	public void listen() {
		super.listen();
		
		//listener del bottone per effettuare il login
		window.getLoginButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				//avvio thread che invia la richiesta di login
				new Gossip_login_thread(input, output, socket, this_listener, new Gossip_user(window.getUsername().getText()), window.getPassword().getPassword()).start();;
			}
		});
		
		//listener del bottone per aprire la form di registrazione
		window.getRegisterButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				//apro finestra di regisrtazione
				Gossip_register_listener rListener = new Gossip_register_listener(input, output, socket);
				closeWindow();
				rListener.init();
			}
		});
	}
}
