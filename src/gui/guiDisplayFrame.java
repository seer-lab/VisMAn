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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.collections15.Transformer;

import data.dataManager;
import data.dataMutant;
import data.dataTest;
import data.dataXMLParser;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


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
	
	private static final int MAX_NODE_SIZE = 80;
	
	
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
	private JButton enterPicked;
	private ButtonGroup selectionOptions;
	private JRadioButton pickButton;
	private JRadioButton translateButton;
	
	private JTextArea sourceArea;
	
	private VisualizationViewer<DefaultMutableTreeNode,String> viewer;
	
	private DefaultModalGraphMouse graphMouse;
	
	
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
		pickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
			}
		});
		translateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
			}
		});
		pickButton.setSelected(true);
		
		zoomIn = new JButton("Zoom In");
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				ScalingControl scaler = new CrossoverScalingControl();
				scaler.scale(viewer, 1.1f, viewer.getCenter());
			}
		});	
		zoomOut = new JButton("Zoom Out");
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				ScalingControl scaler = new CrossoverScalingControl();
				scaler.scale(viewer, (1 / 1.1f), viewer.getCenter());
			}
		});	
		enterPicked = new JButton("Enter Picked");
		enterPicked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				Collection<DefaultMutableTreeNode> pickedNodes = viewer.getPickedVertexState().getPicked();
				if (pickedNodes.size() == 1)
				{
					if(pickedNodes.toArray()[0] instanceof classNode)
					{
						classNode selectedNode = (classNode) pickedNodes.toArray()[0];
						TreePath path = new TreePath(selectedNode.getPath());
						sourceTree.setExpandsSelectedPaths(true);
						sourceTree.setSelectionPath(path);
						sourceArea.setText(selectedNode.getSource());
						highlightSource(selectedNode);
						drawClassGraph(selectedNode);
					}
					else if (pickedNodes.toArray()[0] instanceof packageNode)
					{
						packageNode selectedNode = (packageNode) pickedNodes.toArray()[0];
						TreePath path = new TreePath(selectedNode.getPath());
						sourceTree.setExpandsSelectedPaths(true);
						sourceTree.setSelectionPath(path);
						sourceArea.setText("");
						drawAggregateGraph(selectedNode);
					}
				}
				//OTHERWISE DO NOTHING
			}
		});
		
		controlPane.add(enterPicked);
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
		verticalSplit.setDividerLocation(250);
		horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,verticalSplit,rightPane);
		horizontalSplit.setDividerLocation(400);
		this.getContentPane().add(horizontalSplit);
		
		graphMouse = new DefaultModalGraphMouse();
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
	}
	
	/**
	 * This method will get the source tree that was created by the dataManager, create the action
	 * listeners, and add it to the treePane.
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
					highlightSource(selectedNode);
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
	 * This method is used to instantiate an XML parser to parse the XML file created
	 * by the test harness and add its information to the dataManager.
	 * @param root the root directory of the test harness output
	 */
	private void parseXML(File root)
	{
		//Create a new parser with the data manager that will then hold the extracted information.
		parser = new dataXMLParser(manager);
		File xmlFile = new File(root.getAbsolutePath()+"/xml_output.txt");
		parser.parseDocument(xmlFile);
		
		/* Have the data manager perform the linking and aggregation methods to relate the parsed data
		 * to the nodes of the souceTree.
		 */
		manager.linkMutantsToNodes();
		manager.produceAggregateData();
	}
	

	/**
	 * This method will create the graph of a class level file from the package hierarchy.
	 * @param node the class file to visualize
	 */
	private void drawClassGraph(classNode node)
	{
		//Create a new graph.
		Graph<DefaultMutableTreeNode, String> classGraph = new SparseMultigraph<DefaultMutableTreeNode, String>();
		//Add all of the mutants of the class to the graph as vertices (nodes).
		for (dataMutant mutant:node.getMutantList())
		{
			classGraph.addVertex(mutant);
		}
		
		/* Add the edges to the graph.  This nested for loop structure works by choosing each mutant as 
		 * the reference mutant, then checks its test results agaist each subsequent mutant.  This 
		 * prevents duplicate edges from being created since each pair of nodes is only checked in a
		 * single direction from the reference.  The inner-most for loop compares the results of each test
		 * case to see if both mutants were kill by the same test cases.
		 */
		ArrayList<dataMutant> mutantList = node.getMutantList();
		for (int i = 0; i < mutantList.size(); i++)
		{
			dataMutant referenceMutant = mutantList.get(i);
			for (int j = i+1; j < mutantList.size();j++)
			{
				int similarities = 0;
				dataMutant checkMutant = mutantList.get(j);
				ArrayList<dataTest> referenceTests = referenceMutant.getTestArray();
				ArrayList<dataTest> checkTests = referenceMutant.getTestArray();
				
				for (int q = 0; q < referenceTests.size();q++)
				{
					if (referenceTests.get(q).getResult().equals("yes") && checkTests.get(q).getResult().equals("yes"))
					{
						similarities++;
					}
				}
				
				if (similarities > 0)
				{
					classGraph.addEdge(referenceMutant.getName()+" to "+checkMutant.getName(),referenceMutant,checkMutant);
				}
			}
		}
		
		/* This transformer will change the size of the mutant node based upon the percentage of test cases that are able
		 * to kill it.  It is an inverse relationship meaning that the fewer test cases that kill a mutant, the larger the
		 * node is rendered.
		 */
		Transformer<DefaultMutableTreeNode,Shape> vertexSize = new Transformer<DefaultMutableTreeNode,Shape>()
		{
			public Shape transform(DefaultMutableTreeNode _mutant)
			{
				dataMutant mutant = (dataMutant) _mutant;
				double nodeSize = (1.2-mutant.getPercentKilled())*MAX_NODE_SIZE;
				return new Ellipse2D.Double(-nodeSize/2, -nodeSize/2, nodeSize, nodeSize);
			}
		};
		
		/* This transformer will change the colour of the mutant node based upon the percentage of test cases that are
		 * able to kill it.  Green means that more than 66% of the test cases were able to kill it, yellow means 33%-66%
		 * and red means less than 33%.
		 */
		Transformer<DefaultMutableTreeNode,Paint> threeColour = new Transformer<DefaultMutableTreeNode,Paint>()
		{
			public Paint transform(DefaultMutableTreeNode mutant)
			{
				dataMutant workingMutant = (dataMutant) mutant;
				if (workingMutant.getPercentKilled() < 0.33)
				{
					workingMutant.setColor(Color.red);
					return workingMutant.getColor();
				}
				else if (workingMutant.getPercentKilled() < 0.66)
				{
					workingMutant.setColor(Color.yellow);
					return workingMutant.getColor();
				}
				else
				{
					workingMutant.setColor(Color.green);
					return workingMutant.getColor();
				}
			}
		};
		
		Layout<DefaultMutableTreeNode, String> layout = new CircleLayout(classGraph);
		layout.setSize(new Dimension(graphPane.getWidth(),graphPane.getHeight()));
		viewer = new VisualizationViewer<DefaultMutableTreeNode,String>(layout);
		viewer.setPreferredSize(new Dimension(graphPane.getWidth(),graphPane.getHeight()));
		viewer.getRenderContext().setVertexShapeTransformer(vertexSize);
		viewer.getRenderContext().setVertexFillPaintTransformer(threeColour);
		viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
        viewer.setGraphMouse(graphMouse);
		
		graphPane.removeAll();
		graphPane.add(viewer);
		graphPane.revalidate();
		graphPane.repaint();
	}
	
	/**
	 * This method will create and add an aggregatedGraph (a graph for a package that may contain classes and additional
	 * packages) to the graphPane.
	 * @param node the node to visualize
	 */
	private void drawAggregateGraph(packageNode node)
	{
		Graph<DefaultMutableTreeNode,String> packageGraph = new SparseMultigraph<DefaultMutableTreeNode,String>();
		for (int i = 0; i < node.getChildCount(); i++)
		{
			packageGraph.addVertex((DefaultMutableTreeNode) node.getChildAt(i));
		}
		
		/* This transformer will change the size of the aggregate node based upon the percentage of test cases that are able
		 * to kill the mutants within.  It is an inverse relationship meaning that the fewer test cases that kill a mutant, the larger the
		 * node is rendered.
		 */
		Transformer<DefaultMutableTreeNode,Shape> vertexSize = new Transformer<DefaultMutableTreeNode,Shape>()
		{
			public Shape transform(DefaultMutableTreeNode node)
			{
				if (node instanceof classNode)
				{
					classNode workingNode = (classNode) node;
					double nodeSize = (1.2-workingNode.getAggregateData())*MAX_NODE_SIZE;
					return new Ellipse2D.Double(-nodeSize/2, -nodeSize/2, nodeSize, nodeSize);
				}
				else if(node instanceof packageNode)
				{
					packageNode workingNode = (packageNode) node;
					double nodeSize = (1.2-workingNode.getAveragePercentKilled())*MAX_NODE_SIZE;
					return new Rectangle2D.Double(-nodeSize/2,-nodeSize/2,nodeSize, nodeSize);
				}
				return null;
			}
		};
		
		/* This transformer will change the colour of the aggregaet node based upon the percentage of test cases that are
		 * able to kill the mutants within it.  Green means that more than 66% of the test cases were able to kill it, yellow means 33%-66%
		 * and red means less than 33%.
		 */
		Transformer<DefaultMutableTreeNode,Paint> threeColour = new Transformer<DefaultMutableTreeNode,Paint>()
		{
			public Paint transform(DefaultMutableTreeNode node)
			{
				if (node instanceof classNode)
				{
					classNode workingNode = (classNode) node;
					if (workingNode.getAggregateData() < 0.33)
					{
						return Color.red;
					}
					else if (workingNode.getAggregateData() < 0.66)
					{
						return Color.yellow;
					}
					else
					{
						return Color.green;
					}
				}
				else if (node instanceof packageNode)
				{
					packageNode workingNode = (packageNode) node;
					
					if (workingNode.getAveragePercentKilled() < 0.33)
					{
						return Color.red;
					}
					else if (workingNode.getAveragePercentKilled() < 0.66)
					{
						return Color.yellow;
					}
					else
					{
						return Color.green;
					}
								
				}

				return null;
			}
		};
		
		Layout<DefaultMutableTreeNode, String> layout = new CircleLayout(packageGraph);
		layout.setSize(new Dimension(300,300));
		viewer = new VisualizationViewer<DefaultMutableTreeNode,String>(layout);
		viewer.setPreferredSize(new Dimension(graphPane.getWidth(),graphPane.getHeight()));
		viewer.getRenderContext().setVertexShapeTransformer(vertexSize);
		viewer.getRenderContext().setVertexFillPaintTransformer(threeColour);
		viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
        viewer.setGraphMouse(graphMouse);
		
		graphPane.removeAll();
		graphPane.add(viewer);
		graphPane.revalidate();
		graphPane.repaint();
	}
	
	/**
	 * This method will highlight the lines of the source code associated with
	 * each mutant.  If multiple mutants modify the same line, the color will be
	 * set with the following order: 1) Red 2) Yellow 3) Green
	 * @param selectedNode the classNode currently being displayed
	 */
	public void highlightSource(classNode selectedNode)
	{
		for (dataMutant mutant: selectedNode.getMutantList())
		{
			try 
			{
				int startChar = sourceArea.getLineStartOffset(mutant.getLine());
				int endChar = sourceArea.getLineEndOffset(mutant.getLine());
				Highlighter.HighlightPainter highlighter = new DefaultHighlighter.DefaultHighlightPainter(mutant.getColor());
				sourceArea.getHighlighter().addHighlight(startChar, endChar, highlighter);
				sourceArea.repaint();
			} catch (BadLocationException e) 
			{
				e.printStackTrace();
			}
			
		}
	}
}
