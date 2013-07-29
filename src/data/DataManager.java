package data;

import graph.ClassNode;
import graph.PackageNode;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class DataManager {
	
	private JTree fileHierarchy;
	private DefaultMutableTreeNode rootNode;
	
	private ArrayList<ClassNode> leafNodeList;
	
	private File initialDirectory;
	
	private ArrayList<DataMutant> mutantList;
	
	public DataManager(File directory)
	{
		mutantList = new ArrayList<DataMutant>();
		leafNodeList = new ArrayList<ClassNode>();
		initialDirectory = directory;
		createFileHierarchy(initialDirectory);
	
	}
	
	
	/**
	 * This method will navigate through the file system and create appropriate nodes for each
	 * package and source file.  The nodes will be added to create the full fileHierarchy tree.
	 * @param node
	 * @param currentFile
	 */
	private void getFiles(DefaultMutableTreeNode node, File currentFile)
	{
		if (currentFile.isDirectory())
		{
			PackageNode newChild = new PackageNode(currentFile);
			
			node.add(newChild);
			File fileList[] = currentFile.listFiles();
			for (int i = 0; i < fileList.length; i++)
			{
				getFiles(newChild,fileList[i]);
			}
		}
		else
		{
			if(currentFile.getName().endsWith(".java"))
			{
				ClassNode newChild = new ClassNode(currentFile);
				node.add(newChild);
				leafNodeList.add(newChild);
			}
		}
	}
	
	/**
	 * This method will construct the file hierarchy from the folder selected by the user.
	 * @param path the path to the folder
	 */
	private void createFileHierarchy(File selectedDirectory)
	{
		String selectedPath = selectedDirectory.getAbsolutePath();
		
		File originalDirectory = new File(selectedPath+"/original");
		
		rootNode = new DefaultMutableTreeNode("root");
		fileHierarchy = new JTree(rootNode);
		getFiles(rootNode,originalDirectory);
	}
	
	/**
	 * 
	 */
	public void linkMutantsToNodes()
	{
		for(ClassNode currentNode: leafNodeList)
		{
			for (DataMutant mutant: mutantList)
			{
				if (currentNode.getName().equals(mutant.getModifiedSourceName()))
				{
					currentNode.addMutant(mutant);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void produceAggregateData()
	{
		for (int i = 0; i < rootNode.getChildCount(); i++)
		{
			if (rootNode.getChildAt(i) instanceof PackageNode)
			{
				produceAggregateData((DefaultMutableTreeNode)rootNode.getChildAt(i));
			}
		}
	}
	
	/**
	 * 
	 * @param currentNode
	 */
	private void produceAggregateData(DefaultMutableTreeNode currentNode)
	{
		int numberOfMutants = 0;
		double totalPercentKilled = 0;
		
		int numberOfLow = 0;
		int numberOfMed = 0;
		int numberOfHigh = 0;
		
		for (int i = 0; i < currentNode.getChildCount(); i++)
		{
			if (currentNode.getChildAt(i) instanceof PackageNode)
			{
				produceAggregateData((DefaultMutableTreeNode) currentNode.getChildAt(i));
			}
			
			if (currentNode.getChildAt(i) instanceof PackageNode)
			{
				PackageNode node = (PackageNode) currentNode.getChildAt(i);
				
				numberOfLow += node.getLowDetected();
				numberOfMed += node.getMedDetected();
				numberOfHigh += node.getHighDetected();
				
				numberOfMutants += node.getNumberOfMutants();
				totalPercentKilled += node.getAveragePercentKilled();
			}
			else if (currentNode.getChildAt(i) instanceof ClassNode)
			{
				ClassNode node = (ClassNode) currentNode.getChildAt(i);
				
				numberOfLow += node.getLowDetected();
				numberOfMed += node.getMedDetected();
				numberOfHigh += node.getHighDetected();
				
				numberOfMutants += node.getNumberOfMutants();
				totalPercentKilled += node.getAggregateData();
			}
		}
		PackageNode activeNode = (PackageNode) currentNode;
		double averagePercentKilled = totalPercentKilled/currentNode.getChildCount();
		activeNode.setNumberOfMutants(numberOfMutants);
		activeNode.setAveragePercentKilled(averagePercentKilled);
		activeNode.incrementLowDetected(numberOfLow);
		activeNode.incrementMedDetected(numberOfMed);
		activeNode.incrementHighDetected(numberOfHigh);
		
	}

	/**
	 * 
	 * @return
	 */
	public JTree getFileTree()
	{
		return this.fileHierarchy;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DataMutant> getMutantList()
	{
		return this.mutantList;
	}
	
	/**
	 * 
	 * @param mutant
	 */
	public void addMutant(DataMutant mutant)
	{
		this.mutantList.add(mutant);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ClassNode> getLeafNodes()
	{
		return this.leafNodeList;
	}

}
