import java.util.List;
import java.util.LinkedList;


class Node {
	private int label;
	public boolean visited = false; //endret til public, da transformDirToUndir gjorde alle visited til true.
	public List<Node> neighbors = new LinkedList<Node>(); //måtte gjøre public for BuildAdjacencyMatrix.

	public Node(int label) {
		this.label = label;
	}

	public int getLabel() {
		return label;
	}

	public List<Node> getNeighbors() {
		return neighbors;
	}

	public boolean isVisited() {
		return visited;
	}

	public void visit() {
		visited = true;
	}

	public void addNeighbor(Node n) {
		// legger til en uretta kant fra this til n
		if (!neighbors.contains(n)) {
			neighbors.add(n);
			n.addNeighbor(this);
		}
	}

	public void addSuccessor(Node n) {
		// legger til en retta kant fra this til n
		if (!neighbors.contains(n)) {
			neighbors.add(n);
		}
	}

	public String toString() {
		return Integer.toString(label);
	}
}

class Graph {
	private Node[] nodes;

	public Graph(Node[] nodes) {
		this.nodes = nodes;
	}

	public void printNeighbors() {
		for (Node n1 : nodes) {
			String s = n1.toString() + ": ";
			for (Node n2 : n1.getNeighbors()) {
				s += n2.toString() + " ";
			}
			System.out.println(s.substring(0, s.length() - 1));
		}
	}

	private static Graph buildExampleGraph() {
	    // ukeoppgave
		Node[] nodes = new Node[7];
		for (int i = 0; i < 7; i++) {
			nodes[i] = new Node(i);
		}
		nodes[0].addNeighbor(nodes[1]);
		nodes[0].addNeighbor(nodes[2]);
		nodes[1].addNeighbor(nodes[2]);
		nodes[2].addNeighbor(nodes[3]);
		nodes[2].addNeighbor(nodes[5]);
		nodes[3].addNeighbor(nodes[4]);
		nodes[4].addNeighbor(nodes[5]);
		nodes[5].addNeighbor(nodes[6]);
		return new Graph(nodes);
	}

	private static Graph buildRandomSparseGraph(int numberofV, long seed) {
		// seed brukes av java.util.Random for å generere samme sekvens for samme frø
		// (seed) og numberofV
		java.util.Random tilf = new java.util.Random(seed);
		int tilfeldig1 = 0, tilfeldig2 = 0;
		Node[] nodes = new Node[numberofV];

		for (int i = 0; i < numberofV; i++) {
			nodes[i] = new Node(i);
		}

		for (int i = 0; i < numberofV; i++) {
			tilfeldig1 = tilf.nextInt(numberofV);
			tilfeldig2 = tilf.nextInt(numberofV);
			if (tilfeldig1 != tilfeldig2)
				nodes[tilfeldig1].addNeighbor(nodes[tilfeldig2]);
		}
		return new Graph(nodes);
	}

	private static Graph buildRandomDenseGraph(int numberofV, long seed) {
		java.util.Random tilf = new java.util.Random(seed);
		int tilfeldig1 = 0, tilfeldig2 = 0;
		Node[] nodes = new Node[numberofV];

		for (int i = 0; i < numberofV; i++) {
			nodes[i] = new Node(i);
		}

		for (int i = 0; i < numberofV * numberofV; i++) {
			tilfeldig1 = tilf.nextInt(numberofV);
			tilfeldig2 = tilf.nextInt(numberofV);
			if (tilfeldig1 != tilfeldig2)
				nodes[tilfeldig1].addNeighbor(nodes[tilfeldig2]);
		}
		return new Graph(nodes);
	}

	private static Graph buildRandomDirGraph(int numberofV, long seed) {
		java.util.Random tilf = new java.util.Random(seed);
		int tilfeldig1 = 0, tilfeldig2 = 0;
		Node[] nodes = new Node[numberofV];

		for (int i = 0; i < numberofV; i++) {
			nodes[i] = new Node(i);
		}

		for (int i = 0; i < 2 * numberofV; i++) {
			tilfeldig1 = tilf.nextInt(numberofV);
			tilfeldig2 = tilf.nextInt(numberofV);
			if (tilfeldig1 != tilfeldig2)
				nodes[tilfeldig1].addSuccessor(nodes[tilfeldig2]);
		}
		return new Graph(nodes);
	}

	public void DFS(Node s) {
		s.visit();
		for (Node v : s.getNeighbors()) {
			if (v.isVisited() == false) {
				this.DFS(v);
			}
		}

	}

	public void DFSFull() {
		for (Node v : nodes) {
			if (v.isVisited() == false) {
				this.DFS(v);
			}
		}
	}

    public int numberOfComponents() {
	    int n = 0;
	    for (Node v : nodes) {
	    	if (v.isVisited() == false) {
	    		this.DFS(v);
	    		n += 1;
	    	}
	    }
	    return n;
	}

    public Graph transformDirToUndir() {
	    for (Node u : nodes) {
	    	for (Node v : u.getNeighbors()) {
	  			v.addNeighbor(u);
	    	}
	    }
	    for (Node v : nodes) {
	    	v.visited = false;
	    }
	    return new Graph(nodes);
	}

    public boolean isConnected(){
	   	Graph g = transformDirToUndir();
	    if (g.numberOfComponents() == 1) {
	    	return true;
	    } else {
	    	return false;s
	    }
	}
s

	public int[][] buildAdjacencyMatrix() {
		int size = 0;
		for (Node v : nodes) {
			size += 1;
		}
		int[][] m = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (nodes[i].neighbors.contains(nodes[j])){ //m_ij = 1 hvis i og j er naboer, 0 ellers
					m[i][j] = 1;
				} else {
				m[i][j] = 0;
				}
			System.out.printf("%5d ", m[i][j]);
			}
		System.out.println();
		}
	    return m; 
	}

	public static void main(String[] args) {
		Graph graph = buildExampleGraph();
		graph.printNeighbors();
		graph = buildRandomSparseGraph(11, 201909202359L);
		graph.printNeighbors();
		System.out.println(graph.numberOfComponents());
		System.out.println(graph.isConnected());
		graph.buildAdjacencyMatrix();
		System.out.println("");
		graph = buildRandomDenseGraph(15, 201909202359L);
		graph.printNeighbors();
		graph = buildRandomDirGraph(9, 20323023L);
		graph.printNeighbors();
	}
}
	