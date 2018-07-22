package com.greentree.view.authenticate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;

import com.greentree.model.business.manager.GreenTreeManager;
import com.greentree.view.main.MainJFrameController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This handles events from the {@link AuthJInternalFrame} class.
 *
 * @author david5MX53G
 *
 */
public class AuthJInternalController implements ActionListener {

    /**
     * {@link AuthJInternalFrame} sending events to this controller
     */
    AuthJInternalFrame iFrame;

    /**
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

    /**
     * This is used for retrieving the active authentication token
     */
    private MainJFrameController mainController;

    /**
     * @param iFrame {@link AuthJInternalFrame} sending events to this
     * controller
     * @param main {@link MainJFrameController} that saves the authentication
     * token
     */
    public AuthJInternalController(AuthJInternalFrame iFrame, MainJFrameController main) {
        this.iFrame = iFrame;
        this.mainController = main;

        // wire up listeners for the buttons in the tokenPrompt JFrame
        iFrame.getKeyBtn().addActionListener(this);
        iFrame.getSubmitBtn().addActionListener(this);

        logger.debug("initialized");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(iFrame.getKeyBtn())) {
            keyBtn_actionPerformed(e);
        }

        if (e.getSource().equals(iFrame.getSubmitBtn())) {
            submitBtn_actionPerformed(e);
        }
    }

    /**
     * Retrieves the <code>{@link RSAPublicKey}</code> and <code>password</code>
     * from <code>{@link
     * com.greentree.view.iFrame#getPassword()}</code>, then submits these to <code>{@link
     * com.greentree.model.business.manager.GreenTreeManager#registerToken(RSAPublicKey, String)}</code>
     * and sends the resulting <code>{@link java.util.ArrayList}</code> to a new
     * <code>{@link com.greentree.view.ViewLog.LogJFrame}</code>. It's possible
     * to steal the password <code>String
     * </code> from memory, as discussed
     * <a href="https://stackoverflow.com/questions/5238131">here
     * </a>. The first <code>ArrayList</code> entry is skipped because it will
     * always be {@link
     * com.greentree.model.domain.Block#ROOT}.
     *
     * TODO: use char[] array for storing the password to reduce its attack
     * window
     *
     * @param e ActionEvent - used for log messaging
     *
     */
    private void submitBtn_actionPerformed(ActionEvent ev) {
        String msg = null;
        logger.debug("submitBtn_actionPerformed(ActionEvent)");

        // initialize the GreenTreeManager Singleton
        GreenTreeManager manager = GreenTreeManager.getInstance();
        if (!GreenTreeManager.loadProperties()) {
            msg = "missing properties file";
            logger.debug(msg);
            JOptionPane.showInternalMessageDialog(
                this.mainController.getDesktop().getDesktopPane(),
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, mainController.getKey());
            mainController.setCiphertext(
                Base64.encodeBase64String(
                    cipher.doFinal(new String(iFrame.getPassword()).getBytes())
                )
            );

            if (mainController.getCiphertext() != null) {
                if (manager.registerToken(mainController.getKey(),
                    mainController.getCiphertext())) {
                    logger.debug("manager.registerToken(this.key, ciphertext) PASSED");
                    JOptionPane.showInternalMessageDialog(
                        this.mainController.getDesktop().getDesktopPane(),
                        "Login successful",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    iFrame.dispose();
                } else {
                    logger.debug("manager.registerToken(this.key, ciphertext) FAILED");
                    JOptionPane.showInternalMessageDialog(
                        this.mainController.getDesktop().getDesktopPane(),
                        "Login failed",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                throw new Exception(
                    "TokenPromptController encrypted pass is null"
                );
            }
        } catch (Exception ex) {
            logger.debug(ex.getClass().getSimpleName() + " " + ex.getMessage());
            JOptionPane.showInternalMessageDialog(
                this.mainController.getDesktop().getDesktopPane(),
                "Error encrypting pass with key",
                ex.getClass().getName(),
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * handles events from <code>{@link iFrame#getKeyBtn()}</code>.
     *
     * @param ev ActionEvent - used for log messaging
     */
    private void keyBtn_actionPerformed(ActionEvent ev) {
        logger.debug(ev.getActionCommand());

        // Create a file chooser, as shown in "Java Tutorials Code Sample – FileChooserDemo.java"
        JFileChooser fc = new JFileChooser();

        // prompt for a file and store the resulting return state
        int returnVal = fc.showOpenDialog(iFrame);

        // validate the file is an RSAPublicKey, then store it in this controller
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                logger.debug("opened " + file.getName() + ".");
                this.mainController.setKey((RSAPublicKey) input.readObject());
                logger.debug("RSAPublicKey successfully parsed by TokenPromptController");
            } catch (IOException | ClassNotFoundException ex) {
                logger.debug(ex.getMessage());
                JOptionPane.showInternalMessageDialog(
                    this.mainController.getDesktop().getDesktopPane(),
                    "invalid RSAPublicKey file",
                    ex.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            logger.debug("JFileChooser not approved");
        }
    }
}
