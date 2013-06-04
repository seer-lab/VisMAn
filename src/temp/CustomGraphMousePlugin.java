package temp;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

//TODO Look over the comments.  They should be fine though.

/** 
 * A custom graph mouse plugin created by deleting most of the code for the Jung PickingGraphMousePlugin,
 * modifying some of the leftover code, and then adding in my own code.
 * 
 * see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin in the Jung 2.0 library
 * (alpha 2) for the original code.  If that version no longer exists, see the newest version
 * for similar code.
 * 
 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
 * 
 * 
 * @author Jeff Falkenham
 */
public class CustomGraphMousePlugin<V, E> extends AbstractGraphMousePlugin
implements MouseListener {

    /**
     * the picked Vertex, if any
     */
    protected V vertex;


    /**
     * the picked Edge, if any
     */
    protected E edge;

    /**
     * the x distance from the picked vertex center to the mouse point
     */
    protected double offsetx;

    /**
     * the y distance from the picked vertex center to the mouse point
     */
    protected double offsety;

    /**
     * controls whether the Vertices may be moved with the mouse
     */
    protected boolean locked;

    private DisplayFrame displayFrame;




    protected final MutationVector<MutationData> pDataM;

    protected final JTextPane jTextPane1M;

    /**
     * create an instance with default settings
     */
    public CustomGraphMousePlugin(MutationVector<MutationData> pData, JTextPane jTextPane1, DisplayFrame displayFrame) {
        this(InputEvent.BUTTON3_MASK, pData, jTextPane1, displayFrame);
    }




    /**
     * create an instance with overrides
     * @param selectionModifiers for primary selection
     */
    public CustomGraphMousePlugin(int selectionModifiers, MutationVector<MutationData> pData, JTextPane jTextPane1, DisplayFrame displayFrame) {
        super(selectionModifiers);
        jTextPane1M = jTextPane1;
        pDataM = pData;
        this.displayFrame  = displayFrame;
    }




    /**
     * If the mouse pointer is within a vertex or edge when the right mouse button
     * is clicked, a popup menu will be generated.  The popup menu will contain
     * information relevant to the vertex or edge.
     * 
     * @param e the event
     */
    @SuppressWarnings("unchecked")
    public void mousePressed(MouseEvent e) {
        down = e.getPoint();
        VisualizationViewer<V,E> vv = (VisualizationViewer)e.getSource();
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        PickedState<V> pickedVertexState = vv.getPickedVertexState();
        PickedState<E> pickedEdgeState = vv.getPickedEdgeState();
        if(pickSupport != null && pickedVertexState != null) {
            Layout<V,E> layout = vv.getGraphLayout();
            if(e.getModifiers() == modifiers) {
                // p is the screen point for the mouse event
                Point2D p = e.getPoint();
                // take away the view transform
                Point2D ip = p;

                vertex = pickSupport.getVertex(layout, ip.getX(), ip.getY());
                MyVertex localV = null;
                if(vertex instanceof MyVertex) {
                    localV = (MyVertex)vertex;
                }
                pickedEdgeState.clear();
                pickedVertexState.clear();
                if(vertex != null) {
                    if(pickedVertexState.isPicked(vertex) == false) {
                        pickedVertexState.pick(vertex, true);



                        //creates the popup menu
                        createPopupMenu(e, pickedVertexState, localV);

                    }
                    // layout.getLocation applies the layout transformer so
                    // q is transformed by the layout transformer only
                    Point2D q = layout.transform(vertex);
                    // transform the mouse point to graph coordinate system
                    Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.LAYOUT, ip);

                    offsetx = (float) (gp.getX()-q.getX());
                    offsety = (float) (gp.getY()-q.getY());
                } else if((edge = pickSupport.getEdge(layout, ip.getX(), ip.getY())) != null) {
                    pickedEdgeState.pick(edge, true);
                    displaySimilarities();


                }

            } 
        }
        if(vertex != null) e.consume();
    }

    /**
     * 
     * clean up settings from mousePressed
     */
    @SuppressWarnings("unchecked")
    public void mouseReleased(MouseEvent e) {
        VisualizationViewer<V,E> vv = (VisualizationViewer)e.getSource();
        down = null;
        vertex = null;
        edge = null;
        vv.repaint();
    }


    //code completely written by me starts here-----------------------------------------------------

    /**
     * This is used in the action listener for the "display all results" menu item.
     * 
     * Written entirely by me.
     * 
     * @param indexf		The index of the pData
     */
    public void displayResultsListener(int indexf){
        try{
            StringBuffer holder = new StringBuffer();


            for(int i = 0;i < pDataM.get(indexf).getSize();i++){
                holder.append(pDataM.get(indexf).getData(i)  + System.getProperty("line.separator") + pDataM.get(indexf).getResult(i));
                holder.append(System.getProperty("line.separator") + System.getProperty("line.separator"));
            }



            //jTextArea1M.setText(holder.toString().trim());
            String title = new String("These are the results for: " + pDataM.get(indexf).getName());
            new DisplayDialog(displayFrame, holder.toString().trim(), title );
        }catch(Exception e){
            e.printStackTrace();

        }

    }


    /**
     * Creates a popup menu when the right mouse button is clicked on a vertex or an edge.
     * Also makes the vertex label text blue when it is clicked, and switches the label
     * text back to black when the popup menu is destroyed.  
     * 
     * Written entirely by me.
     * 
     * @param e						The mouse event
     * @param pickedVertexState		Whether the vertex is highlight or not
     */
    public void createPopupMenu(MouseEvent e, PickedState<V> pickedVertexState, MyVertex ver){

        //TODO delete JPopupMenu realPopup = new JPopupMenu();

        //creates popup menu and the title of the menu
        JPopupMenu popup = new JPopupMenu();
        JLabel tempLabel = new JLabel(" Node: " + vertex.toString());
        tempLabel.setForeground(new Color(0.0f, 0.0f, 1.0f));
        popup.add(tempLabel);
        popup.addSeparator();

        //finds the position of the vertex in pData
        final int indexf = findVertexIndex();

        if(pDataM.isMutant()){
            popup.add(createDisplayModifiedSourceMenuItem(indexf));
        }

        popup.add(createSimilaritiesMenuItem(indexf, ver));

        popup.add(createDisplayResultsMenuItem(indexf ));

        popup.add(createListMenuItem(indexf));

        //TODO delete realPopup.add(popup);
        /*JPanel pane = new JPanel();
		pane.add(popup);
		realPopup.setLayout(new BorderLayout());
		realPopup.add(pane, BorderLayout.CENTER);*/


        //show and position the popup
        popup.show(e.getComponent(), e.getX(), e.getY());


        final PickedState<V> pickedVertexStatef = pickedVertexState;
        popup.addPropertyChangeListener(addPopupDestroyListener(pickedVertexStatef));

        //TODO rename method names.  "add" is not a good prefix

    }


    /**
     * Adds an action listener that changes the vertex text back to black when the popup is destroyed
     * 
     * @param popup					The popup menu
     * @param pickedVertexStatef	Whether the vertex is selected or not
     */
    public PropertyChangeListener addPopupDestroyListener(final PickedState<V> pickedVertexStatef){
        return new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt){
                if(evt.getPropertyName().equals("visible")){
                    pickedVertexStatef.clear();
                }
            }
        };

    }

    /**
     * Creates a "Tests" or "Mutants" menu inside the popup.  Inside the menu, the method creates a menu for each Test or Mutant.
     * Inside each Test/Mutant menu, the method creates a menu item for the stored xmlData.  When the xml menu items
     * are clicked, their data is displayed in a jTextArea.
     * 
     * eg:
     * 
     * popup->		Tests->		Test1->					result
     * 													note
     * 													etc
     * 
     * 													
     * 							Test2->					result
     * 
     * 							MeaningfulTestName->	result
     * 													special_note
     * 	
     * 
     * @param popup		The popup menu
     * @param indexf	The index of where the vertex is located
     */
    public JMenu createListMenuItem(final int indexf){
        JMenu testList;
        if(!pDataM.isMutant()){
            testList = new JMenu("Mutants");
        }else{
            testList = new JMenu("Tests");
        }

        //double loop goes through each xml tag and data item for each test
        //(first loop = goes through tests.  Second loop = goes through data inside test)
        for(int i = 0;i < pDataM.get(indexf).getXml().size();i++){

            //The menu for this specific test is created
            JMenu testMenu = new JMenu(pDataM.get(indexf).getData(i));


            for(int t = 0;t < pDataM.get(indexf).getXml().get(i).getSize();t++){

                //creates a menu item for the xml tag
                JMenuItem dataItem = new JMenuItem(pDataM.get(indexf).getXml().get(i).getXmlTag(t));

                final int iF = i;
                final int tF = t;

                //creates an action listener that displays the data item inside the xml tag
                //when the xml tag menu item is clicked
                dataItem.addActionListener(new ActionListener(){

                    public void actionPerformed(ActionEvent evt){
                        String title = new String("The data inside: " + pDataM.get(indexf).getXml().get(iF).getXmlTag(tF));
                        new DisplayDialog(displayFrame, pDataM.get(indexf).getXml().get(iF).getXmlData(tF), title);
                    }

                });
                testMenu.add(dataItem);
            }
            testList.add(testMenu);


        }
        return testList;

    }


    /**
     * Finds the index of the vertex, and returns it.
     * 
     * @return		integer, location of vertex
     */
    public int findVertexIndex(){

        for(int i = 0;i < pDataM.size();i++){
            String s = new String(pDataM.get(i).getName());
            if(s.equals(vertex.toString())){
                return i;
            }

        }

        //Something is wrong with the data or program if this line is executed.
        return -1;
    }


    /**
     * Creates the menu item that displays all the results for the mutant.  
     * Creates it inside the popup menu.
     * 
     * @param popup		The popup menu
     * @param indexf	The index where the vertex is located
     */
    public JMenuItem createDisplayResultsMenuItem(final int indexf ){
        JMenuItem tempItem = new JMenuItem("Show all results");


        tempItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                displayResultsListener(indexf);
            }
        });


        return tempItem;

    }

    /**
     * Display similarities between 2 nodes in a dialog when an edge is right clicked
     * 
     * 
     */
    public void displaySimilarities(){
        StringBuffer holder = new StringBuffer("");
        //holder.append("similarities:"+ System.getProperty("line.separator")+ System.getProperty("line.separator"));
        boolean breaker = false;
        String title = new String("");
        for(int i = 0;i < pDataM.size();i++){
            for(int t = 0;t < pDataM.size();t++){

                //if statement finds the edge
                if(edge.toString().equals(pDataM.get(i).getName() + " " + pDataM.get(t).getName())){

                    breaker = true;

                    for(int q = 0;q < pDataM.get(t).getSize();q++){

                        //if both pieces of data are "yes", add to the counter
                        if(pDataM.get(i).getResult(q).equals("yes")){
                            if(pDataM.get(t).getResult(q).equals("yes")){
                                holder.append(pDataM.get(t).getData(q)+ System.getProperty("line.separator"));
                            }
                        }
                    }
                    title = new String("The similarities between " + pDataM.get(t).getName() + " and " + pDataM.get(i).getName());
                    break;
                }

            }
            if(breaker){
                break;
            }

        }
        new DisplayDialog(displayFrame, holder.toString(), title);
        //jTextPane1M.setText(holder.toString());
    }

    /**
     * Creates the JMenuItem in the popup that can be clicked to show connections in a second tab
     * 
     * @param popup		The popup menu
     * @param indexf	The index of the vertex
     */
    public JMenuItem createSimilaritiesMenuItem(final int indexf, final MyVertex vertex){
        JMenuItem similaritiesMenuItem = new JMenuItem("Show Connections");

        similaritiesMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(vertex instanceof MyVertex) {
                    displayFrame.updateCanvasConnections((MyVertex)vertex);
                }
            }	
        });

        return similaritiesMenuItem;

    }

    /**
     * creates the JMenuItem that displays the modified source when clicked, and puts
     * it into the popup menu
     * 
     * @param popup		The popup menu
     * @param indexf	The index of the vertex
     */
    public JMenuItem createDisplayModifiedSourceMenuItem(final int indexf){
        JMenuItem modifiedSourceMenuItem = new JMenuItem("Modified Source Code");
        modifiedSourceMenuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String title = new String("This is the modified source code for: " + pDataM.get(indexf).getName());
                new DisplayDialog(displayFrame, pDataM.get(indexf).getModifiedSource(), title);
            }
        });
        return modifiedSourceMenuItem;
    }

    //code completely written by me ends here-----------------------------------------------------

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        JComponent c = (JComponent)e.getSource();
        c.setCursor(cursor);
    }

    public void mouseExited(MouseEvent e) {
        JComponent c = (JComponent)e.getSource();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseMoved(MouseEvent e) {
    }

    /**
     * @return Returns the locked.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked The locked to set.
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
