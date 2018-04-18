package Client.Threads.Request_threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Client.Listeners.Gossip_listener;
import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_registration_message;
import Messages.Server_side.Fail_messages.Gossip_fail_message;
import Messages.Server_side.Success_messages.Gossip_success_message;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;


/**
 * Thread che invia richiesta di registrazione
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_register_thread extends Gossip_client_thread {
	
	private String username;
	private char[] password;
	private String language;

	public Gossip_register_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_listener l, String u, char[] p, String la) {
		super(i, o, s, l, null);
		
		if (u == null)
			listener.infoMessage("Inserisci un nickname");
		else if (p == null)
			listener.infoMessage("Inserisci una password");
		else if (p.length < 8)
			listener.infoMessage("Password troppo corta");
		else
			ready = true;
		
		username = u;
		password = p;
		language = la;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_registration_message(username, language, new String(password));	
	}

	@Override
	protected void successOps() {
		if (Gossip_parser.getSuccessType(JSONReply) == Gossip_success_message.successMsg.REGISTRATION) {
			//recupero la lista delle chat e le info sull'account appena creato
			ArrayList<Gossip_chat> chatList = Gossip_parser.getChatList(JSONReply);
			Gossip_user myinfo = Gossip_parser.getUser(JSONReply);
			
			//creo la finestra principale
			Gossip_main_listener main = new Gossip_main_listener(input, output, socket, myinfo, null, chatList, new String(password));
			//chiudo la finestra di registrazione
			listener.closeWindow();
			main.init();
		}
		else
			unknownReplyError();
	}

	@Override
	protected void failureOps() {
		if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.NICKALREADY)
			listener.infoMessage("Nickname già in uso");
		else
			unknownReplyError();
	}
}
