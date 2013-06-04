package temp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

public class CloseButtonIcon implements Icon{
    private static final int HEIGHT_WIDTH = 12;
    
    @Override
    public int getIconHeight() {
        return HEIGHT_WIDTH;
    }

    @Override
    public int getIconWidth() {
        return HEIGHT_WIDTH;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        
        //paints the icon.  pretty self explanatory.
        Graphics2D graphics = (Graphics2D)g.create();
        graphics.setColor(new Color(255, 0, 0));
        graphics.fillRect(x, y, HEIGHT_WIDTH, HEIGHT_WIDTH);
        graphics.setColor(new Color(0, 0, 0));
        graphics.drawRect(x, y, HEIGHT_WIDTH, HEIGHT_WIDTH);
        graphics.drawLine(x, y, x + HEIGHT_WIDTH, y + HEIGHT_WIDTH);
        //graphics.drawLine(x, y, x + HEIGHT_WIDTH, 0);
        graphics.dispose();
        
    }

}
