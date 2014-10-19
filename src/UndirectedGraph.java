import java.util.ArrayList;


public class UndirectedGraph implements Graph {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private ArrayList<Edge> adjacencyList;
	
	public UndirectedGraph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		adjacencyList = new ArrayList<Edge>();
	}
	
	@Override
	public void addNode(Node node) {
		nodes.add(node);
	}

	@Override
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	@Override
	public void addAdjacency(Edge adjacency) {
		adjacencyList.add(adjacency);
	}

	@Override
	public ArrayList<Edge> getAdjacencies(Node a) {
		
		ArrayList <Edge> connectingEdges = new ArrayList<Edge>();
		for(Edge e:adjacencyList){
			if(e.getFrom().getName().equals(a.getName())){
				connectingEdges.add(e);
			}
		}
		return connectingEdges;
	}

	/*public void addEdge(Edge edge) {
		edges.add(edge);
	}*/

	/*public ArrayList<Edge> getEdges() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	public Node getNode(String node){
		for(Node n: nodes){
			if(n.getName().equals(node)){
				return n;
			}
		}
		return null;
	}

}
