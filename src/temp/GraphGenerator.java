package temp;


import java.util.Collection;
import java.util.Vector;

import edu.uci.ics.jung.graph.Graph;


/**
 * This class generates the graph, and contains a method for returning the graph.
 * 
 * @author Jeff Falkenham
 *
 */
public class GraphGenerator {


    private Graph<MyVertex, MyEdge> graph;

    //These should be ordered the same
    private MutationVector<MutationData> pointsToData;
    private Vector<MyVertex> holdsVertexPointers = new Vector<MyVertex>();

    /**
     * This is the constructor.  It generates the graph.
     * 
     * @param pointsToData		this MutationVector object contains the data that will be used for the graph
     */
    public GraphGenerator(MutationVector<MutationData> methodData){

        pointsToData = methodData;

        //create the graph object
        graph = new MainGraphClass<MyVertex, MyEdge>();

        //add all the vertices to the graph from the data
        for(int i = 0;i < pointsToData.size();i++){
            MyVertex myVertex = new MyVertex(pointsToData.get(i).getName());
            graph.addVertex(myVertex);
            holdsVertexPointers.add(myVertex);
        }
        
        /* The following set of three FOR loops adds edges to all nodes that share similar similar mutants or
         * test cases.  The outer most FOR loop (incrementing the variable i) is used to transverse each element
         * in the MutationVector pointsToData.  The node in pointsToData at index "i" can be seen as the reference
         * node for that iteration in the loop.  The middle FOR loop (incrementing the variable t) is used to get
         * each node in pointsToData at indicies greater than the current value of i.  Since an edge is shared
         * between 2 nodes, the nodes previous to index i have already been analyzed.  The most inner FOR loop
         * (incrementing variable q) is used to iterate through each mutant or test case within the nodes at
         * indicies 'i' and 't' to see if there are any similarities.
         */
        
        //Iterate through each node in the graph.
        for(int i = 0;i < pointsToData.size();i++){
        	//Iterate through each node after the one represented by 'i'.
            for(int t = i;t < pointsToData.size();t++){
                int counter = 0;
                //Iterate through the details of nodes at 'i' and 't' checking for similarities.
                for(int q = 0;q < pointsToData.get(t).getSize();q++){
                    if(i == t){
                        break;
                    }
                    if(pointsToData.get(i).getResult(q).equals("yes")){
                        if(pointsToData.get(t).getResult(q).equals("yes")){
                            counter++;
                        }
                    }
                }
                if((counter!=0)&&(i!=t)){
                    String one = pointsToData.get(i).getName();
                    String two = pointsToData.get(t).getName();
                    String name = one + " " + two;
                    MyEdge tempEdge = new MyEdge(name);
                    graph.addEdge(tempEdge, 
                            holdsVertexPointers.get(i), 
                            holdsVertexPointers.get(t));
                    prepareEdge(tempEdge);

                }
            }
        }

    }

    /**
     * Generates the graph for the Connections canvas
     * 
     * @param methodData		The MutationVector that holds the data
     * @param index		The index of the node that was right clicked
     */
    public static Graph<MyVertex, MyEdge> returnConnectionGraph(MyVertex v, Graph<MyVertex, MyEdge> g) {

        Graph<MyVertex, MyEdge> localGraph = new MainGraphClass<MyVertex, MyEdge>();
        localGraph.addVertex(v);
        Collection<MyEdge> temp = g.getEdges();
        for(MyEdge edg : temp) {
            if(edg.containsV(v)) {
                if(!g.containsVertex(edg.getVertexN(v))) {
                    localGraph.addVertex(edg.getVertexN(v));
                }

            }
        }

        for(MyEdge edg : temp) {
            if(edg.containsV(v)) {
                if((!localGraph.containsEdge(edg))||(localGraph.getEdgeCount() == 0)) {
                    localGraph.addEdge(edg, v, edg.getVertexN(v));
                }

            }
        }
        



        return localGraph;




        /*
		graph = new SparseMultigraph<MyVertex, MyEdge>();

		graph.addVertex(new MyVertex(methodData.get(index).getName()));




			for(int t = 0;t < methodData.size();t++){
				int counter = 0;

				for(int q = 0;q < methodData.get(t).getSize();q++){
					if(index == t){
						break;
					}
					if(methodData.get(index).getResult(q).equals("yes")){
						if(methodData.get(t).getResult(q).equals("yes")){
							counter++;
						}
					}
				}
				if((counter!=0)&&(index!=t)){
					String one = methodData.get(index).getName();
					String two = methodData.get(t).getName();
					graph.addVertex(new MyVertex(two));
					String name = one + " " + two;
					graph.addEdge(new MyEdge(name, defaultEdgeStrokeTransformer(name)), one, two);
				}
			}*/


    }

    /**
     * get method to return the graph
     * 
     * @return		The graph
     */
    public Graph<MyVertex, MyEdge> getGraph(){
        return graph;
    }

    public void prepareEdge(MyEdge edge) {
        edge.setEdgeSize(DisplayFrame.defaultEdgeStrokeTransformer(edge, pointsToData));
    }


}