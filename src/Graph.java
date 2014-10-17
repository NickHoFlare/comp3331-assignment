import java.util.ArrayList;


public interface Graph {
	public void addNode(Node node);
	public ArrayList<Node> getNodes();
	public void addEdge(Edge edge);
	public ArrayList<Edge> getEdges();
	public void addAdjacency(ArrayList<Node> adjacency);
	public ArrayList<ArrayList<Node>> getAdjacencies();
}
