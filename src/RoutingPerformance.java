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
	private static Workload workload;
	private static int packetRate;
	private static UndirectedGraph graph;
	
	
	public static void main(String[] args) {
		handleArguments(args);		
		runCommand();
	}
	
	/**
	 * THINGS TO CONSIDER:
	 * 
	 * The number of virtual circuits established is only relevant to the edges that are affected.
	 * For example, for each line of workflow.txt, A VirtualCircuit(VC) object will be constructed and 
	 * added to each edge affected by the shortest path specified in said VC. Thus, the size of the list 
	 * of VCs in each edge will increment by 1. If the number of circuits exceeds the numSimulCircuits 
	 * field in any edge, any further circuits will be "blocked", and the packets lost. If the ttl of any 
	 * circuit is reached, said circuit will expire, and will be removed from all lists that contain it. 
	 * 
	 * STRATEGY:
	 * -> Give each Edge object a list of VirtualCircuits.
	 * 		-> Iterate through each node in the shortestPath field. Add the VirtualCircuit to each relevant
	 * 			edge.
	 * -> Each time we attempt to add a VirtualCircuit to an Edge,
	 * 		-> Check the list in that Edge, to see if any VCs have expired at the time of adding the new
	 * 			VC.
	 * 			-> If Yes, remove that VC. 
	 * 			-> If No, check if list.size() < numSimulCircuits.
	 * 				-> If Yes, add the new VC.
	 * 				-> If No, consider circuit blocked. (Count packets affected)
	 */
	private static void runCommand() {
		// Using CIRCUIT mode
		if(isCircuit) {
			// Using SHP
			if(routingScheme == 0){ 
				for(int i = 0; i < workload.getSize() ; i++) {
					//Run initGraph every time otherwise results from previous algo messes up.
					initGraph();
					
					SHP shp = new SHP(graph);
					System.out.println("Path from "+workload.getOrigins().get(i)+" to "+(workload.getDestinations().get(i))+" is:");
					Node from = graph.getNode(workload.getOrigins().get(i));
					Node to = graph.getNode(workload.getDestinations().get(i));
					VirtualCircuit circuit = new VirtualCircuit(
							shp.shortestPath(from,to), 
							workload.getEstablishTimes().get(i),
							workload.getOrigins().get(i),
							workload.getDestinations().get(i),
							workload.getTtlList().get(i));
				}
			// Using SDP
			} else if(routingScheme == 1) {
				for(int i = 0; i < workload.getSize() ; i++) {
					//Run initGraph every time otherwise results from previous algo messes up.
					initGraph();
					
					SDP sdp = new SDP(graph);
					System.out.println("Path from "+workload.getOrigins().get(i)+"to "+(workload.getDestinations().get(i))+" is:");
					Node from = graph.getNode(workload.getOrigins().get(i));
					Node to = graph.getNode(workload.getDestinations().get(i));
					VirtualCircuit circuit = new VirtualCircuit(
							sdp.shortestPath(from,to), 
							workload.getEstablishTimes().get(i),
							workload.getOrigins().get(i),
							workload.getDestinations().get(i),
							workload.getTtlList().get(i));
				}
			}
		}
	}
	
	public static void initGraph() {
		// Initialise Edges
		graph = new UndirectedGraph();
		for (int i = 0 ; i < topology.getSize() ; i++) {
			Node from = new Node(topology.getOrigins().get(i));
			Node to = new Node(topology.getDestinations().get(i));
			Edge edge1 = new Edge(from,to, 
					topology.getPropDelays().get(i), 
					topology.getNumSimulCircuits().get(i));
			Edge edge2 = new Edge(to,from, 
					topology.getPropDelays().get(i), 
					topology.getNumSimulCircuits().get(i));
			
			//Check whether or not the nodes are already in the graph.
			boolean fromExists = false;
			boolean toExists = false;
			
			for(Node n:graph.getNodes()){
				if(n.getName().equals(from.getName())){
					fromExists = true;
				}
				if(n.getName().equals(to.getName())){
					toExists = true;
				}
			}	
			
			//Add the nodes if they don't exist.
			if(!fromExists){
				graph.addNode(from);
			}
			if(!toExists){
				graph.addNode(to);		
			}
			
			//Undirected graph has edge going both ways.
			graph.addAdjacency(edge1);
			graph.addAdjacency(edge2);
		}
	}
	
	public static void handleArguments(String[] args) {
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
			
			// Handle the topology.txt argument
			if (args[2].endsWith(".txt")) {
				try{
		            File topoFile = new File(args[2]);
		            Scanner reader = new Scanner(topoFile);
		            topology = new Topology();
		            while (reader.hasNext()) {
		            	topology.getOrigins().add(reader.next());
		            	topology.getDestinations().add(reader.next());
		            	topology.getPropDelays().add(reader.nextInt());
		            	topology.getNumSimulCircuits().add(reader.nextInt());
		            }
		            reader.close();
		            System.out.println("Topology file: "+args[2]);
	            } catch (Exception e){
	            	System.err.println("Error: " + e.getMessage());
	            }
			} else {
				System.err.println("Expecting a .txt file as third argument (TOPOLOGY_FILE).");
				System.exit(1);
			}
			
			// Handle the workload.txt argument
			if (args[3].endsWith(".txt")) {
				try{
		            File workloadFile = new File(args[3]);
		            Scanner reader = new Scanner(workloadFile);
		            workload = new Workload();
		            while (reader.hasNext()) {
		            	workload.getEstablishTimes().add(reader.nextDouble());
		            	workload.getOrigins().add(reader.next());
		            	workload.getDestinations().add(reader.next());
		            	workload.getTtlList().add(reader.nextDouble());
		            }
		            reader.close();
		            System.out.println("Workload file: "+args[3]);
	            } catch (Exception e){
	            	System.err.println("Error: " + e.getMessage());
	            }
			} else {
				System.err.println("Expecting a .txt file as fourth argument (WORKLOAD_FILE).");
				System.exit(1);
			}
			
			// Handle the packetRate argument.
			int tempPacketRate = Integer.parseInt(args[4]);
			if (tempPacketRate > 0) {
				packetRate = tempPacketRate;
				System.out.println("Packet Rate: "+packetRate+"Packets / Second");
			} else {
				System.err.println("Expecting a positive integer as fifth argument (PACKET_RATE).");
				System.exit(1);
			}
		} else {
			System.err.println("Only accepting exactly 5 arguments - "
				+ "NETWORK_SCHEME, ROUTING_SCHEME, TOPOLOGY_FILE, WORKLOAD_FILE, PACKET_RATE");
		}

	}
}
