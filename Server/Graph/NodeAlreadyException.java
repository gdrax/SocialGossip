package Server.Graph;

/**
 * Eccezione sollevata quando un nodo è già presente
 * 
 * @author Gioele Bertoncini
 *
 */
public class NodeAlreadyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NodeAlreadyException() {
		super();
	}
	
	public NodeAlreadyException(String s) {
		super();
	}
}
