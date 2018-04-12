package Client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Client.Listeners.Gossip_login_listener;
import Server.Gossip_config;

/**
 * Main di avvio del client
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_client {
	
	public static void main(String[] args) {
		
		DataInputStream input = null;
		DataOutputStream output = null;
		Socket socket = null;
		
		
		try {
			//inizializzo socket
			socket = new Socket(Gossip_config.SERVER_NAME, Gossip_config.TCP_PORT);
			
			//recuper gli stream
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			output = new DataOutputStream(socket.getOutputStream());
			
			//apro finestra di login
			Gossip_login_listener login = new Gossip_login_listener(input, output, socket);
			login.listen();
			
			
		} catch (IOException e) {
			System.out.println("Server non attivo");
		}

	}
}
