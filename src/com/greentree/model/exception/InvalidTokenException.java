/**
 * 
 */
package com.greentree.model.exception;

/**
 * This <code>Exception</code> should be thrown when <code>{@link Token#validate()}</code> returns
 * false. 
 * 
 * @author david5MX53G
 *
 */
public class InvalidTokenException extends Exception {

	/**
	 * This is used by <code>{@link java.io.Serializable}</code> for version control.
	 */
	private static final long serialVersionUID = 7703052563687357970L;
	
	/**
	 * Returns the given message string to the <code>{@link Exception}</code> constructor
	 * 
	 * @param msg passed to the <code>Exception</code> constructor
	 * @param err <code>{@link Throwable}</code> passed to the <code>Exception</code> constructor
	 */
	public InvalidTokenException (String msg, Throwable err) {
		super(msg, err);
	}

}
