import java.util.ArrayList;


public interface Graph {
	public void addNode(Node node);
	public ArrayList<Node> getNodes();
	public ArrayList<Edge> getAdjacencies(Node a);
	public Node getNode(String node);
	public Edge getEdge(Node from,Node to);
}
