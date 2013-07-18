package gui;

import graph.classNode;
import graph.packageNode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.net.URI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.collections15.Transformer;

import data.dataManager;
import data.dataMutant;
import data.dataXMLParser;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;


/**
 * This class is used to display the GUI and all of the internal elements like the 
 * graph, package hierarchy, and source code.  The action listeners for the various
 * elements are also contained within this class.
 * 
 * @author David Petras
 *
 */
public class guiDisplayFrame extends JFrame
	{
	
	//Constants
	private static final String FRAME_TITLE = "VisMAN - Mutation Visualization";
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 800;
	private static final int FRAME_MIN_WIDTH = 300;
	private static final int FRAME_MIN_HEIGHT = 300;
	
	
	//GUI Components
	
	//Menu Bar and associated components.
	private JMenuBar menuBarTop;
	
	private JMenu fileMenu;
	private JMenuItem fileProjectDirectory;
	private JMenuItem fileExit;
	
	private JMenu optionsMenu;
	
	private JMenu helpMenu;
	private JMenuItem helpManual;
	private JMenuItem helpAbout;
	
	//About VisMAN Dialog
	private guiAboutDialog aboutDialog;
	
	private JTree sourceTree;
	
	private JSplitPane verticalSplit;
	private JSplitPane horizontalSplit;
	private JScrollPane treePane;
	private JScrollPane sourcePane;
	private JPanel graphPane;
	private JPanel rightPane;
	
	private JPanel controlPane;
	private JButton zoomIn;
	private JButton zoomOut;
	private ButtonGroup selectionOptions;
	private JRadioButton pickButton;
	private JRadioButton translateButton;
	
	private JTextArea sourceArea;
	
	
	
	//Other entities.
	private dataManager manager;
	private dataXMLParser parser;
	
	/**
	 * This is the main method for displaying the GUI.  It will create an instance
	 * of the guiDisplayFrame (which will initialize all the components) and make
	 * it visible.
	 * @param args
	 */
	public static void main(String[] args)
	{
		guiDisplayFrame displayFrame = new guiDisplayFrame();
		displayFrame.setVisible(true);
	}
	
	/**
	 * This is the constructor for the guiDisplayFrame.  It is a private method
	 * so that instantiation can only take place through the main method contained
	 * within this class.  Upon construction of a new instance, the initializeGUI
	 * method is run to add all of the components to the guiDisplayFrame. 
	 */
	private guiDisplayFrame()
	{
		super();
		initializeGUI();
	}
	
	/**
	 * This method will set all of the parameters of the guiDisplayFrame, add the
	 * components, and set the action listeners for each component.
	 */
	private void initializeGUI()
	{
		//Set the basic parameters for the guiDisplayFrame.
		this.setTitle(FRAME_TITLE);
		this.setSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		this.setMinimumSize(new Dimension(FRAME_MIN_WIDTH,FRAME_MIN_HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create and add the menu bar to the guiDisplayFrame.
		initializeMenuBar();
		
		//Form the layout for each of the components.
		initializeContentArea();
	
	}
	
	/**
	 * This method will create the components required for the action bar, add them to the
	 * guiDisplayFrame, and create the associated action listeners. 
	 */
	private void initializeMenuBar()
	{
		//Create the menu bar and add it to the guiDisplayFrame.
		menuBarTop = new JMenuBar();
		this.setJMenuBar(menuBarTop);
		
		/*
		 * The File menu presents the following options:
		 * 	Load XML - Open the visualization from an XML file.
		 * 	Exit - Exit the program.
		 */
		
		//Create the File menu and add it to the menu bar.
		fileMenu = new JMenu("File");
		menuBarTop.add(fileMenu);
		
		//Add the 'Set Project Directory' option to the File menu.
		fileProjectDirectory = new JMenuItem("Set Project Directory...");
		fileMenu.add(fileProjectDirectory);
		//Add the action listener for clicks on the Set Project Directory button.
		fileProjectDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select project directory...");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(guiDisplayFrame.this);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					createSourceTree(fileChooser.getSelectedFile());
					parseXML(fileChooser.getSelectedFile());
				}
			}
		});
		
		//Add the 'Exit' option to the File menu.
		fileExit = new JMenuItem("Exit");
		fileMenu.add(fileExit);
		fileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				//Issue the exit command.
				System.exit(0);
			}
		});
		
		/*
		 * The Options menu presents the following options:
		 *  
		 */
		
		//Create the Options menu and add it to the menu bar.
		optionsMenu = new JMenu("Options");
		menuBarTop.add(optionsMenu);
		
		//TODO complete the options menu
		
		/*
		 * The Help menu presents the following options:
		 * 	VisMAN Help - load an operations manual for the program
		 *  About - list information about the program and its developers
		 */
		
		//Create the Help menu and add it to the menu bar.
		helpMenu = new JMenu("Help");
		menuBarTop.add(helpMenu);
		
		//Add the 'VisMAN Help' option to the Help menu.
		helpManual = new JMenuItem("VisMAN Help");
		helpMenu.add(helpManual);
		helpManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				//TODO open the manual
			}
		});
		
		//Add the 'About VisMAN' option to the Help menu.
		helpAbout = new JMenuItem("About VisMAN");
		helpMenu.add(helpAbout);
		helpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				aboutDialog = new guiAboutDialog(guiDisplayFrame.this);
				aboutDialog.setVisible(true);
			}
		});	
			
	}
	
	/**
	 * This method will split the JFrame into the various sections required for each component.
	 */
	private void initializeContentArea()
	{
		treePane = new JScrollPane();
		sourcePane = new JScrollPane();
		graphPane = new JPanel();
		controlPane = new JPanel();
		rightPane = new JPanel();
		
		selectionOptions = new ButtonGroup();
		pickButton = new JRadioButton("Pick");
		translateButton = new JRadioButton("Translate");
		selectionOptions.add(pickButton);
		selectionOptions.add(translateButton);
		
		zoomIn = new JButton("Zoom In");
		zoomOut = new JButton("Zoom Out");
		controlPane.add(pickButton);
		controlPane.add(translateButton);
		controlPane.add(zoomIn);
		controlPane.add(zoomOut);
		controlPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Graph Controls"));
		controlPane.setPreferredSize(new Dimension(100,60));
		rightPane.setLayout(new BorderLayout());
		rightPane.add(graphPane,BorderLayout.CENTER);
		rightPane.add(controlPane,BorderLayout.SOUTH);
		sourceArea = new JTextArea();
		sourceArea.setEditable(false);
		sourcePane.setViewportView(sourceArea);
		verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,treePane,sourcePane);
		verticalSplit.setDividerLocation(400);
		horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,verticalSplit,rightPane);
		horizontalSplit.setDividerLocation(400);
		this.getContentPane().add(horizontalSplit);
	}
	
	/**
	 * 
	 */
	private void createSourceTree(File root)
	{
		manager = new dataManager(root);
		sourceTree = manager.getFileTree();
		//Add the action listener to detect when the user clicks on an element in the hierarchy.
		sourceTree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent event) {
				if(sourceTree.getLastSelectedPathComponent() instanceof classNode)
				{
					classNode selectedNode = (classNode) sourceTree.getLastSelectedPathComponent();
					sourceArea.setText(selectedNode.getSource());
					drawClassGraph(selectedNode);
				}
				else if (sourceTree.getLastSelectedPathComponent() instanceof packageNode)
				{
					packageNode selectedNode = (packageNode) sourceTree.getLastSelectedPathComponent();
					sourceArea.setText("");
					drawAggregateGraph(selectedNode);
					
				}
			}
		});
		treePane.setViewportView(sourceTree);
		
		
	}
	
	/**
	 * 
	 * @param root
	 */
	private void parseXML(File root)
	{
		parser = new dataXMLParser(manager);
		File xmlFile = new File(root.getAbsolutePath()+"\\xml_output.txt");
		parser.parseDocument(xmlFile);
		
		manager.linkMutantsToNodes();
	}
	
	/**
	 * 
	 */
	private void drawSampleGraph()
	{
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		g.addVertex((Integer)1);
		g.addVertex((Integer)2);
		g.addVertex((Integer)3);
		
		Layout<Integer, String> layout = new CircleLayout(g);
		layout.setSize(new Dimension(300,300));
		BasicVisualizationServer<Integer,String> viewer = new BasicVisualizationServer<Integer, String>(layout);
		viewer.setPreferredSize(new Dimension(400,400));
		
		Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>()
		{
			public Paint transform(Integer i) 
			{
				if (i == 1)
				{
					return Color.blue;
				}
				else if (i == 2)
				{
					return Color.red;
				}
				else
				{
					return Color.green;
				}
				
			}
		};
		
		viewer.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		
		graphPane.add(viewer);
		
	}
	
	/**
	 * 
	 */
	private void drawClassGraph(classNode node)
	{
		Graph<dataMutant, String> classGraph = new SparseMultigraph<dataMutant, String>();
		for (dataMutant mutant:node.getMutantList())
		{
			classGraph.addVertex(mutant);
		}
		
		Transformer<dataMutant,Shape> vertexSize = new Transformer<dataMutant,Shape>()
		{
			Ellipse2D circle = new Ellipse2D.Double(10,10,20,20);
			public Shape transform(dataMutant mutant)
			{
				return AffineTransform.getScaleInstance(mutant.getPercentKilled(), mutant.getPercentKilled()).createTransformedShape(circle);
			}
		};
		
		Layout<dataMutant, String> layout = new CircleLayout(classGraph);
		layout.setSize(new Dimension(300,300));
		BasicVisualizationServer<dataMutant,String> viewer = new BasicVisualizationServer<dataMutant, String>(layout);
		viewer.setPreferredSize(new Dimension(400,400));
		viewer.getRenderContext().setVertexShapeTransformer(vertexSize);
		graphPane.removeAll();
		graphPane.add(viewer);
		graphPane.revalidate();
		graphPane.repaint();
	}
	
	/**
	 * 
	 * @param node
	 */
	private void drawAggregateGraph(packageNode node)
	{
		graphPane.removeAll();
		graphPane.revalidate();
		graphPane.repaint();
	}
}
