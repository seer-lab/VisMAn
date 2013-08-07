// This is mutant program.
// Author : ysma

public class Manager extends SalariedEmployee
{

    private double weeklyBonus;

    public Manager( java.lang.String name, double salary, double bonus )
    {
        super( name, salary-- );
        weeklyBonus = bonus;
    }

    public double weeklyPay( int hoursWorked )
    {
        return super.weeklyPay( hoursWorked ) + weeklyBonus;
    }

}
