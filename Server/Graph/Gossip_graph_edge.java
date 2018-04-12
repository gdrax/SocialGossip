package Server.Graph;

/**
 * Struttura di un arco del grafo
 * 
 * @author Gioele Bertoncini
 */
public class Gossip_graph_edge<E> {

	//Nodo di origine dell'arco
	private Gossip_graph_node<E> src;
	
	//Nodo di arrivo dell'arco
	private Gossip_graph_node<E> dest;
	

	/**
	 * @param s Nodo di origine dell'arco
	 * @param d Nodo di destinazione dell'arco
	 */
	public Gossip_graph_edge(Gossip_graph_node<E> s, Gossip_graph_node<E> d) {
		src = s;
		dest = d;
	}
	
	/**
	 * Restituisce il nodo di origine di questo arco
	 * @return Nodo di origine dell'arco
	 */
	public Gossip_graph_node<E> getSrc() {
		return src;
	}
	
	/**
	 * Restituisce il nodo di destinazione di questo arco
	 * @return Nodo di destinazione dell'arco
	 */
	public Gossip_graph_node<E> getDest() {
		return dest;
	}
	
	/*
	 * Stampa sullo standard out la destinazione dell'arco
	 */
	public void printInfo() {
		System.out.println("Edge to "+dest.getData().toString());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_graph_edge ))
			return false;
		@SuppressWarnings("unchecked")
		Gossip_graph_edge<E> edge = (Gossip_graph_edge<E>)o;
		return (edge.getSrc().equals(this.getSrc()) && edge.getDest().equals(this.getDest()));
	}
}
