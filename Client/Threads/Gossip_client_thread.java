package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Client.Listeners.Gossip_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_client_message;
import Server.Structures.Gossip_user;

/**
 * Classe astratta che rappresenta il generico thread che invia richieste
 * 
 * @author Gioele Bertoncini
 *
 */
public abstract class Gossip_client_thread extends Thread{

	protected final String serverName = "localhost";
	protected final int port = 5000;
	
	protected DataInputStream input;
	protected DataOutputStream output;
	protected Socket socket;
	protected Gossip_user user;
	
	protected Gossip_listener listener;
	protected String serverReply;
	protected JSONObject JSONReply;
	protected Gossip_client_message request;
	
	protected boolean ready = false;
	
	public Gossip_client_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_listener l, Gossip_user u) {
		super();
		
		if (i == null || o == null || s == null || l == null)
			throw new NullPointerException();
		
		input = i;
		output = o;
		socket = s;
		listener = l;
		user = u;
	}
	
	public void run() {

		if (ready == true) {
			
			System.out.println("Costruisco richiesta");
			//costruisco richiesta
			makeRequest();
			
			try {
				
				//invio richiesta al server
				output.writeUTF(request.getJsonString());
				System.out.println("Richiesta inviata");
				
				//leggo risposta dal server
				serverReply = input.readUTF();
				System.out.println("Letta risposta: "+serverReply);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//interpreto la risposta
			parseReply(serverReply);
		}
	}
	
	/**
	 * Prepara la richiesta da inviare al server
	 */
	protected abstract void makeRequest();
	
	/**
	 * Operazioni da eseguire dopo aver ricevuto una risosta positiva dal server
	 */
	protected abstract void successOps();
	
	/**
	 * Operazioni da eseguire dopo aver ricevuto una risosta negativa dal server
	 */
	protected abstract void failureOps();
	
	/**
	 * Interpreta la risposta
	 */
	protected void parseReply(String s) {

		JSONReply = null;
		try {
			//ottengo oggetto JSON
			JSONReply = Gossip_parser.getJsonObject(s);
				
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//interpreto il tipo della risposta
		switch (Gossip_parser.getServerReplyType(JSONReply)) {
			
			case SUCCESS_OP:
				successOps();
				break;
		
			case FAIL_OP:
				failureOps();
				break;
		
			default:
				listener.errorMessage("Risposta del server sconosciuta");
				break;
		}
	}
	
	/**
	 * Visualizza messaggio di errore per una risposta inconsistente del server
	 */
	protected void unknownReplyError() {
		listener.errorMessage("Risposta del server sconosciuta");
	}
}
