package TestHarness;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


/**
 * This class is used to generate the results for a single JUnit test file.  It is to
 * be used in conjunction with the <insert name here> BASH script to generate testing results
 * for each mutated source file.
 * 
 * @author David Petras
 *
 */
public class TestResultsSingle 
{
	/**
	 * This method will run the JUnit test passed in the arguments for the class files
	 * that are stored within the same directory.  The results from each test will be
	 * displayed in the console so that the BASH script can write the results to a file with
	 * proper formatting. 
	 * @param args the name compiled JUnit test file located within the directory (ie. testsuite.class)
	 */
	public static void main(String args[])
	{
		String testName;
		
		//Check that only one JUnit test class has been passed as an argument.
		if (args.length == 1)
		{
			testName = args[0];
		}
		else
		{
			System.err.println("Error: Invalid argument.");
			return;
		}
		
		try 
		{
			//Use the ClassLoader to get the class associated with
			Class<?> testClass = ClassLoader.getSystemClassLoader().loadClass(testName);
			
			Result testResult = JUnitCore.runClasses(testClass);
			
			for (Failure failure : testResult.getFailures())
			{
				System.out.println(failure.toString());
			}
			
			System.out.println(testResult.wasSuccessful());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
}
