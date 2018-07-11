package com.greentree.view.viewlog;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import com.greentree.model.business.manager.GreenTreeManager;
import com.greentree.model.services.exception.InvalidTokenException;
import com.greentree.view.MessageDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Defines handlers for <code>{@link LogJFrame}</code>
 *
 * @author david.dietrich
 *
 */
public class LogController implements WindowListener {

    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

    /**
     * This {@link MessageDialog} is used for giving the user error messages,
     * primarily
     */
    private MessageDialog diag;

    /**
     * Attaches a new controller to the given {@link LogJFrame}.
     *
     * @param logJFrame {@link LogJFrame} sending events to this
     * {@link ActionListener}
     */
    public LogController(LogJFrame logJFrame) {
        logJFrame.addWindowListener(this);
    }

    @Override
    public void windowActivated(WindowEvent e) {
        logger.debug("windowActivated");
    }

    @Override
    public void windowClosed(WindowEvent ev) {
        logger.debug("windowClosed");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        logger.debug("windowClosing");
        try {
            GreenTreeManager manager = GreenTreeManager.getInstance();
            manager.logOut();
        } catch (InvalidTokenException | IOException ex) {
            String className = ex.getClass().getName();
            logger.debug(ex.getMessage());

            String msg = "no error message";
            if (className == "InvalidTokenException") {
                msg = "invalid authentication token";
            } else if (className == "ServiceLoadException") {
                msg = "error retrieving storage access";
            }

            diag = new MessageDialog(className, msg);
            diag.setModal(true);
            diag.setVisible(true);
            return;
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        logger.debug("windowDeactivated");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        logger.debug("windowDeiconified");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        logger.debug("windowIconified");
    }

    @Override
    public void windowOpened(WindowEvent e) {
        logger.debug("windowOpened");
    }
}
