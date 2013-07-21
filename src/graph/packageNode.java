package graph;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class packageNode extends DefaultMutableTreeNode 
{
	private File directoryPath;
	private int numberOfMutants;
	private double averagePercentKilled;
	
	/**
	 * 
	 * @param directory
	 */
	public packageNode(File directory)
	{
		super(directory);
		directoryPath = directory;
	}
	
	/**
	 *
	 * @return
	 */
	@Override
	public String toString()
	{
		return directoryPath.getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfMutants()
	{
		return numberOfMutants;
	}
	
	/**
	 * 
	 */
	public void setNumberOfMutants(int _numberOfMutants)
	{
		this.numberOfMutants = _numberOfMutants;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getAveragePercentKilled()
	{
		return averagePercentKilled;
	}
	
	/**
	 * 
	 * @param percentKilled
	 */
	public void setAveragePercentKilled(double percentKilled)
	{
		this.averagePercentKilled = percentKilled;
	}
	
	
	
	
}
