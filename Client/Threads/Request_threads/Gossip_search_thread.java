package Client.Threads.Request_threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Listeners.Gossip_main_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Action_messages.Gossip_action_message;
import Messages.Server_side.Fail_messages.Gossip_fail_message;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di informazioni su un utente
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_search_thread extends Gossip_client_thread {
	
	private String toSearch; //nome dell'utente da cercare
	private Gossip_main_listener main; //riferimento al controller principale del client
	
	public Gossip_search_thread (DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener l, Gossip_user u, String ts) {
		super(i, o, s, l, u);
		
		if (u == null)
			throw new NullPointerException();
		if (ts == null)
			listener.infoMessage("Inserisci il nome da cercare");
		else
			ready = true;
		toSearch = ts;
		main = l;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_action_message(Gossip_action_message.Action.LOOKUP_OP, user.getName(), toSearch);
	}

	@Override
	protected void successOps() {
		Gossip_user searchedUser = Gossip_parser.getUserFound(JSONReply);
		if (searchedUser != null)
			//apro finestra informazioni dell'utente cercato
			main.addSearchedUserListener(searchedUser);
		else
			listener.errorMessage("Errore nella ricerca dell'utente");
	}

	@Override
	protected void failureOps() {
		if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.NICKUNKNOWN)
			listener.infoMessage("Utente non trovato");
	}
}
