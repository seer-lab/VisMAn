package XMLGenerator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;

/**
 * This class represents the display used to select the location for the input and output
 * files for the XML Generator.
 * @author David Petras
 *
 */
public class XMLGeneratorDisplay extends JFrame {
	
	//Components for the GUI
	private JPanel panel;
	private XMLGeneratorDisplay generatorDisplay;
	private XMLGenerator generator;
	
	private JButton jButtonBrowseInput;
	private JButton jButtonBrowseOutput;
	private JButton jButtonBrowseTest;
	private JButton jButtonGenerateXML;
	
	private JTextField jTextFieldInputLocation;
	private JTextField jTextFieldTestResults;
	private JTextField jTextFieldTestSourceLocation;
	
	//Constants
	private static final String PROGRAM_NAME = "XML Generator";
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 140;
	private static final boolean RESIZE_WINDOW = true;
	private static final int INPUT_SELECTION = 1;
	private static final int TEST_SELECTION = 2;
	private static final int TEST_SOURCE_SELECTION = 3;
	
	/**
	 * This method is the constructor for the XML Generator which will call the
	 * initializeGUI method to construct the JFrame.
	 */
	public XMLGeneratorDisplay()
	{
		super();
		this.generatorDisplay = this;
		initializeGUI();
	}
	
	
	/**
	 * This method will run the XML generator and launch the GUI.
	 * @param args Not used.
	 */
	public static void main(String args[])
	{
		XMLGeneratorDisplay generator = new XMLGeneratorDisplay();
		generator.setVisible(true);
		
	}
	
	/**
	 * This method will add all of the components to the JFrame.
	 */
	private void initializeGUI()
	{
		this.setTitle(PROGRAM_NAME);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setResizable(RESIZE_WINDOW);
		
		jButtonBrowseInput = new JButton("Browse Program Location");
		jButtonBrowseInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				browseFile(INPUT_SELECTION);
			}
		});
		
		jButtonBrowseTest = new JButton("Browse Test Source");
		jButtonBrowseTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				browseFile(TEST_SOURCE_SELECTION);
			}
		});
		
		
		
		
		jButtonBrowseOutput = new JButton("Browse Testing Results");
		jButtonBrowseOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				browseFile(TEST_SELECTION);
			}
		});
		
		jButtonGenerateXML = new JButton("Generate XML File");
		jButtonGenerateXML.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				generator = new XMLGenerator(jTextFieldInputLocation.getText(),jTextFieldTestResults.getText(),jTextFieldTestSourceLocation.getText());
				System.out.println(generator.generateXMLFile());
			}
		});
		
		jTextFieldInputLocation = new JTextField();
		
		jTextFieldTestSourceLocation = new JTextField();
		jTextFieldTestResults = new JTextField();
		
		
		JPanel inputCard = new JPanel();
		inputCard.setLayout(new BoxLayout(inputCard, BoxLayout.X_AXIS));
		inputCard.add(jButtonBrowseInput);
		inputCard.add(Box.createRigidArea(new Dimension(10,0)));
		inputCard.add(jTextFieldInputLocation);
		
		JPanel outputCard = new JPanel();
		outputCard.setLayout(new BoxLayout(outputCard, BoxLayout.X_AXIS));
		outputCard.add(jButtonBrowseOutput);
		outputCard.add(Box.createRigidArea(new Dimension(20,0)));
		outputCard.add(jTextFieldTestResults);
		
		JPanel sourceCard = new JPanel();
		sourceCard.setLayout(new BoxLayout(sourceCard, BoxLayout.X_AXIS));
		sourceCard.add(jButtonBrowseTest);
		sourceCard.add(Box.createRigidArea(new Dimension(20,0)));
		sourceCard.add(jTextFieldTestSourceLocation);
		
		
		JPanel generateCard = new JPanel();
		generateCard.add(jButtonGenerateXML);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(inputCard);
		panel.add(outputCard);
		panel.add(sourceCard);
		panel.add(generateCard);
		
		this.add(panel);
	
		
	}
	
	/**
	 * This method will open up the JFileChooser window and place the path of the selected
	 * directory 
	 * @param choice Use constant to indicate choice (INPUT_SELECTION or OUTPUT_SELECTION).
	 */
	private void browseFile(int choice)
	{
		JFileChooser chooser = new JFileChooser();
		if (choice == INPUT_SELECTION)
		{
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else if (choice == TEST_SELECTION || choice == TEST_SOURCE_SELECTION)
		{
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		int actionResult = chooser.showOpenDialog(generatorDisplay);
		if (actionResult == JFileChooser.APPROVE_OPTION)
		{
			if(choice == INPUT_SELECTION)
			{
				jTextFieldInputLocation.setText(chooser.getSelectedFile().getAbsolutePath());
			}
			else if(choice == TEST_SELECTION)
			{
				jTextFieldTestResults.setText(chooser.getSelectedFile().getAbsolutePath());
			}
			else if (choice == TEST_SOURCE_SELECTION)
			{
				jTextFieldTestSourceLocation.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
		
	}
	

}
