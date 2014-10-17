
public class Edge {
	private String[] nodes;
	private int propagationDelay;
	private int numSimulCircuits;
	
	public Edge(String[] nodes, int propDelay, int numSimulCircuits) {
		nodes = new String[2];
		propagationDelay = propDelay;
		this.numSimulCircuits = numSimulCircuits;
	}	
	
	public String[] getNodes() {
		return nodes;
	}
	
	public int getPropagationDelay() {
		return propagationDelay;
	}
	
	public int getNumSimulCircuits() {
		return numSimulCircuits;
	}
}
