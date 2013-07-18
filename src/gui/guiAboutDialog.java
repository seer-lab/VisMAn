package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class is used to store and display the information pertaining to
 * the VisMAN program and its developers.
 * @author David Petras
 *
 */
public class guiAboutDialog extends JDialog {
	
	//Constants
	private static final int DIALOG_WIDTH = 400;
	private static final int DIALOG_HEIGHT = 180;
	
	private Color backgroundColor;
	
	//GUI Components
	private JLabel titleLabel;
	private JPanel titlePanel;
	
	private JPanel infoPanel;
	private JEditorPane infoArea;
	
	private JPanel linkPanel;
	private JButton linkButton;
	
	

	
	public guiAboutDialog(JFrame owner)
	{
		super(owner,"About VisMAN");
		initializeDialog();
	}
	
	/**
	 * This method will initialize the dialog window, add
	 * all of the components, and provide the action listeners
	 * for the components.
	 */
	private void initializeDialog()
	{
		backgroundColor = this.getBackground();
		//Set the size of the dialog box and do not allow for it to be changed.
		this.setSize(new Dimension(DIALOG_WIDTH,DIALOG_HEIGHT));
		this.setResizable(false);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		
		//Create and add a label to display the name and short description of the program.
		titleLabel = new JLabel();
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		titleLabel.setText("<HTML><B><U>VisMAN - Visualization of Mutation Testing</U></B></HTML>");
		titlePanel = new JPanel();
		titlePanel.add(titleLabel,BorderLayout.CENTER);
		
		this.getContentPane().add(titlePanel);
		
		//Use a flow layout to separate the credits and the logo.
		infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER,40,0));
		
		infoArea = new JEditorPane();
		infoArea.setContentType("text/html");
		infoArea.setEditable(false);
		infoArea.setBackground(backgroundColor);
		infoArea.setFont(new Font("Arial",Font.PLAIN,14));
		infoArea.setText("<B>Credits:</B><BR>David Petras<BR>Dr. Jeremy Bradbury<BR>Jeff Falkenham");
		
		infoPanel.add(infoArea);

		URL imageURL = guiAboutDialog.class.getResource("logo.png");
		JLabel logoLabel = new JLabel(new ImageIcon(imageURL));
		logoLabel.setMaximumSize(new Dimension(356,110));
		
		
		infoPanel.add(logoLabel);
		
		this.getContentPane().add(infoPanel);
		
		//Add a link to the SQR Lab website.
		linkPanel = new JPanel();
		linkButton = new JButton();
		linkButton.setText("http://faculty.uoit.ca/bradbury/sqrlab/");
		linkButton.setOpaque(false);
		linkButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				URI website;
				try 
				{
					website = new URI("http://faculty.uoit.ca/bradbury/sqrlab/");
					Desktop.getDesktop().browse(website);
				} catch (URISyntaxException e) 
				{
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		linkPanel.add(linkButton,BorderLayout.CENTER);
		
		this.getContentPane().add(linkPanel);
	}
}
