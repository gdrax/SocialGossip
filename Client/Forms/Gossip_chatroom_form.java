package Client.Forms;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Server.Structures.Gossip_chat;

/**
 * Struttura della finestra di una chatroom
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_chatroom_form {

	private Gossip_chat chat;
	private JFrame frame;
	private JTextField msgBox;
	private JButton sendMsgButton;
	private JTextArea messages;
	
	
	public Gossip_chatroom_form(Gossip_chat c) {
		chat = c;
		
		if (chat.getName() == null)
			throw new NullPointerException();
		
		frame = new JFrame();
		frame.setBounds(500, 100, 400, 600);
		frame.setTitle(chat.getName());
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
		
		//area di testo dove viene visualizzata la conversazione
		messages = new JTextArea();
		messages.setBounds(50, 30, 300, 400);
		messages.setEditable(false);
		messages.setBorder(new LineBorder(Color.black));
		messages.setLineWrap(true);
		
		panel.add(sendMsgButton);
		panel.add(msgBox);
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
	
	public Gossip_chat getChat() {
		return chat;
	}
	
	public JTextArea getTextArea() {
		return messages;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_chatroom_form)) {
			return false;
		}
		Gossip_chatroom_form form = (Gossip_chatroom_form)o;
		return form.getChat().equals(this.getChat());
	}
}
