package data;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class represents a mutated class file for a program.  Instances of this class
 * contain information pertaining to the details of the mutation such as the name of
 * the mutant, the operator that created it, where the mutation occurred, and wheter or
 * not a test case was able to detect it.
 * @author David Petras
 *
 */
public class dataMutant extends DefaultMutableTreeNode
{
	private String name; //Name of the mutant. (ie. ROR_1)
	private String type; //Operation applied to create the mutant. (ROR)
	private String modifiedSource; //The modified source code.
	private String modifiedSourceName; //The name of the modified source code.
	private int line; //The line number where the modification occurred.
	private ArrayList<dataTest> tests; //All of the results from the tests run on the mutant.
	private double percentKilled; //The percentage of test cases that were able to kill the mutant.
	private Color nodeColor; //The color of the node on the graph.
	
	/**
	 * This method is the constructor and will create a new ArrayList for
	 * holding the tests.
	 */
	public dataMutant()
	{
		tests = new ArrayList<dataTest>();
	}
	
	/**
	 * Mutator (setter) method to set the name of the mutant.
	 * @param _name the name of the mutant
	 */
	public void setName(String _name)
	{
		this.name = _name;
	}
	
	/**
	 * Mutator (setter) method to set the type of mutation (mutation operator used
	 * to create the mutant).
	 * @param _type the type of mutation applied to the code
	 */
	public void setType(String _type)
	{
		this.type = _type;
	}
	
	/**
	 * Mutator (setter) method to set the modified source code file name.
	 * @param _modifiedName the file name of the modified source code
	 */
	public void setModifiedSourceName(String _modifiedName)
	{
		this.modifiedSourceName = _modifiedName;
	}
	
	/**
	 * Mutator (setter) method to set the modified source code.
	 * @param _modifiedSource the modified version of the source code
	 */
	public void setModifiedSource(String _modifiedSource)
	{
		this.modifiedSource = _modifiedSource;
	}
	
	/**
	 * Mutator (setter) method to set which line of the source code was modified.
	 * @param _line the line of code modified to create the mutant
	 */
	public void setLine(int _line)
	{
		this.line = _line;
	}
	
	/**
	 * Accessor method to get the name of the mutant.
	 * @return the name of the mutant.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Accessor method to get the type of the mutant.
	 * @return the mutation operator that was applied to create the mutant
	 */
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * Accessor method to get the modified source code.
	 * @return the modified source code
	 */
	public String getModifiedSource()
	{
		return this.modifiedSource;
	}
	
	/**
	 * Accessor method to get the name of the modified source file.
	 * @return the name of the modified source file
	 */
	public String getModifiedSourceName()
	{
		return this.modifiedSourceName;
	}
	
	/**
	 * Accessor method to get the line number where the mutation occurred.
	 * @return the line number where the code was mutated
	 */
	public int getLine()
	{
		return this.line;
	}
	
	/**
	 * This method adds a test to the mutant.
	 * @param _test the test to associate with the mutant.
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
	
	/**
	 * Accessor method to retrieve a list of the tests. 
	 * @return a list containing all of the added tests
	 */
	public ArrayList<dataTest> getTestArray()
	{
		return this.tests;
	}
	
	@Override
	/**
	 * This method will return the name of the mutant.
	 * @return the name of the mutant
	 */
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Mutator (setter) method of setting the color of the node.
	 * @param _color the color of the node
	 */
	public void setColor(Color _color)
	{
		this.nodeColor = _color;
	}
	
	/**
	 * Accessor method for getting the color of the ndoe.
	 * @return the color of the node.
	 */
	public Color getColor()
	{
		return this.nodeColor;
	}

}
