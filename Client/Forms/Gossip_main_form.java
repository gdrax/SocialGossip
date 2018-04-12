package Client.Forms;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Struttura della finestra principale del client
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_main_form {

	private JFrame frame;
	private JButton searchButton;
	private JTextField searchText;
	private JList<String> friendList;
	private JButton friendButton;
	private JList<String> chatList;
	private JButton addChatButton;
	private JTextField newChatText;
	private JButton removeChatButton;
	private JButton enterChatButton;
	private JButton exitChatButton;
	private JButton logoutButton;
	private DefaultListModel<String> userModel;
	private DefaultListModel<String> chatModel;
	private ArrayList<Gossip_user> friends;
	private ArrayList<Gossip_chat> chatrooms;
	private Gossip_user user;
	
	public Gossip_main_form(ArrayList<Gossip_user> f, ArrayList<Gossip_chat> c, Gossip_user u) {
		
		if (u == null)
			throw new NullPointerException();
		friends = f;
		chatrooms = c;
		user = u;
		
		frame = new JFrame();
		frame.setBounds(400, 100, 850, 500);
		frame.setTitle("Social-gossip");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container panel = frame.getContentPane();
		panel.setBackground(Color.lightGray);
		panel.setLayout(null);
		
		//bottone per cercare un utente
		searchButton = new JButton("Cerca utente");
		searchButton.setBounds(40,  340,  130,  20);
		
		//campo di testo per ceracre un utente
		searchText = new JTextField();
		searchText.setBounds(175,  340, 120, 20);
		
		JLabel friendLabel = new JLabel("I tuoi amici");
		friendLabel.setBounds(125, 60, 125, 20);
		
		//lista degli amici dell'utente
		JScrollPane friendPane = new JScrollPane();
		friendList = new JList<String>();
		userModel = new DefaultListModel<String>();
		friendList.setModel(userModel);
		if (friends != null)
			for (Gossip_user friend: friends)
				userModel.addElement(friend.getName());
		friendPane.setBounds(100,  80,  125,  200);
		friendPane.setViewportView(friendList);

		//bottone per avviare chat con un amico
		friendButton = new JButton("Avvia chat");
		friendButton.setBounds(100,  300,  125,  20);
		
		JLabel chatLabel = new JLabel("Chat disponibili");
		chatLabel.setBounds(645, 60, 125, 20);
		
		//lista delle chatroom
		JScrollPane chatPane = new JScrollPane();
		chatList = new JList<String>();
		chatModel = new DefaultListModel<String>();
		chatList.setModel(chatModel);
		if (chatrooms != null)
			for (Gossip_chat chat: chatrooms)
				chatModel.addElement(chat.getName());
		chatPane.setBounds(635,  80,  125,  200);
		chatPane.setViewportView(chatList);
		
		//bottoni per interagire con le chat
		enterChatButton = new JButton("Entra");
		enterChatButton.setBounds(565, 290, 125, 20);
		exitChatButton = new JButton("Esci");
		exitChatButton.setBounds(700, 290, 125, 20);
		removeChatButton = new JButton("Elimina chat");
		removeChatButton.setBounds(635, 320, 125, 20);
		addChatButton = new JButton("Crea chat");
		addChatButton.setBounds(565, 350, 125, 20);
		
		//campo di testo dove inserire il nome della chat da creare
		newChatText = new JTextField();
		newChatText.setBounds(700,  350,  125,  20);
		
		//bottone per eseguire il logout
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(363,  430,  125,  20);
		
		//JPanel namePanel = new JPanel();
		
		JLabel nameLabel = new JLabel(user.getName());
		nameLabel.setBorder(new LineBorder(Color.black));
		nameLabel.setBounds(325,  50,  200,  30);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
				
		
		panel.add(searchButton);
		panel.add(searchText);
		panel.add(friendPane);
		panel.add(friendLabel);
		panel.add(friendButton);
		panel.add(chatPane);
		panel.add(chatLabel);
		panel.add(enterChatButton);
		panel.add(newChatText);
		panel.add(exitChatButton);
		panel.add(addChatButton);
		panel.add(removeChatButton);
		panel.add(logoutButton);
		panel.add(nameLabel);
		frame.setResizable(false);
	}
	
	
	/**
	 * Cambia la visibilità della finestra
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}
	
	public DefaultListModel<String> getFriendModel() {
		return userModel;
	}
	
	public DefaultListModel<String> getChatModel() {
		return chatModel;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JButton getSearchButton() {
		return searchButton;
	}
	
	public JTextField getSearchText() {
		return searchText;
	}
	
	public JTextField getNewChatText() {
		return newChatText;
	}
	
	public JList<String> getFriendList() {
		return friendList;
	}
	
	public JButton getFriendButton() {
		return friendButton;
	}
	
	public JList<String> getChatList() {
		return chatList;
	}
	
	public JButton getAddChatButton() {
		return addChatButton;
	}
	
	public JButton getRemoveChatButton() {
		return removeChatButton;
	}
	
	public JButton getEnterChatButton() {
		return enterChatButton;
	}
	
	public JButton getExitChatButton() {
		return exitChatButton;
	}
	
	public JButton getLogoutButton() {
		return logoutButton;
	}
}
