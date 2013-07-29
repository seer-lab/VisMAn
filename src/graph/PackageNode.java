package graph;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class is used to represent the internal nodes of the source tree
 * which are the directories that make up the packaged structure.
 * @author David Petras
 *
 */
public class PackageNode extends DefaultMutableTreeNode 
{
	private File directoryPath;
	private int numberOfMutants;
	private double averagePercentKilled;
	private int numberOfLowDetected;
	private int numberOfMedDetected;
	private int numberOfHighDetected;
	
	/**
	 * This is the constructor which will assign the File passed to it
	 * to the newly instantiated packageNode.
	 * @param directory
	 */
	public PackageNode(File directory)
	{
		super(directory);
		directoryPath = directory;
	}
	
	/**
	 * This method will return a string representing the packageNode.
	 * @return the name of the package
	 */
	@Override
	public String toString()
	{
		return directoryPath.getName();
	}
	
	/**
	 * Accessor method to retrieve the number of mutants within the classes of
	 * the package.
	 * @return the number of mutants within the package
	 */
	public int getNumberOfMutants()
	{
		return numberOfMutants;
	}
	
	/**
	 * Mutator (setter) method to set the number of mutants within the package.
	 * @param _numberOfMutants the number of mutants within the package
	 */
	public void setNumberOfMutants(int _numberOfMutants)
	{
		this.numberOfMutants = _numberOfMutants;
	}
	
	/**
	 * Accessor method to get the average percentage of test cases that kill the mutants
	 * within the package.
	 * @return the average percent of test cases that kill the mutants within the package
	 */
	public double getAveragePercentKilled()
	{
		return averagePercentKilled;
	}
	
	/**
	 * Mutator (setter) method to set the average percent of test cases that kill a mutant within
	 * the package.
	 * @param percentKilled the average percent of test cases that kill a mutant with the package
	 */
	public void setAveragePercentKilled(double percentKilled)
	{
		this.averagePercentKilled = percentKilled;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void incrementLowDetected(int value)
	{
		numberOfLowDetected += value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void incrementMedDetected(int value)
	{
		numberOfMedDetected += value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void incrementHighDetected(int value)
	{
		numberOfHighDetected += value;
	}
	
	public int getLowDetected()
	{
		return this.numberOfLowDetected;
	}
	
	public int getMedDetected()
	{
		return this.numberOfMedDetected;
	}
	
	public int getHighDetected()
	{
		return this.numberOfHighDetected;
	}

}
