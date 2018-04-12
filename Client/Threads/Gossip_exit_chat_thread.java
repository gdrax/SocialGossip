package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_chat_message;
import Messages.Server_side.Gossip_fail_message;
import Server.Structures.Gossip_chat;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di uscita da chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_exit_chat_thread extends Gossip_client_thread {
	
	private String chatname;
	private Gossip_main_listener main;

	public Gossip_exit_chat_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener l, Gossip_user u, String c) {
		super(i, o, s, l, u);
		
		if (u == null)
			throw new NullPointerException();
		if (c == null)
			listener.infoMessage("Seleziona una chat");
		else
			ready = true;
		chatname = c;
		main = l;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_chat_message(Gossip_chat_message.ChatOp.REMOVEME_OP, chatname, user.getName());
	}

	@Override
	protected void successOps() {
		//rimuovo il listener
		main.removeChatroomListener(new Gossip_chat(chatname));
	}

	@Override
	protected void failureOps() {
		if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.CHATUNKNOWN)
			listener.errorMessage("Chat inesistente");
		else
			unknownReplyError();
	}
}
