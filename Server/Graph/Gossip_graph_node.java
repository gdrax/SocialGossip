package Server.Graph;

import java.util.ArrayList;

/**
 * Struttura di un nodo del grafo
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_graph_node<E> {
	//Contenuto del nodo
	private E data;
	
	//Lista degli archi uscenti dal nodo
	private ArrayList<Gossip_graph_edge<E>> edges;
	
	
	public Gossip_graph_node(E d) {
		data = d;
		edges = new ArrayList<Gossip_graph_edge<E>>();
	}
	
	/**
	 * @param node: nodo destinazione dell'arco
	 * @return true: successo, false: fallimento
	 * @throws NullPointerException
	 */
	public boolean addEdge(Gossip_graph_node<E> node) throws NullPointerException {
		if (node == null) throw new NullPointerException();
		Gossip_graph_edge<E> newEdge = new Gossip_graph_edge<E>(this, node);
		
		if (edges.contains(newEdge) == false)
			edges.add(newEdge);
		return true;
	}
	
	/**
	 * @param dest: nodo destinazione dell'arco
	 * @return indice dell'arco nell'array (-1: arco non presente)
	 * @throws NullPointerException
	 */
	public int containsEdge(E dest) throws NullPointerException {
		if (dest == null) throw new NullPointerException();
		Gossip_graph_edge<E> searchEdge = new Gossip_graph_edge<E>(this, new Gossip_graph_node<E>(dest));
		return edges.indexOf(searchEdge);
	}
	
	/**
	 * @param dest: nodo destinazione dell'arco
	 * @return true: successo, false: nodo destinazione non trovato
	 */
	public boolean removeEdge(E dest) {
		int i = this.containsEdge(dest);
		if (i != -1) {
			edges.remove(i);
			return true;
		}
		return false;
	}
	
	/**
	 * @return contenuto del nodo
	 */
	public E getData() {
		return data;
	}
	
	/**
	 * @return lista degli archi uscenti
	 */
	public ArrayList<Gossip_graph_edge<E>> getEdges() {
		return edges;
	}
	
	/**
	 * Stampa sullo standard out nome e lista di archi del nodo corrente
	 */
	public void printInfo() {
		System.out.println("Node info: "+data.toString());
		System.out.println("Edges list:");
		for (Gossip_graph_edge<E> edge: edges) {
			edge.printInfo();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Gossip_graph_node)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Gossip_graph_node<E> node = (Gossip_graph_node<E>)o;
		return node.getData().equals(this.getData());
	}
}
