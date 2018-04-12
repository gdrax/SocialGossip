package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 * Opzioni di configurazione di server e client
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_config {

	public static final int TCP_PORT = 5000;
	public static final String SERVER_NAME = "localhost";
	public static final int RMI_PORT = 7000;
	public static final String RMI_NAME = "Gossip_notification";
	public static final String MULTICAST_FIRST = "226.0.0.0";
	public static final String MUTLICAST_LAST = "226.0.0.255";
	public static final String DOWNLOAD_DIR = "\\downloads\\";
	public static final String UPLOAD_DIR = "\\uploads\\";
	
	/**
	 * @return una porta libera
	 * @throws IOException 
	 * @throws Exception
	 */
	public static int findPort() throws PortNotFoundException, IOException {
		int min = 1025;
		int max = 65535;
		
		for (int i=min; i<max+1; i++) {
			DatagramSocket tmp = null;
			ServerSocket tmp2 = null;
			
			try {
				tmp2 = new ServerSocket(i);
				tmp2.setReuseAddress(true);
				tmp = new DatagramSocket(i);
				tmp.setReuseAddress(true);
				return i;
			} catch (IOException e) {}
			  finally {
				  if (tmp != null)
					  tmp.close();
				  if (tmp2 != null)
					  tmp2.close();
			  }
		}
		throw new PortNotFoundException();
	}	
}
