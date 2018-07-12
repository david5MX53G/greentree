package com.greentree.model.business.exception;

/**
 * This <code>{@link Exception}</code> is used by the <code>{@link PropertyManager}</code> when it
 * has problems loading the <code>{@link Properties}</code> object.
 * 
 * @author david5MX53G
 *
 */
public class PropertyFileNotFoundException extends Exception {

	/** Eclipse-generated ID for implementing the <code>{@link Serializable}</code> class */
	private static final long serialVersionUID = 2687769507731414014L;
	
	/**
	 * Throws a new <code>{@link PropertyFileNotFoundException}</code> 
	 * 
	 * @param msg describes the problem so that humans can fix it
	 * @param exp will be thrown to the <code>{@link Exception}</code> constructor
	 */
	public PropertyFileNotFoundException(final String msg, final Throwable exp) {
		super(msg, exp);
	}
}
