package com.greentree.view.authenticate;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import com.greentree.model.business.manager.GreenTreeManager;

/**
 * This is the main <code>{@link JFrame}</code> for registering and authenticating with 
 * <code>{@link Token}</code> objects. The "PropertyFileNotFoundException" error occurs when the 
 * app is launched without the -Dprop_location="path/to/application.properties",
 * 
 * @author david.dietrich
 *
 */
public class AuthJFrame extends JFrame {

	/**
	 * Eclipse-generated serial number for extending <code>{@link java.io.Serializable}</code>
	 */
	private static final long serialVersionUID = 5244184069990011946L;
	
	/** 
	 * The key is used to look up which {@link com.greentree.model.domain.Token} will be 
	 * authenticated.
	 */
	private JLabel keyLbl = new JLabel("Key:");
	private JButton keyBtn = new JButton("Open key file...");
	
	/** 
	 * The passphrase will be validated by the selected {@link com.greentree.model.domain.Token}
	 */
	private JLabel passLbl = new JLabel("Passphrase:");
	private JPasswordField passFld = new JPasswordField(20);
	
	/**  This triggers authentication. */
	private JButton submitBtn = new JButton("Submit");

	/** {@link Container}, {@link GridBagLayout}, and {@link GridBagConstraints} members */
	Container cPane = getContentPane();
	GridBagLayout gBagL = new GridBagLayout();
	GridBagConstraints gBagC = new GridBagConstraints();
	
	/**
	 * Creates a new UI which prompts the user to create or submit a <code>{@link RSAPublicKey}
	 * </code> and passphrase. These will be used to manage a <code>{@link Token}</code> object for
	 * the user.
	 * 
	 * @param manager {@link GreenTreeManager} to authenticate and store the <code>Token</code>
	 */
	public AuthJFrame() {
		super("Authenticate");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cPane.setLayout(gBagL);
		gBagC.weightx = 10.0;
		gBagC.fill = GridBagConstraints.HORIZONTAL;
		
		// add key label
		gBagC.gridx = 0;
		gBagC.gridy = 0;
		gBagC.anchor = GridBagConstraints.EAST;
		gBagL.setConstraints(keyLbl, gBagC);
		cPane.add(keyLbl);
		
		// add key button
		gBagC.gridx = 1;
		gBagC.gridy = 0;
		gBagC.anchor = GridBagConstraints.CENTER;
		gBagL.setConstraints(keyBtn, gBagC);
		cPane.add(keyBtn, gBagC);
		
		// add passphrase label
		gBagC.gridx = 0;
		gBagC.gridy = 1;
		gBagC.anchor = GridBagConstraints.EAST;
		cPane.add(passLbl, gBagC);
		
		// add passphrase field
		gBagC.gridx = 1;
		gBagC.gridy = 1;
		gBagC.anchor = GridBagConstraints.CENTER;
		cPane.add(passFld, gBagC);
		
		// add submit button
		gBagC.gridx = 1;
		gBagC.gridy = 2;
		gBagC.anchor = GridBagConstraints.CENTER;
		cPane.add(submitBtn, gBagC);
		
		pack();
		setVisible(true);
	}
	
	/** @return "Open key file..." <code>{@link JButton}</code> */
	public JButton getKeyBtn() {
		return this.keyBtn;
	}

	/** @return "Submit" <code>{@link JButton}</code> */
	public JButton getSubmitBtn() {
		return this.submitBtn;
	}
	
	/**
	 * Returns the char[] value of the password field. A String is not used because it is immutable
	 * and remains in memory as garbage for an indeterminate time. See "What are the security 
	 * reasons for JPasswordField.getPassword()?" (Stackoverflow). 
	 * 
	 * @return char[] - password field value; any variable holding this should be zeroed after use 
	 *     as shown in "How to Use Password Fields" (Oracle, "The Java Tutorials")
	 */
	public char[] getPassword() {
		return passFld.getPassword();
	}
}
