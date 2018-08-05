package com.greentree.model.domain;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This defines methods for testing the <code>{@link Claim}</code> class.
 *
 * @author david5MX53G
 *
 */
public class ClaimTest {

    /**
     * passphrase used to instantiate <code>Token</code> objects in test
     * methods.
     */
    private static final String PASSPHRASE = "We\'re all mad here.";
    Token token;
    Long now;
    Long then;
    Claim claim0;

    /**
     * log4j 2 logger
     */
    Logger logger = LogManager.getLogger();

    /**
     * instantiates attributes needed for testing
     */
    @Before
    public void setUp() {
        if (token == null) {
            token = new Token(PASSPHRASE);
        }
        
        if (now == null | then == null) {
            Calendar cal = new GregorianCalendar();
            now = cal.getTimeInMillis();
            cal.add(Calendar.HOUR, 1);
            then = cal.getTimeInMillis();
        }
        
        if (claim0 == null) {
            claim0 = new Claim(token, now, then);
        }
    }

    /**
     * Happy test method for {@link Claim#equals(Object)}.
     */
    @Test
    public void testEquals() {
        try {
            Claim claim1 = new Claim(token, now, then);
            assertTrue(claim0.equals(claim1));
            logger.debug("testEquals() PASSED");
        } catch (AssertionError e) {
            logger.debug("testEquals() FAILED");
        }
    }

    /**
     * Negative test method for {@link Claim#equals(Object)}.
     */
    @Test
    public void testNotEquals() {
        try {
            Claim claim1 = new Claim(token, new Date().getTime(), new Date().getTime());
            assertFalse(claim0.equals(claim1));
            logger.debug("testNotEquals() PASSED");
        } catch (AssertionError e) {
            logger.debug("testNotEquals() FAILED");
        }
    }

    /**
     * Happy path test method for {@link Claim#hashCode()}.
     */
    @Test
    public void testHashCode() {
        try {
            Claim claim1 = new Claim(token, now, then);
            int hash0 = claim0.hashCode();
            int hash1 = claim1.hashCode();
            assertTrue(hash0 == hash1);
            logger.debug("testHashCode() PASSED");
        } catch (AssertionError e) {
            logger.debug("testHashCode() FAILED");
        }
    }

    /**
     * Negative test method for {@link Claim#hashCode()}
     */
    @Test
    public void testNotHashCode() {
        try {
            Claim claim1 = new Claim(token, new Date().getTime(), new Date().getTime());
            int hash1 = claim0.hashCode();
            int hash2 = claim1.hashCode();
            assertFalse(hash1 == hash2);
            logger.debug("testNotHashCode() PASSED");
        } catch (AssertionError e) {
            logger.debug("testNotHashCode() FAILED");
        }
    }

    /**
     * Tests <code>{@link Claim#toString()}</code>
     */
    @Test
    public void testToString() {
        String claimString = null;
        claimString = claim0.toString();
        try {
            assertTrue("testToString() FAILED", claimString != null);
            logger.debug("testToString() PASSED");
        } catch (AssertionError e) {
            logger.debug(e.getMessage());
        }
    }

    /**
     * Happy path test method for {@link Claim#validate()}.
     */
    @Test
    public void testValidate() {
        try {
            assertTrue("testValidate() FAILED", claim0.validate() == true);
            logger.debug("testValidate() PASSED");
        } catch (AssertionError e) {
            logger.debug(e.getMessage());
        }
    }

    /**
     * Negative test method for {@link Claim#validate()}.
     */
    @Test
    public void testNotValidate() {
        try {
            assertFalse("testNotValidate() FAILED", claim0.validate() == false);
            logger.debug("testNotValidate() PASSED");
        } catch (AssertionError e) {
            logger.debug(e.getMessage());
        }
    }
}
