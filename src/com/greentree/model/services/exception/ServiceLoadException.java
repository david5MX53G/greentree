/**
 * 
 */
package com.greentree.model.services.exception;

/**
 * This is used to handle <code>{@link Exception}</code> objects thrown in the 
 * <code>{@link ServiceFactory}</code> class.
 * 
 * @author david.dietrich
 *
 */
@SuppressWarnings("serial")
public class ServiceLoadException extends Exception {

	/**
	 * @param msg sent to the <code>{@link Exception}</code> constructor
	 * @param exc re-thrown to the <code>Exception</code> constructor
	 */
	public ServiceLoadException(final String msg, final Throwable exc) {
		super(msg, exc);
	}
}
