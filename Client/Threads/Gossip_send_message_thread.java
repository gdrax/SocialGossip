package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Listeners.Gossip_friend_chat_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_action_msg_message;
import Server.Structures.Gossip_user;

/**
 * Thread che invia messaggio di testo
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_send_message_thread extends Gossip_client_thread {

	String receiver;
	String text;
	Gossip_friend_chat_listener chat;
	
	public Gossip_send_message_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_friend_chat_listener l,
			Gossip_user u, String r, String t) {
		super(i, o, s, l, u);
		
		if (r == null)
			throw new NullPointerException();
		if (t.isEmpty())
			listener.errorMessage("Scrivi qualcosa");
		else
			ready = true;
		chat = l;
		receiver = r;
		text = t;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_action_msg_message(text, user.getName(), receiver);
	}

	@Override
	protected void successOps() {
		chat.getTextArea().append("[Me]: "+text+"\n");
	}

	@Override
	protected void failureOps() {
		switch (Gossip_parser.getFailType(JSONReply)) {
			case FRIENDOFFLINE:
				listener.errorMessage(receiver+" non è più online");
				break;
			case OPNOTALLOWED:
				listener.errorMessage(receiver+" non è più tuo amico");
				break;
			default:
				unknownReplyError();
				break;
		}
	}
}
