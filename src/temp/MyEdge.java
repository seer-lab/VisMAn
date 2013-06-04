package temp;

import java.awt.Stroke;

public class MyEdge {

    protected String name;
    protected Stroke edgeSize;
    protected String edgeLabel;
    private MyVertex v1;
    private MyVertex v2;

    MyEdge(String name) {
        this.name = name;
    }
    
    public void setEdgeSize(Stroke edgeSize) {
        this.edgeSize = edgeSize;
    }
    
    public boolean equals(Object o) {
        return name.equals(o);
    }
    
    public String toString() {
        return name;
    }
    
    public void addVerticesToEdge(MyVertex v1, MyVertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
    
    public MyVertex getVertexN(MyVertex notThisVertex) {
        if(v1 == notThisVertex) {
            return v2;
        } else if(v2 == notThisVertex) {
            return v1;
        } else {
            throw new NullPointerException();
        }
    }
    
    public boolean containsV(MyVertex v) {
        if(v1 == v) {
            return true;
        } else if(v2 == v) {
            return true;
        } else {
            return false;
        }
    }
    
}

