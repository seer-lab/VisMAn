package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;

import data.dataMutant;


/**
 * This class is used to represent the leaf nodes of the file system tree which
 * are the class level source files of the program.
 * @author David Petras
 *
 */
public class classNode extends DefaultMutableTreeNode {
	
	private File sourceFile; //The actual .java source code file.
	private String sourceCode; //A string containing the source code for the class.
	private ArrayList<dataMutant> mutantList;
	
	/**
	 * This method is used to construct a new instance of a class node.
	 * @param _sourceFile
	 */
	public classNode(File _sourceFile)
	{
		super(_sourceFile);
		mutantList = new ArrayList<dataMutant>();
		sourceFile = _sourceFile;
		loadSourceCode();
	}
	
	@Override
	public String toString()
	{
		return sourceFile.getName();
	}
	
	/**
	 * This method will take each line of the source file and add them to the
	 * source code string.
	 */
	private void loadSourceCode()
	{
		Scanner input;
		sourceCode = "";
		try 
		{
			input = new Scanner(sourceFile);
			while(input.hasNextLine())
			{
				sourceCode += "\n"+input.nextLine();
			}
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will return the source code for a class node.
	 * @return the source code
	 */
	public String getSource()
	{
		return this.sourceCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return sourceFile.getName();
	}
	
	/**
	 * 
	 * @param _mutant
	 */
	public void addMutant(dataMutant _mutant)
	{
		this.mutantList.add(_mutant);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<dataMutant> getMutantList()
	{
		return this.mutantList;
	}
}
