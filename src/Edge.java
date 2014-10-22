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
		System.out.println("numCircuits: "+numCircuits()+", numSimulCircuits field: "+numSimulCircuits);
		if (numCircuits() < numSimulCircuits) {
			System.out.println("System has capacity.");
			return true;
		} else {
			System.out.println("Insufficient capacity.");
			return false;
		}
	}
	
	public void cleanup(VirtualCircuit newCircuit) {
		double newEstablishTime = newCircuit.getEstablishTime();
		
		for (int i = 0 ; i < circuits.size() ; i++) {
			double expireTime = circuits.get(i).getEstablishTime() + circuits.get(i).getTTL();
			// If the VC has already expired at the time of adding the new circuit, remove the old one.
			if (expireTime < newEstablishTime) {
				System.out.println("circuit expired, removing.");
				circuits.remove(i);
			}
		}
	}
}
