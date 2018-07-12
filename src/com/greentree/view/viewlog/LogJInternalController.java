package com.greentree.view.viewlog;

import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Defines handlers for <code>{@link LogJFrame}</code>
 *
 * @author david5MX53G
 *
 */
public class LogJInternalController implements ComponentListener {
    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

    /**
     * attaches a new controller to the given {@link LogJInternalFrame}.
     * Frankly, this doesn't do much.
     *
     * @param iFrame {@link LogJInternalFrame} sending events to this
     * {@link ActionListener}
     */
    public LogJInternalController(LogJInternalFrame iFrame) {
        iFrame.addComponentListener(this);
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
        logger.debug("componentHidden");
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
        logger.debug("componentResized");
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
        logger.debug("componentShown");
    }
}
