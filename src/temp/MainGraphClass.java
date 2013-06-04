package temp;

import edu.uci.ics.jung.graph.SparseMultigraph;

public class MainGraphClass<V, E> extends SparseMultigraph<V, E> {

    private static final long serialVersionUID = 1;

    public boolean addEdge(E e, V v1, V v2) {
        boolean methodResultHolder = super.addEdge(e, v1, v2);
        if((v1 instanceof MyVertex)&&(v2 instanceof MyVertex)&&(e instanceof MyEdge)) {
            ((MyEdge)e).addVerticesToEdge((MyVertex)v1, (MyVertex)v2);
        }
        return methodResultHolder;
    }
    
    
}
