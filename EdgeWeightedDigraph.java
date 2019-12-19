import java.util.Stack;

/******************************************************************************
 *  
 *
 *  An edge-weighted directed graph that represents a network, implemented using adjacency lists.
 *  Changes are made to adapt to solving the network problems
 *  The way weight is calculated  is changed
 *  Added functions to support solve project problems
 *  Parallel edges and self-loops are permitted.
 *
 *

/**
 *  The {@code EdgeWeightedDigraph} class represents a edge-weighted
 *  digraph of vertices named 0 through <em>V</em> - 1, where each
 *  directed edge is of type {@link DirectedEdge} and has a real-valued weight.
 *  It supports the following two primary operations: add a directed edge
 *  to the digraph and iterate over all of edges incident from a given vertex.
 *  It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident from a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  
 *  @author runyuanyan
 */
public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<DirectedEdge>[] adj;
    private int[] indegree;             // indegree[v] = indegree of vertex v
    private boolean CopperOnlyConnection = true;
    /**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<DirectedEdge>();
        }
    }

    


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(DirectedEdge e) {
    	if(e.material().equals("optical"))
    	{
    		CopperOnlyConnection = false;
    	}
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    /**
     * Returns the edges incident on vertex {@code v}.
     *
     * @param  v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }
    
    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }
    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }
    /*checks if the network is copper only
   */
  //Find out whether graph is connected with only copper, connected considering only copper, or neither
  	public void determineCopperConnectivity(){
  		if(CopperOnlyConnection){ //If the graph consists only copper wires
  			System.out.println("-- This graph consists of only copper wires,it is copper-connected.");
  		} else{
  			boolean connectedWithCopper = true; //Assume the graph is connected with only copper
  			
  			for(int i = 0; i < V; i++){ //Iterate through every vertex and check to make sure it has at least one copper connection
  				boolean hasCopperConnection = false;
  				for(DirectedEdge e :adj[i])
  				{
  					if(e.material().equals("copper"))//There exists a copper wire from this vertex
  					{
  						hasCopperConnection = true;
  						break;
  					}
  				}


  				if(!hasCopperConnection){ //If this vertex does not have a single copper connection, then the graph cannot be copper connected
  					connectedWithCopper = false;
  					break;
  				}
  			}

  			if(connectedWithCopper)	System.out.println("-- This graph has optical wires but can be connected with only copper wires. ");
  			else System.out.println("-- This graph is not copper-only and cannot be connected with only copper wires.");
  		}
  	}
  	/*Finds the lowest latency spanning tree per edge in this network graph
  	 * 
  	 */
  	public void findLowestAverageSpanningTree()
  	{
  		int treeSize = 0;
  		KruskalMST kmst = new KruskalMST(this);
  		System.out.println("lowest average latency spanning tree for the graph: \n");
  		for(DirectedEdge e : kmst.edges())
  		{
  			System.out.println(e);
  			treeSize+=1;
  		}
  		System.out.printf("\nThe average latency of this spanning tree is %.3f nanoseconds.\n", kmst.weight()/treeSize);
  		
  	}
  	/*Determine if any two failures of points would disconnect the graph
  	 * If any vertex has less than 3 incident edges, it won't survive the failure of two disconnected edges
  	 */
  	public void findFailurePoints()
  	{
  		for(int i = 0; i < V; i++)
  		{
  			if(adj[i].size() < 3)
  			{
  				System.out.println("\n The network will be disconnected if "); 
  				for(DirectedEdge e: adj[i]) {System.out.println(e);} 
  				System.out.println("both fail");
  				return;
  			}
  		}
  		System.out.println("\n The network will survive any failure of any two points");
  		
  	}
    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }



}
