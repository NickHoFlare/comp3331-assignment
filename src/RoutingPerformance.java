import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class RoutingPerformance {
	private static boolean isCircuit;
	private static int routingScheme;
	private static Topology topology;
	
	public static void main(String[] args) {
		if (args.length == 5) {
			// If the boolean isCircuit is true, CIRCUIT was chosen as network scheme.
			// If the boolean isCircuit is false, PACKET was chosen as network scheme.
			if (args[0].equalsIgnoreCase("CIRCUIT")) {
				isCircuit = true;
				System.out.println("Network Scheme: circuit");
			} else if (args[0].equalsIgnoreCase("PACKET")) {
				isCircuit = false;
				System.out.println("Network Scheme: packet");
			} else {
				System.err.println("First argument must be either 'CIRCUIT' or 'PACKET'");
				System.exit(1);
			}
			
			// If routingScheme = 0, Shortest Hop Path (SHP) was chosen as routing scheme.
			// If routingScheme = 1, Shortest Delay Path (SDP) was chosen as routing scheme.
			// If routingScheme = 2, Least Loaded Path (LLP) was chosen as routing scheme.
			if (args[1].equalsIgnoreCase("SHP")) {
				routingScheme = 0;
				System.out.println("Routing Scheme: SHP");
			} else if (args[1].equalsIgnoreCase("SDP")) {
				routingScheme = 1;
				System.out.println("Routing Scheme: SDP");
			} else if (args[1].equalsIgnoreCase("LLP")) {
				routingScheme = 2;
				System.out.println("Routing Scheme: LLP");
			} else {
				System.err.println("Second argument must be either 'SHP', 'SDP' or 'LLP'");
				System.exit(1);
			}
			
			if (args[2].endsWith(".txt")) {
				try{
		            File topoFile = new File(args[2]);
		            Scanner reader = new Scanner(topoFile);
		            topology = new Topology();
		            while (reader.hasNext()) {
		            	topology.getOrigins().add(reader.next());
		            	topology.getDestinations().add(reader.next());
		            	topology.getPropDelays().add(reader.nextInt());
		            	topology.getNumSimulCircuitsList().add(reader.nextInt());
		            }
		            reader.close();
	            } catch (Exception e){
	            	System.err.println("Error: " + e.getMessage());
	            }
			} else {
				System.err.println("Expecting a .txt file as third argument (TOPOLOGY_FILE).");
				System.exit(1);
			}
			
			if (args[3].matches(".txt$")) {
				
			} else {
				System.err.println("Expecting a .txt file as fourth argument (WORKLOAD_FILE).");
				System.exit(1);
			}
		} else {
			System.err.println("Only accepting exactly 5 arguments - "
				+ "NETWORK_SCHEME, ROUTING_SCHEME, TOPOLOGY_FILE, WORKLOAD_FILE, PACKET_RATE");
		}

	}
}
