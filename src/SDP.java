import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class SDP {

	private Graph graph;
	public SDP(Graph graph){
		this.graph = graph;
	}
	
	/**
	 * This is an implementation of dijikstras algorithm using a priority queue.
	 * Code is based from http://www.algolist.com/code/java/Dijkstra's_algorithm
	 * @param source The source node.
	 * @param Destination The destination node.
	 */
	public ArrayList<Node> shortestPath(Node source,Node Destination){
		PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
		
		source.setMinDist(0);
		nodeQueue.add(source);
		
		//Run the algorithm
		while (!nodeQueue.isEmpty()){
			Node from = nodeQueue.poll();
			
			// Visit each edge exiting Node from
			for (Edge e: graph.getAdjacencies(from)){
				Node toNode = e.getTo();
				// toNode is the node stored inside the adjacency list. to is the node stored inside the graph.
				Node to = graph.getNode(toNode.getName());
				// SDP is dijkstras with weight 'propagation delay' for each edge.
				double totalDistance = from.getMinDist() + e.getPropagationDelay(); 
				
				// Check if the total distance calculated for the current Edge is smaller than the minDistance
				// field of the node that we are visiting. If we are visiting for the first time, it will be 
				// infinity (or close to it)
				// If we probe a node and find a path that has shorter distance than the node is currently
				// "aware" about, we remove the node from the queue, update the minDistance with the new
				// shorter distance, and re-add the node to the queue.
				if (totalDistance < to.getMinDist()){
					nodeQueue.remove(to);
					to.setMinDist(totalDistance);
					to.setPrev(from);
					nodeQueue.add(to);
				}
			}
		}
		
		//Find the shortest path using the successive prev values from the destination.
		ArrayList<Node> path = new ArrayList<Node>();
        for (Node n = Destination; n != null; n = n.getPrev()){
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
