package Client.Threads.Request_threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_chat_message;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di inserimento in una chat
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_enter_chat_thread extends Gossip_client_thread {
	
	private String chatname; //nome della chat nella quale entrare
	private Gossip_main_listener main; //riferimento al controller principale del client

	public Gossip_enter_chat_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener l, Gossip_user u, String c) {
		super(i, o, s, l, u);
		if (u == null)
			throw new NullPointerException();
		if (c == null)
			listener.infoMessage("Seleziona una chat");
		else
			ready = true;
		
		main = l;
		chatname = c;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_chat_message(Gossip_chat_message.ChatOp.ADDME_OP, chatname, user.getName());
	}

	@Override
	protected void successOps() {
		try {
			//aggiungo e apro il listener della chat
			main.addChatroomListener(main.getChatroom(chatname));
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void failureOps() {
		switch (Gossip_parser.getFailType(JSONReply)) {
			case CHATUNKNOWN:
				listener.errorMessage("Chat inesistente");
				break;
			case NICKALREADY:
			try {
				//l'utente faceva già parte della chat, aprò il listener
				main.addChatroomListener(main.getChatroom(chatname));
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			}
				break;
			default:
				unknownReplyError();
		}
	}
}
