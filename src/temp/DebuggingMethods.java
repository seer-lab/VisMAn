package temp;

import java.util.Collection;

/**
 * This is a debugging tool that prints a stack trace wherever
 * it is invoked using PrintStackTrace.run()
 * 
 * @author Jeff Falkenham
 *
 */
public class DebuggingMethods {
    
    /**
     * runs the debugging tool (prints the stack trace)
     */
    public static void printStack() {
        try {
            //throw new Exception();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static <U> void printCollection(Collection<U> c) {
        for(U t : c) {
            System.out.println(t);
        }
    }
    
}
