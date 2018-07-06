package com.greentree.model.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This class defines methods for testing the <code>{@link Block}</code> class.
 * 
 * @author david.dietrich
 *
 */
public class BlockTest {
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
		
		if (block0 == null) {
			block0 = new Block("Tweedle Dee", Block.ROOT, tk);
		}
		
		if (block1 == null) {
			block1 = new Block("Tweedle Dee", Block.ROOT, tk);
		}
		
		if (block2 == null) {
			block2 = new Block("Tweedle Dum", Block.ROOT, tk);
		}
	}

	/** Test method for {@link Block#equals(java.lang.Object)}. */
	@Test
	public void testEquals() {
		try {
			assertTrue("testEquals() FAILED", block0.equals(block1));
			System.out.println("testEquals() PASSED");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	/** Test method for {@link Block#equals(java.lang.Object)}. */
	@Test
	public void testNotEquals() {
		try {
			assertFalse("testNotEquals() FAILED", block1.equals(block2));
			System.out.println("testNotEquals() PASSED");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	/** Test method for {@link Block#hashCode()}. */
	@Test
	public void testHashCode() {
		try {
			assertTrue("testHashCode() FAILED", block0.hashCode() == block1.hashCode());
			System.out.println("testHashCode() PASSED");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	/** Test method for {@link Block#hashCode()}. */
	@Test
	public void testNotHashCode() {
		try {
			assertFalse(block1.hashCode() == block2.hashCode());
			System.out.println("testNotHashCode() PASSED");
		} catch (AssertionError e) {
			System.out.println("testNotHashCode() FAILED");
		}
	}
	
	/** Test method for {@link Block#toString()}. */
	@Test
	public void testToString() {
		String test = block0.toString();
		try {
			assertTrue(test.contains("claims: "));
			assertTrue(test.contains("hash: " + block0.getHash()));
			assertTrue(test.contains("issuer: "));
			assertTrue(test.contains("referee: "));
			assertTrue(test.contains("timestamp: " + String.valueOf(block0.getTimeStamp())));
			System.out.println("testToString() PASSED");
		} catch (AssertionError e) {
			System.out.println("testToString() FAILED: " + e.getMessage());
		}
	}	

	/** Test method for {@link Block#validate()}. */
	@Test
	public void testValidate() {
		try {
			assertTrue("testValidate() FAILED", block0.validate());
			assertTrue("testValidate() FAILED", block1.validate());
			assertTrue("testValidate() FAILED", block2.validate());
			System.out.println("testValidate() PASSED");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}
}
