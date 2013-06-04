package temp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.uci.ics.jung.visualization.VisualizationViewer;

public class TabComponents extends JPanel{

    private JTabbedPane parent;
    String nodeName;
    
    //connection canavases
    private Vector<VisualizationViewer<MyVertex, MyEdge>> canvases;
    private static final long serialVersionUID = 1;
    
    TabComponents(String node, JTabbedPane pane, 
            Vector<VisualizationViewer<MyVertex, MyEdge>> canvases) {
        
        this.nodeName =node;
        parent = pane;
        this.canvases = canvases;
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){ 
                buttonListener();
            }
        });
        
        this.setLayout(new FlowLayout());
        JLabel label = new JLabel(){

            private static final long serialVersionUID = 1;
            
            public String getText(){
                return getLabelText();
            }
            
        };
        this.add(label);
        
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(12, 12));
        closeButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                // TODO Auto-generated method stub
                buttonListener();
                
            }
            
        });
        this.add(closeButton);
        
        Dimension size = new Dimension(0, 0);
        size.height = Math.max(label.getPreferredSize().height, closeButton.getPreferredSize().height);
        size.width = label.getPreferredSize().width + closeButton.getPreferredSize().width + 15;
        
        
        this.setPreferredSize(size);
        //this.setPreferredSize(new Dimension(500, 500));
        
   
    }
    
    private void buttonListener() {
        int i = parent.indexOfTabComponent(this);
        canvases.remove(i - 1);
        parent.remove(i);
        
    }
    
    private String getLabelText(){
        return nodeName;
    }
    
}
