package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_chat_message;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di eliminazione chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_remove_chat_thread extends Gossip_client_thread {
	
	private String chatname;
	
	public Gossip_remove_chat_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener l, Gossip_user u, String c) {
		super(i, o, s, l, u);
		
		if (u == null)
			throw new NullPointerException();
		if (c == null)
			listener.infoMessage("Seleziona un chat da rimuovere");
		else
			ready = true;
		chatname = c;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_chat_message(Gossip_chat_message.ChatOp.CLOSE_CHAT_OP, chatname, user.getName());
	}

	@Override
	protected void successOps() {
		listener.infoMessage("Chat rimossa");
	}

	@Override
	protected void failureOps() {
		switch (Gossip_parser.getFailType(JSONReply)) {
		
			case CHATUNKNOWN:
				listener.errorMessage("Chat inesistente");
				break;
			case OPNOTALLOWED:
				listener.errorMessage("Solo l'owner di una chat può chiuderla");
				break;
			default:
				unknownReplyError();
		}
	}
}
