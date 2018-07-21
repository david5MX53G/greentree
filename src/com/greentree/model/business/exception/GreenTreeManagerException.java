package com.greentree.model.business.exception;

import org.apache.logging.log4j.Logger;

/**
 * This class is used by
 * {@link com.greentree.model.business.manager.GreenTreeManager} to obfuscate 
 * the implementation details of lower architectural layers from the 
 * presentation layer.
 *
 * @author david5MX53G
 *
 */
public class GreenTreeManagerException extends Exception {

    /**
     * Eclipse generated this long so this class can implement
     * {@link java.io.Serializable}
     */
    private static final long serialVersionUID = -5623945419507033002L;
    
    /** 
     * This is the only information passed to other layers. More details are 
     * logged using {@link org.apache.logging.log4j.Logger}.
     */
    private static final String MSG = "An error has occurred";

    /**
     * This rethrows an {@link Exception} object, prepending the name of the
     * {@link Throwable} param and logging the message using the given
     * {@link org.apache.logging.log4j.Logger}.
     *
     * @param msg {@link String} for all the pretty people to see and understand
     * @param log {@link org.apache.logging.log4j.Logger}. prints error message
     * @param e {@link Throwable} gets passed to the super constructor
     */
    public GreenTreeManagerException(String msg, Logger log, Throwable e) {
        super(MSG);
        log.error(e.getClass().getSimpleName() + " " + msg);
    }

    /**
     * This rethrows an {@link Exception} object, logging the message using the
     * given {@link org.apache.logging.log4j.Logger}.
     *
     * @param msg {@link String} for all the pretty people to see and understand
     * @param log {@link java.util.logging.Logger} prints error message
     */
    public GreenTreeManagerException(String msg, Logger log) {
        super(MSG);
        log.error(msg);
    }
}
