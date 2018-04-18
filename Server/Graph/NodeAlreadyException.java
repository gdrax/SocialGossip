package Server.Graph;

/**
 * Eccezione sollevata quando un nodo è già presente
 * 
 * @author Gioele Bertoncini
 *
 */
public class NodeAlreadyException extends RuntimeException {

	private static final long serialVersionUID = 5257438701436602461L;

	public NodeAlreadyException() {
		super();
	}
	
	public NodeAlreadyException(String s) {
		super();
	}
}
