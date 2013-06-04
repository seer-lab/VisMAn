package temp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.WindowConstants;




/**
 * This class displays a custom dialog that allows the user to choose custom colors for their graph.
 * 
 * @author Jeff Falkenham
 *
 */
public class ColorDialog extends JDialog {

	/**
     * 
     */
    private static final long serialVersionUID = 1;
    //TODO change from protected to private, and use get methods to retrieve in other classes
	protected Vector<Double> range = new Vector<Double>();
	protected JList jList;
	protected JColorChooser jColorChooserMain;
	protected JTextField jTextFieldEnd;
	protected JLabel jLabelTo;
	protected JTextField jTextFieldStart;
	protected JLabel jLabelRange;
	protected JPanel jPanelRangeAndColor;
	protected JPanel jPanelHoldRangeColorPanel;
	//TODO Change rgbRed/Green/Blue to a Vector of Color.  Change in all other classes as well
	protected Vector<Integer> rgbRed = new Vector<Integer>();
	protected Vector<Integer> rgbGreen = new Vector<Integer>();
	protected Vector<Integer> rgbBlue = new Vector<Integer>();
	protected JButton jButtonCancel;
	protected JButton jButtonFinish;
	protected JButton jButtonNext;
	protected JButton jButtonPrev;
	protected JPanel jPanelButtons;
	protected ListModel jListModel;
	protected int currentRange;
	protected DisplayFrame parentFrame;
	protected JPanel listPanel;
	protected JScrollPane listScrollPane;

	/**
	 * @param displayFrame
	 * 							the parent frame
	 * @param displayFrameRange
	 * 							the range vector in the parent frame
	 * @param displayFrameRed
	 * 							parent frame's copy of rgbRed
	 * @param displayFrameGreen
	 * 							parent frame's version of rgbGreen
	 * @param displayFrameBlue
	 * 							parent frame's version of rgbBlue
	 */
	ColorDialog(DisplayFrame displayFrame, Vector<Double> displayFrameRange, Vector<Integer> displayFrameRed, Vector<Integer> displayFrameGreen, Vector<Integer> displayFrameBlue){
		super(displayFrame, true);

		//copy addresses of parent frame variables to ColorDialog
		parentFrame = displayFrame;
		range = displayFrameRange;
		rgbRed = displayFrameRed;
		rgbGreen = displayFrameGreen;
		rgbBlue = displayFrameBlue;


		//only close using appropriate buttons
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setLayout(new BorderLayout());

		//create a panel for the JButtons
		jPanelButtons = new JPanel();
		FlowLayout jPanel1Layout = new FlowLayout();
		getContentPane().add(jPanelButtons, BorderLayout.SOUTH);
		jPanelButtons.setLayout(jPanel1Layout);
		currentRange = 0;

		//making range.get(0) = 0 makes the rest of the code easier
		//the color for position 0 is never used
		if(range.size() == 0){
			range.add(0.0);
			rgbRed.add(0);
			rgbGreen.add(0);
			rgbBlue.add(0);
		}

		//add previous button
		jButtonPrev = new JButton();
		jPanelButtons.add(jButtonPrev);
		jButtonPrev.setText("Prev");
		jButtonPrev.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				prevButtonListener();
			}
		});

		//add next button
		jButtonNext = new JButton();
		jButtonNext.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				nextButtonListener();

			}

		});
		jPanelButtons.add(jButtonNext);
		jButtonNext.setText("Next");

		//add finish button
		jButtonFinish = new JButton();
		jPanelButtons.add(jButtonFinish);
		jButtonFinish.setText("Finish");
		jButtonFinish.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				finishButtonListener();						

			}

		});

		//add cancel button
		jButtonCancel = new JButton();
		jPanelButtons.add(jButtonCancel);
		jButtonCancel.setText("Cancel");
		jButtonCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				cancelButtonListener();
			}
		});



		//jPanelHoldRangeColorPanel = new JPanel();
		//BorderLayout jPanelCenter2Layout = new BorderLayout();
		//getContentPane().add(jPanelHoldRangeColorPanel, BorderLayout.CENTER);
		//jPanelHoldRangeColorPanel.setLayout(jPanelCenter2Layout);

		//add a panel for the range text boxes, label box, and for the colorchooser
		jPanelRangeAndColor = new JPanel();
		//jPanelHoldRangeColorPanel.add(jPanelRangeAndColor, BorderLayout.CENTER);
		getContentPane().add(jPanelRangeAndColor, BorderLayout.CENTER);
		FlowLayout jPanelCenterLayout = new FlowLayout();
		jPanelRangeAndColor.setMaximumSize(new Dimension(100,100));
		jPanelRangeAndColor.setLayout(jPanelCenterLayout);

		//add the range label box "Select Range"
		jLabelRange = new JLabel();
		jPanelRangeAndColor.add(jLabelRange);
		jLabelRange.setText("Select Range:");

		//add the range text box "start"
		jTextFieldStart = new JTextField();
		jPanelRangeAndColor.add(jTextFieldStart);
		jTextFieldStart.setText("0.0");
		jTextFieldStart.setEnabled(false);
		jTextFieldStart.setMaximumSize(new Dimension(10,20));
		jTextFieldStart.setPreferredSize(new java.awt.Dimension(47,18));

		//add the range label box "to"
		jLabelTo = new JLabel();
		jPanelRangeAndColor.add(jLabelTo);
		jLabelTo.setText("to");
		jLabelTo.setPreferredSize(new java.awt.Dimension(26,24));

		//add the range text box "end"
		jTextFieldEnd = new JTextField();
		jPanelRangeAndColor.add(jTextFieldEnd);
		jTextFieldEnd.setText("");
		jTextFieldEnd.setPreferredSize(new java.awt.Dimension(47,18));

		//add the colorchooser
		jColorChooserMain = new JColorChooser();
		jPanelRangeAndColor.add(jColorChooserMain);










		//create the JList inside its own panel
		jListModel = new DefaultComboBoxModel();
		jList = new JList();
		listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		listScrollPane = new JScrollPane(jList);
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listPanel.add(listScrollPane, BorderLayout.CENTER);
		getContentPane().add(listPanel, BorderLayout.NORTH);
		jList.setModel(jListModel);
		jList.setCellRenderer(new ListRenderer(this));

		//Finish button is enabled if a previous custom color range exists
		jButtonFinish.setEnabled(false);
		if(range.size() > 1){
			jButtonFinish.setEnabled(true);
		}

		//previous button disabled because the ColorDialog starts at range 0
		jButtonPrev.setEnabled(false);

		int tempHeight = 700;
		int tempWidth = 600;

		this.setSize(new Dimension(tempWidth, tempHeight));
		this.setPreferredSize(new Dimension(tempWidth, tempHeight));
		this.setMinimumSize(new Dimension(tempWidth/3, tempHeight/3));

		//update the JList
		updateList();

		//if a previous custom color range exists, set the text box and colorchooser to the appropriate values
		if(range.size() > 1){
			jTextFieldEnd.setText(String.valueOf(range.get(1)));
			jColorChooserMain.setColor(new Color(rgbRed.get(1), rgbGreen.get(1), rgbBlue.get(1)));
		}


		this.pack();
		jTextFieldEnd.requestFocus();

		this.setLocationRelativeTo(displayFrame);
		this.setVisible(true);

	}



	/**
	 * Refreshes the JList
	 * 
	 */
	public void updateList(){
		jListModel = new DefaultComboBoxModel(range);
		jList.setModel(jListModel);

	}



	/**
	 * Stores the range and color values when the next button is pressed,
	 * and prepares everything for the next value to be stored
	 * 
	 */
	public void nextButtonListener(){
		
		//the text inside jTextFieldEnd
		double endText;
		
		//change the focus to the text field
		if(jTextFieldEnd.isEnabled()){
			jTextFieldEnd.requestFocus();
		}
		
		//make sure there is a number inside the text field
		try{
			endText = Double.parseDouble(jTextFieldEnd.getText());
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Invalid text in text field");
			if(range.size() - 1 >= currentRange + 1){
				jTextFieldEnd.setText(String.valueOf(range.get(currentRange + 1)));
			}else{
				jTextFieldEnd.setText("");
			}
			return;
		}
		
		//make sure the number in the text field is in the appropriate range
		if((endText > 100.0)||(endText < range.get(currentRange).intValue())){
			JOptionPane.showMessageDialog(this, "Invalid number in text field");
			if(range.size() - 1 >= currentRange + 1){
				jTextFieldEnd.setText(String.valueOf(range.get(currentRange + 1)));
			}else{
				jTextFieldEnd.setText("");
			}
			return;
		}

		//if next has been pressed, then a previous value exists
		jButtonPrev.setEnabled(true);


		//currentRange is the value in the left text field
		//so currentRange + 1 is the value we want to edit or add
		
		
		
		//the last value in range is the one we are editing
		if((range.size() - 1) == (currentRange + 1)){
			range.set(currentRange + 1, endText);
			rgbRed.set(currentRange + 1, jColorChooserMain.getColor().getRed());
			rgbGreen.set(currentRange + 1, jColorChooserMain.getColor().getGreen());
			rgbBlue.set(currentRange + 1, jColorChooserMain.getColor().getBlue());

			jTextFieldEnd.setText("");

			jTextFieldStart.setText(String.valueOf(endText));
			currentRange++;
			jColorChooserMain.setColor(new Color(rgbRed.get(currentRange), rgbGreen.get(currentRange), rgbBlue.get(currentRange)));

			

			
		//We are editing a value in range that is not the last value
		}else if((range.size() - 1) > (currentRange)){
			if((endText > range.get(currentRange + 2))||(endText == 100.0)){
				JOptionPane.showMessageDialog(this, "Invalid number in text field");
				jTextFieldEnd.setText(String.valueOf(range.get(currentRange + 1)));
				return;
			}
			range.set(currentRange + 1, endText);
			rgbRed.set(currentRange + 1, jColorChooserMain.getColor().getRed());
			rgbGreen.set(currentRange + 1, jColorChooserMain.getColor().getGreen());
			rgbBlue.set(currentRange + 1, jColorChooserMain.getColor().getBlue());

			jTextFieldEnd.setText(String.valueOf(range.get(currentRange + 2)));

			jTextFieldStart.setText(String.valueOf(endText));
			currentRange++;
			jColorChooserMain.setColor(new Color(rgbRed.get(currentRange + 1), rgbGreen.get(currentRange + 1), rgbBlue.get(currentRange + 1)));

			
			
		//we are adding a value to the end of range
		}else{
			range.add(endText);
			rgbRed.add(jColorChooserMain.getColor().getRed());
			rgbGreen.add(jColorChooserMain.getColor().getGreen());
			rgbBlue.add(jColorChooserMain.getColor().getBlue());
			jTextFieldStart.setText(String.valueOf(endText));
			jTextFieldEnd.setText("");
			currentRange++;
			
		}

		//100 is the last value, so disable next, enable finish, disable jTextFieldEnd
		//etc
		if(endText == 100.0){
			jButtonFinish.setEnabled(true);
			jButtonNext.setEnabled(false);
			jTextFieldEnd.setEnabled(false);
			jTextFieldStart.setText(String.valueOf(endText));
			jTextFieldEnd.setText("");
		}

		
		//update the JList
		updateList();



	}

	/**
	 * Not used yet.  Used to return a new color when passed a color as a parameter.
	 * Can be used for easy cloning of a Color variable, since
	 * Colorvariable1 = Colorvariable2 would just copy an address 
	 * 
	 * @param c
	 * 			The color being cloned
	 * @return
	 * 			The new color
	 */
	public Color returnNewColor(Color c){
		return new Color(c.getRed(), c.getGreen(), c.getBlue());
	}

	/**
	 * Destroy the Dialog
	 */
	public void finishButtonListener(){
		this.dispose();
	}

	/**
	 * Switch to the previous range value, and update all text fields, Color Chooser, etc
	 */
	public void prevButtonListener(){
		jTextFieldEnd.setText(String.valueOf(range.get(currentRange)));
		jTextFieldEnd.setEnabled(true);
		jColorChooserMain.setColor(new Color(rgbRed.get(currentRange), rgbGreen.get(currentRange), rgbBlue.get(currentRange)));

		currentRange--;

		jTextFieldStart.setText(String.valueOf(range.get(currentRange)));

		if((currentRange) == 0){
			jButtonPrev.setEnabled(false);
		}
		jButtonNext.setEnabled(true);

	}

	//TODO make cancel button discard changes instead
	/**
	 * Cancels everything, and clears all data
	 */
	public void cancelButtonListener(){

		JOptionPane.showMessageDialog(this, "All data has been disposed");
		parentFrame.setRedColorButtonChecked();
		rgbRed.clear();
		rgbGreen.clear();
		rgbBlue.clear();
		range.clear();


		this.dispose();
	}

}



/**
 * The renderer for the JList.  Decides how each field will be displayed.
 * 
 * @see ListCellRenderer
 * 
 * @author Jeff Falkenham
 *
 */
class ListRenderer extends JLabel implements ListCellRenderer {

	/**
     * 
     */
    private static final long serialVersionUID = 1;
    private ColorDialog colorDialog;

	/**
	 * Constructor for the ListRenderer.  Must be passed the ColorDialog so
	 * certain values can be accessed
	 * 
	 * @param d
	 * 			the ColorDialog
	 */
	ListRenderer(ColorDialog d){
		colorDialog = d;
		this.setBackground(Color.WHITE);
	}

	@Override
	public Component getListCellRendererComponent(JList arg0, Object arg1, int index, boolean arg3, boolean arg4) {

		//Since the JList is passed the range vector, and range.get(0) does not refer
		//to a used color, put something useful in that field
		if(index == 0){
			this.setText("List of all Colors:");
			this.setIcon(null);
			return this;
		}

		//Set the Color and text
		this.setText(colorDialog.range.get(index - 1) + " to " + colorDialog.range.get(index));
		this.setIcon(new ColorDialogIcon(new Color(colorDialog.rgbRed.get(index), colorDialog.rgbGreen.get(index), colorDialog.rgbBlue.get(index))));
		return this;

	}


}
