package Server.Structures;

/**
 * Eccezione di chat già presente
 * 
 * @author Gioele Bertoncini
 *
 */
public class ChatAlreadyException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ChatAlreadyException() {
		super();
	}
	
	public ChatAlreadyException(String s) {
		super(s);
	}

}
