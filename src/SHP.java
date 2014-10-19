import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class SHP {

	private Graph graph;
	public SHP(Graph graph){
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
		
		//System.out.println("----------------------------");
		
		//Run the algorithm
		while (!NodeQueue.isEmpty()){
			Node from = NodeQueue.poll();
			for (Edge e: graph.getAdjacencies(from)){
				//System.out.println("Path from "+e.getFrom().getName() + " to " + e.getTo().getName());
				Node toNode = e.getTo();
				//toNode is the node stored inside the adjacency list. to is the node stored inside the graph.
				Node to = graph.getNode(toNode.getName());
				// SHP is dijkstras with weight 1 for each edge.
				int totalDistance = from.getMinDist() + 1; 
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
