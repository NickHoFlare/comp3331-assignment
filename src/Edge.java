
public class Edge {
	private Node from;
	private Node to;
	private int propagationDelay;
	private int numSimulCircuits;
	
	public Edge(Node from,Node to, int propDelay, int numSimulCircuits) {
		this.from = from;
		this.to = to;
		propagationDelay = propDelay;
		this.numSimulCircuits = numSimulCircuits;
	}	
	
	public int getPropagationDelay() {
		return propagationDelay;
	}
	
	public int getNumSimulCircuits() {
		return numSimulCircuits;
	}
	
	public Node getTo(){
		return to;
	}
}
