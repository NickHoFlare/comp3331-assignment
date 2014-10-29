import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class LLPOld {

	private Graph graph;
	public LLPOld(Graph graph){
		this.graph = graph;
	}
	
	/**
	 * This code only runs correctly under the assumption that the number of connected users
	 * for each edge are updated correctly.
	 * This is an implementation of dijikstras algorithm using a priority queue.
	 * Code is based from http://www.algolist.com/code/java/Dijkstra's_algorithm
	 * @param source The source node.
	 * @param destination The destination node.
	 */
	public ArrayList<Node> shortestPath(Node source,Node destination){
		PriorityQueue<Node> NodeQueue = new PriorityQueue<Node>();
		
		source.setMinDist(0);
		NodeQueue.add(source);
				
		//Run the algorithm
		while (!NodeQueue.isEmpty()){		// ***I MADE THIS CHANGE. YOU USED THIS FOR SDP AND SHP.***
		//while (NodeQueue.size()>0){
			Node from = NodeQueue.poll();
			for (Edge e: graph.getAdjacencies(from)){
				Node toNode = e.getTo();
				//toNode is the node stored inside the adjacency list. to is the node stored inside the graph.
				Node to = graph.getNode(toNode.getName());
				
				//Find the load on the current edge
				double load = (e.numCircuits() / e.getNumSimulCircuits());
				
				//Use this code to test overall load.
				/*System.err.println("Circuits:" +e.numCircuits());
				System.err.println("Capacity:" +e.getNumSimulCircuits());
				System.err.println("Load" + load);
				System.err.println("---");*/
				
				//Compute the new load overall by comparing with the load just generated.
				//We are trying to get the largest number here.
				ArrayList<Node> path = new ArrayList<Node>();
		        for (Node n = destination; n != null; n = n.getPrev()){
		            path.add(n);
		        }
		        for (int i = 0; i < path.size()-1; i++) {
		            Edge currentEdge = graph.getEdge(path.get(i), path.get(i+1));
		            double currentLoad = currentEdge.numCircuits() / currentEdge.getNumSimulCircuits();
		            if(currentLoad > load){
		            	load = currentLoad;
		            }
		        }
		        //Now we have the maximum load defined as load.
		        				
				double totalDistance = load; 
				if(totalDistance < to.getMinDist()){
					NodeQueue.remove(to);
					to.setMinDist(totalDistance);
					to.setPrev(from);
					NodeQueue.add(to);
				}
			}
		}
		
		//Find the shortest path using the successive prev values from the destination.
		ArrayList<Node> path = new ArrayList<Node>();
        for (Node n = destination; n != null; n = n.getPrev()){
            path.add(n);
        }
        Collections.reverse(path);
        
        //Print out the path
        String output = "";
        for(Node n:path){
        	output = output + ", " + n.getName();
        }
        output = output.substring(2);
        System.out.println(output);
        
        return path;
		
	}
	
}