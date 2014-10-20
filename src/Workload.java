import java.util.ArrayList;

public class Workload {
	private ArrayList<Double> establishTimes;
	private ArrayList<String> origins;
	private ArrayList<String> destinations;
	private ArrayList<Double> activeDurationList;
	
	public Workload() {
		origins = new ArrayList<String>();
		destinations = new ArrayList<String>();
		establishTimes = new ArrayList<Double>();
		activeDurationList = new ArrayList<Double>();
	}
	
	public ArrayList<String> getOrigins() {
		return origins;
	}
	
	public ArrayList<String> getDestinations() {
		return destinations;
	}
	
	public ArrayList<Double> getEstablishTimes() {
		return establishTimes;
	}
	
	public ArrayList<Double> getActiveDurationList() {
		return activeDurationList;
	}
	
	public int getSize(){
		return origins.size();
	}
}
