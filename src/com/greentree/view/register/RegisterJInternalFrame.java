package com.greentree.view.register;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

/**
 * {@link JInternalFrame} for registering new {@link com.greentree.domain.model.Token} objects
 * from within the {@link MainJFrameDesktop}.
 * 
 * @author david5MX53G
 *
 */
public class RegisterJInternalFrame extends JInternalFrame {
	/** Eclipse generated this long for extending {@link JInternalFrame} */
	private static final long serialVersionUID = -4684132667378900155L;
	
	/** 
	 * These are used to position this {@link JInternalFrame} using <code>setLocation(int, int)
	 * </code>
	 */
	static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
	
	/**
	 * These are the fields to gather input for registering a new <code>{@link com.greentree.model.
	 * domain.Token}</code>
	 */
	private JLabel passLbl = new JLabel("New Passphrase: ");
	private JPasswordField passFld = new JPasswordField(20);
	private JButton submitBtn = new JButton("Submit");
	private JButton cancelBtn = new JButton("Cancel");

    /**
     * constructs a new {@link RegisterJInternalFrame}
     */
    public RegisterJInternalFrame() {
        super("Register", 
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        
        // EXIT_ON_CLOSE would kill the parent window; we don't want that
		// setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // layout JFrame and JComponent objects
		Container cntnr = getContentPane();
		GridBagLayout layout = new GridBagLayout();
		cntnr.setLayout(layout);
		GridBagConstraints cnst = new GridBagConstraints();
		cnst.weightx = 10.0;
		cnst.fill = GridBagConstraints.HORIZONTAL;
		
		// add passphrase label
		cnst.gridx = 0;
		cnst.gridy = 0;
		layout.setConstraints(passLbl, cnst);
		cntnr.add(passLbl);
		
		// add passphrase field
		cnst.gridx = 1;
		cnst.gridy = 0;
		layout.setConstraints(passFld, cnst);
		cntnr.add(passFld, cnst);
		
		// add submit button
		cnst.gridx = 0;
		cnst.gridy = 1;
		cntnr.add(submitBtn, cnst);
		
		// add cancel field
		cnst.gridx = 1;
		cnst.gridy = 1;
		cntnr.add(cancelBtn, cnst);
		
        // Set the window's size and location.
		pack();
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }

	/** 
	 * This should close the <code>{@link RegisterJFrame}</code> <code>{@link JFrame}</code>
	 * 
	 * @return JButton - "Cancel" <code>{@link JButton}</code> 
	 */
	public JButton getCancelBtn() {
		return this.cancelBtn;
	}

	/** 
	 * This should call <code>{@link com.greentree.model.business.manager.GreenTreeManager
	 * #registerToken(String)}</code>
	 * 
	 * @return "Submit" <code>{@link JButton}</code>
	 */
	public JButton getSubmitBtn() {
		return this.submitBtn;
	}

	/**
	 * Returning a password as a <code>String</code> is technically less secure than returning a 
	 * <code>char[]</code> because the former persists in memory longer; however, the fact that an 
	 * unauthorized entity has read access to sensitive data objects in system memory is of far 
	 * greater concern. See <a href="https://stackoverflow.com/questions/8881291/why-is-char-
	 * preferred-over-string-for-passwords">Stackoverflow 8881291</a>.
	 * 
	 * @return <code>{@link CharArray}</code> value of this object's <code>{@link JPasswordField}</code>
	 */
	public char[] getPass() {
		return this.passFld.getPassword();
	}
}
