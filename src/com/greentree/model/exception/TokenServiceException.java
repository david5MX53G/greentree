/*
 * The MIT License
 *
 * Copyright 2018 david5MX53G.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.greentree.model.exception;

import org.apache.logging.log4j.Logger;

/**
 * This class provides a wrapper for the various exceptions which are thrown by
 * implementation classes of
 * {@link com.greentree.model.services.tokenservice.ITokenService}. This allows
 * new implementations to cast any exceptions to a new
 * {@link TokenServiceException} without modifying the <code>ITokenService
 * </code> interface.
 *
 * @author david5MX53G
 */
public class TokenServiceException extends Exception {
    /** 
     * This is a simple wrapper for instantiating an {@link Exception} object. 
     * It adds the class name of the {@link Throwable} param to the message of 
     * the new <code>Exception</code> object.
     * 
     * @param exp {@link Throwable} object to be re-thrown
     */
    public TokenServiceException(Throwable exp) {
        super(exp.getClass().getSimpleName() + " " + exp.getMessage());
    }

    /**
     * This adds a custom message to a new {@link Exception} object.
     * @param msg {@link String} should be suitable for logging
     * @param exp {@link Throwable} object for creating the new <code>Exception
     * </code>
     */
    public TokenServiceException(String msg, Throwable exp) {
        super(exp.getClass().getSimpleName() + " " + msg);
    }

    /**
     * This adds a custom message to a new {@link Exception} object.
     * @param msg {@link String} should be suitable for logging
     * @param logger {@link org.apache.logging.log4j.Logger} for logging the 
     * error message after re-throwing the {@link Exception}.
     */
    public TokenServiceException(String msg, Logger logger) {
        super(msg);
        logger.error(msg);
    }

    /**
     * ...yet another way to throw {@link TokenServiceException}.
     * 
     * @param message {@link String} for logging
     * @param logger {@link org.apache.logging.log4j.Logger} for writing the msg
     * @param ex {@link Throwable} for logging the original {@link Exception} 
     * class name
     */
    public TokenServiceException(
        String message, Logger logger, Throwable ex
    ) {
        super(ex.getClass().getSimpleName() + " " + ex.getMessage());
        logger.error(ex.getMessage());
    }
}
