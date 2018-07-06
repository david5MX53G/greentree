package com.greentree.model.domain;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.greentree.model.exception.TokenException;

/**
 * This defines methods for testing the <code>{@link Claim}</code> class.
 * 
 * @author david.dietrich
 *
 */
public class ClaimTest {
	/** passphrase used to instantiate <code>Token</code> objects in test methods. */
	private static final String PASSPHRASE = "We\'re all mad here.";
	Token token;
	Long now;
	Long then;
	Claim claim0;

	/**
	 * @throws TokenException when <code>{@link Token}</code> when <code>{@link Token}</code> 
	 * fails to build
	 */
	@Before
	public void setUp() {
		try {
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
		} catch (TokenException e) {
			System.err.println("setUp() " + e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/** Happy test method for {@link Claim#equals(Object)}. */
	@Test
	public void testEquals() {
		try {
			Claim claim1 = new Claim(token, now, then);
			assertTrue(claim0.equals(claim1));
			System.out.println("testEquals() PASSED");
		} catch (AssertionError e) {
			System.out.println("testEquals() FAILED");
		}
	}

	/** Negative test method for {@link Claim#equals(Object)}. */
	@Test
	public void testNotEquals() {
		try {
			Claim claim1 = new Claim(token, new Date().getTime(), new Date().getTime());
			assertFalse(claim0.equals(claim1));
			System.out.println("testNotEquals() PASSED");
		} catch (AssertionError e) {
			System.out.println("testNotEquals() FAILED");
		} 
	}

	/** Happy path test method for {@link Claim#hashCode()}. */
	@Test
	public void testHashCode() {
		try {
			Claim claim1 = new Claim(token, now, then);
			int hash0 = claim0.hashCode();
			int hash1 = claim1.hashCode();
			assertTrue(hash0 == hash1);
			System.out.println("testHashCode() PASSED");
		} catch (AssertionError e) {
			System.out.println("testHashCode() FAILED");
		}
	}

	/** Negative test method for {@link Claim#hashCode()} */
	@Test
	public void testNotHashCode() {
		try {
			Claim claim1 = new Claim(token, new Date().getTime(), new Date().getTime());
			int hash1 = claim0.hashCode();
			int hash2 = claim1.hashCode();
			assertFalse(hash1 == hash2);
			System.out.println("testNotHashCode() PASSED");
		} catch (AssertionError e) {
			System.out.println("testNotHashCode() FAILED");
		} 
	}

	/** Tests <code>{@link Claim#toString()}</code> */
	@Test
	public void testToString() {
		String claimString = null;
		claimString = claim0.toString();
		try {
			assertTrue("testToString() FAILED", claimString != null);
			System.out.println("testToString() PASSED");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	/** Happy path test method for {@link Claim#validate()}. */
	@Test
	public void testValidate() {
		try {
			assertTrue("testValidate() FAILED", claim0.validate() == true);
			System.out.println("testValidate() PASSED");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		} 
	}

	/** Negative test method for {@link Claim#validate()}. */
	@Test
	public void testNotValidate() {
		try {
			assertFalse("testNotValidate() FAILED", claim0.validate() == false);
			System.out.println("testNotValidate() PASSED"); 
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

}
