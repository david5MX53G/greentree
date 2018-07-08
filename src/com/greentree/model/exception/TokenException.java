package com.greentree.model.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used by the <code>{@link Token}</code> class to consolidate
 * various <code>{@link Exception}</code> types so that code invoking
 * <code>Token</code> methods only need handle one class.
 *
 * @author david.dietrich
 *
 */
public class TokenException extends Exception {/** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    Logger logger = LogManager.getLogger();

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
     */
    public TokenException(String msg, Throwable err) {
        super(msg, err);
        logger.error(msg);
    }
}
