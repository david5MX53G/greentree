package com.greentree.view.ViewLog;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import com.greentree.model.business.manager.GreenTreeManager;
import com.greentree.model.services.exception.InvalidTokenException;
import com.greentree.view.MessageDialog;

/**
 * Defines handlers for <code>{@link LogJFrame}</code>
 * 
 * @author david.dietrich
 *
 */
public class LogController implements WindowListener {
	/** prefixed to log messages from this class */
	private String logPrefix;
	
	/**
	 * This {@link MessageDialog} is used for giving the user error messages, primarily
	 */
	private MessageDialog diag;
	
	/**
	 * Attaches a new controller to the given {@link LogJFrame}.
	 * 
	 * @param logJFrame {@link LogJFrame} sending events to this {@link ActionListener}
	 */
	public LogController(LogJFrame logJFrame) {
		logPrefix = this.getClass().getName();		
		logJFrame.addWindowListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		System.out.println(logPrefix + ": windowActivated");
	}

	@Override
	public void windowClosed(WindowEvent ev) {
		System.out.println(logPrefix + ": windowClosed");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println(logPrefix + ": windowClosing");
		try {
			GreenTreeManager manager = GreenTreeManager.getInstance();
			manager.logOut();
		} catch (InvalidTokenException | IOException ex) {
			String className = ex.getClass().getName();
			System.out.println(logPrefix + ":" + className + ": " + ex.getMessage());
			
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
		System.out.println(logPrefix + ": windowDeactivated");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println(logPrefix + ": windowDeiconified");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println(logPrefix + ": windowIconified");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println(logPrefix + ": windowOpened");
	}
}
