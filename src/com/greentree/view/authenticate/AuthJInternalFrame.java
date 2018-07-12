package com.greentree.view.authenticate;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This {@link JInternalFrame} is used to authenticate with an existing key.
 *
 * @author david5MX53G
 *
 */
public class AuthJInternalFrame extends JInternalFrame {

    /**
     * Eclipse generated this long so this class can extend JInternalFrame
     */
    private static final long serialVersionUID = 2336510016862396782L;

    /**
     * The key is used to look up which {@link com.greentree.model.domain.Token}
     * will be authenticated.
     */
    private JLabel keyLbl = new JLabel("Key:");
    private JButton keyBtn = new JButton("Open key file...");

    /**
     * The passphrase will be validated by the selected
     * {@link com.greentree.model.domain.Token}
     */
    private JLabel passLbl = new JLabel("Passphrase:");
    private JPasswordField passFld = new JPasswordField(20);

    /**
     * This triggers authentication.
     */
    private JButton submitBtn = new JButton("Submit");

    /**
     * These are used to position this {@link JInternalFrame} using <code>setLocation(int, int)
     * </code>
     */
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    
    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

    /**
     * builds a new {@link AuthJInternalFrame} with
     * {@link Container}, {@link GridBagLayout}, and {@link GridBagConstraints}
     * members.
     */
    public AuthJInternalFrame() {
        super("Authenticate",
            true, //resizable
            true, //closable
            true, //maximizable
            true);//iconifiable
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // set up content pane, layout, and constraints
        Container cPane = getContentPane();
        GridBagLayout gBagL = new GridBagLayout();
        GridBagConstraints gBagC = new GridBagConstraints();
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

        // set the window size and location		
        pack();
        setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
        setVisible(true);
        logger.debug("instantiated successfully");
    }

    /**
     * @return "Open key file..." <code>{@link JButton}</code>
     */
    public JButton getKeyBtn() {
        return this.keyBtn;
    }

    /**
     * @return "Submit" <code>{@link JButton}</code>
     */
    public JButton getSubmitBtn() {
        return this.submitBtn;
    }

    /**
     * Returns the char[] value of the password field. A String is not used
     * because it is immutable and remains in memory as garbage for an
     * indeterminate time. See "What are the security reasons for
     * JPasswordField.getPassword()?" (Stackoverflow).
     *
     * @return char[] - password field value; any variable holding this should
     * be zeroed after use as shown in "How to Use Password Fields" (Oracle,
     * "The Java Tutorials")
     */
    public char[] getPassword() {
        return passFld.getPassword();
    }
}
