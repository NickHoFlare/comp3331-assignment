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
	
	private static int vcRequests;
	private static int successfulVCs;
	private static int totalPackets;
	private static int routedPackets;
	private static double routedPacketsPercent;
	private static int blockedPackets;
	private static double blockedPacketsPercent;
	private static int totalHops;
	private static double averageCircuitHops;
	private static double totalPropDelay;
	private static double averagePropDelay;
	
	public static void main(String[] args) {
		initStats();
		handleArguments(args);		
		runCommand();
		calculateStats();
		printStats();
	}
	
	public static void initStats() {
		vcRequests = 0;
		successfulVCs = 0;
		totalPackets = 0;
		routedPackets = 0;
		routedPacketsPercent = 0;
		blockedPackets = 0;
		blockedPacketsPercent = 0;
		totalHops = 0;
		averageCircuitHops = 0;
		totalPropDelay = 0;
		averagePropDelay = 0;
	}
	
	//TODO: move initGraph out of the loop. make sure graph state persists. 
	// ensure that additions/deletions of VCs properly affect state of the graph
	
	private static void runCircuit() {
		initGraph();
		
		// Using SHP
		if(routingScheme == 0){ 
			for(int i = 0; i < workload.getSize() ; i++) {
				System.out.println("------------------------------");
				int numPackets = (int) Math.ceil(packetRate * workload.getActiveDurationList().get(i)); //No of packets that will potentially be sent for line of the workload
				totalPackets += numPackets;
				System.out.println("num packets being sent out: "+numPackets);
				
				// Run initGraph every time otherwise results from previous algo messes up.
				// initGraph();
				
				// Run Shortest Hop Algorithm
				refreshGraph(); //Prevents prev search interfering with current search.
				SHP shp = new SHP(graph);
				System.out.println("Path from "+workload.getOrigins().get(i)+" to "+(workload.getDestinations().get(i))+" is:");
				Node from = graph.getNode(workload.getOrigins().get(i));
				Node to = graph.getNode(workload.getDestinations().get(i));
				ArrayList<Node> shortestPath = shp.shortestPath(from,to);
				
				// Create virtual circuit using generated shortest path
				VirtualCircuit circuit = new VirtualCircuit(
						shortestPath, 
						workload.getEstablishTimes().get(i),
						workload.getOrigins().get(i),
						workload.getDestinations().get(i),
						workload.getActiveDurationList().get(i));
				vcRequests++;
				
				// Find the list of edges between the nodes of the shortest path
				ArrayList<Edge> shortestPathEdges = getShortestPathEdges(shortestPath);
				
				manageCircuits(circuit, shortestPathEdges, numPackets);
			}
		// Using SDP
		} else if(routingScheme == 1) {
			for(int i = 0; i < workload.getSize() ; i++) {
				System.out.println("------------------------------");
				int numPackets = (int) Math.ceil(packetRate * workload.getActiveDurationList().get(i));
				totalPackets += numPackets;
				
				System.out.println("num packets being sent out: "+numPackets);
				
				// Run Shortest Delay Path Algorithm
				refreshGraph(); //Prevents prev search interfering with current search.
				SDP sdp = new SDP(graph);
				System.out.println("Path from "+workload.getOrigins().get(i)+"to "+(workload.getDestinations().get(i))+" is:");
				Node from = graph.getNode(workload.getOrigins().get(i));
				Node to = graph.getNode(workload.getDestinations().get(i));
				ArrayList<Node> shortestPath = sdp.shortestPath(from,to);
				
				
				// Create virtual circuit using generated shortest path
				VirtualCircuit circuit = new VirtualCircuit(
						shortestPath, 
						workload.getEstablishTimes().get(i),
						workload.getOrigins().get(i),
						workload.getDestinations().get(i),
						workload.getActiveDurationList().get(i));
				vcRequests++;
				
				// Find the list of edges between the nodes of the shortest path
				ArrayList<Edge> shortestPathEdges = getShortestPathEdges(shortestPath);
				
				manageCircuits(circuit, shortestPathEdges, numPackets);
			}
		// Using LLP
		} else if(routingScheme == 2) {
			for(int i = 0; i < workload.getSize() ; i++) {
				System.out.println("------------------------------");
				int numPackets = (int) Math.ceil(packetRate * workload.getActiveDurationList().get(i));
				totalPackets += numPackets;
				
				System.out.println("num packets being sent out: "+numPackets);
				
				// Run LLP Delay Path Algorithm
				refreshGraph(); //Prevents prev search interfering with current search.
				LLP llp = new LLP(graph);
				System.out.println("Path from "+workload.getOrigins().get(i)+"to "+(workload.getDestinations().get(i))+" is:");
				Node from = graph.getNode(workload.getOrigins().get(i));
				Node to = graph.getNode(workload.getDestinations().get(i));
				ArrayList<Node> shortestPath = llp.shortestPath(from,to);
				
				
				// Create virtual circuit using generated shortest path
				VirtualCircuit circuit = new VirtualCircuit(
						shortestPath, 
						workload.getEstablishTimes().get(i),
						workload.getOrigins().get(i),
						workload.getDestinations().get(i),
						workload.getActiveDurationList().get(i));
				vcRequests++;
				
				// Find the list of edges between the nodes of the shortest path
				ArrayList<Edge> shortestPathEdges = getShortestPathEdges(shortestPath);
				
				manageCircuits(circuit, shortestPathEdges, numPackets);
			}
		}
	}
	
	private static void runPacket() {
		initGraph();
		
		// Using SHP
		if(routingScheme == 0){ 
			for(int i = 0 ; i < workload.getSize() ; i++) {
				int numPackets = (int) Math.ceil(packetRate * workload.getActiveDurationList().get(i));
				totalPackets += numPackets;
				
				double ttl = 1.0 / packetRate;
				double currentStart = workload.getEstablishTimes().get(i); //Start of the current virtual circuit we are working with.
				
				for (int j = 0 ; j < numPackets ; j++) {
					System.out.println("------------------------------");

					System.out.println("currentStart is "+currentStart+", ttl is "+ttl+", packetRate is "+packetRate);
					
					// Run Shortest Hop Algorithm
					refreshGraph();
					SHP shp = new SHP(graph);
					System.out.println("Path from "+workload.getOrigins().get(i)+" to "+(workload.getDestinations().get(i))+" is:");
					Node from = graph.getNode(workload.getOrigins().get(i));
					Node to = graph.getNode(workload.getDestinations().get(i));
					ArrayList<Node> shortestPath = shp.shortestPath(from,to);
					
					// Create virtual circuit using generated shortest path
					VirtualCircuit circuit = new VirtualCircuit(
							shortestPath, 
							currentStart,
							workload.getOrigins().get(i),
							workload.getDestinations().get(i),
							ttl);
					vcRequests++;
					
					// Find the list of edges between the nodes of the shortest path
					ArrayList<Edge> shortestPathEdges = getShortestPathEdges(shortestPath);
					
					//1 Packet since we are making a new vc for each packet.
					manageCircuits(circuit, shortestPathEdges, 1);
					
					currentStart += ttl;
				}
			}
			
		// Using SDP
		} else if (routingScheme == 1) {
			for(int i = 0 ; i < workload.getSize() ; i++) {
				int numPackets = (int) Math.ceil(packetRate * workload.getActiveDurationList().get(i));
				totalPackets += numPackets;
				
				double ttl = 1.0 / packetRate;
				double currentStart = workload.getEstablishTimes().get(i);
				
				for (int j = 0 ; j < numPackets ; j++) {
					System.out.println("------------------------------");
					System.out.println("currentStart is "+currentStart+", ttl is "+ttl+", packetRate is "+packetRate);
					
					// Run Shortest Hop Algorithm
					refreshGraph(); //Prevents prev search interfering with current search.

					SDP sdp = new SDP(graph);
					System.out.println("Path from "+workload.getOrigins().get(i)+" to "+(workload.getDestinations().get(i))+" is:");
					Node from = graph.getNode(workload.getOrigins().get(i));
					Node to = graph.getNode(workload.getDestinations().get(i));
					ArrayList<Node> shortestPath = sdp.shortestPath(from,to);
					
					// Create virtual circuit using generated shortest path
					VirtualCircuit circuit = new VirtualCircuit(
							shortestPath, 
							currentStart,
							workload.getOrigins().get(i),
							workload.getDestinations().get(i),
							ttl);
					vcRequests++;
					
					// Find the list of edges between the nodes of the shortest path
					ArrayList<Edge> shortestPathEdges = getShortestPathEdges(shortestPath);
					
					manageCircuits(circuit, shortestPathEdges, 1);
					
					currentStart += ttl;
				}
			}
			
		// Using LLP
		} else if (routingScheme == 2) {
			for(int i = 0 ; i < workload.getSize() ; i++) {
				int numPackets = (int) Math.ceil(packetRate * workload.getActiveDurationList().get(i));
				totalPackets += numPackets;
				
				double ttl = 1.0 / packetRate;
				double currentStart = workload.getEstablishTimes().get(i);
				
				for (int j = 0 ; j < numPackets ; j++) {
					System.out.println("------------------------------");
					System.out.println("currentStart is "+currentStart+", ttl is "+ttl+", packetRate is "+packetRate);
					
					
					// Run Shortest Hop Algorithm
					refreshGraph(); //Prevents prev search interfering with current search.

					LLP llp = new LLP(graph);
					System.out.println("Path from "+workload.getOrigins().get(i)+" to "+(workload.getDestinations().get(i))+" is:");
					Node from = graph.getNode(workload.getOrigins().get(i));
					Node to = graph.getNode(workload.getDestinations().get(i));
					ArrayList<Node> shortestPath = llp.shortestPath(from,to);
					
					// Create virtual circuit using generated shortest path
					VirtualCircuit circuit = new VirtualCircuit(
							shortestPath, 
							currentStart,
							workload.getOrigins().get(i),
							workload.getDestinations().get(i),
							ttl);
					vcRequests++;
					
					// Find the list of edges between the nodes of the shortest path
					ArrayList<Edge> shortestPathEdges = getShortestPathEdges(shortestPath);
					
					manageCircuits(circuit, shortestPathEdges, 1);
					
					currentStart += ttl;
				}
			}
		}
	}
	
	// This can be used for both VCN and VPN. If VPN, set numPackets to 1.
	public static void manageCircuits(VirtualCircuit circuit, ArrayList<Edge> shortestPathEdges, int numPackets) {
		// We use tempTotalHops and tempTotalPropDelay because we don't want to increment the real fields if the VC
		// ends up being blocked. So we increment temp values, and commit to the real fields ONLY if the entire circuit
		// gets established.
		int tempHops = 0;
		double tempPropDelay = 0.0;
		
		// For each edge in the list of edges, clean up any expired VCs, and add the new 
		// circuit if there is capacity. If at any point an edge has insufficient capacity 
		// to add the new circuit, set the circuit as blocked and leave the loop immediately.
		for (Edge e : shortestPathEdges) {
			e.cleanup(circuit);
			if (e.hasCapacity()) {
				e.addCircuit(circuit);
				tempHops++; 
				tempPropDelay += e.getPropagationDelay();
				// If entire circuit has been established, we consider the packets allocated.
				if (shortestPathEdges.indexOf(e) == shortestPathEdges.size()-1) {
					routedPackets += numPackets;
					System.out.println("Total packets Routed: "+routedPackets);
					successfulVCs++;
					System.out.println("Num total successfulVCs: "+successfulVCs);
					
					// Because we call manageCircuits for each directional edge, we increment totalHops and propdelay a redundant
					// second time for each bidirectional link. This removes it.
					tempHops /= 2;
					tempPropDelay /= 2;
					totalHops += tempHops;
					totalPropDelay += tempPropDelay;
				}
			} else {
				circuit.setBlocked();
				blockedPackets += numPackets;
				System.out.println("Total packets Blocked: "+blockedPackets);
				break;
			}
		}
		
		// Check if the circuit is blocked. If yes, iterate through edges in shortest path
		// again, and remove all instances of that circuit from any lists that contain it.
		if (circuit.blocked()) {
			for (int i = 0 ; i < shortestPathEdges.size() ; i++) {
				if (shortestPathEdges.get(i).getCircuits().contains(circuit))
					shortestPathEdges.get(i).getCircuits().remove(circuit);
			}
			/*
			for (Edge e : shortestPathEdges) {
				if (e.getCircuits().contains(circuit))
					e.getCircuits().remove(circuit);
			}
			*/
		}
	}
	
	/**
	 * THINGS TO CONSIDER: (The following should hold true for both CIRCUIT and PACKET)
	 * 
	 * The number of virtual circuits established is only relevant to the edges that are affected.
	 * For example, for each line of workflow.txt, A VirtualCircuit(VC) object will be constructed and 
	 * added to each edge affected by the shortest path specified in said VC. Thus, the size of the list 
	 * of VCs in each edge will increment by 1. If the number of circuits exceeds the numSimulCircuits 
	 * field in any edge, any further circuits will be "blocked", and the packets lost. If the ttl of any 
	 * circuit is reached, said circuit will expire, and will be removed from all lists that contain it. 
	 * If any one circuit gets blocked, the WHOLE PATH IS BLOCKED. 
	 * 
	 * W.r.t. PACKET: 
	 * 
	 * Done the same way as CIRCUIT. However, we calculate the number of packets that are to be sent using
	 * ttl / packetRate. The number of VCs we create for each Edge = above calculated numPackets. Assume
	 * completely free Edge (all resources available), numPackets = 20 and numSimulCircuits = 15, we can
	 * send the first 15 packets but the last 5 packets will be lost.
	 * 
	 * PLEASE READ THE SPEC AS WELL AND CONFIRM IF THE ABOVE IS TRUE!!!!!!!!!!!!!!!!!!
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
	 * 
	 * 1) Need to add VC.
	 * 2) call edge.cleanup(newVC) to remove all expired VCs
	 * 3) if edge.hasCapacity(), addCircuit(newVC)
	 * 4) if !edge.hasCapacity(), increment blockedPackets counter, do not addCircuit.
	 */
	private static void runCommand() {
		// Using CIRCUIT mode
		if(isCircuit) {
			runCircuit();
		} else {
			runPacket();
		}
	}
	
	public static ArrayList<Edge> getShortestPathEdges(ArrayList<Node> shortestPath) {
		ArrayList<Edge> shortestPathEdges = new ArrayList<Edge>();
		Node current;
		Node next;
		
		for (int i = 0 ; i < (shortestPath.size()-1) ; i++) {
			current = shortestPath.get(i);
			next = shortestPath.get(i+1);
			for (Edge e : graph.getEdges()) {
				// Since the edges are meant to be bidirectional, we add both edges pointing in either direction.
				if ((e.getFrom().getName().equalsIgnoreCase(current.getName()) && e.getTo().getName().equalsIgnoreCase(next.getName()))
				 || (e.getTo().getName().equalsIgnoreCase(current.getName()) && e.getFrom().getName().equalsIgnoreCase(next.getName()))) {
					shortestPathEdges.add(e);
				}
			}
		}
		System.out.println("size of shortestPathEdges: "+shortestPathEdges.size());
		return shortestPathEdges;
	}
	
	public static void calculateStats() {
		routedPacketsPercent = (double) routedPackets / (double) totalPackets * 100.0;
		blockedPacketsPercent = (double) blockedPackets / (double) totalPackets * 100.0;
		if (isCircuit) {
			averageCircuitHops = (double) totalHops / (double) vcRequests;
		} else {
			averageCircuitHops = (double) totalHops / (double) successfulVCs;
		}
		averagePropDelay = totalPropDelay / successfulVCs;
	}
	
	public static void printStats() {
		System.out.println("===== STATISTICS =====");
		System.out.println("total number of virtual circuit requests: "+vcRequests+"\n");
		System.out.println("total number of packets: "+totalPackets+"\n");
		System.out.println("number of successfully routed packets: "+routedPackets+"\n");
		System.out.println("percentage of successfully routed packets: "+String.format("%.2f",routedPacketsPercent)+"\n");
		System.out.println("number of blocked packets: "+blockedPackets+"\n");
		System.out.println("percentage of blocked packets: "+String.format("%.2f",blockedPacketsPercent)+"\n");
		System.out.println("average number of hops per circuit: "+String.format("%.2f",averageCircuitHops)+"\n");
		System.out.println("average cumulative propagation delay per circuit: "+String.format("%.2f",averagePropDelay)+"\n");
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
			graph.addEdge(edge1);
			graph.addEdge(edge2);
		}
	}
	
	/**
	 * Every time that we want to run a path algorithm
	 * it is necessary to reset the nodes to thier
	 * default values to prevent previous searches
	 * interfering with the current search.
	 */
	public static void refreshGraph() {
		for(Node n:graph.getNodes()){
			n.setPrev(null);
			n.setMinDist(2000000);
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
		            	workload.getActiveDurationList().add(reader.nextDouble());
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
