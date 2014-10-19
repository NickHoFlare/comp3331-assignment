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
		PriorityQueue<Node> NodeQueue = new PriorityQueue<Node>();
		
		source.setMinDist(0);
		NodeQueue.add(source);
		
		//Run the algorithm
		while (!NodeQueue.isEmpty()){
			Node from = NodeQueue.poll();
			for (Edge e: graph.getAdjacencies(from)){
				Node to = e.getTo();
				int totalDistance = from.getMinDist() + e.getPropagationDelay(); //SDP is dijkstras with weight 'propagation delay' for each edge.
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
