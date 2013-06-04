package temp;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;


/**
 * Creates a 12 by 12 icon.  The icon consists of a black outline, filled in with a single Color.
 * 
 * @see Icon
 * 
 * @author Jeff Falkenham
 *
 */
public class ColorDialogIcon implements Icon{
	private int red;
	private int green;
	private int blue;
	private int height = 12;
	private int width = 12;
	
	/**
	 * The constructor for the class.  A Color parameter is used to determine
	 * the fill color of the icon
	 * 
	 * @param c
	 * 			the fill color of the icon
	 */
	ColorDialogIcon(Color c){
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
	}
	
	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		//paints the icon.  pretty self explanatory.
		Graphics2D graphics = (Graphics2D)g.create();
		graphics.setColor(new Color(red, green, blue));
		graphics.fillRect(x, y, width, height);
		graphics.setColor(new Color(0, 0, 0));
		graphics.drawRect(x, y, width, height);
		graphics.dispose();
		
	}


	
}
