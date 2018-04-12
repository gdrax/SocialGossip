package Client.Forms;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Server.Structures.Gossip_user;

/**
 * Struttura della finestra di una chat tra amici
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_friend_chat_form {

	private Gossip_user friend;
	private JFrame frame;
	private JTextField msgBox;
	private JButton sendMsgButton;
	private JTextField fileBox;
	private JButton sendFileButton;
	private JTextArea messages;
	
	
	public Gossip_friend_chat_form(Gossip_user f) {
		friend = f;
		
		if (friend.getName() == null)
			throw new NullPointerException();
		
		frame = new JFrame();
		frame.setBounds(500, 100, 400, 600);
		frame.setTitle(friend.getName());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container panel = frame.getContentPane();
		panel.setBackground(Color.lightGray);
		panel.setLayout(null);
		
		//bottone per inviare un messaggio
		sendMsgButton = new JButton("Invia messaggio");
		sendMsgButton.setBounds(210,  450,  150,  20);
		
		//area di testo dove scrivere il messaggio
		msgBox = new JTextField();
		msgBox.setBounds(40,  450,  150,  20);
		
		//bottone per inviare un file
		sendFileButton = new JButton("Invia file");
		sendFileButton.setBounds(210,  480,  150,  20);
		
		//area di testo per inserire il nome del file
		fileBox = new JTextField();
		fileBox.setBounds(40,  480,  150,  20);
		
		//area di testo dove viene visualizzata la conversazione
		messages = new JTextArea();
		messages.setBounds(50, 30, 300, 400);
		messages.setEditable(false);
		messages.setBorder(new LineBorder(Color.black));
		messages.setLineWrap(true);
		
		panel.add(sendMsgButton);
		panel.add(msgBox);
		panel.add(sendFileButton);
		panel.add(fileBox);
		panel.add(messages);
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
	
	public JTextField getMsgBox() {
		return msgBox;
	}
	
	public JButton getSendMsgButton() {
		return sendMsgButton;
	}
	
	public JTextField getFileBox() {
		return fileBox;
	}
	
	public JButton getSendFileButton() {
		return sendFileButton;
	}
	
	public Gossip_user getFriend() {
		return friend;
	}
	
	public JTextArea getTextArea() {
		return messages;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_friend_chat_form)) {
			return false;
		}
		Gossip_friend_chat_form form = (Gossip_friend_chat_form)o;
		return form.getFriend().equals(this.getFriend());
	}
}

