import java.util.ArrayList;


public class Node {
	private String name;
	private ArrayList<Edge> edges;
	private ArrayList<Node> neighbours;
	
	public Node(String name) {
		this.name = name;
		edges = new ArrayList<Edge>();
		neighbours = new ArrayList<Node>();
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}
}
