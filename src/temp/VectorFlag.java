package temp;

import java.util.Vector;

/**
 * Overloads Vector(int initialCapacity) to use int as a flag
 * 
 * @author Jeff Falkenham
 *
 * @param <T>
 */
public class VectorFlag<T> extends Vector<T> {

    private static final long serialVersionUID = 1;
    
    public static final int FLAG_CLOSE_DISCRETE = 10001;
    public static final int FLAG_CLOSE_ANALOG = 10002;
    public static final int FLAG_CLOSE_DEFAULT = 10003;
    
    protected int flag;
    
    VectorFlag(int flag) {
        //default size for default constructor
        super(10);
        this.flag = flag;
        
    }
    
    
    
}
