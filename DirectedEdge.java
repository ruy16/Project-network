/******************************************************************************
 *  Compilation:  javac Edge.java
 *  Execution:    java Edge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted edge.
 *
 ******************************************************************************/

/**
 *  The {@code Edge} class represents a weighted edge that represents a cable in an network
 *  {@link EdgeWeightedGraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the edge and
 *  the weight. The natural order for this data type is by
 *  ascending order of weight.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  
 *  @author runyuan yan
 */
public class DirectedEdge implements Comparable<DirectedEdge> { 

    private final int from;//source
    private final int to;//destination
    private double latency;//time takes to travel,the actual weight
    private	int bandWidth;
    private	int	length;
    private	String material;
    private final int COPPER_SPEED = 230000000; //The speed at which a single data packet can be sent across copper wire in meters per second
	private final int FIBER_SPEED = 200000000; 


    /**
     * Initializes an edge between vertices {@code v} and {@code w} of
     * the given {@code weight}.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @param  weight the weight of this edge
     * @throws IllegalArgumentException if either {@code v} or {@code w} 
     *         is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public DirectedEdge(int from, int to,String material, int length,int bandWidth) {
        if (from < 0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
        if (to < 0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
        if (Double.isNaN(length)) throw new IllegalArgumentException("Weight is NaN");
        this.from = from;
        this.to = to;
        this.length = length;
        this.bandWidth = bandWidth;
        this.material = material;
        if(material.equals("copper"))
        {
        	latency = (double) (length * Math.pow(10, 9)/COPPER_SPEED);
        }
        if(material.equals("optical"))
        {
        	latency =  ((double) 1/FIBER_SPEED) * length * Math.pow(10, 9); 
        }
        
    }

    /**
     * Returns the weight of this edge.
     *
     * @return the weight of this edge
     */
    public double latency() {
        return latency;
    }

    /**
     * Returns source of this edge.
     *
     * @return either source of this edge
     */
    public int from() {
        return from;
    }
    
    /**
     * Returns the destination vertex of the directed edge.
     * @return the destination vertex of the directed edge
     */
    public int to() {
        return to;
    }
    public int bandWidth()
    {
    	return bandWidth;
    }
    public int length()
    {
    	return length;
    }
    public String material()
    {
    	return material;
    }
    /**
     * Compares two edges by weight.
     * Note that {@code compareTo()} is not consistent with {@code equals()},
     * which uses the reference equality implementation inherited from {@code Object}.
     *
     * @param  that the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     *         the weight of this is less than, equal to, or greater than the
     *         argument edge
     */
    @Override
    public int compareTo(DirectedEdge that) {
        return Double.compare(this.latency, that.latency);
    }

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return from + "->" + to + " " + String.format("%5.2f", latency);
    }
    
}
