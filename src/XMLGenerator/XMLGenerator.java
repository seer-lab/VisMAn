package XMLGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class represents the XML Generator that takes output from MuJava and converts
 * it to the XML representation read by VisMan.
 * @author David Petras
 *
 */
public class XMLGenerator {
	
	//Instance variables.
	private String inputFilePath;
	private String testFilePath;
	private String resultsPath;
	
	private String programName;
	
	private String xmlOutput;
	
	private HashMap<String, HashMap> mutantHash;
	private HashMap<String, Boolean> testHash;
	
	//Constants
	private static final String NEW_LINE = "\n";
	private static final String XML_HEADER = "<?xml version=\"1.0\"?>";
	private static final String OPENING_DATA_TAG = "<data>";
	private static final String CLOSING_DATA_TAG = "</data>";
	private static final String OPENING_ORIGINAL_TAG = "<original_program>";
	private static final String CLOSING_ORIGINAL_TAG = "</original_program>";
	
	/**
	 * This method constructs an instance of the XMLGenerator.  In its current form, the generator is designed to
	 * handle testing only a single class at a time.  This is for prototype purposes only.
	 * @param _inputFilePath the path to desired folder in the muJava results folder
	 * @param _testFilePath the path to the JUnit test file
	 * @param _outputFilePath the path to the output folder
	 */
	public XMLGenerator(String _inputFilePath, String _resultsPath, String _testFilePath)
	{
		this.inputFilePath = _inputFilePath;
		this.testFilePath = _testFilePath;
		this.resultsPath = _resultsPath;
		xmlOutput = "";
		mutantHash = new HashMap<String, HashMap>();
		testHash = new HashMap<String, Boolean>();
	}
	
	/**
	 * This method will begin the process of generating the XML file from the muJava output.
	 * @return a properly formatted XML file for the VisMan tool
	 */
	public String generateXMLFile()
	{
		//Set the header for the XML output file.
		xmlOutput = xmlOutput + XML_HEADER + NEW_LINE;
		//Open the data tag for the XML output file.
		xmlOutput = xmlOutput + OPENING_DATA_TAG + NEW_LINE;
		//Add the original source code.
		xmlOutput = xmlOutput + getOriginalProgram() + NEW_LINE;
		//Add the mutation information.
		xmlOutput = xmlOutput + getMutantPrograms() + NEW_LINE;
		//Close the data tag for the XML output file.
		xmlOutput = xmlOutput + CLOSING_DATA_TAG;
		return xmlOutput; //Return the XML code.
	}
	
	/**
	 * This method will generate the XML code for the original program.
	 * @return the XML code for the original program
	 */
	private String getOriginalProgram()
	{
		
		//String to hold the XML code for the original_program section.
		String originalProgramOutput = "";
		
		//Add the opening tag for the original_program.
		originalProgramOutput += OPENING_ORIGINAL_TAG + NEW_LINE;
		
		File originalDirectory = new File (inputFilePath+"/original");
		//Create a filter to only retrieve the .java source file from the original folder.
		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File originalDirectory, String fileName)
			{
				return fileName.endsWith(".java");
			}
		};
		
		File[] originalContents = originalDirectory.listFiles(filter);
		//Locate the original source file from the contents of the directory.
		File sourceFile = originalContents[0];
		
		//Set the programName instance variable.
		programName = sourceFile.getName();
		
		//Open the source_code tag.
		originalProgramOutput += "<source_code name=\""+sourceFile.getName()+"\" main=\"false\">"+NEW_LINE;
		
		//Create a scanner to read each line from the code and add it to the programOutput XML code.
		String originalProgramHolder = "";
		try 
		{
			Scanner scanner = new Scanner(sourceFile);
			while (scanner.hasNextLine())
			{
				originalProgramHolder += scanner.nextLine() + NEW_LINE;
			}
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		originalProgramHolder = originalProgramHolder.replace("&", "&amp;");
		originalProgramHolder = originalProgramHolder.replace("<", "&lt;");
		originalProgramHolder = originalProgramHolder.replace(">", "&gt;");
		originalProgramHolder = originalProgramHolder.replace("\"", "&quot");
		originalProgramHolder = originalProgramHolder.replace("'","&apos;");
		
		originalProgramOutput += originalProgramHolder + NEW_LINE;
		
		//Close the source_code tag.
		originalProgramOutput += "</source_code>" + NEW_LINE;
		
		//Close the original_program tag.
		originalProgramOutput += CLOSING_ORIGINAL_TAG + NEW_LINE;
		
		
		
		return originalProgramOutput;
	}
	/**
	 * This method will generate the XML code for the mutant programs by running the JUnit test suite for each
	 * one. 
	 * @return the XML code for the mutant programs
	 */
	private String getMutantPrograms()
	{
		//Create a string to hold the XML code for the mutated programs.
		String mutationXMLCode = "";
		
		/*Parse the mutation_log to extract the required information about each mutant; store the information in a hashtable
		 * with the mutation name as the key and the ModifiedCode object as the stored object.
		 */
		HashMap<String, ModifiedCode> mutantMap = new HashMap<String,ModifiedCode>();
		ArrayList<String> mutantList = new ArrayList<String>();
		
		File mutationLog = new File(inputFilePath + "/traditional_mutants/mutation_log");
		
		try {
			Scanner logScanner = new Scanner(mutationLog);
			while (logScanner.hasNext())
			{
				String logLine = logScanner.nextLine();
				String[] parsedLine = logLine.split(":");
				String[] typeArray = parsedLine[0].split("_");
				String mutantType = typeArray[0];
				ModifiedCode mutantCode = new ModifiedCode("",parsedLine[0],mutantType,Integer.parseInt(parsedLine[1]),Integer.parseInt(parsedLine[1]),parsedLine[2]);
				mutantList.add(mutantCode.getName());
				mutantMap.put(mutantCode.getName(), mutantCode);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Examine each mutant and set the associated source code from file.
		for (String mutantName: mutantList)
		{
			ModifiedCode targetCode = mutantMap.get(mutantName);
			File mutantSource = new File(inputFilePath+"/traditional_mutants/"+targetCode.getMethod()+"/"+targetCode.getName()+"/"+programName);
			try 
			{
				Scanner mutantScanner = new Scanner(mutantSource);
				String mutantSourceCode = "";
				while (mutantScanner.hasNext())
				{
					mutantSourceCode += mutantScanner.nextLine() + NEW_LINE;
				}
				mutantScanner.close();
				targetCode.setCode(mutantSourceCode);
				mutantMap.put(mutantName, targetCode);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
		//Parse the test case information.
		ArrayList<String> testCases = JUnitParser.getTestNames(testFilePath);
		
		
		
		//Examine each entry in the mutantMap and generate the XML and testing code for it.
		for (String mutantName: mutantList)
		{
			mutationXMLCode += "<mutant_program name=\""+mutantMap.get(mutantName).getName()+"\" type=\""+mutantMap.get(mutantName).getType()+"\">" + NEW_LINE;
			mutationXMLCode += "<modified_source name=\""+programName+"\" start_line=\""+mutantMap.get(mutantName).getStartLine()+"\" end_line=\"" + mutantMap.get(mutantName).getEndLine()+"\">"+NEW_LINE;
			
			String mutationCodeHolder = mutantMap.get(mutantName).getCode();
			//Replace the reserved XML characters with the appropriate entity reference.
			mutationCodeHolder = mutationCodeHolder.replace("&", "&amp;");
			mutationCodeHolder = mutationCodeHolder.replace("<", "&lt;");
			mutationCodeHolder = mutationCodeHolder.replace(">", "&gt;");
			mutationCodeHolder = mutationCodeHolder.replace("\"", "&quot");
			mutationCodeHolder = mutationCodeHolder.replace("'","&apos;");
			
			mutationXMLCode += mutationCodeHolder + NEW_LINE;
			mutationXMLCode += "</modified_source>"+NEW_LINE;
			

			String testName = "";
			
			try {
				Scanner resultScanner = new Scanner(new File(resultsPath));
				while (resultScanner.hasNextLine())
				{
					String resultLine = resultScanner.nextLine();
					if (resultLine.matches("====(.+)===="))
					{
						char[] splitArray = resultLine.toCharArray();
						testName = "";
						for (int i = 4; i < splitArray.length-4; i++)
						{
							testName += splitArray[i];
						}
						
						testHash = new HashMap<String, Boolean>();
						//Initially set all of the mutants to not killed.
						for (String nameOfTest: testCases)
						{
							testHash.put(nameOfTest, false);
						}
					}
					//If the line contains true or false, then all of the 
					else if (resultLine.matches("(true)|(false)"))
					{
						mutantHash.put(testName, testHash);
					}
					else
					{
						String[] testSplit = resultLine.split("\\(");
						testHash.put(testSplit[0], true);
					}
						
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			for (String testCaseName: testCases)
			{
				mutationXMLCode += "<test name=\""+testCaseName+"\">"+NEW_LINE;
				String testResult;
				if ((boolean) mutantHash.get(mutantName).get(testCaseName) == true)
				{
					testResult = "yes";
				}
				else
				{
					testResult = "no";
				}
				mutationXMLCode += "<result>"+testResult+"</result>"+NEW_LINE;
				mutationXMLCode += "</test>" + NEW_LINE;
			}
			
			mutationXMLCode += "</mutant_program>" + NEW_LINE;
		}
		
		
		return mutationXMLCode;
	}
	
	
	
}
