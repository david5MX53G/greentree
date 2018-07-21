package com.greentree.model.exception;

import org.apache.logging.log4j.Logger;

/**
 * This class is used by the <code>{@link Token}</code> class to consolidate
 * various <code>{@link Exception}</code> types so that code invoking
 * <code>Token</code> methods only need handle one class.
 *
 * @author david5MX53G
 *
 */
public class TokenException extends Exception {
    /**
     * Auto-generated value for implementing <code>{@link Serializable}</code>
     */
    private static final long serialVersionUID = -2850294554342062924L;

    /**
     * Constructor for the <code>{@link TokenException}</code> class.
     *
     * @param msg describes what went wrong in human terms
     * @param err <code>{@link Throwable}</code> object passed to the
     * <code>{@link Exception}</code> constructor
     * @param logger {@link org.apache.logging.log4j.Logger} for log messaging
     */
    public TokenException(String msg, Throwable err, Logger logger) {
        super(msg, err);
        logger.error(err.getClass().getSimpleName() + " " + msg);
    }

    /**
     * This constructor assumes that logging is done elsewhere
     * @param msg {@link String} for debugging
     */
    public TokenException(String msg) {
        super(msg);
    }

    /**
     * 
     * @param msg {@link String} used for logging and the Exception(String) 
     * constructor
     * 
     * @param logger {@link org.apache.logging.log4j.Logger} for debug output
     */
    public TokenException(String msg, Logger logger) {
        super(msg);
        logger.error(msg);
    }
}
