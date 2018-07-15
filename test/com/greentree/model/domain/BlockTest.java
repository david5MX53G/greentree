package com.greentree.model.domain;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This class defines methods for testing the <code>{@link Block}</code> class.
 * 
 * @author david5MX53G
 *
 */
public class BlockTest {
    /** log4j logger for debug output */
    Logger logger = LogManager.getLogger();
    
    /** passphrase used to instantiate objects for testing. */
    private static final String PASSPHRASE = "We\'re all mad here.";

    /** <code>{@link Token}</code> used to build <code>Block</code> objects */
    private Token tk;

    /** one of three <code>{@link Block}</code> objects against which tests are run */
    private Block block0;

    /** identical to <code>block0</code> */
    private Block block1;

    /** different from <code>block0</code> */
    private Block block2;

    /**
     * Instantiates a new factory used by test methods elsewhere in this class.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        if (tk == null) {
            tk = new Token(PASSPHRASE);
        }
    }

    /** Test method for {@link Block#equals(java.lang.Object)}. */
    @Test
    public void testEquals() {
        block0 = new Block("Tweedle Dee", Block.ROOT, tk);
        block1 = new Block("Tweedle Dee", Block.ROOT, tk);
        try {
            assertTrue("testEquals() FAILED", block0.equals(block1));
            logger.debug("testEquals() PASSED");
        } catch (AssertionError e) {
            fail();
            logger.debug(e.getMessage());
        }
    }

    /** Test method for {@link Block#equals(java.lang.Object)}. */
    @Test
    public void testNotEquals() {
        block1 = new Block("Tweedle Dee", Block.ROOT, tk);
        block2 = new Block("Tweedle Dum", Block.ROOT, tk);
        try {
            assertFalse("testNotEquals() FAILED", block1.equals(block2));
            logger.debug("testNotEquals() PASSED");
        } catch (AssertionError e) {
            fail();
            logger.debug(e.getMessage());
        }
    }

    /** Test method for {@link Block#hashCode()}. */
    @Test
    public void testHashCode() {
        block0 = new Block("Tweedle Dee", Block.ROOT, tk);
        block1 = new Block("Tweedle Dee", Block.ROOT, tk);
        try {
            assertTrue("testHashCode() FAILED", block0.hashCode() == block1.hashCode());
            logger.debug("testHashCode() PASSED");
        } catch (AssertionError e) {
            fail();
            logger.debug(e.getMessage());
        }
    }

    /** Test method for {@link Block#hashCode()}. */
    @Test
    public void testNotHashCode() {
        block1 = new Block("Tweedle Dee", Block.ROOT, tk);
        block2 = new Block("Tweedle Dum", Block.ROOT, tk);
        try {
            assertFalse(block1.hashCode() == block2.hashCode());
            logger.debug("testNotHashCode() PASSED");
        } catch (AssertionError e) {
            fail();
            logger.debug("testNotHashCode() FAILED");
        }
    }

    /** Test method for {@link Block#toString()}. */
    @Test
    public void testToString() {
        block0 = new Block("Tweedle Dee", Block.ROOT, tk);
        String test = block0.toString();
        try {
            assertTrue(test.contains("claims: "));
            assertTrue(test.contains("hash: " + block0.getHash()));
            assertTrue(test.contains("issuer: "));
            assertTrue(test.contains("referee: "));
            assertTrue(test.contains("timestamp: " + String.valueOf(block0.getTimeStamp())));
            logger.debug("testToString() PASSED");
        } catch (AssertionError e) {
            fail();
            logger.debug("testToString() FAILED: " + e.getMessage());
        }
    }	

    /** Test method for {@link Block#validate()}. */
    @Test
    public void testValidate() {
        block0 = new Block("Tweedle Dee", Block.ROOT, tk);
        block1 = new Block("Tweedle Dee", Block.ROOT, tk);
        block2 = new Block("Tweedle Dum", Block.ROOT, tk);
        try {
            assertTrue("testValidate() FAILED", block0.validate());
            assertTrue("testValidate() FAILED", block1.validate());
            assertTrue("testValidate() FAILED", block2.validate());
            logger.debug("testValidate() PASSED");
        } catch (AssertionError e) {
            fail();
            logger.debug(e.getMessage());
        }
    }
    
    /** Test method for {@link Block#CompareTo(Object)}. */
    @Test
    public void testCompareTo() {
        String failmsg = "testCompareTo() FAILED since it did not return ";
        block0 = new Block("Tweedle Dee", Block.ROOT, tk);
        block1 = new Block("Tweedle Dum", block0, tk);
        try {
            assertTrue(failmsg + "-1", block0.compareTo(Block.ROOT) == -1);
            assertTrue(failmsg + "-1", block1.compareTo(block0) == -1);
            assertTrue(failmsg + "0", block0.compareTo(block0) == 0);
            assertTrue(failmsg + "1", block0.compareTo(block1) == 1);
        } catch (AssertionError e) {
            fail();
            logger.error(e.getMessage());
        }
    }
}
