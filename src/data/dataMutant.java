package data;

import java.util.ArrayList;

/**
 * 
 * @author 100425830
 *
 */
public class dataMutant 
{
	private String name; //Name of the mutant. (ie. ROR_1)
	private String type; //Operation applied to create the mutant. (ROR)
	private String modifiedSource; //The modified source code.
	private String modifiedSourceName; //The name of the modified source code.
	private int line; //The line number where the modification occurred.
	private ArrayList<dataTest> tests; //All of the results from the tests run on the mutant.
	private double percentKilled;
	
	/**
	 * 
	 */
	public dataMutant()
	{
		tests = new ArrayList<dataTest>();
	}
	
	/**
	 * 
	 * @param _name
	 */
	public void setName(String _name)
	{
		this.name = _name;
	}
	
	/**
	 * 
	 * @param _type
	 */
	public void setType(String _type)
	{
		this.type = _type;
	}
	
	/**
	 * 
	 * @param _modifiedName
	 */
	public void setModifiedSourceName(String _modifiedName)
	{
		this.modifiedSourceName = _modifiedName;
	}
	
	/**
	 * 
	 * @param _modifiedSource
	 */
	public void setModifiedSource(String _modifiedSource)
	{
		this.modifiedSource = _modifiedSource;
	}
	
	/**
	 * 
	 * @param _line
	 */
	public void setLine(int _line)
	{
		this.line = _line;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModifiedSource()
	{
		return this.modifiedSource;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModifiedSourceName()
	{
		return this.modifiedSourceName;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLine()
	{
		return this.line;
	}
	
	/**
	 * 
	 * @param _test
	 */
	public void addTest(dataTest _test)
	{
		this.tests.add(_test);
	}
	
	/**
	 * This method calculates and returns the number of test cases that were able to successfully detect
	 * the mutant.
	 * @return the percentage of test cases that are able to kill the mutant
	 */
	public double getPercentKilled()
	{
		double killedCounter = 0;
		for (dataTest test: tests)
		{
			if (test.getResult().equals("yes"))
			{
				killedCounter++;
			}
		}
		this.percentKilled = killedCounter/tests.size();
		return this.percentKilled;
	}

}
