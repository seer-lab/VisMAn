package data;

/**
 * This class represents a test case from a JUnit test suite.
 * @author David Petras
 *
 */
public class DataTest {

	private String testName; //The name of the test method from the JUnit test suite
	private String testResult; //The result from the test.
	
	/**
	 * Mutator (setter) method to set the name of the test. 
	 * @param _name the name of the test case
	 */
	public void setName(String _name)
	{
		this.testName = _name;
	}
	
	/**
	 * Mutator (setter) method to set the result of the test.
	 * @param _result "yes" if the test killed the mutant or "no" if it did not
	 */
	public void setResult(String _result)
	{
		this.testResult = _result;
	}
	
	/**
	 * Accessor method to get the result of the test.
	 * @return the result of the test
	 */
	public String getResult()
	{
		return this.testResult;
	}
	
	/**
	 * Accessor method to get the name of the test.
	 * @return the name of the test
	 */
	public String getName()
	{
		return this.testName;
	}
}	

