package Client.Forms;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Struttura della finestra di login
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_login_form {

	private JFrame frame;
	private JPasswordField pswField;
	private JTextField usrField;
	private JButton loginButton;
	private JButton registerButton;
	
	public Gossip_login_form() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 400);
		frame.setTitle("Social-gossip login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container panel = frame.getContentPane();
		panel.setBackground(Color.lightGray);
		panel.setLayout(null);
		
		//label per i campida riemprie
		JLabel usrLabel = new JLabel("Username");
		usrLabel.setBounds(150,  50,  100,  20);
		JLabel pswLabel = new JLabel("Password");
		pswLabel.setBounds(150,  100,  100,  20);
		
		//campo di testo per il nickname
		usrField = new JTextField();
		usrField.setBounds(250,  50,  120,  25);
		
		//campo di testo per la password
		pswField = new JPasswordField();
		pswField.setBounds(250, 100, 120, 25);
		
		//bottone per accedere
		loginButton = new JButton("Accedi");
		loginButton.setBounds(200, 200, 150, 40);
		
		//bottone per aprire la finestra di registrazione
		registerButton = new JButton("Crea account");
		registerButton.setBounds(200, 265, 150, 40);
		
		
		panel.add(usrLabel);
		panel.add(pswLabel);
		panel.add(usrField);
		panel.add(pswField);
		panel.add(loginButton);
		panel.add(registerButton);
		frame.setResizable(false);

	}
	

	/**
	 * Cambia la visibilità della finestra
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JButton getLoginButton() {
		return loginButton;
	}
	
	public JButton getRegisterButton() {
		return registerButton;
	}
	
	public JPasswordField getPassword() {
		return pswField;
	}
	
	public JTextField getUsername() {
		return usrField;
	}
}
