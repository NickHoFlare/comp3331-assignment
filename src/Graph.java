import java.util.ArrayList;


public interface Graph {
	public void addNode(Node node);
	public ArrayList<Node> getNodes();
	public void addAdjacency(Edge adjacency);
	public ArrayList<Edge> getAdjacencies(Node a);
	public Node getNode(String node);
}
