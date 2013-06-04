package temp;
import java.util.Vector;



/**
 * A class that extends Vector.  Used to give the MutationVector a single variable
 * To determine which is being used as vertices/nodes (Tests or Mutants).  Also stores the
 * source code and file names.
 * 
 * @author Jeff Falkenham
 *
 * @param <T>		The type of the Vector
 */
public class MutationVector<T> extends Vector<T> {

    private static final long serialVersionUID = 1;

    private boolean mutant;
	private Vector<String> source = new Vector<String>();
	private Vector<String> file = new Vector<String>();
	private Vector<Vector<String>> newSource = new Vector<Vector<String>>();
	
	/**
	 * Constructor.  Calls the constructor for Vector, then gives a value to the mutant variable.
	 * 
	 * @param mutant		Whether the MutationVector refers to Tests or Mutants as nodes
	 */
	MutationVector(boolean mutant){
		super();
		this.mutant = mutant;
	}
		
	/**
	 * a get method for the mutant variable.
	 * 
	 * @return		a boolean that is used to determine whether this object uses Mutants or Tests as nodes.
	 */
	public boolean isMutant(){
		return mutant;
	}
	
	/**
	 * add source code and the file which contains the source code
	 * 
	 * @param src		the source
	 * @param filename	the file
	 */
	public void addSource(String src, String filename){
		source.add(src);
		file.add(filename);
		
	}
	
	/**
	 * get method for the source code
	 * 
	 * @param i		the index of the source code
	 * @return		the source code
	 */
	public String getSource(int i){
		return source.get(i);
	}
	
	
	
	/**
	 * get method for the filename
	 * 
	 * @param i		The index of the filename
	 * @return		The filename
	 */
	public String getFile(int i){
		return file.get(i);
	}
	
	/**
	 * Get the size of the vector holding all the source code
	 * 
	 * @return		the size
	 */
	public int getSourceSize(){
		return source.size();
	}
	
	
	public void convertSource(){
	    //TODO  does this do anything yet?
		newSource.clear();
		String newLine = "\n"; 
		for(int x = 0;x < source.size();x++){
			Vector<String> temp = new Vector<String>();
			int counter = 0;
			for(int y = 0;y < source.get(x).length() - newLine.length() + 1;y++){
				if(source.get(x).substring(y, y + newLine.length() - 1).equals(newLine)){
					temp.add(source.get(x).substring(counter, y - 1));
					y = y + newLine.length();
					counter = y;
				}
			}
			temp.add(source.get(x).substring(counter, source.get(x).length() - 1));
			newSource.add(temp);
		}
		
	}

}
