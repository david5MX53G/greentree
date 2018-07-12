package com.greentree.model.exception;

/**
 * This <code>{@link Exception}</code> class is thrown when a given passphrase is invalid.
 * 
 * @author david5MX53G
 *
 */
public class InvalidPassException extends Exception {
	/** Eclipse-generated ID for implementing <code>{@link Serializable}</code> */
	private static final long serialVersionUID = 19104921705654340L;

	/** @param msg passed to the <code>Exception</code> super constructor */
	public InvalidPassException (String msg) {
		super("InvalidPassException: " + msg);
	}
}
