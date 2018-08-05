package com.greentree.model.services.tokenservice;

import com.greentree.model.exception.TokenServiceException;
import static org.junit.Assert.*;

import java.security.interfaces.RSAPublicKey;
import org.junit.Test;

import com.greentree.model.domain.Token;
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

    /**
     * log4j 2 logger
     */
    static final Logger LOGGER = LogManager.getLogger();

    /**
     * This {@link Token} variable will be committed and read from file system
     * storage.
     */
    private static Token token;

    /**
     * This {@link ITokenService} will be used to store and retrieve the Token.
     */
    private static ITokenService service;

    /**
     * This is used to instantiate a new FileSystemTokenServiceImpl instance
     * using a Class type erasure rather than the Class type specified in the
     * application properties file.
     */
    private static final String CLASSNAME
        = "com.greentree.model.services.tokenservice.FileSystemTokenServiceImpl";

    /**
     * static initializer block for static variables
     */
    static {
        try {
            Class<?> classy = Class.forName(CLASSNAME);
            service = (ITokenService) classy.newInstance();
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex) {
            fail(ex.getClass().getSimpleName() + " " + ex.getMessage());
        }

        token = new Token("My name is Alice.");
        if (token instanceof Token) {
            LOGGER.debug("Token instantiated successfully");
        } else {
            LOGGER.error(
                "static initialization block failed to initialize Token"
            );
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

        if (token.validate()) {
            assertTrue(true);
            LOGGER.debug("testSelectToken() PASSED");
        } else {
            fail("selectToken(RSAPublicKey) FAILED");
        }

    }

}
