package temp;

import java.awt.Paint;
import java.awt.Shape;
import java.util.Vector;

public class MyVertex {
    protected String name;
    protected String type;
    protected Vector<String> data;
    protected Shape sizeAndShape;
    protected Paint color;

    MyVertex(String _name, String _type, Vector<String> _vec) {
        this.name = _name;
        this.type = _type;
        this.data = _vec;
    }
    
    
    public boolean equals(Object o) {
        return o.equals(name);
    }
    
    public String toString() {
        return name;
    }
    

    
    
}
