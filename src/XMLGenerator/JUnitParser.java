package XMLGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class will parse a JUnit source file (.java) and return an ArrayList containing
 * the names of each test.
 * @author David Petras
 *
 */
public class JUnitParser {
	
	private JUnitParser()
	{
	}
	
	public static ArrayList<String> getTestNames(String jUnitSourcePath)
	{
		ArrayList<String> testNames = new ArrayList<String>();
		
		
		File jUnitFile = new File(jUnitSourcePath);
		String sourceLine;
		
		try 
		{
			Scanner testScanner = new Scanner(jUnitFile);
			
			while(testScanner.hasNextLine())
			{
				sourceLine = testScanner.nextLine();
				sourceLine = sourceLine.replaceAll("\t", "");
				
				if (sourceLine.equals("@Test"))
				{
					String methodLine = testScanner.nextLine();
					methodLine = methodLine.replaceAll("\t", "");
					String[] methodParts = methodLine.split(" ");
					String methodName = methodParts[2];
					methodName = methodName.replaceAll("\\(\\)","");
					testNames.add(methodName);
				}
			}
			
			testScanner.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return testNames;
	}
	
	
}
