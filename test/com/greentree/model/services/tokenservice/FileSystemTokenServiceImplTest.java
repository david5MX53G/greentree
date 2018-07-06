package com.greentree.model.services.tokenservice;

import static org.junit.Assert.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import org.junit.Test;

import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenException;
import com.greentree.model.services.exception.InvalidTokenException;
import com.greentree.model.services.exception.ServiceLoadException;
import com.greentree.model.services.factory.ServiceFactory;
import java.io.IOException;

/**
 * This defines methods for testing the <code>{@link FileSystemTokenService}</code> class.
 * 
 * @author david.dietrich
 *
 */
public class FileSystemTokenServiceImplTest {
	private static Token token;
	private static ITokenService service;
    
    /** static initializer block for static variables */
    static {
    	ServiceFactory factory = ServiceFactory.getInstance();
    	try {
			service = (ITokenService) factory.getService("ITokenService");
		} catch (ServiceLoadException e) {
			String msg = e.getClass().getName() + " in static init block of "
					+ "FileSystemTokenServiceImplTest: " + e.getMessage();
			System.out.println(msg);
		}
    	
    	try {
			token = new Token("My name is Alice.");
		} catch (TokenException e) {
			String msg = e.getClass().getName() + " in static init block of "
			    + "FileSystemTokenServiceImplTest: " + e.getMessage();
			System.out.println(msg);
		}
		
    }

	/**
	 * Test method for <code>{@link FileSystemTokenServiceImpl#commit(Token)}</code>.
	 */
	@Test
	public void testCommit()  {
		try {
			service.commit(token);
			System.out.println("commit(token) test PASSED");
		} catch (IOException | InvalidTokenException e) {
			fail(e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	/**
	 * Test method for <code>{@link FileSystemTokenServiceImpl#selectToken(RSAPublicKey)}</code>.
	 */
	@Test
	public void testSelectToken() {
		// commit token
		try {
			service.commit(token);
		} catch (InvalidTokenException | IOException e) {
			fail(e.getClass().getSimpleName() + ": " + e.getMessage());
		}

		// get public key and delete token
		RSAPublicKey key = token.getPublicKey();
		token = null;
		
		// re-build token from commit using public key
		try {
			token = service.selectToken(key);
		} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | 
                         InvalidKeySpecException e) {
			fail("selectToken(RSAPublicKey) FAILED: " + e.getClass().getName() + ": " + 
                e.getMessage());
		}
		
		try {
			if (token.validate()) {
				assertTrue(true);
				System.out.println("testSelectToken() PASSED");
			} else {
				fail("selectToken(RSAPublicKey) FAILED");
			}
		} catch (TokenException e) {
			fail("selectToken(RSAPublicKey) FAILED: " + e.getMessage());
		}
		
	}

}
