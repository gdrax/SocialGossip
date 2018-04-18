package Server;

/**
 * Eccezione di porta non trovata (tutte le porte sono già in uso)
 * 
 * @author Gioele Bertoncini
 *
 */
public class PortNotFoundException extends Exception{

	private static final long serialVersionUID = 3963616439932579757L;

	public PortNotFoundException() {
		super();
	}

	public PortNotFoundException(String s) {
		super();
	}
}
