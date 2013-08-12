import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class EmployeeTester {

	Employee empA, empB, empC;
	
	@Before
	public void setup()
	{
		empA = new HourlyEmployee("Name, Name",20);
		empB = new SalariedEmployee("Name, Name", 500);
		empC = new Manager("Name, Name", 400,10);
	}
	
	@Test
	public void hourlyTest()
	{
		assertEquals(40.0,empA.weeklyPay(2),0.01);
	}

	@Test
	public void managerTest() 
	{
		assertEquals(17.69,empC.weeklyPay(2),0.01);
	}
	
	@Test
	public void salariedTest()
	{
		assertEquals(9.61,empB.weeklyPay(2),0.01);
	}

}
