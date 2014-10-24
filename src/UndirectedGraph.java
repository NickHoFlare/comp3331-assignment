import java.util.ArrayList;


public class UndirectedGraph implements Graph {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public UndirectedGraph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getAdjacencies(Node a) {
		ArrayList <Edge> connectingEdges = new ArrayList<Edge>();
		for(Edge e:edges){
			if(e.getFrom().getName().equals(a.getName())){
				connectingEdges.add(e);
			}
		}
		return connectingEdges;
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	
	public Node getNode(String node){
		for(Node n: nodes){
			if(n.getName().equals(node)){
				return n;
			}
		}
		return null;
	}
	
	public Edge getEdge(Node from,Node to){
		for(Edge e:edges){
			if(e.getFrom().getName().equals(from.getName()) &&
			   e.getTo().getName().equals(to.getName())){
				return e;
			}
		}
		return null;
	}

}
