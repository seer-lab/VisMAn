package XMLGenerator;

/**
 * This class represents a mutated source code file.
 * @author David Petras
 */
public class ModifiedCode extends SourceCode {
	
	private String type; //The type of mutation applied to the code.
	private int startLine; //The first line where the mutation was applied.
	private int endLine; //The last line where the mutation was applied.
	private String mutantCode;
	private String method;
	
	/**
	 * This method constructs a new instance of the modified code.
	 * @param code The Java code for the program.
	 * @param name The name of the file.
	 * @param type The type of mutation applied to the code.
	 * @param startLine The first line where the mutation was applied.
	 * @param endLine The last line where the mutation was applied.
	 */
	public ModifiedCode(String code, String name, String type, int startLine, int endLine, String method)
	{
		super(code,name,false);
		this.type = type;
		this.startLine = startLine;
		this.endLine = endLine;
		this.mutantCode = code;
		this.method = method;
	}
	
	/**
	 * This method allows for access to the type of mutation applied to the code.
	 * @return The type of mutation applied to the code.
	 */
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * This method allows for access to the first line of the mutation.
	 * @return The first line of the mutation.
	 */
	public int getStartLine()
	{
		return this.startLine;
	}
		
	/**
	 * This method allows for access to the last line of the mutation.
	 * @return The last line of the mutation.
	 */
	public int getEndLine()
	{
		return this.endLine;
	}
	
	/**
	 * This method allows for access to the method under which the mutation was made.
	 * @return the method where the mutation took place.
	 */
	public String getMethod()
	{
		return this.method;
	}
	
	/**
	 * This method allows for the modification of the associated code.
	 */
	public void setCode(String _code)
	{
		this.mutantCode = _code;
	}
	
	@Override
	/**
	 * This method overrides the getCode method from the superclass and returns the mutated code.
	 */
	public String getCode()
	{
		return this.mutantCode;
	}

}
