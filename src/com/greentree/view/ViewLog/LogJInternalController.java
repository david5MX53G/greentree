package com.greentree.view.ViewLog;

import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Defines handlers for <code>{@link LogJFrame}</code>
 * 
 * @author david.dietrich
 *
 */
public class LogJInternalController implements ComponentListener {
	/** prefixed to log messages from this class */
	private String logPrefix;
	
	/**
	 * attaches a new controller to the given {@link LogJInternalFrame}. Frankly, this doesn't do 
	 * much.
	 * 
	 * @param iFrame {@link LogJInternalFrame} sending events to this {@link ActionListener}
	 */
	public LogJInternalController(LogJInternalFrame iFrame) {
		logPrefix = this.getClass().getSimpleName();		
		iFrame.addComponentListener(this);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		System.out.println(logPrefix + ": componentHidden");
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentResized(ComponentEvent arg0) {
		System.out.println(logPrefix + ": componentResized");
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		System.out.println(logPrefix + ": componentShown");
	}
}
