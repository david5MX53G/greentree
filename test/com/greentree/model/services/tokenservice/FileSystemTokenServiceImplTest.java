package com.greentree.model.services.tokenservice;

import com.greentree.model.business.exception.TokenServiceException;
import static org.junit.Assert.*;

import java.security.interfaces.RSAPublicKey;
import org.junit.Test;

import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenException;
import com.greentree.model.services.exception.ServiceLoadException;
import com.greentree.model.services.factory.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This defines methods for testing the
 * <code>{@link FileSystemTokenService}</code> class.
 *
 * @author david5MX53G
 *
 */
public class FileSystemTokenServiceImplTest {
    /** log4j 2 logger */
    static final Logger LOGGER = LogManager.getLogger();

    private static Token token;
    private static ITokenService service;

    /**
     * static initializer block for static variables
     */
    static {
        ServiceFactory factory = ServiceFactory.getInstance();
        try {
            service = (ITokenService) factory.getService("ITokenService");
        } catch (ServiceLoadException e) {
            String msg = e.getClass().getName() + " in static init block of "
                + "FileSystemTokenServiceImplTest: " + e.getMessage();
            LOGGER.error(msg);
        }

        try {
            token = new Token("My name is Alice.");
        } catch (TokenException e) {
            String msg = e.getClass().getName() + " in static init block of "
                + "FileSystemTokenServiceImplTest: " + e.getMessage();
            LOGGER.error(msg);
        }

    }

    /**
     * Test method for
     * <code>{@link FileSystemTokenServiceImpl#commit(Token)}</code>.
     */
    @Test
    public void testCommit() {
        try {
            service.commit(token);
            LOGGER.debug("commit(token) test PASSED");
        } catch (TokenServiceException e) {
            fail("testCommit() FAILED: " + e.getMessage());
        }
    }

    /**
     * Test method for
     * <code>{@link FileSystemTokenServiceImpl#selectToken(RSAPublicKey)}</code>.
     */
    @Test
    public void testSelectToken() {
        // commit token
        try {
            service.commit(token);
        } catch (TokenServiceException e) {
            fail(e.getMessage());
        }

        // get public key and delete token
        RSAPublicKey key = token.getPublicKey();
        token = null;

        // re-build token from commit using public key
        try {
            token = service.selectToken(key);
        } catch (TokenServiceException e) {
            fail(
                "selectToken(RSAPublicKey) FAILED: " 
                    + e.getClass().getName() + ": "
                    + e.getMessage()
            );
        }

        try {
            if (token.validate()) {
                assertTrue(true);
                LOGGER.debug("testSelectToken() PASSED");
            } else {
                fail("selectToken(RSAPublicKey) FAILED");
            }
        } catch (TokenException e) {
            fail("selectToken(RSAPublicKey) FAILED: " + e.getMessage());
        }

    }

}
