// This is mutant program.
// Author : ysma

public class SalariedEmployee extends Employee
{

    private double annualSalary;

    public SalariedEmployee( java.lang.String name, double salary )
    {
        setName( name );
        annualSalary = salary;
    }

    // public double weeklyPay( int hoursWorked ){ ... }

}
