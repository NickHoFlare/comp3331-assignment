import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class LLP {

	private Graph graph;
	public LLP(Graph graph){
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
		double maxLoad = 0;
		
		source.setMinDist(0);
		nodeQueue.add(source);
		
		//Run the algorithm
		while (!nodeQueue.isEmpty()){
			Node from = nodeQueue.poll();
			double bestDistance = 0.0;
			
			// Probe each edge exiting the Node "from"
			for (Edge e: graph.getAdjacencies(from)){
				// toNode is the node stored inside the adjacency list. to is the node stored inside the graph.
				Node toNode = e.getTo();
				Node to = graph.getNode(toNode.getName());
				
				// We calculate the load on the current edge by getting the number of circuits it currently
				// holds, divided by its maximum quota of circuits.
				// The best distance is the max between the minimum distance of the previous node from origin
				// and the linkLoad of the current Edge.
				double linkLoad = e.getCircuits().size() / e.getNumSimulCircuits();
				bestDistance = Math.max(from.getMinDist(), linkLoad);
				//double bestDistance = Math.max(from.getMinDist(), linkLoad);
								
				if (bestDistance < to.getMinDist()){
					nodeQueue.remove(to);
					to.setMinDist(bestDistance);
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
        //System.out.println(output);
        
        return path;	
	}
}
