package com.greentree.view.AddMsg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;

import javax.swing.JFileChooser;

import com.greentree.model.business.manager.GreenTreeManager;
import com.greentree.view.MessageDialog;
import com.greentree.view.Main.MainJFrameController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * consumes events from the {@link AddMsgJFrame}.
 *
 * @author david.dietrich
 *
 */
public class AddMsgController implements ActionListener {
    /** {@link org.apache.logging.log4j.Logger} logger */
    Logger logger = LogManager.getLogger();

    /**
     * {@link AddMsgJFrame} sending events to this controller
     */
    private AddMsgJFrame addMsgJFrame;

    /**
     * for sending error messages and such to the user
     */
    private MessageDialog diag;

    /**
     * {@link GreenTreeManager} for managing data objects
     */
    private GreenTreeManager manager = GreenTreeManager.getInstance();

    /**
     * {@link AddMsgJFrame} sends events to this controller
     */
    private MainJFrameController mainCtrl;

    /**
     * {@link RSAPublicKey} which is given read access to the message
     */
    private RSAPublicKey toKey;

    /**
     * @param jFrame {@link AddMsgJFrame} sends events to this
     * {@link AddMsgController}
     * @param main {@link MainJFrameController} which provides the active
     * session key
     */
    public AddMsgController(AddMsgJFrame jFrame, MainJFrameController main) {
        if (main.getKey() == null) {
            logger.debug("error 0957: must be authenticated first");
            diag = new MessageDialog("Error 0957", "must be authenticated first");
            diag.setVisible(true);
            this.addMsgJFrame.dispose();
        } else {
            this.addMsgJFrame = jFrame;
            this.mainCtrl = main;
            addMsgJFrame.getKeyBtn().addActionListener(this);
            addMsgJFrame.getSubmitBtn().addActionListener(this);
            logger.debug("initialized");
        }
    }

    @Override
    public void actionPerformed(ActionEvent aev) {
        if (aev.getSource().equals(addMsgJFrame.getKeyBtn())) {
            keyBtnAction();
        };

        if (aev.getSource().equals(addMsgJFrame.getSubmitBtn())) {
            submitBtnAction();
        };
    }

    /**
     * marshals data from message fields and adds a
     * {@link com.greentree.model.domain.Block}
     */
    private void submitBtnAction() {
        String diagTitle = null;
        String diagText = null;
        GregorianCalendar expires = new GregorianCalendar();
        String msg = this.addMsgJFrame.getMsgFld();
        boolean success;

        if (this.addMsgJFrame.getHours() > 0) {
            expires.add(GregorianCalendar.HOUR, this.addMsgJFrame.getHours());
        } else {
            new MessageDialog("Error", "invalid time input");
        }

        success = manager.registerService("TokenService");
        if (!success) {
            new MessageDialog("Error", "failed to register storage service");
        }

        try {
            if (!manager.registerToken(mainCtrl.getKey(), mainCtrl.getCiphertext())) {
                new MessageDialog("Error", "failed to retrieve authenticated token");
            } else if (!manager.addBlock(msg, toKey, new Date().getTime(), expires.getTimeInMillis())) {
                new MessageDialog("Error", "failed to add message");
            } else {
                logger.debug("added message, '" + msg + "'");
                diagTitle = "Success";
                diagText = "Message added";
            }
        } catch (InputMismatchException ex) {
            logger.debug(ex.getMessage());
            diagTitle = ex.getClass().getSimpleName();
            diagText = ex.getMessage();
        }
        diag = new MessageDialog(diagTitle, diagText);
        diag.setModal(true);
        diag.setVisible(true);
    }

    private void keyBtnAction() {
        JFileChooser fc = new JFileChooser();

        // prompt for a file and store the resulting return state
        int returnVal = fc.showOpenDialog(this.addMsgJFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            logger.debug("opening " + file.getName());

            // load a new frame with the log messages from the chosen key
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                toKey = (RSAPublicKey) input.readObject();
                logger.debug("RSAPublicKey successfully parsed");
            } catch (Exception ex) {
                logger.debug(ex.getMessage());
            }
        }
    }
}
