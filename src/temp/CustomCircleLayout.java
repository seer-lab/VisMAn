package temp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;


/**
 * This class extends CircleLayout, allows you to set a custom seed, and overrides
 * orderVertices() and initialize()
 * 
 * @author Jeff Falkenham
 *
 * @param <V>		Vertex type
 * @param <E>		Edge type
 */
public class CustomCircleLayout<V, E> extends CircleLayout<V, E> {

	private long seed = 1;
	private boolean initializeCounter = false;
	
	/**
	 * Constructor for the CustomCircleLayout
	 * 
	 * @param g		The graph that this layout corresponds to
	 */
	CustomCircleLayout(Graph<V,E> g){
		super(g);
		//this.setRadius(1000.0);
		//long t;
		
		//TODO add in a random seed that never changes
		
		//Calendar temp = new Calendar.getInstance();
		
	}

	/**
	 * Sets the seed to the value of seeder
	 * 
	 * @param seeder		The seed value
	 */
	public void setCustomSeed(long seeder){
		seed = seeder;
	}
	
	/**
	 * Sets the seed to the value of seeder, and orders the vertices using this seed.
	 * 
	 * @param seeder		The seed value
	 */
	public void setCustomSeedAndOrderVertices(long seeder){
		seed = seeder;
		//if(this.getGraph().getVertices().toArray() instanceof V[])
		@SuppressWarnings("unchecked")
		V[] temp = (V[])this.getGraph().getVertices().toArray();
        //TODO setvertextorder()
		this.orderVertices(temp);
	}

	
	/** 
	 * Overrides orderVertices to not use a random seed
	 * 
	 * @see edu.uci.ics.jung.algorithms.layout.CircleLayout#orderVertices(V[])
	 */
	public void orderVertices(V[] vertices) {
		List<V> list = Arrays.asList(vertices);
		Random rnd = new Random(seed);
		Collections.shuffle(list, rnd);
	}
	
	/**
	 * Overrides initialize() so that it can only be called once
	 * 
	 * @see edu.uci.ics.jung.algorithms.layout.CircleLayout#initialize()
	 */
	public void initialize(){
		if(initializeCounter == true){
			return;
		}
		super.initialize();
		initializeCounter = true;
	}


}