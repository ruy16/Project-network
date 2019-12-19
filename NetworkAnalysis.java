/*This is the diver class that analyzes a given network graph by reading a input file
This program does the following:
find lowest latency path(shortest path)
determine if the network is copper only
find lowest average latency path.
find a pair of two articulation points that will fail the network
*@ Author:runyuan Yan
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NetworkAnalysis{
  public static void main(String[] args) throws FileNotFoundException {
	  String userInput;
	  Scanner fileReader = new Scanner(new File(args[0]));		//read the input file
	  EdgeWeightedDigraph network = new EdgeWeightedDigraph(fileReader.nextInt());			// Instantiate our network using size from the file(first line)
	  String[] line ;
	  fileReader.nextLine();
	  while(fileReader.hasNextLine())
	  {
		  line = fileReader.nextLine().split(" ");
		  //System.out.println(Integer.parseInt(line[0]));
		  network.addEdge(new DirectedEdge(Integer.parseInt(line[0]),Integer.parseInt(line[1]),line[2],Integer.parseInt(line[4]),Integer.parseInt(line[3])));
		  network.addEdge(new DirectedEdge(Integer.parseInt(line[1]),Integer.parseInt(line[0]),line[2],Integer.parseInt(line[4]),Integer.parseInt(line[3])));
		  
	  }
	  fileReader.close();
	  
	  Scanner scanner = new Scanner(System.in);
	  System.out.println("\nEnter 1 to find the lowest latency path between any two points");
	  System.out.println("\nEnter 2 to determine copper-only ");
	  System.out.println("\nEnter 3 to find the lowest average latency spanning tree");
	  System.out.println("\nEnter 4 to test if the graph can survive 2-vertex failure");
	  System.out.println("\nEnter 5 to quit the program");
	  int start;//used to store input vertex
	  int end;
	  while (scanner.hasNext())
	  {
		  userInput = scanner.next();
		  if(userInput.equals("1"))
		  {
			 
			  System.out.println("Enter the starting point ");
			  start = scanner.nextInt();
			  System.out.println("Enter the end point ");
			  end = scanner.nextInt();
			  DijkstraSP sp = new DijkstraSP(network, start);			// create a Dijkstra path 
			  ArrayList<DirectedEdge> results = sp.pathTo(end);
		        StdOut.printf("The lowest latency path for %d to %d (%.2f)  :", start, end, sp.distTo(end));
		        //use DJ to find the path and print
		        for(int i = results.size()-1;i>=0;i--)
		        {
		        	System.out.println(results.get(i));
		        }
		        System.out.println("The total bandwidth: "+sp.bandwidth(results));			//print the bandwidth
		  }
		  if(userInput.equals("2"))
		  {
			  network.determineCopperConnectivity();
		  }
		  if(userInput.equals("3"))
		  {
			  network.findLowestAverageSpanningTree();
		  }
		  
		  if(userInput.equals("4"))
		  {
			  network.findFailurePoints();
		  }
		  
		  
		  if(userInput.equals("5"))
		  {
			  scanner.close();
			  System.exit(0);
		  }
		  System.out.println("\nEnter 1 to find the lowest latency path between any two points");
		  System.out.println("\nEnter 2 to determine copper-only ");
		  System.out.println("\nEnter 3 to find the lowest average latency spanning tree");
		  System.out.println("\nEnter 4 to test if the graph can survive 2-vertex failure");
		  System.out.println("\nEnter 5 to quit the program");
		  
		  
	  }
  }
}




