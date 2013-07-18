package graph;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class packageNode extends DefaultMutableTreeNode 
{
	private File directoryPath;
	
	public packageNode(File directory)
	{
		super(directory);
		directoryPath = directory;
	}
	
	@Override
	public String toString()
	{
		return directoryPath.getName();
	}
}
