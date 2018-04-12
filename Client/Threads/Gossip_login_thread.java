package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Client.Listeners.Gossip_listener;
import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_login_message;
import Messages.Server_side.Gossip_fail_message;
import Messages.Server_side.Gossip_success_message;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di login
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_login_thread extends Gossip_client_thread {
	
	private char[] password;
	
	public Gossip_login_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_listener l, Gossip_user u, char[] p) {
		super(i, o, s, l, u);
		
		if (u.getName() == null)
			listener.infoMessage("Inserisci un nickname");
		else if (p == null)
			listener.infoMessage("Inserisci una password");
		else
			ready = true;
		
		password = p;
		
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_login_message(user.getName(), password);
	}

	@Override
	protected void successOps() {
		
		if (Gossip_parser.getSuccessType(JSONReply) == Gossip_success_message.successMsg.LOGIN) {
			//recupero la lista degli amici e la lista delle chat
			ArrayList<Gossip_user> friends = Gossip_parser.getFriendList(JSONReply);
			ArrayList<Gossip_chat> chats = Gossip_parser.getChatList(JSONReply);
			Gossip_user myinfo = Gossip_parser.getUser(JSONReply);
			
			//creo e rendo visibile la finestra principale del client
			Gossip_main_listener main = new Gossip_main_listener(input, output, socket, myinfo, friends, chats);
			listener.closeWindow();
			main.init();
		}
		else
			unknownReplyError();
		
	}

	@Override
	protected void failureOps() {
		if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.NICKUNKNOWN)
			listener.errorMessage("Utente non registrato");
		else if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.WRONGPASSWORD)
			listener.errorMessage("Password errata");
		else
			unknownReplyError();
	}
}