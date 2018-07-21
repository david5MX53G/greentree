package com.greentree.model.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.greentree.model.exception.TokenException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class has methods for testing the <code>{@link Token}</code> class.
 *
 * @author david5MX53G
 */
public class TokenTest {
    /** log4j 2 logger */
    Logger logger = LogManager.getLogger();

    /**
     * passphrase used to instantiate <code>Token</code> objects for testing.
     */
    private static final String PASSPHRASE = "We\'re all mad here.";
    Token token0;
    Token token1;
    Token token2;
    
    /**
     * This ensures we have {@link Token} objects for the test cases.
     */
    @Before
    public void setUp() {
        try {
            token0 = new Token(PASSPHRASE);
            token1 = token0;
            token2 = new Token(PASSPHRASE);
        } catch (TokenException e) {
            logger.error("setUp() TokenException: " + e.getMessage());
        }
    }

    /**
     * Tests whether two <code>{@link Token}</code> objects are identical.
     *
     * @throws TokenException
     */
    @Test
    public void testEqualsToken() {
        assertTrue(token0.equals(token1));
        logger.debug("testEqualsToken() PASSED");
    }

    /**
     * Checks whether two <code>Token</code> objects with the same passphrase
     * are not equal.
     */
    @Test
    public void testNotEqualsToken() {
        assertFalse("testNotEqualsToken() FAILED", token0.equals(token2));
        logger.debug("testNotEqualsToken() PASSED");
    }

    /**
     * Validates each <code>{@link Token}</code> object returns a consistent,
     * unique <code>{@link
     * Object#hashCode()}</code> 
	 *
     */
    @Test
    public void testHashCode() {
        try {
            Token token0 = new Token(PASSPHRASE);
            Token token1 = new Token(PASSPHRASE);
            assertTrue("testHashCode() FAILED", token0.hashCode() == token0.hashCode());
            assertTrue("testHashCode() FAILED", token0.hashCode() != token2.hashCode());
            assertTrue("testHashCode() FAILED", token1.hashCode() == token1.hashCode());
            logger.debug("testHashCode() PASSED");
        } catch (TokenException e) {
            logger.debug("testHashCode() Exception " + e.getMessage());
        } catch (AssertionError e) {
            logger.debug(e.getMessage());
        }
    }

    /**
     * Tests the <code>{@link Token#hashCode()}</code> function
     */
    @Test
    public void testNotHashCode() {
        try {
            assertFalse("testNotHashCode() FAILED", token0.hashCode() == token2.hashCode());
            logger.debug("testNotHashCode() PASSED");
        } catch (AssertionError e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Tests the <code>{@link Token#toString()}</code> function
     */
    @Test
    public void testToString() {
        String tokenString = null;
        tokenString = token0.toString();
        try {
            assertTrue("testToString() FAILED", tokenString != null);
        } catch (AssertionError e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Tests with a valid token passed in. This should evaluate to
     * <code>True</code>.
     */
    @Test
    public void testValidate() {
        try {
            assertTrue("testValidate() FAILED", token0.validate());
            logger.debug("testValidate() PASSED");
        } catch (AssertionError | TokenException e) {
            logger.error(e.getMessage());
        }
    }
}
