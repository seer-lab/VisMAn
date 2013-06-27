package XMLGenerator;

/**
 * This class represents an individual source code file.
 * @author David Petras
 */
public class SourceCode {
	private String code; //The Java code for the program.
	private String name; //The name of the file.
	private Boolean main; //Shows if the class contains the main method.
	
	/**
	 * This method constructs a new sourceCode instance.
	 * @param code The Java code for the program.
	 * @param name The name of the file.
	 * @param main Shows if the class contains the main method.
	 */
	public SourceCode (String code, String name, Boolean main)
	{
		this.code = code;
		this.name = name;
		this.main = main;
	}
	
	/**
	 * This method provides access to the source code.
	 * @return The source code.
	 */
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * This method provides access to the name of the file.
	 * @return The name of the source file.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * This method returns if the file contains the main method.
	 * @return Whether or not the file contains the main method.
	 */
	public Boolean isMain()
	{
		return this.main;
	}

}
