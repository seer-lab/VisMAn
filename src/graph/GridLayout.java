package graph;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;

public class GridLayout extends StaticLayout  {

	public static final int SPACER_DISTANCE = 100;
	
	public Graph layoutGraph;
	
	public GridLayout(Graph graph) {
		super(graph);
		layoutGraph = graph;
		Object[] nodeArray = layoutGraph.getVertices().toArray();
		
		int currentNode = 0;
		
		for (int i = 0; i < Math.ceil(Math.sqrt(nodeArray.length)); i++)
		{
			for (int j = 0; j < Math.ceil(Math.sqrt(nodeArray.length)); j++)
			{
				this.setLocation(nodeArray[currentNode], i*SPACER_DISTANCE+70, j*SPACER_DISTANCE+70);
				currentNode++;
				if (currentNode == nodeArray.length)
				{
					break;
				}
			}
			
			if (currentNode == nodeArray.length)
			{
				break;
			}
		}
		
	}

}
