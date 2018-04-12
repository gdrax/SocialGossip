package Client.Threads;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import Server.Gossip_config;

/**
 * Thread che si occupa di ricevere un file
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_file_receiver extends Thread {

	private ServerSocketChannel socket;
	private String filename;
	
	public Gossip_file_receiver(ServerSocketChannel s, String f) {
		if (s == null || f == null)
			throw new NullPointerException();
		socket = s;
		filename = f;
	}
	
	public void run() {
		String downloadPath = Gossip_config.DOWNLOAD_DIR+filename;	
		
		//creo il path
		Path path = Paths.get(new File("").getAbsolutePath()+downloadPath);
		
		SocketChannel friendSocket = null;
		try {
			
			//apro il file
			FileChannel file = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
			
			//accetto conenssione
			friendSocket = socket.accept();
			
			ByteBuffer buf = ByteBuffer.allocate(1024);
			
			//leggo dal socket e scrivo su file
			while (friendSocket.read(buf) > 0) {
				
				buf.flip();
				
				while (buf.hasRemaining())
					file.write(buf);
				
				buf.clear();
			}
			
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//chiudo connessioni
				if (socket != null)
					socket.close();
				if (friendSocket != null)
					friendSocket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
