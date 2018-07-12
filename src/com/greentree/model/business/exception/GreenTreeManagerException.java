package com.greentree.model.business.exception;

/**
 * used by {@link com.greentree.model.business.manager.GreenTreeManager} to throw {@link Exception}
 * objects. 
 * 
 * @author david5MX53G
 *
 */
public class GreenTreeManagerException extends Exception {

	/** Eclipse generated this long so this class can implement {@link java.io.Serializable} */
	private static final long serialVersionUID = -5623945419507033002L;

	/**
	 * returns a new instance of the {@link GreenTreeManagerException} class. The <code>code</code>
	 * is simply prepended to the <code>msg</code> in the spirit of HTTP or SMTP status codes. This
	 * helps reference the type of error in logs.
	 * 
	 * @param code <code>int</code> uniquely identifying this type of error (e.g., "404" error)
	 * @param msg {@link String} for all the pretty people to see and understand
	 * @param e {@link Throwable} for passing to the {@link Exception} constructor
	 */
	public GreenTreeManagerException(int code, String msg, Throwable e) {
		super(String.valueOf(code) + " " + msg, e);
		System.out.print(this.getMessage());
	}
}
