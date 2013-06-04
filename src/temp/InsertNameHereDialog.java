package temp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class InsertNameHereDialog extends JDialog{

    private static final long serialVersionUID = 1;
    JCheckBox jCheckBoxIsDiscrete;
    JPanel jPanelSpinners;
    //Vector<JSpinner> jSpinners = new Vector<JSpinner>();
    //<size, interval end> 2 element arrays
    //Vector<Integer[]> intervals = new Vector<Integer[]>();
    DisplayFrame framePointer;

    private static final int INTERNAL_FLAG_CLOSE_DISCRETE = 2461;
//  private static final int INTERNAL_FLAG_CLOSE_ANALOG = 5711;
    private static final int INTERNAL_FLAG_CLOSE_DEFAULT = 8251;


    InsertNameHereDialog(final DisplayFrame owner, boolean modal) {
        super(owner, modal);

        JLabel jLabelMain;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        framePointer = owner;
        framePointer.customShapeSize.clear();


        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        jLabelMain = new JLabel("Size Options:");

        jCheckBoxIsDiscrete = new JCheckBox("Discrete Intervals");

        jCheckBoxIsDiscrete.setSelected(false);

        jPanelSpinners = new JPanel();
        jPanelSpinners.setLayout(new FlowLayout());


        jCheckBoxIsDiscrete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jPanelSpinners.removeAll();
                framePointer.customShapeSize.clear();

                if(jCheckBoxIsDiscrete.isSelected()) {
                    constructDiscreteComponents();
                }else {
                    constructAnalogComponents();

                }
                //InsertNameHereDialog.this.repaint();
                //InsertNameHereDialog.this.getContentPane().invalidate();
                InsertNameHereDialog.this.getContentPane().validate();
                InsertNameHereDialog.this.getContentPane().repaint();

            }

        });

        this.add(jLabelMain);
        this.add(jCheckBoxIsDiscrete);
        this.add(jPanelSpinners);
        
        JButton jButtonDefault = new JButton("Default Settings");
        
        ((JButton)this.add(jButtonDefault)).addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        closeAndKillDialog(INTERNAL_FLAG_CLOSE_DEFAULT);
                    }  
                });
        
        jButtonDefault.setAlignmentX(JButton.CENTER_ALIGNMENT);
        
        jCheckBoxIsDiscrete.setSelected(false);
        jCheckBoxIsDiscrete.doClick();
        this.setPreferredSize(new Dimension(600, 400));
        this.pack();
        this.setVisible(true);







    }

    private void closeAndKillDialog(int flag) {
        if(flag == INTERNAL_FLAG_CLOSE_DISCRETE) {


            ((VectorFlag<Integer[]>)framePointer.customShapeSize).flag = VectorFlag.FLAG_CLOSE_DISCRETE;
            this.dispose();
        } else if(flag == INTERNAL_FLAG_CLOSE_DEFAULT) {
            framePointer.customShapeSize.clear();
            ((VectorFlag<Integer[]>)framePointer.customShapeSize).flag = VectorFlag.FLAG_CLOSE_DEFAULT;
            this.dispose();
        }
    }
    
    private void constructDiscreteComponents() {
        final JSpinner jSpinnerStart = (JSpinner)jPanelSpinners.add(new JSpinner(new SpinnerNumberModel(0, 0, 0, 1)));

        jSpinnerStart.setEnabled(false);
        JPanel jPanelStart = new JPanel();
        jPanelStart.add(new JLabel("Start of interval:"));
        jPanelStart.add(jSpinnerStart);
        JPanel jPanelEnd = new JPanel();
        jPanelEnd.add(new JLabel("End of Interval:"));
        final JSpinner jSpinnerEnd = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        jPanelEnd.add(jSpinnerEnd);
        final JSpinner jSpinnerSize = new JSpinner(new SpinnerNumberModel(0, 0, 30000, 1));
        JPanel jPanelSize = new JPanel();
        jPanelSize.add(new JLabel("Radius Size:"));
        jPanelSize.add(jSpinnerSize);


        final JButton jButtonNext = new JButton("Next");
        ((JButton)jPanelEnd.add(jButtonNext)).addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    jSpinnerEnd.commitEdit();
                }catch(Exception e) {
                    System.out.println("Error.  Useful info: ");
                    e.printStackTrace();
                    return;
                }
                int startValue;
                int endValue;
                int size;
                try {
                    startValue = (Integer)jSpinnerStart.getValue();
                    endValue = (Integer)jSpinnerEnd.getValue();
                    size = (Integer)jSpinnerSize.getValue();
                }catch (Exception e) {
                    System.out.println("Error.  Useful info: ");
                    e.printStackTrace();
                    return;

                }

                if((startValue >= 0) && (endValue > startValue) && (endValue <= 100)) {
                    Integer[] tempArray = {new Integer(size), new Integer(endValue)};
                    framePointer.customShapeSize.add(tempArray);
                    jSpinnerStart.setValue(jSpinnerEnd.getValue());

                    if(endValue == 100) {
                        closeAndKillDialog(InsertNameHereDialog.INTERNAL_FLAG_CLOSE_DISCRETE);
                    }




                }


            }

        });
        jPanelSpinners.add(jPanelStart);
        jPanelSpinners.add(jPanelSize);
        jPanelSpinners.add(jPanelEnd);

    }
    
    private void constructAnalogComponents() {
        JPanel jPanelMin = new JPanel();
        JLabel jLabelMin = new JLabel("Minimum Size:");
        final JSpinner jSpinnerMin = new JSpinner(
                new SpinnerNumberModel(0, 0, 30000, 1));

        JPanel jPanelMax = new JPanel();
        JLabel jLabelMax = new JLabel("Maximum Size:");
        final JSpinner jSpinnerMax = new JSpinner(
                new SpinnerNumberModel(0, 0, 30000, 1));
        
        JButton jButtonDone = new JButton("Done");
        jButtonDone.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    jSpinnerMax.commitEdit();
                    jSpinnerMin.commitEdit();
                    
                }catch(Exception e) {
                    //TODO better error message
                    System.out.println("Error.");
                    return;
                }
                
                
            }
            
        });

        jPanelMin.add(jLabelMin);
        jPanelMin.add(jSpinnerMin);
        jPanelMax.add(jLabelMax);
        jPanelMax.add(jSpinnerMax);
        
        //JButton jButtonConfirm
        
        
        jPanelSpinners.add(jPanelMin);
        jPanelSpinners.add(jPanelMax);
    }



}
