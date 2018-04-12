package Client.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import Client.Listeners.Gossip_friend_chat_listener;
import Messages.Gossip_parser;
import Messages.Client_side.Gossip_action_file_message;
import Server.Gossip_config;
import Server.Structures.Gossip_user;

/**
 * Thread che si occupa di inviare un file
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_send_file_thread extends Gossip_client_thread {
	
	private String receiver;
	private String filename;
	private Gossip_friend_chat_listener chat;
	private File file;

	public Gossip_send_file_thread(DataInputStream i, DataOutputStream o, Socket s, Gossip_friend_chat_listener l, Gossip_user u, String r, String f) {
		super(i, o, s, l, u);
		
		if (r == null)
			throw new NullPointerException();
		receiver = r;
		chat = l;
		filename = f;
		if (f.isEmpty())
			chat.infoMessage("Inserisci un nome di file");
		else 
			ready = checkFile();
	}


	/**
	 * @return true se il file esiste ed è regolare, false altrimenti
	 */
	private boolean checkFile() {
		file = new File(new File("").getAbsolutePath()+Gossip_config.UPLOAD_DIR+filename);

		if (file.exists() && file.isFile())
			return true;
		else {
			chat.infoMessage("File non trovato");
			return false;
		}
	}
	
	
	@Override
	protected void makeRequest() {
		request = new Gossip_action_file_message(filename, user.getName(), receiver);	
	}

	@Override
	protected void successOps() {
		SocketChannel friendSocket = null;
		chat.getTextArea().append("[Me]: [FILE]: "+filename+"\n");
		//recupero info connessione
		String hostname = Gossip_parser.getHostname(JSONReply);
		int port = Gossip_parser.getPort(JSONReply);
		
		if (hostname == null) {
			chat.errorMessage("Connessione con client non riuscita");
			return;
		}
		try {
			
			//inizializzo socket
			friendSocket  = SocketChannel.open();
			friendSocket.connect(new InetSocketAddress(hostname, port));
			
			//apro file e ottengo filechannel
			RandomAccessFile openFile = new RandomAccessFile(file, "r");
			FileChannel inputChannel = openFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);
			
			//leggo dal file e scrivo sul socket
			while(inputChannel.read(buf) > 0) {
				buf.flip();
				
				while(buf.hasRemaining())
					friendSocket.write(buf);
				
				buf.clear();

			}
			
			openFile.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (friendSocket != null)
					friendSocket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
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
