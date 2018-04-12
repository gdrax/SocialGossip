package Client.Forms;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Struttura della finestra di registrazione
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_register_form {
	private JFrame frame;
	private JPasswordField pswField;
	private JTextField usrField;
	private JComboBox<String> languageBox;
	private JButton registerButton;
	private JButton backButton;
	
	public Gossip_register_form() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 400);
		frame.setTitle("Social-gossip registration");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container panel = frame.getContentPane();
		panel.setBackground(Color.lightGray);
		panel.setLayout(null);
		
		//label per i campi da riempire
		JLabel usrLabel = new JLabel("Username");
		usrLabel.setBounds(150,  50,  100,  20);
		JLabel pswLabel = new JLabel("Password");
		pswLabel.setBounds(150,  100,  100,  20);
		JLabel languageLabel = new JLabel("Language");
		languageLabel.setBounds(150,  150,  100,  20);
		
		//campo di testo per il nickname
		usrField = new JTextField();
		usrField.setBounds(250,  50,  120,  25);
		
		//campo di testo per la password
		pswField = new JPasswordField();
		pswField.setBounds(250, 100, 120, 25);
		
		//combo box per scegliere la lingua
		languageBox = new JComboBox<String>(new String[] {"English", "French", "German", "Italian", "Latin", "Portoguese", "Spanish"});
		languageBox.setBounds(250,  150,  120,  25);
		
		//bottone per tornare alla finestra di login
		backButton = new JButton("Torna al login");
		backButton.setBounds(200,  305,  150,  40);
		
		//bottone per registrarsi
		registerButton = new JButton("Crea account");
		registerButton.setBounds(200, 240, 150, 40);
		
		
		panel.add(usrLabel);
		panel.add(pswLabel);
		panel.add(languageLabel);
		panel.add(usrField);
		panel.add(pswField);
		panel.add(languageBox);
		panel.add(registerButton);
		panel.add(backButton);
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
	
	public JPasswordField getPassword() {
		return pswField;
	}
	
	public JTextField getusername() {
		return usrField;
	}
	
	public JComboBox<String> getLanguage() {
		return languageBox;
	}
	
	public JButton getRegisterButton() {
		return registerButton;
	}
	
	public JButton getBackButton() {
		return backButton;
	}
	
	
}
