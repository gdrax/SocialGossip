package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Client.Listeners.Gossip_main_listener;
import Messages.Client_side.Gossip_client_message;
import Server.Graph.NodeNotFoundException;
import Server.Structures.Gossip_user;

/**
 * Thread che invia richiesta di logout
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_logout_thread extends Gossip_client_thread {
	private Gossip_main_listener main;
	
	public Gossip_logout_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_main_listener l, Gossip_user u) {
		super(i, o, s, l, u);
		
		if (u == null)
			throw new NullPointerException();
		
		main = l;
		ready = true;
	}

	@Override
	protected void makeRequest() {
		request = new Gossip_client_message(Gossip_client_message.Op.LOGOUT_OP, user.getName());
	}

	@Override
	protected void successOps() {
		try {
			//deregistro la mia interfaccia RMI
			main.getServerInterface().unregisterForCallback(user.getName());
			UnicastRemoteObject.unexportObject(main.getCallback(), true);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} finally {
			main.closeConnection();
			main.closeWindow();
			main.closeChatroomConnections();
			main.closeReceiverThread();
		}
	}

	@Override
	protected void failureOps() {
		unknownReplyError();
	}
}
