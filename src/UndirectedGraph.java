import java.util.ArrayList;


public class UndirectedGraph implements Graph {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private ArrayList<ArrayList<Node>> adjacencyList;
	
	public UndirectedGraph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		adjacencyList = new ArrayList<ArrayList<Node>>();
	}
	
	@Override
	public void addNode(Node node) {
		if(!nodes.contains(node)){
			nodes.add(node);
		}
	}

	@Override
	public ArrayList<Node> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAdjacency(ArrayList<Node> adjacency) {
		// TODO Auto-generated method stub
	}

	@Override
	public ArrayList<ArrayList<Node>> getAdjacencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	@Override
	public ArrayList<Edge> getEdges() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Node getNode(String node){
		for(Node n: nodes){
			if(n.getName().equals(node)){
				return n;
			}
		}
		return null;
	}

}
