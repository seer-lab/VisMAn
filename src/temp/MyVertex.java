package temp;

import java.awt.Paint;
import java.awt.Shape;

public class MyVertex {
    protected String name;
    protected Shape sizeAndShape;
    protected Paint color;

    MyVertex(String name) {
        this.name = name;
    }
    
    
    public boolean equals(Object o) {
        return o.equals(name);
    }
    
    public String toString() {
        return name;
    }
    
    
}
