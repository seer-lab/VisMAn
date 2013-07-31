package temp;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


/*TODO  KNOWN BUGS:
                    
 */
//TODO edit comments that refer to pData (now called mutantData), tData (now called testData), and mData (now called currentData)
//TODO fix zoom when switching tabs
//TODO mutant source and original source displayed in the dialog


/**
 * This is the main class that displays the GUI and has most of the code for
 * displaying the graphs. Also contains action listeners and stuff
 * 
 * @author Jeff Falkenham, Summer 2008/Summer 2009
 * 
 */
public class DisplayFrame extends JFrame {




    private static final long serialVersionUID = 1;

    //Constants
    private static final Color CANVAS_COLOR = new Color(1.0f, 1.0f, 1.0f);
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String PROGRAM_TITLE = "Mutation Analysis Vizualization Tool";
    public static final Rectangle TAB_SIZE = new Rectangle(20, 20);
    public static final String SLASH_N = "\n";
    public static final String MUTANT_TAB_TITLE = "Mutant Graph";
    public static final String TEST_TAB_TITLE = "Test Graph";
    public static final int FLAG_TRANSFORMER_SHAPE_DEFAULT = 9182;
    public static final int FLAG_TRANSFORMER_SHAPE_CUSTOM =6374;

    //flags
    private static final int FLAG_ADVANCED_CIRCLE = 345;
    private static final int FLAG_ADVANCED_FR = 346;
    private static final int FLAG_ADVANCED_SPRING = 346;

    //Vectors to store mutation test data
    MutationVector<MutationData> currentData = new MutationVector<MutationData>(true);
    MutationVector<MutationData> mutantData = new MutationVector<MutationData>(true);
    MutationVector<MutationData> testData = new MutationVector<MutationData>(false);

    //VisualizationViewer is basically the canvas for the graph.
    //They are being set to blank values here so they can be added when the GUI is initialized
    private Vector<VisualizationViewer<MyVertex, MyEdge>> connectionCanvases = new Vector<VisualizationViewer<MyVertex, MyEdge>>();
    private Vector<VisualizationViewer<MyVertex, MyEdge>> connectionMutantCanvases = new Vector<VisualizationViewer<MyVertex, MyEdge>>();
    private Vector<VisualizationViewer<MyVertex, MyEdge>> connectionTestCanvases = new Vector<VisualizationViewer<MyVertex, MyEdge>>();


    private VisualizationViewer<MyVertex, MyEdge> currentMainCanvas = new VisualizationViewer<MyVertex, MyEdge>(
            new CircleLayout<MyVertex, MyEdge>(new GraphGenerator(
                    new MutationVector<MutationData>(true)).getGraph()));

    private VisualizationViewer<MyVertex, MyEdge> mutantMainCanvas = new VisualizationViewer<MyVertex, MyEdge>(
            new CircleLayout<MyVertex, MyEdge>(new GraphGenerator(
                    new MutationVector<MutationData>(true)).getGraph()));
    private VisualizationViewer<MyVertex, MyEdge> testMainCanvas = new VisualizationViewer<MyVertex, MyEdge>(
            new CircleLayout<MyVertex, MyEdge>(new GraphGenerator(
                    new MutationVector<MutationData>(true)).getGraph()));



    private PluggableGraphMouse mouse = new PluggableGraphMouse();
    private TranslatingGraphMousePlugin translate = new TranslatingGraphMousePlugin(
            InputEvent.BUTTON1_MASK);
    private PickingGraphMousePlugin<MyVertex, MyEdge> pick = new PickingGraphMousePlugin<MyVertex, MyEdge>();
    private CustomGraphMousePlugin<MyVertex, MyEdge> pop = new CustomGraphMousePlugin<MyVertex, MyEdge>(
            new MutationVector<MutationData>(true), new JTextPane(),
            this);

    //The variables for the Custom Color radio button.
    //the rgb variables store the color values.
    //range stores the range that applies to each color.
    private Vector<Double> range = new Vector<Double>();
    private Vector<Integer> rgbRed = new Vector<Integer>();
    private Vector<Integer> rgbGreen = new Vector<Integer>();
    private Vector<Integer> rgbBlue = new Vector<Integer>();
    private Layout<MyVertex, MyEdge> layout; // layout for the canvas graph

    //GUI Components
    private ButtonGroup buttonGroupMutantTest;
    private ButtonGroup colorGroup;
    private ButtonGroup labelGroup;
    private ButtonGroup mouseGroup;
    private JButton jButtonZoomIn;
    private JButton jButtonZoomOut;
    private JMenu jMenuColor;
    private JMenu jMenuFile;
    private JMenu jMenuHelp;
    private JMenu jMenuLayout;
    private JMenu jMenuMouse;
    private JMenu jMenuOptions;
    private JMenu labelMenu;
    private JMenuBar jMenuBarTop;
    private JMenuItem jMenuItemAbout;
    private JMenuItem jMenuItemLoad;
    private JMenuItem jMenuItemOpen;
    private JMenuItem jMenuItemQuit;
    private JMenuItem jMenuItemSave;
    private JPanel cardPanel = new JPanel(new CardLayout());
    private JPanel jPanelCanvas;
    private JRadioButton blueButton;
    private JRadioButton customColorButton;
    private JRadioButton greenButton;
    private JRadioButton jRadioButtonCircleLayout;
    private JRadioButton jRadioButtonSpringLayout;
    private JRadioButton jRadioButtonFRLayout;
    private JRadioButton jRadioButtonMutant;
    private JRadioButton jRadioButtonTest;
    private JRadioButton multiButton;
    private JRadioButton noLabel;
    private JRadioButton percentLabel;
    private JRadioButton redButton;
    private JRadioButton stringLabel;
    private JRadioButtonMenuItem jRadioButtonMenuItemPick;
    private JRadioButtonMenuItem jRadioButtonMenuItemTranslate;
    private JScrollPane jScrollPaneCanvas;
    private JScrollPane jScrollPaneText;
    private JSplitPane jSplitPaneCenter;
    private JTabbedPane jTabbedPaneCurrentCanvas;
    private JTabbedPane jTabbedPaneMutantCanvas;
    private JTabbedPane jTabbedPaneTestCanvas;
    private JTextPane jTextPaneMain;

    //new stuff
    protected Vector<Object> advancedLayoutList = new Vector<Object>();
    private Boolean onMutantCard;


    /**
     * [size, interval end] 2 element arrays
     * 
     * 
     */
    protected final Vector<Integer[]> customShapeSize = 
        new VectorFlag<Integer[]>(VectorFlag.FLAG_CLOSE_DEFAULT);





        public Graph<MyVertex, MyEdge> getCurrentCanvasGraph() {
            return currentMainCanvas.getGraphLayout().getGraph();
        }

        /**
         * Adds all the individual components to the frame, and associates the action
         * listeners with the components.
         * 
         */
        private void initGUI() {
            try {
                //Initializes the basic elements of the GUI.
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                this.setTitle(PROGRAM_TITLE);
                currentMainCanvas.setBackground(CANVAS_COLOR);
                //TODO connectionCanvases.setBackground(CANVAS_COLOR);
                this.setMinimumSize(new Dimension(300, 300));

                //Create the menu bar.
                createMenu();

                //Create the panel that is used when displaying the graph and associated control components.
                createGraphPanel();

                // Create split pane to contain source code on the left and graphs on the right.
                jTabbedPaneMutantCanvas = new JTabbedPane();

                
                //Set onMutantCard to true to prevent the program from switching to a card it is already on.
                onMutantCard = true;

                jTabbedPaneMutantCanvas.add(MUTANT_TAB_TITLE, currentMainCanvas);
                //TODO refactor identifier, or remove completely if not needed
                cardPanel.add(jTabbedPaneMutantCanvas, "identifier1");


                jTabbedPaneTestCanvas = new JTabbedPane();


                jTabbedPaneTestCanvas.add(TEST_TAB_TITLE, currentMainCanvas);

                //TODO refactor identifier, or remove completely if not needed
                cardPanel.add(jTabbedPaneTestCanvas, "identifier2");

                jTabbedPaneCurrentCanvas = jTabbedPaneMutantCanvas;


                jScrollPaneText = new JScrollPane();

                jSplitPaneCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        jScrollPaneText, cardPanel) {

                    private static final long serialVersionUID = 1;

                    public void setDividerLocation(double proportionalLocation) {
                        super.setDividerLocation(proportionalLocation);
                        resizeCustomTextPane();
                    }

                    public void setDividerLocation(int location) {
                        super.setDividerLocation(location);
                        resizeCustomTextPane();
                    }
                };

                //a workaround that allows the TextPane to have no wordwrap
                jTextPaneMain = new JTextPane() {

                    private static final long serialVersionUID = 1;

                    public boolean getScrollableTracksViewportWidth() {
                        return false;
                    }

                    public void setSize(Dimension d) {
                        try {
                            if (d.width < this.getParent().getSize().getWidth()) {
                                d.width = this.getParent().getWidth();
                                super.setSize(d);
                                return;
                            }
                        } catch (Exception e) {

                        }
                        super.setSize(d);

                    }

                    public void setSize(int width, int height) {
                        try {
                            if (width < this.getParent().getSize().getWidth()) {
                                super.setSize((int) this.getParent().getSize()
                                        .getWidth(), height);
                                return;
                            }
                        } catch (Exception e) {

                        }
                        super.setSize(width, height);

                    }
                };

                jTextPaneMain.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // TODO Auto-generated method stub
                        textMouseListener(e);

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }



                });

                jTextPaneMain.setEditable(false);
                jTextPaneMain.setSize(jScrollPaneText.getSize());
                jScrollPaneText.setViewportView(jTextPaneMain);
                jTextPaneMain.setText("");
                jScrollPaneText.setMinimumSize(new Dimension(150, 150));
                currentMainCanvas.setMinimumSize(new Dimension(150, 150));
                getContentPane().add(jSplitPaneCenter, BorderLayout.CENTER);
                jSplitPaneCenter.setDividerSize(2);

                jScrollPaneCanvas = new JScrollPane();
                jScrollPaneCanvas.setViewportView(jPanelCanvas);


                pack();
                //TODO 1024x768
                this.setSize(800, 600);
                jSplitPaneCenter.setDividerLocation(this.getWidth() / 2);
                jSplitPaneCenter.setResizeWeight(0.5);
                // added in the updateCanvas() method
                //jTabbedPaneCurrentCanvas.setSelectedIndex(0);
                updateCanvas();

                //jTabbedPaneCurrentCanvas.setSelectedIndex(0);





            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * This method creates the panel that is used to display the Mutant or Test graphs and the
         * various control components.  Creates the following components: jPanelCanvas, jRadioButtonMutant,
         * jRadioButtonTest, jButtonZoomIn, jButtonZoomOut.
         */
        private void createGraphPanel() {
        	//Create the jPanelCanvas that will hold the graph and the control components.
            jPanelCanvas = new JPanel();
            FlowLayout jPanelCanvasLayout = new FlowLayout();
            jPanelCanvasLayout.setAlignment(FlowLayout.RIGHT);
            jPanelCanvas.setLayout(jPanelCanvasLayout);
            
            //Create and add the radio button for selecting the mutant graph.
            jRadioButtonMutant = new JRadioButton("mutant");
            jRadioButtonMutant.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	//Call updateCanvas with 1 as the flag to show that the mutant radio button was clicked.
                    updateCanvas(1);
                }
            });
            jPanelCanvas.add(jRadioButtonMutant);
            
            //Create and add the radio button for selecting the test graph.
            jRadioButtonTest = new JRadioButton("test");
            jRadioButtonTest.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	//Call updateCanvas with 2 as the flag to show that the test radio button was clicked.
                    updateCanvas(2);
                }
            });
            jPanelCanvas.add(jRadioButtonTest);
            
            //Add the mutant and test radio buttons to a button group.
            buttonGroupMutantTest = new ButtonGroup();
            buttonGroupMutantTest.add(jRadioButtonMutant);
            buttonGroupMutantTest.add(jRadioButtonTest);
            
            //Begin with the mutant radio button selected.
            jRadioButtonMutant.setSelected(true);
            
            //Create and add the Zoom Out button for changing the zoom on a graph.
            jButtonZoomOut = new JButton();
            jPanelCanvas.add(jButtonZoomOut);
            jButtonZoomOut.setText("Zoom Out");
            jButtonZoomOut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    zoomOut(currentMainCanvas);
                }
            });
            
            //Create and add the Zoom In button for changing the zoom on a graph. 
            jButtonZoomIn = new JButton();
            jPanelCanvas.add(jButtonZoomIn);
            jButtonZoomIn.setText("Zoom In");
            jButtonZoomIn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    zoomIn(currentMainCanvas);
                }
            });
        }



        /**
         * This method will create and associate action listeners for all of the components in the program's 
         * menu bar.
         */
        private void createMenu() {
        	//Create the menu bar.
            jMenuBarTop = new JMenuBar();
            this.setJMenuBar(jMenuBarTop);
            
            /* The File menu presents four options:
             *	Open - Open a visualization from an XML file.
             *	Save -
             *	Load -
             *	Quit - Quit the program. */
            
            //Create the File menu and add it to the menu bar.
            jMenuFile = new JMenu();
            jMenuBarTop.add(jMenuFile);
            jMenuFile.setText("File");
            
            //Add the open option to the File menu.
            jMenuItemOpen = new JMenuItem("Open");
            jMenuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                    ActionEvent.CTRL_MASK));
            jMenuFile.add(jMenuItemOpen);
            jMenuItemOpen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    openButtonListener(evt);
                }
            });
            
            //Add the save option to the File menu.
            jMenuItemSave = new JMenuItem("Save");
            jMenuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    ActionEvent.CTRL_MASK));
            jMenuFile.add(jMenuItemSave);
            jMenuItemSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    saveButtonListener();
                }
            });
            
            //Add the load option to the File menu.
            jMenuItemLoad = new JMenuItem("Load");
            jMenuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                    ActionEvent.CTRL_MASK));
            jMenuFile.add(jMenuItemLoad);
            jMenuItemLoad.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    loadButtonListener();
                }
            });

            //Add the quit option to the File menu.
            jMenuItemQuit = new JMenuItem("Quit");
            jMenuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                    ActionEvent.CTRL_MASK));
            jMenuFile.addSeparator();
            jMenuFile.add(jMenuItemQuit);
            jMenuItemQuit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });

            /* The Options menu presents five options:
             * 	Mouse - Set the mouse to translate the entire graph or move select nodes.
             * 	Node Colour - Change the colour scheme for the nodes.
             * 	Label Options - Change what information is displayed on the edges.
             * 	Layout - Change the layout algorithm for generating the graph.
             * 	Custom Node Size - Customize the radius of the nodes. 
             */
            
            //Create the Options menu and add it to the menu bar.
            jMenuOptions = new JMenu("Options");
            jMenuBarTop.add(jMenuOptions);
            jMenuMouse = new JMenu();
            
            //Add the mouse option to the Options menu.
            jMenuOptions.add(jMenuMouse);
            jMenuMouse.setText("Mouse");
            mouseGroup = new ButtonGroup();
            jRadioButtonMenuItemTranslate = new JRadioButtonMenuItem();
            jMenuMouse.add(jRadioButtonMenuItemTranslate);
            jRadioButtonMenuItemTranslate.setText("Translate");
            jRadioButtonMenuItemTranslate.setSelected(true);
            mouseGroup.add(jRadioButtonMenuItemTranslate);
            //When translate is selected, remove the pick plugin from the mouse and add the translate plugin.
            jRadioButtonMenuItemTranslate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!jRadioButtonMenuItemTranslate.isSelected()) {
                        jRadioButtonMenuItemTranslate.setSelected(true);
                        return;
                    }
                    jRadioButtonMenuItemPick.setSelected(false);
                    mouse.remove(pick);
                    mouse.add(translate);

                }
            });
            jRadioButtonMenuItemPick = new JRadioButtonMenuItem();
            jMenuMouse.add(jRadioButtonMenuItemPick);
            jRadioButtonMenuItemPick.setText("Pick");
            jRadioButtonMenuItemPick.setSelected(false);
            mouseGroup.add(jRadioButtonMenuItemPick);
            //When pick is selected, remove the translate plugin from the mouse and add the pick plugin.
            jRadioButtonMenuItemPick.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!jRadioButtonMenuItemPick.isSelected()) {
                        jRadioButtonMenuItemPick.setSelected(true);
                        return;
                    }
                    jRadioButtonMenuItemTranslate.setSelected(false);
                    mouse.remove(translate);
                    mouse.add(pick);

                }
            });
            
            //Add the node colour option to the Options menu.
            jMenuColor = new JMenu("Node Color");
            jMenuOptions.add(jMenuColor);
            colorGroup = new ButtonGroup();
            redButton = new JRadioButton("Red");
            jMenuColor.add(redButton);
            addRadioButtonUpdateTransformerListener(redButton);
            colorGroup.add(redButton);
            blueButton = new JRadioButton("Blue");
            jMenuColor.add(blueButton);
            addRadioButtonUpdateTransformerListener(blueButton);
            colorGroup.add(blueButton);
            greenButton = new JRadioButton("Green");
            jMenuColor.add(greenButton);
            addRadioButtonUpdateTransformerListener(greenButton);
            colorGroup.add(greenButton);
            multiButton = new JRadioButton("Red/Yellow/Green");
            jMenuColor.add(multiButton);
            addRadioButtonUpdateTransformerListener(multiButton);
            colorGroup.add(multiButton);
            customColorButton = new JRadioButton("Custom Colors");
            jMenuColor.add(customColorButton);
            customColorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    colorActionListener();
                }
            });
            colorGroup.add(customColorButton);
            redButton.setSelected(true);

            //Add the label options option to the Options menu.
            labelMenu = new JMenu("Label Options");
            jMenuOptions.add(labelMenu);
            stringLabel = new JRadioButton("Edge Name");
            labelMenu.add(stringLabel);
            stringLabel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateTransformers();
                }
            });
            noLabel = new JRadioButton("No Label");
            labelMenu.add(noLabel);
            noLabel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateTransformers();
                }
            });
            percentLabel = new JRadioButton("Edge Statistics");
            labelMenu.add(percentLabel);
            percentLabel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateTransformers();
                }
            });
            labelGroup = new ButtonGroup();
            labelGroup.add(stringLabel);
            labelGroup.add(percentLabel);
            labelGroup.add(noLabel);
            percentLabel.setSelected(true);
            
            //Add the layout option to the Options Menu
            jRadioButtonCircleLayout = new JRadioButton("Circle");
            jRadioButtonFRLayout = new JRadioButton("Fruchterman-Reingold Force Directed Algorithm");
            jRadioButtonSpringLayout = new JRadioButton("Spring Layout");
            jRadioButtonSpringLayout.setEnabled(false);
            jRadioButtonCircleLayout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    updateCanvas();
                }

            });
            jRadioButtonSpringLayout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    updateCanvas();
                }

            });
            jRadioButtonFRLayout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    updateCanvas();
                }

            });
            ButtonGroup layoutGroup = new ButtonGroup();
            layoutGroup.add(jRadioButtonCircleLayout);
            layoutGroup.add(jRadioButtonFRLayout);
            layoutGroup.add(jRadioButtonSpringLayout);

            jMenuLayout = new JMenu("Layout");
            jMenuLayout.add(jRadioButtonCircleLayout);
            jMenuLayout.add(jRadioButtonFRLayout);
            jMenuLayout.add(jRadioButtonSpringLayout);
            jMenuOptions.add(jMenuLayout);

            jRadioButtonCircleLayout.setSelected(true);

            ((JMenuItem)jMenuOptions.add(new JMenuItem("Custom Node Size"))).addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new InsertNameHereDialog(DisplayFrame.this, true);
                            updateTransformers();
                        }    
                    });
            
            JMenuItem temp = new JMenuItem("Customize");
            temp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {

                    if(jRadioButtonCircleLayout.isSelected()) {
                        new AdvancedLayoutDialog(DisplayFrame.this, true, AdvancedLayoutDialog.FLAG_CIRCLE_LAYOUT);
                        updateCanvas(DisplayFrame.FLAG_ADVANCED_CIRCLE);
                    }else if(jRadioButtonFRLayout.isSelected()) {
                        new AdvancedLayoutDialog(DisplayFrame.this, true, AdvancedLayoutDialog.FLAG_FR_LAYOUT);
                        updateCanvas(DisplayFrame.FLAG_ADVANCED_FR);
                    }else if(jRadioButtonSpringLayout.isSelected()) {
                        new AdvancedLayoutDialog(DisplayFrame.this, true, AdvancedLayoutDialog.FLAG_SPRING_LAYOUT);
                        updateCanvas(DisplayFrame.FLAG_ADVANCED_SPRING);
                    }
                }
            });
            jMenuLayout.add(temp);
          
            /* The Help menu presents one option:
             * 	About - Display the information about the program and its contributors.
             */
            
            //Create the Help menu and add it to the menu bar.
            jMenuHelp = new JMenu("Help");
            jMenuBarTop.add(jMenuHelp);
            //Add the about option to the Help menu.
            jMenuItemAbout = new JMenuItem("About");
            jMenuHelp.add(jMenuItemAbout);
            jMenuItemAbout.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                    helpActionListener();
                }
            });
        }

        /**
         * This method creates an instance of the DisplayFrame and causes it to become visible.
         * 
         * @param args Not used.
         */
        public static void main(String[] args) {
            DisplayFrame inst = new DisplayFrame();
            inst.setLocationRelativeTo(null);
            inst.setVisible(true);
        }

        /**
         * This is the constructor for the frame. Constructs the frame by calling
         * the initGUI method.
         * 
         * Also adds the graph mouse plugins to the mouse.
         * 
         */
        public DisplayFrame() {
            super();
            /* Adding translate to the PluggableGraphMouse mouse allows for left-clicking and dragging
             * a node to move (translate) its location.
             * Adding pop to the PluggableGraphMouse mouse allows for the generation of a popup menu
             * providing further information and options for the node. 
             */
            mouse.add(translate);
            mouse.add(pop);
            //Initialize the GUI.
            initGUI();
        }

        /**
         * This method converts a MutationVector containing Mutants as nodes into a
         * MutationVector containing Tests as nodes.
         * 
         * @param a
         *            a MutationVector containing Mutants as nodes
         * @param b
         *            an empty MutationVector that will contain Tests as nodes
         */
        public void convertProgramToTest(MutationVector<MutationData> a,
                MutationVector<MutationData> b) {
            //If there is no mutation data then return an empty Vector of tests
            if (a.size() == 0) {
                return;
            }

            // copies all relevant data into the new Test MutationVector
            // This loops is very complicated, and is easiest to understand if
            // you draw a diagram on a piece of paper. That's what I did to write
            // the loop.
            for (int aTestSize = 0; aTestSize < a.get(0).getSize(); aTestSize++) {
                b.add(new MutationData(a.get(0).getData(aTestSize)));

                for (int aMutantSize = 0; aMutantSize < a.size(); aMutantSize++) {
                    b.get(aTestSize).add(a.get(aMutantSize).getName());
                    b.get(aTestSize).add(a.get(aMutantSize).getResult(aTestSize));
                    b.get(aTestSize).getXml().add(new XmlStorage());
                    for (int aXmlSize = 0; aXmlSize < a.get(aMutantSize).getXml()
                    .get(aTestSize).getSize(); aXmlSize++) {
                        String tag = a.get(aMutantSize).getXml().get(aTestSize)
                        .getXmlTag(aXmlSize);
                        String data = a.get(aMutantSize).getXml().get(aTestSize)
                        .getXmlData(aXmlSize);
                        b.get(aTestSize).getXml().get(aMutantSize)
                        .setXml(tag, data);
                    }
                }
            }

        }

        /**
         * Uses JFileChooser to open a data (xml) file. Calls updateCanvas() to call
         * all of the graphing methods, and displaySource() to display the source
         * code in the textpane.
         * 
         * This button also visualizes the data, and controls everything related to
         * visualizing the data.
         * 
         * @param evt
         *            is not used, but is required for action listeners
         */
        private void openButtonListener(ActionEvent evt) {

            // Reinitialize mutantData and parses the selected file to extract and store mutation data.
            mutantData = new MutationVector<MutationData>(true);
            XMLParser pars = new XMLParser(mutantData);
            pars.parseDocument();

            //Initialize a new testData object and use it as the destination of a mutantData conversion.
            testData = new MutationVector<MutationData>(false);
            convertProgramToTest(mutantData, testData);

            //Update the graph canvas.
            updateCanvas();
            //Display the source code.
            displaySource();
        }

        /**
         * This method allow for updateCanvas to be called without a flag and will call updateCanvas
         * with a -1 flag.
         */
        public void updateCanvas() {
            updateCanvas(-1);
        }

        /**
         * This method controls everything related to updating the canvas.
         * 
         * It draws all the lines and nodes, and contains all the transformers for
         * the canvas.
         * 
         */
        public void updateCanvas(int flag) {

            final String TAB_TITLE;

            if(jRadioButtonMutant.isSelected()) {
                TAB_TITLE = MUTANT_TAB_TITLE;
            } else {
                TAB_TITLE = TEST_TAB_TITLE;
            }

            // Decides which MutationVector to use based on the selection of the mutant or test radio buttons.
            if (jRadioButtonMutant.isSelected()) {
                currentData = mutantData;
                jTabbedPaneCurrentCanvas = jTabbedPaneMutantCanvas;
                currentMainCanvas = mutantMainCanvas;
                connectionCanvases = connectionMutantCanvases;
            } else if (jRadioButtonTest.isSelected()) {
                currentData = testData;
                jTabbedPaneCurrentCanvas = jTabbedPaneTestCanvas;
                currentMainCanvas = testMainCanvas;
                connectionCanvases = connectionTestCanvases;

            }

            
            /* Prevents the program from attempting to flip to the next card when the radio button of 
             * the current card is selected.
             */
            
    
            if(flag == 1 ) //Mutant radio button was clicked.
            {
            	if (!onMutantCard)
            	{
            		flipToNextCard();
            	}
            }
            //
            else if(flag == 2 ) //Test radio button was clicked. 
            {
            	if (onMutantCard)
            	{
            		flipToNextCard();
            	}
            }
            

            // Creates a new "GraphGenerator", which basically just stores the graph
            GraphGenerator grapher = new GraphGenerator(currentData);

            if (jRadioButtonCircleLayout.isSelected()) { 
                layout = new CustomCircleLayout<MyVertex, MyEdge>(grapher.getGraph());
                ((CustomCircleLayout<MyVertex, MyEdge>) (layout))
                .setCustomSeedAndOrderVertices(3);
                if(flag == DisplayFrame.FLAG_ADVANCED_CIRCLE) {
                    if(advancedLayoutList.get(0) instanceof Integer)
                        ((CustomCircleLayout<MyVertex, MyEdge>)layout).setRadius((Integer)advancedLayoutList.get(0));
                }

            } else if (jRadioButtonFRLayout.isSelected()) {
                layout = new FRLayout<MyVertex, MyEdge>(grapher.getGraph());
                if(flag == DisplayFrame.FLAG_ADVANCED_FR) {
                    if(advancedLayoutList.size() == 3) {
                        ((FRLayout<MyVertex, MyEdge>)layout).setAttractionMultiplier((Double)advancedLayoutList.get(0));
                        ((FRLayout<MyVertex, MyEdge>)layout).setRepulsionMultiplier((Double)advancedLayoutList.get(1));
                        ((FRLayout<MyVertex, MyEdge>)layout).setMaxIterations((Integer)advancedLayoutList.get(2));

                    }
                }

            }else if (jRadioButtonSpringLayout.isSelected()) {
                layout = new SpringLayout<MyVertex, MyEdge>(grapher.getGraph());
                if(flag == DisplayFrame.FLAG_ADVANCED_SPRING) {
                    if(advancedLayoutList.size() == 3) {
                        ((SpringLayout<MyVertex, MyEdge>)layout).setForceMultiplier((Double)advancedLayoutList.get(0));
                        ((SpringLayout<MyVertex, MyEdge>)layout).setRepulsionRange((int)((Double)advancedLayoutList.get(1)).doubleValue());
                        ((SpringLayout<MyVertex, MyEdge>)layout).initialize();
                        for(int i = 0; i < (Integer)advancedLayoutList.get(2); i++) {
                            ((SpringLayout<MyVertex, MyEdge>)layout).step();
                        }


                    }
                }

            }

            int tempLocation = jSplitPaneCenter.getDividerLocation();



            // Creates a new canvas, adds the layout to it

            currentMainCanvas = new VisualizationViewer<MyVertex, MyEdge>(layout);

            currentMainCanvas.setBackground(CANVAS_COLOR);
            currentMainCanvas.setMinimumSize(new Dimension(150, 150));

            // adds the canvas to the jPanel
            int index23 = -1;
            if(jTabbedPaneCurrentCanvas.getTabCount() > 0) {
                index23 = jTabbedPaneCurrentCanvas.getSelectedIndex();
                jTabbedPaneCurrentCanvas.remove(0);
            }
            jTabbedPaneCurrentCanvas.insertTab(TAB_TITLE, null, currentMainCanvas, null, 0);
            if(index23 >= 0) {
                jTabbedPaneCurrentCanvas.setSelectedIndex(index23);
            }


            //jTabbedPaneCurrentCanvas.setSelectedIndex(tabIndex);

            //jTabbedPaneCurrentCanvas.setTitleAt(0, tabbedPaneTitle);

            // redraws the frame
            this.validate();

            updateTransformers();
            //TODO updateTransformers(connectionCanvases);

            // Sets the graph mouse for the canvas, since the canvas was recreated
            currentMainCanvas.setGraphMouse(mouse);

            // remove the popup menu mouse plugin, and add a new one for the current
            // data, since a new data file
            // may have been loaded
            mouse.remove(pop);
            pop = new CustomGraphMousePlugin<MyVertex, MyEdge>(currentData, jTextPaneMain, this);
            mouse.add(pop);

            currentMainCanvas.setLayout(new BorderLayout());
            currentMainCanvas.add(jScrollPaneCanvas, BorderLayout.SOUTH);
            jSplitPaneCenter.setDividerLocation(tempLocation);

            //beenClicked = false;

        }

        /**
         * This transformer controls the labels of the edges.
         * 
         * @param edge
         *            a String containing the name of the edge
         * @return returns a String containing the label name
         */
        public String edgeLabelTransformer(MyEdge edge) {
            if (stringLabel.isSelected()) {
                return edge.name;
            } else if (noLabel.isSelected()) {
                return "";
            }
            // double loop to compare every name and find the edge
            for (int i = 0; i < currentData.size(); i++) {
                for (int t = 0; t < currentData.size(); t++) {

                    // if statement finds the edge
                    if (edge.equals(currentData.get(i).getName() + " "
                            + currentData.get(t).getName())) {

                        // initialize a counter and start a new loop to compare the
                        // data in the 2 vertexes
                        int counter = 0;
                        for (int q = 0; q < currentData.get(t).getSize(); q++) {

                            // if both pieces of data are "yes", add to the counter
                            if (currentData.get(i).getResult(q).equals("yes")) {
                                if (currentData.get(t).getResult(q).equals("yes")) {
                                    counter++;
                                }
                            }
                        }
                        if (counter != 0) {

                            // convert the counter to a percentage of similarities,
                            // and create a line thickness
                            // based on this.
                            double percent = ((double) counter)
                            / (double) (currentData.get(t).getSize()) * 100;
                            percent = Math.round((percent * 100.0))/100.0;
                            String label = percent + "%" + " (" + counter
                            + " similar)";
                            return label;

                        }

                    }
                }
            }

            return edge.name;

        }

        /**
         * Updates the transformers for the specified canvas
         * 
         * @param canvas
         *            the canvas which is having its transformers updated
         */
        public void updateTransformers(VisualizationViewer<MyVertex, MyEdge> canvas) {

            // creates a new transformer for the vertex shape/size.
            if(((VectorFlag<Integer[]>)customShapeSize).flag == VectorFlag.FLAG_CLOSE_DEFAULT) {

                Collection<MyVertex> vertices = canvas.getGraphLayout().getGraph().getVertices();
                for(MyVertex v : vertices) {
                    v.sizeAndShape = returnNodeShape(DisplayFrame.FLAG_TRANSFORMER_SHAPE_DEFAULT,
                            currentData, v, customShapeSize);
                }

                canvas.getRenderContext().setVertexShapeTransformer(new Transformer<MyVertex, Shape>() {
                    @Override
                    public Shape transform(MyVertex v) {
                        return v.sizeAndShape;
                    }   
                });

            }else if(((VectorFlag<Integer[]>)customShapeSize).flag == VectorFlag.FLAG_CLOSE_DISCRETE) {
                //canvas.getRenderContext().setVertexShapeTransformer(returnShapeTransformer(FLAG_TRANSFORMER_SHAPE_CUSTOM));
                Collection<MyVertex> vertices = canvas.getGraphLayout().getGraph().getVertices();
                for(MyVertex v : vertices) {
                    v.sizeAndShape = returnNodeShape(DisplayFrame.FLAG_TRANSFORMER_SHAPE_CUSTOM,
                            currentData, v, customShapeSize);
                }

                canvas.getRenderContext().setVertexShapeTransformer(new Transformer<MyVertex, Shape>() {
                    @Override
                    public Shape transform(MyVertex v) {
                        return v.sizeAndShape;
                    }   
                });
            }

            // Sets a label transformer for the vertexes that uses the name as a
            // label
            canvas.getRenderContext().setVertexLabelTransformer(
                    new ToStringLabeller<MyVertex>());
            // Label will appear in the center of the vertex
            canvas.getRenderer().getVertexLabelRenderer()
            .setPosition(Position.CNTR);

            {
                Collection<MyEdge> edges = canvas.getGraphLayout().getGraph().getEdges();
                for(MyEdge e : edges) {
                    e.edgeSize = defaultEdgeStrokeTransformer(e, currentData);
                }
            }
            // creates a transformer for the edges that will control thickness
            canvas.getRenderContext().setEdgeStrokeTransformer(
                    new Transformer<MyEdge, Stroke>() {
                        public Stroke transform(MyEdge s) {
                            return s.edgeSize;
                        }
                    });

            // set a label transformer for the edges

            Collection<MyEdge> edges = canvas.getGraphLayout().getGraph().getEdges();
            for(MyEdge edg : edges) {
                edg.edgeLabel = edgeLabelTransformer(edg);
            }

            canvas.getRenderContext().setEdgeLabelTransformer(
                    new Transformer<MyEdge, String>() {
                        public String transform(MyEdge s) {
                            return s.edgeLabel;
                        }

                    });

            // set a transformer for the vertex color

            Collection<MyVertex> vertices = canvas.getGraphLayout().getGraph().getVertices();
            for(MyVertex vertex : vertices) {
                vertex.color = vertexPaintTransformer(vertex);
            }

            canvas.getRenderContext().setVertexFillPaintTransformer(
                    new Transformer<MyVertex, Paint>() {
                        public Paint transform(MyVertex s) {
                            return s.color;
                        }

                    });

            canvas.repaint();
        }

        /**
         * update the transformers for the main canvas
         * 
         */
        public void updateTransformers() {
            updateTransformers(currentMainCanvas);
        }

        /**
         * Completely updates the connections canvas with a new graph, draws it,
         * etc.
         * 
         * @param tempGraph
         *            The graph that will be drawn on the canvas
         */
        public void updateCanvasConnections(MyVertex v) {

            Graph<MyVertex, MyEdge> oGraph = GraphGenerator.returnConnectionGraph(v, this.getCurrentCanvasGraph());
            final VisualizationViewer<MyVertex, MyEdge> newCanvas = new VisualizationViewer<MyVertex, MyEdge>(
                    new CustomCircleLayout<MyVertex, MyEdge>(oGraph));
            connectionCanvases.add(newCanvas);
            newCanvas.setBackground(CANVAS_COLOR);
            jTabbedPaneCurrentCanvas.add(newCanvas);
            jTabbedPaneCurrentCanvas.setTitleAt(connectionCanvases.size(), v.name + "      ");
            JPanel panelThing = new TabComponents(v.name, jTabbedPaneCurrentCanvas, connectionCanvases);
            //panelThing.setPreferredSize(new Dimension(30, 30));

            jTabbedPaneCurrentCanvas.setTabComponentAt(connectionCanvases.size(), panelThing);





            // update the transformers for the canvas
            updateTransformers(newCanvas);

            // set a graph mouse for the canvas
            newCanvas.setGraphMouse(mouse);

            // add the zoom in and zoom out buttons
            newCanvas.setLayout(new BorderLayout());
            JPanel jPanelCanvas2 = new JPanel();
            JScrollPane jScrollPaneCanvas2 = new JScrollPane();
            JButton jButtonCanvas2ZoomIn = new JButton("Zoom in");
            JButton jButtonCanvas2ZoomOut = new JButton("Zoom out");
            jPanelCanvas2.setLayout(new FlowLayout());
            jPanelCanvas2.add(jButtonCanvas2ZoomIn);
            jPanelCanvas2.add(jButtonCanvas2ZoomOut);
            jScrollPaneCanvas2.setViewportView(jPanelCanvas2);
            newCanvas.add(jScrollPaneCanvas2, BorderLayout.SOUTH);
            jButtonCanvas2ZoomIn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    zoomIn(newCanvas);
                }
            });
            jButtonCanvas2ZoomOut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    zoomOut(newCanvas);
                }
            });

        }

        /**
         * TODO fix comment, method has been moved and/or modified
         * 
         * Transformer method that calculates the thickness of an edge.
         * 
         * @param s
         *            The name of the edge (String)
         * @return The thickness of the edge (Stroke)
         */
        public static Stroke defaultEdgeStrokeTransformer(MyEdge edge, final MutationVector<MutationData> currentData) {


            Stroke line = (Stroke) (new BasicStroke(1.0f));

            // double loop to compare every name and find the edge
            nestedLoop:
                for (int i = 0; i < currentData.size(); i++) {
                    for (int t = 0; t < currentData.size(); t++) {

                        // if statement finds the edge
                        if (edge.equals(currentData.get(i).getName() + " "
                                + currentData.get(t).getName())) {

                            // initialize a counter and start a new loop to compare the
                            // data in the 2 vertexes
                            int counter = 0;
                            for (int q = 0; q < currentData.get(t).getSize(); q++) {

                                // if both pieces of data are "yes", add to the counter
                                if (currentData.get(i).getResult(q).equals("yes")) {
                                    if (currentData.get(t).getResult(q).equals("yes")) {
                                        counter++;
                                    }
                                }
                            }
                            assert(counter > 0);
                            //TODO double check that this works
                            //if (counter != 0) {

                            // convert the counter to a percentage of similarities,
                            // and create a line thickness
                            // based on this.
                            double percent = ((double) counter)
                            / (double) (currentData.get(t).getSize());
                            line = new BasicStroke(
                                    (float) (10 * percent) + 1);
                            //return line;
                            break nestedLoop;

                            //}

                        }
                    }
                }


            return line;

        }


        /**
         * Transformer method that calculates the size of the node
         * 
         * @param s
         *            The name of the node (String)
         * @return The shape/size of the node (Shape)
         */
        public static Shape returnNodeShape(
                int flag, 
                final MutationVector<MutationData> currentData, 
                MyVertex vertex,
                final Vector<Integer[]> customShapeSize) {

            Shape node;

            if(flag == FLAG_TRANSFORMER_SHAPE_DEFAULT) {

                {
                    //TODO comment this fixed line
                    node = new Rectangle2D.Double(0, 0, 100, 100);

                    	nestedLoop:
                        for (int i = 0; i < currentData.size(); i++) {
                            if (vertex.equals(currentData.get(i).getName())) {
                                double size = (1-currentData.get(i).getPercent()) * 100 + 50;
                                double halfsize = (size / 2) * (-1);
                                //halfsize centers the nodes
                                node = new Ellipse2D.Double(halfsize, halfsize, size,
                                        size);
                               break nestedLoop;
                            }
                        }

                    return node;
                }

            }else if(flag == FLAG_TRANSFORMER_SHAPE_CUSTOM){
                for (int i = 0; i < currentData.size(); i++) {
                    if (vertex.equals(currentData.get(i).getName())) {
                        double percent = currentData.get(i).getPercent() * 100;
                        for(int j = 0; j < customShapeSize.size(); j++) {
                            if(percent <= customShapeSize.get(j)[1]) {
                                double halfsize = (-1)*(customShapeSize.get(j)[0]/2);
                                return new Ellipse2D.Double(halfsize, halfsize, 
                                        customShapeSize.get(j)[0], customShapeSize.get(j)[0]);
                            }
                        }

                    }
                }
                //this line should not execute
                return null;

            }else {
                return null;
            }

        }


        /**
         * Transformer method that calculates the paint color of the node
         * 
         * @param vvvvvvvvvvvvv
         *            The name of the vertex (String)
         * @return The color of the vertex (Color, which implements Paint)
         */
        public Paint vertexPaintTransformer(MyVertex vvvvvvvvvvvvv) {

            for (int i = 0; i <= currentData.size(); i++) {
                if (currentData.get(i).getName().equals(vvvvvvvvvvvvv.name)) {
                    float temp = currentData.get(i).getPercent();
                    return getPaintColor(temp);

                }

            }
            return Color.RED;

        }

        /**
         * Chooses the correct method for calculating paint color based on which
         * button is selected
         * 
         * @param temp
         *            The percentage given by the getPercent() method in
         *            MutationData (float)
         * @return The color of the vertex (Color, which implements Paint)
         */
        public Paint getPaintColor(float temp) {
            if (redButton.isSelected()) {
                return getRedPaint(temp);
            } else if (blueButton.isSelected()) {
                return getBluePaint(temp);
            } else if (greenButton.isSelected()) {
                return getGreenPaint(temp);
            } else if (multiButton.isSelected()) {
                return getMultiColorPaint(temp);
            } else if (customColorButton.isSelected()) {
                return getCustomPaint(temp);
            }
            return getRedPaint(temp);

        }

        /**
         * Tries to set the TextPane to 1 pixel by 1 pixel. The custom setSize()
         * method will resize the TextPane to the size of its parent
         * 
         */
        public void resizeCustomTextPane() {
            jTextPaneMain.setSize(1, 1);
        }

        /**
         * Displays the source code in the TextPane with the correct background
         * color on each line
         * 
         */
        public void displaySource() {
            // TODO comment all this

            //creates a new styled document and sets its style to default
            StyledDocument doc = new DefaultStyledDocument();
            Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(
                    StyleContext.DEFAULT_STYLE);
            jTextPaneMain.setText("");


            for (int q = 0; q < currentData.getSourceSize(); q++) {
                String currentFile = currentData.getFile(q);
                try {
                    if (q != 0) {
                        doc.insertString(doc.getLength(), NEW_LINE, defaultStyle);
                        doc.insertString(doc.getLength(), NEW_LINE, defaultStyle);
                    }
                    doc.insertString(doc.getLength(), currentData.getFile(q),
                            defaultStyle);
                    doc.insertString(doc.getLength(), NEW_LINE, defaultStyle);
                } catch (Exception e) {

                }
                int counter = 0;
                int lineCounter = 1;
                for (int i = 0; i < currentData.getSource(q).length(); i++) {
                    String newLineSearch = "\n"; // for searching, not printing
                    if (i + newLineSearch.length() < currentData.getSource(q).length()) {
                        if (currentData.getSource(q).substring(i,
                                i + newLineSearch.length()).equals(newLineSearch)) {
                            //System.out.pri//ntln("block");
                            try {
                                String temp2 = "" + lineCounter;
                                Style temp = doc.addStyle(temp2, defaultStyle);
                                //System.out.pri//ntln(findLinePercent(lineCounter,
                                        //currentFile));
                                float percent = findLinePercent(lineCounter,
                                        currentFile);
                                if (percent >= 0.0f) {
                                    StyleConstants.setBackground(temp,
                                            (Color) getPaintColor(percent));
                                } else {
                                    StyleConstants.setForeground(temp, new Color(
                                            0.75f, 0.75f, 0.75f));
                                }
                                doc.insertString(doc.getLength(), currentData
                                        .getSource(q).substring(counter, i), temp);
                                doc.insertString(doc.getLength(), NEW_LINE,
                                        defaultStyle);
                            } catch (Exception e) {

                            }
                            counter = i + newLineSearch.length();
                            lineCounter++;
                            i = counter;

                        }
                    } else {
                        String temp2 = "" + lineCounter;
                        Style temp = doc.addStyle(temp2, defaultStyle);
                        
                        float percent = findLinePercent(lineCounter, currentFile);
                        
                        if (percent >= 0.0f) {
                            StyleConstants.setBackground(temp,
                                    (Color) getPaintColor(percent));
                        } else {
                            StyleConstants.setForeground(temp, new Color(0.75f,
                                    0.75f, 0.75f));
                        }
                        
                        try {

                            doc.insertString(doc.getLength(),
                                    currentData.getSource(q).substring(counter,
                                            currentData.getSource(q).length()), temp);
                        } catch (Exception e) {

                        }
                        break;
                    }

                }
            }
            jTextPaneMain.setDocument(doc);

        }

        /**
         * Calculates the color of the node. Uses red, yellow, and green, and does
         * not vary the shade. Red if temp is less than 0.33333, yellow if temp is
         * less than 0.6666666, and green otherwise.
         * 
         * @param temp
         *            The percentage given by the getPercent() method in
         *            MutationData
         * @return The color of the node
         */
        public Paint getMultiColorPaint(float temp) {
            float frac1 = 1.0f / 3.0f;
            float frac2 = 2.0f / 3.0f;
            if (temp <= frac1) {
                Paint color = new Color(255, 0, 51);
                return color;
            } else if (temp <= frac2) {
                Paint color = new Color(255, 204, 51);
                return color;

            } else {
                Paint color = new Color(51, 204, 0);
                return color;
            }
        }

        /**
         * Gives the node a certain shade of red based on temp
         * 
         * @param temp
         *            The percentage given by getPercent() in MutationData (float)
         * @return The color of the node (Color)
         */
        public Paint getRedPaint(float temp) {
            int colorIndexGB = (int) (255 * temp);
            return new Color(255, colorIndexGB, colorIndexGB);
        }

        /**
         * Gives the node a certain shade of blue based on temp
         * 
         * @param temp
         *            The percentage given by getPercent() in MutationData (float)
         * @return The color of the node (Color)
         */
        public Paint getBluePaint(float temp) {
            int colorIndexGB = (int) (255 * temp);
            return new Color(colorIndexGB, colorIndexGB, 255);

        }

        /**
         * Gives the node a certain shade of green based on temp
         * 
         * @param temp
         *            The percentage given by getPercent() in MutationData (float)
         * @return The color of the node (Color)
         */
        public Paint getGreenPaint(float temp) {
            int colorIndexGB = (int) (255 * temp);
            return new Color(colorIndexGB, 255, colorIndexGB);
        }

        /**
         * Calculates the custom color. Red/green/blue are stored in rgbRed,
         * rgbGreen, and rgbBlue. The percent ranges are stored in the range
         * variable.
         * 
         * eg: if range.get(0) is 40, then between 0 and 40, the color will be
         * Color(rgbRed(0), rgbGreen(0), rgbBlue(0))
         * 
         * @param temp
         *            The percentage given by getPercent() in MutationData (float)
         * @return The color of the node (Color)
         */
        public Paint getCustomPaint(float temp) {
            float percent = temp * 100;
            for (int i = 1; i < range.size(); i++) {
                if (percent <= range.get(i)) {
                    return new Color(rgbRed.get(i), rgbGreen.get(i), rgbBlue.get(i));
                }
            }
            return new Color(0, 0, 0);
        }

        /**
         * Calculates the background color of a line of source code by averaging the
         * getPercent() of the mutants associated with the line of source code.
         * Doesn't actually return a color, but returns a the average of all the
         * getPercent() values, which can be passed to the getPaintColor() method.
         * 
         * @param line
         *            the line number (int)
         * @param file
         *            the file which the line is contained in (String)
         * @return the average percent, between 0.0 and 1.0, -1.0f if the line does
         *         not belong to any mutants
         */
        public float findLinePercent(int line, String file) {
            // TODO change function name
            int counterTotal = 0;
            float percentTotal = 0;
            boolean mutantFound = false;

            for (int i = 0; i < currentData.size(); i++) {
                for (int t = 0; t < currentData.get(i).getLineNumbers().size(); t++) {
                    //System.out.pri//ntln(i + " "
                      //      + currentData.get(i).getLineNumbers().get(t).intValue() + " "
                        //    + line);
                    if (currentData.get(i).getLineNumbers().get(t).intValue() == line) {
                        //System.out.pri//ntln(currentData.get(i).getSourceFile() + " "
                          //      + file);
                        if (currentData.get(i).getSourceFile().equals(file)) {
                            mutantFound = true;
                            percentTotal = percentTotal + currentData.get(i).getPercent();
                            //System.out.pri//ntln("it happened");
                            counterTotal++;
                        }
                    }
                }
            }

            float percent;
            if (counterTotal != 0) {
                percent = percentTotal / counterTotal;
            } else {
                percent = 0.0f;
            }
            if (!mutantFound) {
                return -1.0f;
            }
            return percent;
        }

        /**
         * Zooms the graph out
         * 
         * @param canvas
         *            The canvas to be zoomed
         */
        public void zoomOut(VisualizationViewer<MyVertex, MyEdge> canvas) {
            ScalingControl scaler = new CrossoverScalingControl();
            scaler.scale(canvas, (1 / 1.1f), canvas.getCenter());
        }

        /**
         * zooms the graph in
         * 
         * @param canvas
         *            The canvas to be zoomed
         */
        public void zoomIn(VisualizationViewer<MyVertex, MyEdge> canvas) {
            ScalingControl scaler = new CrossoverScalingControl();
            scaler.scale(canvas, 1.1f, canvas.getCenter());
        }

        /**
         * Adds and ActionListener to the JRadioButton's that control node/source
         * color. This ActionListener updates all transformers, and redisplays the
         * source
         * 
         * 
         * @param button
         *            The JRadioButton which is having its ActionListener added
         */
        public void addRadioButtonUpdateTransformerListener(JRadioButton button) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    updateTransformers();
                    //updateTransformers(connectionCanvases);
                    displaySource();
                }
            });
        }

        /**
         * The ActionListener method for the custom color radio button. Gives a
         * color dialog, then updates the source and tranformers
         * 
         */
        public void colorActionListener() {
            new ColorDialog(this, range, rgbRed, rgbGreen, rgbBlue);
            updateTransformers();
            //TODO updateTransformers(connectionCanvases);
            displaySource();
        }

        /**
         * TODO fix
         * 
         * Action Listener method for the save button. Saves the position of all the
         * nodes in the main canvas.
         * 
         * 
         */
        public void saveButtonListener() {/*

            JFileChooser tempFile = new JFileChooser();
            tempFile.showSaveDialog(this);
            File file = new File(tempFile.getSelectedFile().getAbsolutePath());
            PrintWriter writer;
            try {
                writer = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "file not found");
                return;
            }
            for (int x = 0; x < currentData.size(); x++) {

                CustomCircleLayout<MyVertex, MyEdge> tempLayout = ((CustomCircleLayout<MyVertex, MyEdge>) (layout));

                writer.println(currentData.get(x).getName());
                writer.println(tempLayout.getX(currentData.get(x).getName()));
                if (x < (currentData.size() - 1)) {
                    writer.println(tempLayout.getY(currentData.get(x).getName()));
                } else {
                    writer.print(tempLayout.getY(currentData.get(x).getName()));
                }

            }
            writer.close();*/

        }

        /**
         * TODO fix
         * 
         * Action Listener method for the load button. Loads the location of all the
         * nodes.
         * 
         * DOES NOT check to see if the file is correct. Use at your own risk. (Only
         * use it if you know the file you are loading is for your data)
         * 
         */
        public void loadButtonListener() {/*
            JFileChooser tempFile = new JFileChooser();
            tempFile.showSaveDialog(this);
            File file = new File(tempFile.getSelectedFile().getAbsolutePath());
            Scanner scanner;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "file not found");
                return;
            }

            scanner.useDelimiter(NEW_LINE);

            for (int x = 0; x < currentData.size(); x++) {

                String name = scanner.next();
                double pointx = Double.parseDouble(scanner.next());
                double pointy = Double.parseDouble(scanner.next());
                ((CustomCircleLayout<MyVertex, MyEdge>) (layout)).setLocation(name,
                        new Point2D.Double(pointx, pointy));

            }

            currentMainCanvas.repaint();
         */
        }

        /**
         * Selects the JButton for a red graph
         */
        public void setRedColorButtonChecked(){
            redButton.setSelected(true);
        }

        /**
         * Creates a dialog with information about the program's authors.
         */
        public void helpActionListener(){
            String text = new String(PROGRAM_TITLE + NEW_LINE + "Authors: " + NEW_LINE + "Jeff Falkenham" + NEW_LINE + "Dr. Jeremy Bradbury" + NEW_LINE + "David Petras");
            String title = new String("about");
            new DisplayDialog(this, text, title);
        }
        
        /**
         * Changes the card being displayed from the mutant graph to the test graph and vice-versa.
         */
        public void flipToNextCard() {
        	onMutantCard = !onMutantCard;
        	((CardLayout)(cardPanel.getLayout())).next(cardPanel);
        }

        
        public void textMouseListener(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)) {
                double lineCoord = e.getY();
                double textHeight = e.getComponent().getHeight();

                int counter = 0;
                String text = jTextPaneMain.getText();
                String lineBreak = "\n";

                for(int i = 0; i < text.length() - lineBreak.length(); i++) {
                    if(text.substring(i, i + lineBreak.length()).equals(lineBreak)) {
                        counter++;
                    }
                }

                //lines do not have to end with linebreaks
                counter++;


                Vector<Integer> line = getLineNumber((int)Math.ceil(((lineCoord/textHeight)*counter)));

                JPopupMenu popup = new JPopupMenu();

                boolean showPopup = false;

                for(int i = 0; i < currentData.size(); i++) {
                    if(currentData.get(i).isLineNumber(line.get(1))) {
                        if(currentData.get(i).getSourceFile().equals(
                                currentData.getFile(line.get(0)))) {
                            showPopup = true;
                            popup.add(createPopupMenu(e, i));

                        }
                    }
                }
                
                if(showPopup) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }


            }
        }


        /**
         * Creates a popup menu when the right mouse button is clicked on a vertex or an edge.
         * Also makes the vertex label text blue when it is clicked, and switches the label
         * text back to black when the popup menu is destroyed.  
         * 
         * Written entirely by me.
         * 
         * @param e                     The mouse event
         * @param pickedVertexState     Whether the vertex is highlight or not
         */
        public JMenu createPopupMenu(MouseEvent e, int index){


            //creates popup menu and the title of the menu
            JMenu oldPopmenu = new JMenu(currentData.get(index).getName());
            JLabel tempLabel = new JLabel(" Node: " + currentData.get(index).getName());
            tempLabel.setForeground(new Color(0.0f, 0.0f, 1.0f));
            oldPopmenu.add(tempLabel);
            oldPopmenu.addSeparator();



            DebuggingMethods.printStack();


            oldPopmenu.add(pop.createDisplayModifiedSourceMenuItem(index));


            //oldPopmenu.add(pop.createSimilaritiesMenuItem(index));

            oldPopmenu.add(pop.createDisplayResultsMenuItem(index));

            oldPopmenu.add(pop.createListMenuItem(index));

            return oldPopmenu;

        }

        /**
         * @param textLine
         * @return   <sourcefile, linenumber>
         */
        public Vector<Integer> getLineNumber(int textLine) {
            //int counter;
            //filename line
            int counter2 = 1;
            Vector<Integer> returnData = new Vector<Integer>();
            returnData.add(0);
            returnData.add(0);

            File file = new File("debug.txt");
            PrintWriter lol;
            try {
                lol = new PrintWriter(file);
            }catch(Exception e) {
                return null;
            }

            for(int i = 0; i < currentData.getSourceSize(); i++) {
                //counter = 0;




                returnData.set(0, i);
                int newLineCounter = 1;

                if(i != 0)
                    counter2 += 2;

                for(int j = 0; j < currentData.getSource(i).length(); j++) {
                    if(currentData.getSource(i).substring(j, j + SLASH_N.length()).equals(SLASH_N)) {
                        newLineCounter++;
                    }
                    lol.println("textline: " + textLine + 
                            "    line: " + newLineCounter + 
                            "               counters: " + (counter2 + newLineCounter));
                    if(textLine == (counter2 + newLineCounter)) {
                        returnData.set(1, newLineCounter);
                        lol.close();
                        return returnData;
                    }

                }
                //3 = newline newline filename
                counter2 += newLineCounter;
            }

            returnData.set(0, -1);

            returnData.set(1, -1);
            lol.close();
            return returnData;
            //TODO break out of loop

        }




}
