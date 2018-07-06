package com.greentree.model.business.manager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import com.greentree.model.domain.Token;
import com.greentree.model.services.exception.InvalidTokenException;
import java.io.IOException;

/**
 * This class has methods to test the <code>{@link GreentreeManager}</code> class.
 * 
 * @author david.dietrich
 *
 */
public class GreenTreeManagerTest {
    /** <code>GreenTreeManager</code> object for testing. */
    private static GreenTreeManager manager;

    /** <code>{@link RSAPublicKye}</code> for testing <code>Token</code>. */
    private static RSAPublicKey key;

    /** Passphrase for managing <code>{@link Token}</code> objects. */
    private static final String PASS = "More pepper!";

    /**
     * ensures the manager, token service, and key of this test instance are
     * built.
     */
    @Before
    public void setUp() {
        if (GreenTreeManagerTest.manager == null) {
                registerInstanceTest();
        }

        if (GreenTreeManagerTest.manager.getTokenService() == null) {
                registerServiceTest();
        }

        if (GreenTreeManagerTest.key == null) {
                registerTokenTest();
        }
    }

    /** Tests the <code>{@link GreenTreeManager#loadProperties()}</code> method */
    @Test
    public void loadPropertiesTest() {
            try {
                    GreenTreeManager.loadProperties();
                    System.out.println("loadPropertiesTest() PASSED");
            } catch (Exception e) {
                    fail("loadPropertiesTest() threw PropertyFileNotFoundException: " + e.getMessage());
            }
    }

    /** Tests the <code>{@link GreenTreeManager#getInstance()}</code> method */
    @Test
    public void registerInstanceTest() {
        if (GreenTreeManagerTest.manager == null) {
            GreenTreeManagerTest.manager = GreenTreeManager.getInstance();
            assertTrue("registerInstanceTest() FAILED", GreenTreeManagerTest.manager != null);
            System.out.println("registerInstanceTest() PASSED");
        }
    }

    /** Tests the <code>{@link GreenTreeManager#registerService(String)}</code> method */
    @Test
    public void registerServiceTest() {
        if (GreenTreeManagerTest.manager.getTokenService() == null) {
            try {
                    GreenTreeManagerTest.manager.registerService("TokenService");
                    assertTrue(GreenTreeManagerTest.manager.getTokenService() != null);
                    System.out.println("registerServiceTest() PASSED");
            } catch (AssertionError e) {
                    fail("registerServiceTest() " + e.getClass().getSimpleName() + 
                         e.getMessage());
            }
        }
    }

    /** Tests the <code>{@link GreenTreeManager#registerToken(String)}</code> method */
    @Test
    public void registerTokenTest() {
        if (GreenTreeManagerTest.key == null) {
            try {
                    manager.registerToken(GreenTreeManagerTest.PASS);
                    GreenTreeManagerTest.key = manager.getPublicKey();
                    assertTrue(GreenTreeManagerTest.key != null);
                    System.out.println("registerTokenTest() PASSED");
            } catch (Exception e) {
                    fail("GreenTreeManager#registerToken threw: " + 
                         e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    /** Tests the <code>{@link GreenTreeManager#logOut(Token)}</code> method */
    @Test
    public void logOutTest() {		
        try {
            GreenTreeManagerTest.manager.logOut();
            assertTrue(true);
            System.out.println("logOutTest() PASSED");
        } catch (InvalidTokenException | IOException e) {
            fail("logOutTest() " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

    }

    /** Tests the <code>{@link GreenTreeManager#auth(RSAPublicKey, String)}</code> method */
    @Test
    public void authTest() {
        Cipher cipher;
        String ciphertext = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ciphertext = Base64.encodeBase64String(
                cipher.doFinal(GreenTreeManagerTest.PASS.getBytes("UTF-8"))
            );
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | 
                 BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            fail("authTest() " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("authTest() FAILED");
        }

        if (ciphertext != null) {
            try {
                GreenTreeManagerTest.manager.registerToken(key, ciphertext);
                assertTrue(GreenTreeManagerTest.manager.getPublicKey() instanceof RSAPublicKey);
                System.out.println("authTest() PASSED");
            } catch (Exception e) {
                fail("authTest() FAILED with " + e.getClass().getName() + ": " + e.getMessage());
            }
        } else {
                fail("authTest() FAILED encrypting pass");
        }
    }
}
