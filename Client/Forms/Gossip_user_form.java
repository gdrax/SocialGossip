package Client.Forms;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Server.Structures.Gossip_user;

/**
 * Struttura della finestra di informazioni su un utente
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_user_form {
	
	private Gossip_user user; 	//dati dell'utente visualizzato
	private Gossip_user owner;	//dati dell'utente che sta usando il client
	private JFrame frame;
	private JButton addFriendButton;
	private JButton removeFriendButton;

	/**
	 * @param n: nickname dell'utente
	 * @param l: lingua dell'utente
	 * @param s: status dell'utente (online, offline)
	 */
	public Gossip_user_form(Gossip_user u, Gossip_user o) {
		if (u == null || o == null)
			throw new NullPointerException();
		user = u;
		owner = o;
		
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 300);
		frame.setTitle("Scheda utente");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container panel = frame.getContentPane();
		panel.setBackground(Color.lightGray);
		panel.setLayout(null);
		
		//campi dei dati
		JLabel usrField = new JLabel("Nickname:");
		usrField.setBounds(50,  20,  100,  20);
		JLabel languageField = new JLabel("Lingua:");
		languageField.setBounds(50,  50,  100,  20);
		JLabel statusField = new JLabel("Status:");
		statusField.setBounds(50,  80,  100,  20);
		
		//valori dei dati
		JLabel usrVal = new JLabel(user.getName());
		usrVal.setBounds(170,  20,  100,  20);
		JLabel languageVal = new JLabel(user.getLanguage());
		languageVal.setBounds(170,  50,  100,  20);
		JLabel statusVal;
		if (user.getStatus())
			statusVal = new JLabel("Online");
		else
			statusVal = new JLabel("Offline");
		statusVal.setBounds(170,  80,  100,  20);
		
		//se visualizzo i dati dell'utente che sta usando il client non inserisco i bottoni per l'amicizia
		if (!owner.equals(user)) {
			//bottone per aggiungere l'utente agli amici
			addFriendButton = new JButton("Aggiungi amico");
			addFriendButton.setBounds(60, 120, 180,  40);
			
			//bottone per rimuovere l'utente dagli amici
			removeFriendButton = new JButton("Rimuovi amico");
			removeFriendButton.setBounds(60, 180, 180,  40);
			
			panel.add(addFriendButton);
			panel.add(removeFriendButton);
		}
		
		panel.add(usrField);
		panel.add(languageField);
		panel.add(statusField);
		panel.add(usrVal);
		panel.add(languageVal);
		panel.add(statusVal);
		frame.setResizable(false);
	}
	
	/**
	 * Cambia la visibilità della finestra
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	public Gossip_user getUser() {
		return user;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_user_form)) {
			return false;
		}
		Gossip_user_form form = (Gossip_user_form)o;
		return form.getUser().equals(this.getUser());
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public JButton getAddFriendButton() {
		return addFriendButton;
	}
	
	public JButton getRemoveFriendButton() {
		return removeFriendButton;
	}
}