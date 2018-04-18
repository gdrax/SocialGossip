package Client.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Forms.Gossip_register_form;
import Client.Threads.Request_threads.Gossip_register_thread;

/**
 * Controller della finestra di registrazione
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_register_listener extends Gossip_listener {
	
	private Gossip_register_form window;
	
	public Gossip_register_listener(DataInputStream i, DataOutputStream o, Socket s) {
		super(i, o, s, null);
		this_listener = this;
		window = new Gossip_register_form();
		window.setVisible(true);
		this.setFrame(window.getFrame());;
	}
	
	public void listen( ) {
		super.listen();
		
		//listener del bottone per la registrazione
		window.getRegisterButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				//avvio thread per spedire la richiesta di registrazione
				new Gossip_register_thread(input, output, socket, this_listener, window.getusername().getText(), window.getPassword().getPassword(), (String)window.getLanguage().getSelectedItem()).start();
			}
		});
		
		//listener del bottone per tornare alla form di login
		window.getBackButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//creo finestra di login
				Gossip_login_listener lListener = new Gossip_login_listener(input, output, socket);
				//chiudo la finestra di registrazione
				closeWindow();
				//apro la finestra di login
				lListener.init();
			}
		});
	}
}
