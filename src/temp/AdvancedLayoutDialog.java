package temp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdvancedLayoutDialog extends JDialog{


    private static final long serialVersionUID = 1;
    public static final int FLAG_CIRCLE_LAYOUT = 35;
    public static final int FLAG_FR_LAYOUT = 935;
    public static final int FLAG_SPRING_LAYOUT = 932;
    private static final int INTERNAL_FLAG_SPRING_LAYOUT_2 = 9329;
    private static final int INTERNAL_FLAG_FR_LAYOUT_2 = 2;

    private JSpinner jSpinnerRadius;
    private DisplayFrame framePointer;
    private JLabel jLabelMain;
    private Vector<Component> components;


    AdvancedLayoutDialog(DisplayFrame owner, boolean modal, int flag) {

        super(owner, modal);
        framePointer = owner;
        
        this.setTitle("Settings");
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);


        switch(flag) {
        case FLAG_CIRCLE_LAYOUT:

            BoxLayout layout = new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS);



            this.setLayout(layout);


            jLabelMain = new JLabel("Circle Layout Options");
            JPanel jPanelLabel = new JPanel();
            jPanelLabel.add(jLabelMain);
            jPanelLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            this.add(jPanelLabel);

            JLabel jLabelRadius = new JLabel("Radius: ");

            jSpinnerRadius = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            jSpinnerRadius.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    // TODO Find out if this is needed

                    try {
                        jSpinnerRadius.commitEdit();
                    } catch(Exception epn) {
                        System.out.println("Error.  Useful details:\n");
                        epn.printStackTrace();
                    }

                }

            });

            JPanel jPanelRadius = new JPanel(new FlowLayout());
            jPanelRadius.add(jLabelRadius);
            jPanelRadius.add(jSpinnerRadius);

            this.setPreferredSize(new Dimension(200, 130));
            //this.setMaximumSize(new Dimension(200, 100));
            this.setResizable(false);
            JPanel jPanelSpinner = new JPanel();
            jPanelSpinner.setLayout(new FlowLayout());
            //tempSpinner.setMaximumSize(tempSpinner.getPreferredSize());
            //lol.setMaximumSize(this.getPreferredSize());
            this.add(jPanelRadius);

            JButton jButtonClose = new JButton("Close");
            jButtonClose.setAlignmentX(Component.CENTER_ALIGNMENT);
            jButtonClose.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    jButtonCloseCircleListener(e);
                }

            });
            this.add(jButtonClose);
            this.add(Box.createRigidArea(new Dimension(
                    this.getPreferredSize().width - jSpinnerRadius.getPreferredSize().width - jLabelMain.getPreferredSize().width + 200,
                    this.getPreferredSize().height - jSpinnerRadius.getPreferredSize().height - jLabelMain.getPreferredSize().height + 200)));



            break;  //placeholder
        case FLAG_FR_LAYOUT:
            jLabelMain = new JLabel("FR-Layout Options");
            flag = INTERNAL_FLAG_FR_LAYOUT_2;
        case FLAG_SPRING_LAYOUT:
            jLabelMain = new JLabel("Spring Layout Options");
            flag = INTERNAL_FLAG_SPRING_LAYOUT_2;
        case INTERNAL_FLAG_FR_LAYOUT_2:
            flag = INTERNAL_FLAG_SPRING_LAYOUT_2;
        case INTERNAL_FLAG_SPRING_LAYOUT_2:
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
            components = new Vector<Component>();
            
            components.add(jLabelMain);
            components.add(new JLabel("Set Attraction: "));
            components.add(new JLabel("Set Repulsion: "));
            components.add(new JLabel("Set Max Iterations: "));
            components.add(new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 1.0)));
            components.add(new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000, 1.0)));
            components.add(new JSpinner(new SpinnerNumberModel(0, 0, 30000, 1)));

            
            Vector<JPanel> jPanels = new Vector<JPanel>();
            jPanels.add(new JPanel());
            jPanels.get(0).add(jLabelMain);
            this.add(jPanels.get(0));
            for(int i = 1; (i + 3) < components.size(); i++) {

                
                jPanels.add(new JPanel());
                

                
                jPanels.get(i).add(components.get(i));
                jPanels.get(i).add(components.get(i + 3));
                this.add(jPanels.get(i));
            }
            
            JButton jButtonClose2 = new JButton("Close");
            
            ((JButton)this.add(jButtonClose2)).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    jButtonCloseFRListener();
                }
                
            });
            jButtonClose2.setAlignmentX(JButton.CENTER_ALIGNMENT);
            this.add(Box.createRigidArea(this.getPreferredSize()));
            
            this.setPreferredSize(new Dimension(250, 200));
            
            
            
        }
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }
    
    private void jButtonCloseCircleListener(ActionEvent evt) {
        try {
            jSpinnerRadius.commitEdit();
            framePointer.advancedLayoutList.clear();
            framePointer.advancedLayoutList.add((Integer)jSpinnerRadius.getValue());
            this.dispose();
        }catch(Exception e) {
            System.out.println("Error.  Useful details:\n");
            e.printStackTrace();
        }
    }
    
    private void jButtonCloseFRListener() {
        try {
            for(Component i:components) {
                if(i instanceof JSpinner)
                    ((JSpinner)i).commitEdit();
            }
            
            framePointer.advancedLayoutList.clear();
            
            int counter = 0;
            for(int i = 0; i < components.size(); i++) {
                if(components.get(i) instanceof JSpinner){
                    if(counter != 2){
                        framePointer.advancedLayoutList.add(Double.parseDouble(String.valueOf((((JSpinner)components.get(i)).getValue()))));
                    }else {
                        framePointer.advancedLayoutList.add(Integer.parseInt(String.valueOf((((JSpinner)components.get(i)).getValue()))));
                    }
                    counter++;
                }
            }
            
            this.dispose();
        }catch(Exception e) {
            System.out.println("Error.  Useful details:\n");
            e.printStackTrace();
        }
    }



}
