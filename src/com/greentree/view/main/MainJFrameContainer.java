package com.greentree.view.main;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * provides options to register, authenticate, view a log, and add a message.
 * 
 * @author david.dietrich
 *
 */
public class MainJFrameContainer extends JFrame {
	/** Eclipse generated this long so the class can implement {@link java.io.Serializable} */
	private static final long serialVersionUID = -7983998605015556125L;
	
	/** {@link Container} and {@link GridBagLayout} members */
	Container cPane = getContentPane();
	GridBagLayout gBagL = new GridBagLayout();
    GridBagConstraints gBagC = new GridBagConstraints();
	
	/** Register button */
	private JButton regJButton = new JButton("Register");
	
	/** Login button */
	private JButton authJButton = new JButton("Authenticate");
	
	/** View Log button */
	private JButton logJButton = new JButton("View Log");
	
	/** Add Message button */
	private JButton msgJButton = new JButton("New Message");
	
	/** 
	 * implements a new {@link MainJFrameContainer} object without a controller. Pass the new <code>
	 * MainJFrame</code> to a {@link MainJFrameController} to assign event handlers. 
	 */
	public MainJFrameContainer() {
		// build a basic JFrame and set up the container
		super("Main Menu");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		cPane.setLayout(gBagL);
		gBagC.weightx = 10.0;
		gBagC.ipadx = 200;
		gBagC.fill = GridBagConstraints.HORIZONTAL;
		
		// add a button for registering a new key
		gBagC.gridx = 0;
		gBagC.gridy = 0;
		gBagC.anchor = GridBagConstraints.CENTER;
		gBagL.setConstraints(regJButton, gBagC);
		cPane.add(regJButton, gBagC);
		
		// add a button for authenticating with an old key
		gBagC.gridx = 0;
		gBagC.gridy = 1;
		gBagC.anchor = GridBagConstraints.CENTER;
		gBagL.setConstraints(authJButton, gBagC);
		cPane.add(authJButton, gBagC);
		
		// add a button for viewing the log messages of a key
		gBagC.gridx = 0;
		gBagC.gridy = 2;
		gBagC.anchor = GridBagConstraints.CENTER;
		gBagL.setConstraints(logJButton, gBagC);
		cPane.add(logJButton, gBagC);
		
		// add a button for adding a message to the log of a key
		gBagC.gridx = 0;
		gBagC.gridy = 3;
		gBagC.anchor = GridBagConstraints.CENTER;
		gBagL.setConstraints(msgJButton, gBagC);
		cPane.add(msgJButton, gBagC);
		
		// size and draw the frame on screen
		pack();
		setVisible(true);
	}

    /**
     * @return {@link JButton} regJButton = new JButton("Register");
     */
    public JButton getRegJButton() {
    	return this.regJButton;
    }
    
    /**
     * @return {@link JButton} loginJButton = new JButton("Authenticate");
     */
    public JButton getAuthJButton() {
    	return this.authJButton;
    }
    
    /**
     * @return {@link JButton} logJButton = new JButton("View Log");
     */
    public JButton getLogJButton() {
    	return this.logJButton;
    }
    
    /**
     * @return {@link JButton} msgJButton = new JButton("New Message");
     */
    public JButton getMsgJButton() {
    	return this.msgJButton;
    }
}
