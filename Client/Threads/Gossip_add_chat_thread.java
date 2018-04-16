package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_chat_message;
import Messages.Server_side.Gossip_fail_message;
import Messages.Server_side.Gossip_success_message;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di creazione chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_add_chat_thread extends Gossip_client_thread {
	
	private String chatname;
	private Gossip_main_listener main;

	public Gossip_add_chat_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener l, Gossip_user u, String c) {
		super(i, o, s, l, u);
		
		if (u == null)
			throw new NullPointerException();
		if (c.isEmpty())
			listener.infoMessage("Insersci un nome per la nuova chat");
		else 
			ready = true;

		chatname = c;
		main = l;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_chat_message(Gossip_chat_message.ChatOp.CREATE_CHAT_OP, chatname, listener.getUser().getName());
	}

	@Override
	protected void successOps() {
		System.out.println(Gossip_parser.getSuccessType(JSONReply));
		if (Gossip_parser.getSuccessType(JSONReply) == Gossip_success_message.successMsg.CHATINFO) {
			listener.infoMessage("Chat creata con successo");
			try {
				main.addChatroomListener(Gossip_parser.getChat(JSONReply));
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("THERE?");
		}
		else {
			unknownReplyError();
			System.out.println("HERE?");
		}
	}

	@Override
	protected void failureOps() {
		if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.CHATALREADY)
			listener.infoMessage("Nome della chat già in uso");
		else
			unknownReplyError();
	}
}
