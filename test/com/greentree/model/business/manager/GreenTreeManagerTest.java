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
import com.greentree.model.services.manager.PropertyManager;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class has methods to test the <code>{@link GreentreeManager}</code> class.
 * 
 * @author david5MX53G
 *
 */
public class GreenTreeManagerTest {
    /** <code>GreenTreeManager</code> object for testing. */
    private static GreenTreeManager manager;

    /** <code>{@link RSAPublicKye}</code> for testing <code>Token</code>. */
    private static RSAPublicKey key;

    /** Passphrase for managing <code>{@link Token}</code> objects. */
    private static final String PASS = "More pepper!";
    
    /** log4j logger for debug output */
    Logger logger;

    /**
     * setUp() ensures the manager, token service, and key of this test instance 
     * are built.
     * @throws java.io.IOException from {@link 
     * PropertyManager#getProperty(java.lang.String)}
     */
    @Before
    public void setUp() throws IOException {
        /**
         * "Since naming Loggers after their owning class is such a common 
         * idiom, the convenience method LogManager.getLogger() is provided to 
         * automatically use the calling class's fully qualified class name as 
         * the Logger name." (Apache Software Foundation, "Log4j – Log4j 2 
         * Architecture - Apache Log4j 2", "Retrieving Loggers" section).
         */
        logger = LogManager.getLogger();
        logger.debug("logger configured");
        
        if (GreenTreeManagerTest.manager == null) {
            registerInstanceTest();
            logger.debug("initialized private static GreenTreeManager manager");
        }

        if (GreenTreeManagerTest.manager.getTokenService() == null) {
            registerServiceTest();
            logger.debug("GreenTreeManager getTokenService() completed successfully");
        }

        if (GreenTreeManagerTest.key == null) {
            registerTokenTest();
            logger.debug("registerTokenTest() completed successfully");
        }
    }

    /** Tests the <code>{@link GreenTreeManager#loadProperties()}</code> method */
    @Test
    public void loadPropertiesTest() {
            try {
                    GreenTreeManager.loadProperties();
                    logger.debug("loadPropertiesTest() PASSED");
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
            logger.debug("registerInstanceTest() PASSED");
        }
    }

    /** Tests the <code>{@link GreenTreeManager#registerService(String)}</code> method */
    @Test
    public void registerServiceTest() {
        if (GreenTreeManagerTest.manager.getTokenService() == null) {
            try {
                    GreenTreeManagerTest.manager.registerService("TokenService");
                    assertTrue(GreenTreeManagerTest.manager.getTokenService() != null);
                    logger.debug("registerServiceTest() PASSED");
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
                    logger.debug("registerTokenTest() PASSED");
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
            logger.debug("logOutTest() PASSED");
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
            logger.debug("authTest() FAILED");
        }

        if (ciphertext != null) {
            try {
                GreenTreeManagerTest.manager.registerToken(key, ciphertext);
                assertTrue(GreenTreeManagerTest.manager.getPublicKey() instanceof RSAPublicKey);
                logger.debug("authTest() PASSED");
            } catch (Exception e) {
                fail("authTest() FAILED with " + e.getClass().getName() + ": " + e.getMessage());
            }
        } else {
            fail("authTest() FAILED encrypting pass");
        }
    }
}
