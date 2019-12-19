import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/******************************************************************************
 *
 *  Revised Dijkstra's algorithm to adapt to solving the  lowest latency network problem. Computes the shortest path .
 *  Assumes all weights are nonnegative.
 *
/**
 *  The {@code DijkstraSP} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are nonnegative.
 *  <p>
 *  This implementation uses Dijkstra's algorithm with a binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Each call to {@code distTo(int)} and {@code hasPathTo(int)} takes constant time;
 *  each call to {@code pathTo(int)} takes time proportional to the number of
 *  edges in the shortest path returned.
 *  <p>
 *  For additional documentation,    
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  
 *  @author Runyuan Yan
 */
public class DijkstraSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
  

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) {
            if (e.latency() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.latency()) {
            distTo[w] = distTo[v] + e.latency();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }
    
    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public ArrayList<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        ArrayList<DirectedEdge> path = new ArrayList<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
        	//System.out.println(e);
            path.add(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.latency() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.latency() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.latency() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    /*calculates the total bandwidth on a given list of edges
     */
     
    public int bandwidth(ArrayList<DirectedEdge> path)
    {
    	int bandWidth =0;
    	for(DirectedEdge e:path)
    	{
    		bandWidth += e.bandWidth();
    	}
    	return bandWidth;
    }
	
    /**
     * Unit tests the {@code DijkstraSP} data type.
     *
     * @param args the command-line arguments
     */
    /*public static void main(String[] args) {
    	 int source = 0;
    	 int destination = 1;
    	 int vertex = 5;
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(vertex);//create duplex edges upon initiation
       G.addEdge(new DirectedEdge(0,2,"optical",10,10000));
       G.addEdge(new DirectedEdge(2,0,"optical",10,10000));
       G.addEdge(new DirectedEdge(0,3,"optical",10,10000));
       G.addEdge(new DirectedEdge(3,0,"optical",10,10000));
       G.addEdge(new DirectedEdge(1,2,"optical",10,10000));
       G.addEdge(new DirectedEdge(2,1,"optical",10,10000));
       G.addEdge(new DirectedEdge(1,3,"optical",10,10000));
       G.addEdge(new DirectedEdge(3,1,"optical",10,10000));
       G.addEdge(new DirectedEdge(0,4,"copper",8,100));
       G.addEdge(new DirectedEdge(4,0,"copper",8,100));
       G.addEdge(new DirectedEdge(1,4,"copper",8,100));
       G.addEdge(new DirectedEdge(2,4,"copper",8,100));
       G.addEdge(new DirectedEdge(4,2,"copper",8,100));
       G.addEdge(new DirectedEdge(4,1,"copper",8,100));
       G.addEdge(new DirectedEdge(3,4,"copper",8,100));
       G.addEdge(new DirectedEdge(4,3,"copper",8,100));
       
        // compute shortest paths
        DijkstraSP sp = new DijkstraSP(G, source);// edit this to have 2 parameters, source and destination
		*/
        // print shortest path
        /*
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", source, t, sp.distTo(t));
                for (DirectedEdge e : sp.pathTo(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d  no path\n", source, t);
            }
        }*/
        //ArrayList<DirectedEdge> results = sp.pathTo(destination);
       // StdOut.printf("%d to %d (%.2f)  ", source, destination, sp.distTo(destination));
        //for(int i = results.size()-1;i>=0;i--)
        //{
        	//System.out.println(results.get(i));
        //}
        
        //System.out.println(G.isCopperOnly());
       // System.out.println(sp.bandwidth(results));
    //}

}
