import java.util.ArrayList;


public class Edge {
	private Node from;
	private Node to;
	private int propagationDelay;
	private int numSimulCircuits;
	private ArrayList<VirtualCircuit> circuits;
	
	public Edge(Node from,Node to, int propDelay, int numSimulCircuits) {
		this.from = from;
		this.to = to;
		propagationDelay = propDelay;
		this.numSimulCircuits = numSimulCircuits;
		circuits = new ArrayList<VirtualCircuit>();
	}	
	
	public int getPropagationDelay() {
		return propagationDelay;
	}
	
	public int getNumSimulCircuits() {
		return numSimulCircuits;
	}
	
	public Node getFrom(){
		return from;
	}
	
	public Node getTo(){
		return to;
	}
	
	public void addCircuit(VirtualCircuit circuit) {
		circuits.add(circuit);
	}
	
	public ArrayList<VirtualCircuit> getCircuits() {
		return circuits;
	}
	
	public int numCircuits() {
		return circuits.size();
	}
	
	public boolean hasCapacity() {
		if (numCircuits() < numSimulCircuits) {
			return true;
		} else {
			return false;
		}
	}
	
	public void cleanup(VirtualCircuit newCircuit) {
		double newEstablishTime = newCircuit.getEstablishTime();
		
		for (VirtualCircuit vc : circuits) {
			
		}
	}
}
