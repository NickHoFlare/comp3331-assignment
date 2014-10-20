import java.util.ArrayList;

/**
 * This class creates a VirtualCircuit object.
 * If CIRCUIT mode is on, one VirtualCircuit object is created for each line of the workload file.
 * If PACKET mode is on, multiple VirtualCirtuit objects are created, one for each packet. 
 */
public class VirtualCircuit {
	private ArrayList<Node> shortestPath;
	private double establishTime;
	private String origin;
	private String destination;
	private double ttl;
	private boolean blocked;
	
	public VirtualCircuit(ArrayList<Node> path, double establishTime, String origin, String destination, double ttl) {
		shortestPath = path;
		this.establishTime = establishTime;
		this.origin = origin;
		this.destination = destination;
		this.ttl = ttl;
		blocked = false;
	}
	
	public ArrayList<Node> getShortestPath() {
		return shortestPath;
	}
	
	public double getEstablishTime() {
		return establishTime;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public double getTTL() {
		return ttl;
	}
	
	public boolean blocked() {
		return blocked;
	}
	
	public void setBlocked() {
		blocked = true;
	}
}
