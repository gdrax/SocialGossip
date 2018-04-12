package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.Listeners.Gossip_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_action_message;
import Messages.Server_side.Gossip_fail_message;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di eliminazione relazione di amicizia
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_remove_friend_thread extends Gossip_client_thread {
	
	private Gossip_user friend;

	public Gossip_remove_friend_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_listener l, Gossip_user u, Gossip_user f) {
		super(i, o, s, l, u);
		
		if (u == null || f == null)
			throw new NullPointerException();

		friend = f;
		ready = true;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_action_message(Gossip_action_message.Action.RM_FRIENDSHIP_OP, user.getName(), friend.getName());
	}

	@Override
	protected void successOps() {
		listener.infoMessage(friend.getName()+" rimosso dagli amici");
	}

	@Override
	protected void failureOps() {
		if (Gossip_parser.getFailType(JSONReply) == Gossip_fail_message.failMsg.NICKUNKNOWN)
			listener.errorMessage("Utente sconosciuto");
		else
			unknownReplyError();
	}
}
