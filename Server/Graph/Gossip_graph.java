package Server.Graph;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Struttura di un grafo non orientato
 * 
 * @author Gioele Bertoncini
 *
 */
public class Gossip_graph<E> {

	//tabella hash contentente i nodi del grafo
	private Hashtable<E, Gossip_graph_node<E>> adj;
	
	
	public Gossip_graph() {
		adj = new Hashtable<E, Gossip_graph_node<E>>();
	}
	
	/**
	 * Controlla se il grafo contiene un nodo
	 * 
	 * @param node: contenuto del nodo da controllare
	 * @return true: nodo presente, false: nodo non presente
	 * @throws NullPointerException: parametro nullo
	 * @throws NodeNotFoundException: nodo non trovato
	 */
	public boolean checkNode(E node) throws NullPointerException, NodeNotFoundException {
		synchronized (adj) {
			if (node == null)
				throw new NullPointerException();
			if (adj.containsKey(node))
				return true;
			else
				throw new NodeNotFoundException();
		}
	}
	
	/**
	 * Aggiunge un arco al grafo
	 * 
	 * @param src: Nodo di origine dell'arco
	 * @param dest: Nodo di destinazione dell'arco
	 * @return true: successo, false: arco già presente
	 * @throws: NodeNotFoundException: nodo non trovato
	 */
	public boolean addEdge(E src, E dest) throws NodeNotFoundException {
		synchronized (adj) {
			if (checkNode(src) && checkNode(dest)) {
				try {
					if (adj.get(src).containsEdge(adj.get(dest).getData()) != -1)
						return false;
					if (adj.get(dest).containsEdge(adj.get(src).getData()) != -1)
						return false;
					adj.get(src).addEdge(adj.get(dest));
					adj.get(dest).addEdge(adj.get(src));
				} catch (NullPointerException e) {}
				return true;
			}
			return false;
		}
	}
	

	/**
	 * Rimuove un arco dal grafo
	 * 
	 * @param src: Nodo di origine dell'arco
	 * @param dest: Nodo di destinazione dell'arco
	 * @return true: successo, false: fallimento
	 */
	public boolean removeEdge(E src, E dest) {
		boolean ret = false;
		synchronized (adj) {
			try {
				if (checkNode(src) && checkNode(dest)) {
					ret = adj.get(src).removeEdge(dest);
					ret = adj.get(dest).removeEdge(src);
				}
			} catch (NodeNotFoundException e) {
				return true;
			}
			return ret;
		}
	}
	
	/**
	 * Aggiunge un nodo al grafo
	 * 
	 * @param data: contenuto del nodo da aggiungere
	 * @return nodo aggiunto
	 * @throws NodeAlreadyException: nodo già presente nel grafo
	 */
	public boolean addNode (E data) throws NodeAlreadyException {
		Gossip_graph_node<E> newNode = new Gossip_graph_node<E>(data);
		synchronized (adj) {
			try {
				if (checkNode(data))
					throw new NodeAlreadyException();
			} catch (NodeNotFoundException e) {
				adj.put(data, newNode);
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Rimuove un nodo dal grafo
	 * 
	 * @param data: contenuto del nodo da rimuovere
	 * @return true: successo, false: fallimento
	 */
	public boolean removeNode(E data) {
		boolean ret = false;
		synchronized (adj) {
			if (!adj.containsKey(data)) {
				return true;
			}
			for (Gossip_graph_edge<E> edge: adj.get(data).getEdges()) {
				ret = adj.get(edge.getDest().getData()).removeEdge(adj.get(data).getData());
			}
			adj.remove(data);
			return ret;
		}
	}
	
	/**
	 * @param data
	 * @return
	 * @throws NullPointerException
	 * @throws NodeNotFoundException
	 */
	public E getNode(E data) throws NodeNotFoundException {
		if (data == null)
			throw new NullPointerException();
		synchronized (adj) {
			for (E key: adj.keySet()) {
				if (key.equals(data))
					return key;
			}
			throw new NodeNotFoundException();
		}
	}
	
	/**
	 * @return la lista dei dati contenuti nei nodi del grafo
	 */
	public ArrayList<E> getNodes() {
		ArrayList<E> list = new ArrayList<E>();
		synchronized (adj) {
			for (Gossip_graph_node<E> node: adj.values()) {
				list.add(node.getData());
			}
			return list;
		}
	}
	
	/**
	 * Stampa il contenuto del grafo sullo standard out
	 */
	public void printGraph() {
		synchronized (adj)  {
			for (Gossip_graph_node<E> node: adj.values()) {
				node.printInfo();
			}
		}
	}
}
