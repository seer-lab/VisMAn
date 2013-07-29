package graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

public class PieChartIcon implements Icon {

	private double percentRed;
	private double percentYellow;
	private double percentGreen;
	
	private static final int sideLength = 60;
	
	public PieChartIcon(double _percentRed, double _percentYellow, double _percentGreen)
	{
		this.percentRed = _percentRed;
		this.percentYellow = _percentYellow;
		this.percentGreen = _percentGreen;
	}
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setColor(Color.red);
		g2d.fillRect(x, y + (int) (sideLength-(percentRed*sideLength)), sideLength, (int) percentRed*sideLength);
		
		g2d.setColor(Color.yellow);
		g2d.fillRect(x, y + (int) percentGreen*sideLength, sideLength, (int) percentYellow*sideLength);
		
		g2d.setColor(Color.green);
		g2d.fillRect(x, y, sideLength, (int) percentGreen * sideLength);
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return sideLength;
	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return sideLength;
	}

}
