package temp;
import java.util.Vector;



/**
 * This class is used to store all the information for a node.  The node can be
 * a Mutant or a Test, but in the comments it will just be referred to as a node.
 * The data stored in the node will be referred to as data item.
 * 
 * @author Jeff Falkenham
 *
 */
class MutationData{
	//the name of the data item
	private Vector<String> data = new Vector<String>();
	
	//Whether the bug was found or not
	private Vector<String> found = new Vector<String>();
	
	//line numbers of source code that correspond to the node (only used if the node is a mutant).
	private Vector<Integer> lineNumbers = new Vector<Integer>(); 
	
	//the name of the node
	private String name;
	
	private String modifiedSource;
	
	
	//stores extra data from the xml file
	private Vector<XmlStorage> xmlStore = new Vector<XmlStorage>();
	
	private String mutantType;
	
	private String sourceFile;
	
	/**
	 * this is a get method for the xml data
	 * 
	 * @return		returns the object containing the xml data
	 */
	public Vector<XmlStorage> getXml(){
		return xmlStore;
	}
	
	/**
	 * Set the type of the Mutant (only used when the node is a mutant)
	 * 
	 * @param type		The type
	 */
	public void setType(String type){
		mutantType = type;
	}
	
	/**
	 * Return the type of the Mutant (only used when the node is a mutant)
	 * 
	 * @return		The type
	 */
	public String getType(){
		return mutantType;
	}
	
	
	/**
	 * This method is the contructor for the class
	 * 
	 * @param name	The name of the node
	 */
	MutationData(String name){
		this.name = name;
	}
	
	/**
	 * Changes the name of this node.
	 * 
	 * @param name		The new name of this node.
	 */
	public void changeName(String name){
		this.name = name;
	}
		
	
	/**
	 * Returns the size of the data item variable
	 * 
	 * @return	returns the size of the data item variable
	 */
	public int getSize(){
		return data.size();
	}
	
	
	/**
	 * Adds the name of the data item, and the result contained in the data item.
	 * Assumes the user will first call the method to add the data item, and then the result,
	 * in that order.
	 * 
	 * @param string	This is the data/result that is added to the object
	 */
	public void add(String string){
		if(data.size()==found.size()){
			data.addElement(string);
		}else{
			found.addElement(string);
		}
		
	}
		

	
	/**
	 * Gets the name of the specified data item
	 * 
	 * @param index		The index where the data item you want is located
	 * @return			The name of the data item (String)
	 */
	public String getData(int index){
		return data.get(index);
	}
	
	/**
	 * Gets the result of the data item for the user.
	 * 
	 * @param index			The index where the result you want is located
	 * @return				The result (String)
	 */
	public String getResult(int index){
		return found.get(index);
	}
	
	
	/**
	 * Gets the name of the node.
	 * 
	 * @return	The name of the node.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the percentage of successfully killed mutants
	 * 
	 * @return		a float containing the percentage of successfully
	 * 				 killed mutants (1.0 = 100%, 0.0 = 0%)
	 */
	public float getPercent(){
		float total = 0;
		//loops through all the values
		for(int i = 0;i < found.size();i++){
			if(found.get(i).equals("yes")){
				total++;
			}
		}
		total = (total/found.size());
		return total;
		
	}
	
	/**
	 * adds a line number to this Node. (Only used when the node is a mutant)
	 * 
	 * @param line		the line number (int)
	 */
	public void addLineNumber(int line){
		Integer tempLine = new Integer(line);
		lineNumbers.add(tempLine);
	}
	
	
	/**
	 * Adds the line numbers from start to end.  (only used when the node is a mutant)
	 * 
	 * @param start		Starting line number
	 * @param end		Ending line number
	 */
	public void addLineNumbers(int start, int end){
		for(int x = start;x <= end;x++){
			lineNumbers.add(x);
		}
	}
	
	/**
	 * get method to return the line number Vector
	 * 
	 * @return		The line number Vector
	 */
	public Vector<Integer> getLineNumbers(){
		return lineNumbers;
	}
	
	public boolean isLineNumber(int i) {
	    for(int x = 0; x < lineNumbers.size(); x++) {
	        if(lineNumbers.get(x) == i) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	/**
	 * Sets the filename that the lines of source code are located in
	 * (only used when the node is a mutant)
	 * 
	 * @param name		The filename
	 */
	public void setSourceFile(String name){
		sourceFile = name;
	}
	
	/**
	 * Gets the filename that the lines of source code are located in
	 * (only used when the node is a mutant)
	 * 
	 * @return		the filename
	 */
	public String getSourceFile(){
		return sourceFile;
	}
	
	/**
	 * Sets the modified source variable, that holds the mutated code.
	 * (Only used when the node is a mutant)
	 * 
	 * @param source		The mutated source code
	 */
	public void setModifiedSource(String source){
		modifiedSource = source;
	}
	
	/**
	 * Gets the mutated code
	 * (only used when the node is a mutant)
	 * 
	 * 
	 * @return		the mutated code
	 */
	public String getModifiedSource(){
		return modifiedSource;
	}
	
	/**
	 * Gets the data vector.
	 * @return
	 */
	public Vector<String> getDataVector()
	{
		return found;
	}
}
