package Client.Listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Server.Structures.Gossip_user;

/**
 * Classe astratta che rappresenta il generico controller di una finestra
 * 
 * @author Gioele Bertoncini
 *
 */
public abstract class Gossip_listener {

	protected DataInputStream input;
	protected DataOutputStream output;
	protected Socket socket;
	protected JFrame frame;
	protected Gossip_listener this_listener;
	protected Gossip_user user;
	
	public Gossip_listener(DataInputStream i, DataOutputStream o, Socket s, Gossip_user u) {
		if (i == null || o == null || s == null)
			throw new NullPointerException();
		input = i;
		output = o;
		socket = s;
		user = u;
	}
	
	public Gossip_listener() {}
	
	/**
	 * Inizializza i listener degli oggetti della finestra
	 */
	public void listen() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				//closeConnection();
				frame.setVisible(false);
			}
		});
	}
	
	/**
	 * Cambia la visibilità della finestra
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	/**
	 * Rende visibile la finestra e chiama il metodo listen
	 */
	public void init() {
		frame.setVisible(true);
		this_listener.listen();
	}
	
	/**
	 * Rende invisible la finestra e rilascia le risorse
	 */
	public void closeWindow() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	
	/**
	 * Chiude il socket e i due stream
	 */
	public void closeConnection() {
		try {
			if (socket != null)
				socket.close();
			if (input != null)
				input.close();
			if (output != null)
				output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void setFrame(JFrame f) {
		frame = f;
	}
	
	/**
	 * Mostra un messaggio informativo
	 * @param info: testo da mostrare
	 * @param title: titolo della finestra
	 */
	public void infoMessage(String info) {
		if (info == null)
			throw new NullPointerException();
		
		JOptionPane infoPane = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dialog = infoPane.createDialog(frame, "Info");
		
		dialog.setVisible(true);
	}
	
	/**
	 * Mostra un messaggio di errore
	 * @param error: testo da mostrare
	 * @param title: titolo della finestra
	 */
	public void errorMessage(String error) {
		if (error == null)
			throw new NullPointerException();
		
		JOptionPane errorPane = new JOptionPane(error, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dialog = errorPane.createDialog(frame, "Errore");
		
		dialog.setVisible(true);
	}
	
	public Gossip_user getUser() {
		return user;
	}
}
