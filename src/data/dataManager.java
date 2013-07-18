package data;

import graph.classNode;
import graph.packageNode;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class dataManager {
	
	private JTree fileHierarchy;
	private DefaultMutableTreeNode rootNode;
	
	private ArrayList<classNode> leafNodeList;
	
	private File initialDirectory;
	
	private ArrayList<dataMutant> mutantList;
	
	public dataManager(File directory)
	{
		mutantList = new ArrayList<dataMutant>();
		leafNodeList = new ArrayList<classNode>();
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
			packageNode newChild = new packageNode(currentFile);
			
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
				classNode newChild = new classNode(currentFile);
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
		for(classNode currentNode: leafNodeList)
		{
			for (dataMutant mutant: mutantList)
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
	public ArrayList<dataMutant> getMutantList()
	{
		return this.mutantList;
	}
	
	/**
	 * 
	 * @param mutant
	 */
	public void addMutant(dataMutant mutant)
	{
		this.mutantList.add(mutant);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<classNode> getLeafNodes()
	{
		return this.leafNodeList;
	}

}
