package temp;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;


/**
 * This class displays a JDialog that displays information contained in specific data items
 * 
 * @author Jeff Falkenham
 *
 */
public class DisplayDialog extends JDialog {

	/**
     * 
     */
    private static final long serialVersionUID = 1;


    /**
	 * If no title is given, this constructor calls the other constructor with the title
	 * field set to ""
	 * 
	 * @param displayFrame		The parent frame
	 * @param text				The text displayed in the JDialog
	 */
	DisplayDialog(DisplayFrame displayFrame, String text){
		this(displayFrame, text, "");
	}
	
	/**
	 * Creates a dialog with a Button to close the dialog, a title, and text displayed
	 * in a JTextArea
	 * 
	 * @param displayFrame		The parent frame
	 * @param text				The text displayed in the TextArea
	 * @param title				The title of the Dialog
	 */
	DisplayDialog(DisplayFrame displayFrame, String text, String title){
				
		super(displayFrame, true);
		this.setTitle(title);
		this.setLayout(new BorderLayout());
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JTextArea jTextAreaText = new JTextArea(text);
		jTextAreaText.setEditable(false);
		JScrollPane jScrollPaneText = new JScrollPane();
		jScrollPaneText.setViewportView(jTextAreaText);
		this.getContentPane().add(jScrollPaneText, BorderLayout.CENTER);
		JButton jButtonOk = new JButton("OK");
		jButtonOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionListenerOkButton();
			}
		});
		
		this.getContentPane().add(jButtonOk, BorderLayout.SOUTH);
		

		this.setSize(new Dimension(400, 480));
		this.setPreferredSize(new Dimension(400, 480));
		this.setMinimumSize(new Dimension(400, 480));
		this.pack();
		this.setLocationRelativeTo(displayFrame);
		this.setVisible(true);		


		
		
		
	}
	
	
	/**
	 * Dispose of the dialog when the button is clicked.
	 * 
	 */
	public void actionListenerOkButton(){
		//this.setVisible(false);
		this.dispose();
	}

}
