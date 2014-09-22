import java.util.ArrayList;


public class Topology {
	private ArrayList<String> origins;
	private ArrayList<String> destinations;
	private ArrayList<Integer> propDelays;
	private ArrayList<Integer> numSimulCircuitsList;
	
	public Topology() {
		origins = new ArrayList<String>();
		destinations = new ArrayList<String>();
		propDelays = new ArrayList<Integer>();
		numSimulCircuitsList = new ArrayList<Integer>();
	}
	
	public ArrayList<String> getOrigins() {
		return origins;
	}
	
	public ArrayList<String> getDestinations() {
		return destinations;
	}
	
	public ArrayList<Integer> getPropDelays() {
		return propDelays;
	}
	
	public ArrayList<Integer> getNumSimulcircuitsList() {
		return numSimulCircuitsList;
	}
}
