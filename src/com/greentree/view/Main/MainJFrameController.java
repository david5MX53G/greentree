package com.greentree.view.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;

import javax.swing.JFileChooser;

import com.greentree.model.business.exception.GreenTreeManagerException;
import com.greentree.model.business.manager.GreenTreeManager;
import com.greentree.view.MessageDialog;
import com.greentree.view.AddMsg.AddMsgController;
import com.greentree.view.AddMsg.AddMsgJFrame;
import com.greentree.view.AddMsg.AddMsgJInternalController;
import com.greentree.view.AddMsg.AddMsgJInternalFrame;
import com.greentree.view.Authenticate.AuthController;
import com.greentree.view.Authenticate.AuthJFrame;
import com.greentree.view.Authenticate.AuthJInternalController;
import com.greentree.view.Authenticate.AuthJInternalFrame;
import com.greentree.view.Register.RegisterController;
import com.greentree.view.Register.RegisterJFrame;
import com.greentree.view.Register.RegisterJInternalFrame;
import com.greentree.view.ViewLog.LogController;
import com.greentree.view.ViewLog.LogJFrame;
import com.greentree.view.ViewLog.LogJInternalController;
import com.greentree.view.ViewLog.LogJInternalFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * consumes events from the {@link MainJFrameContainer}
 *
 * @author david.dietrich
 *
 */
public class MainJFrameController implements ActionListener {

    /**
     * {@link MainJFrameContainer} sending events to this controller
     */
    private MainJFrameContainer jFrameContainer;

    /**
     * {@link MainJFrameContainer} sending events to this controller
     */
    private MainJFrameDesktop jFrameDesktop;

    /**
     * for sending error messages and such to the user
     */
    private MessageDialog diag;

    /**
     * {@link GreenTreeManager} for managing data objects
     */
    private GreenTreeManager manager = GreenTreeManager.getInstance();

    /**
     * This {@link RSAPublicKey} is used to instantiate {@link Token} objects
     */
    private RSAPublicKey key;

    /**
     * This {@link String} is used to instantiate {@link Token} objects
     */
    private String ciphertext;

    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

    /**
     * Registers handlers for the given <code>{@link MainJFrameContainer}</code>
     *
     * @param jFrame - <code>AuthJFrame</code> object that will receive handlers
     * from this
     */
    public MainJFrameController(MainJFrameContainer jFrame) {
        this.jFrameContainer = jFrame;
        String msg;

        // wire up listeners for the buttons in the JFrame
        jFrame.getAuthJButton().addActionListener(this);
        jFrame.getLogJButton().addActionListener(this);
        jFrame.getMsgJButton().addActionListener(this);
        jFrame.getRegJButton().addActionListener(this);

        if (!manager.registerService("TokenService")) {
            msg = "manager.registerService(\"TokenService\") was unsuccessful";
            logger.debug(msg);
            diag = new MessageDialog("Error 0338", msg);
        } else {
            logger.debug("manager and listeners registered");
        }
    }

    /**
     * constructs a new {@link MainJFrameController} with listeners attached to
     * the menu items of the given {@link MainJFrameDesktop}.
     *
     * @param jFrame {@link MainJFrameDesktop} sending action events to this
     * controller and receiving {@link JInternalFrame} objects from it in
     * response
     */
    public MainJFrameController(MainJFrameDesktop jFrame) {
        this.jFrameDesktop = jFrame;
        this.jFrameDesktop.setVisible(true);

        // wire up listeners for the buttons in the JFrame
        jFrame.getAuthMI().addActionListener(this);
        jFrame.getRegMI().addActionListener(this);
        jFrame.getNewMI().addActionListener(this);
        jFrame.getLogMI().addActionListener(this);

        if (!manager.registerService("TokenService")) {
            String msg = "manager.registerService(\"TokenService\") was unsuccessful";
            logger.debug(msg);
            diag = new MessageDialog("Error 0927", msg);
        } else {
            logger.debug("manager and listeners registered");
        }
    }

    /**
     * spawns a new JFrame appropriate to the
     * {@link MainJFrameContainer} {@link JButton} pressed
     */
    @Override
    public void actionPerformed(ActionEvent aev) {
        logger.debug("actionPerformed(ActionEvent) " + aev.getActionCommand());

        if (this.jFrameContainer instanceof MainJFrameContainer) {
            // spawn an authentication JFrame, if the login button was pressed
            if (aev.getSource().equals(jFrameContainer.getAuthJButton())) {
                logger.debug("new AuthJFrame()");
                new AuthController(new AuthJFrame(), this);
            };

            // spawn a view log JFrame, if the view log button was pressed
            if (aev.getSource().equals(jFrameContainer.getLogJButton())) {
                logButton_action(aev);
            };

            // spawn a new message JFrame, if the new message button was pressed
            if (aev.getSource().equals(jFrameContainer.getMsgJButton())) {
                logger.debug("new AddMsgController(new AddMsgJFrame());");
                new AddMsgController(new AddMsgJFrame(), this);
            };

            // spawn a new key registration JFrame, if the register button was pressed
            if (aev.getSource().equals(jFrameContainer.getRegJButton())) {
                logger.debug("new RegisterController(new RegisterJFrame());");
                new RegisterController(new RegisterJFrame());
            }
        } else if (this.jFrameDesktop instanceof MainJFrameDesktop) {
            // add a RegisterJInternalFrame to the JDesktopPane on the register menu item action
            if ("reg".equals(aev.getActionCommand())) {
                logger.debug("new RegisterJInternalFrame();");
                RegisterJInternalFrame iFrame = new RegisterJInternalFrame();
                new RegisterController(iFrame);
                iFrame.setVisible(true);
                this.jFrameDesktop.add(iFrame);
                try {
                    iFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
            }

            // add a RegisterJInternalFrame to the JDesktopPane on the register menu item action
            if ("auth".equals(aev.getActionCommand())) {
                logger.debug("new AuthJInternalFrame();");
                AuthJInternalFrame iFrame = new AuthJInternalFrame();
                new AuthJInternalController(iFrame, this);
                iFrame.setVisible(true);
                this.jFrameDesktop.add(iFrame);
                try {
                    iFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
            }

            // add a RegisterJInternalFrame to the JDesktopPane on the register menu item action
            if ("log".equals(aev.getActionCommand())) {
                logButton_action(aev);
            }

            // add a new AddMsgJInternalFrame to the JDesktopPane on the new msg menu item action
            if ("new".equals(aev.getActionCommand())) {
                logger.debug("new AddMsgJInternalController;");
                AddMsgJInternalFrame iFrame = new AddMsgJInternalFrame();
                new AddMsgJInternalController(iFrame, this);
                iFrame.setVisible(true);
                this.jFrameDesktop.add(iFrame);
                try {
                    iFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
            }
        } else {
            logger.debug("missing JFrame;");
        }
    }

    /**
     * prompts the user for an {@link RSAPublicKey}, then reads all the
     * available messages for the associated {@link Token} using the
     * <code>Token</code> registered in the {@link
     * GreenTreeManager} Singleton of this {@link MainJFrameController}.
     *
     * @param aev {@link ActionEvent} isn't used for anything
     */
    private void logButton_action(ActionEvent aev) {
        logger.debug("" + aev.getActionCommand());

        // Create a file chooser, as shown in "Java Tutorials Code Sample – FileChooserDemo.java"
        JFileChooser fc = new JFileChooser();
        int returnVal;

        // prompt for a file and store the resulting return state
        if (this.jFrameContainer instanceof MainJFrameContainer) {
            returnVal = fc.showOpenDialog(jFrameContainer);
        } else if (this.jFrameDesktop instanceof MainJFrameDesktop) {
            returnVal = fc.showOpenDialog(jFrameDesktop);
        } else {
            logger.debug("could not get state from JFileChooser");
            return;
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            logger.debug("opening " + file.getName() + ".");

            // load a new frame with the log messages from the chosen key
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                RSAPublicKey key = (RSAPublicKey) input.readObject();
                logger.debug("RSAPublicKey successfully parsed");

                // get the active token and load the log for it
                if (manager.registerToken(this.key, this.ciphertext)) {
                    Iterator<String> it = manager.getData(key).iterator();

                    // use LogJFrame, if MainJFrameContainer is active
                    if (this.jFrameContainer instanceof MainJFrameContainer) {
                        new LogController(new LogJFrame(it));
                    } // use LogJInternalFrame, if MainJFrameDesktop is active
                    else if (this.jFrameDesktop instanceof MainJFrameDesktop) {
                        LogJInternalFrame iFrame = new LogJInternalFrame(it);
                        new LogJInternalController(iFrame);
                        iFrame.setVisible(true);
                        this.jFrameDesktop.add(iFrame);
                        try {
                            iFrame.setSelected(true);
                        } catch (java.beans.PropertyVetoException e) {
                        }
                    } // this should seriously never happen...
                    else {
                        new MessageDialog("Error", "failed to get parent window");
                    }
                } else {
                    new MessageDialog("Error", "failed to register authentication token");
                }
            } // the manager will throw an exception if it doesn't have a token registered yet
            catch (IOException | ClassNotFoundException
                | GreenTreeManagerException | NullPointerException ex) {
                String msg;
                logger.debug(ex.getClass().getName() + ": " + ex.getMessage());
                if (!(this.key instanceof RSAPublicKey)) {
                    msg = "no key authenticated";
                } else {
                    msg = ex.getMessage();
                }
                diag = new MessageDialog(ex.getClass().getSimpleName(), "Error 0339: " + msg);
                diag.setModal(true);
                diag.setVisible(true);
            }
        } else {
            logger.debug("Open command cancelled by user.");
        }
    }

    /**
     * @param string {@link String} used to instantiate {@link Token} objects
     */
    public void setCiphertext(String string) {
        this.ciphertext = string;
    }

    /**
     * @return {@link String} ciphertext to authenticate {@link Token} objects
     */
    public String getCiphertext() {
        return this.ciphertext;
    }

    /**
     * @return {@link RSAPublicKey} used with ciphertext to auth {@link Token}
     * objects
     */
    public RSAPublicKey getKey() {
        return this.key;
    }

    /**
     * @param key {@link RSAPublicKey} used with ciphertext to authenticate a
     * {@link Token}
     */
    public void setKey(RSAPublicKey key) {
        this.key = key;
    }
}
