/*
 * The MIT License
 *
 * Copyright 2018 david5MX53G.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.greentree.model.services.tokenservice;

import com.greentree.model.exception.TokenServiceException;
import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenException;
import java.security.interfaces.RSAPublicKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author david5MX53G
 */
public class HibernateTokenServiceImplTest {
    /**
     * This logs messages using {@link org.apache.logging.log4j.Logger}.
     */
    protected static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * This {@link com.greentree.model.services.tokenservice.ITokenService}
     * stores the
     * {@link com.greentree.model.services.tokenservice.JDBCTokenServiceImpl} to
     * be tested.
     */
    private static ITokenService service;

    private static final String CLASSNAME = 
        "com.greentree.model.services.tokenservice.HibernateTokenServiceImpl";

    /**
     * This static initializer block sets up static variables for testing. The
     * Class Generic is used here to mimic
     * {@link com.greentree.model.services.factory.ServiceFactory}. The factory
     * itself is not used since this instantiates the class defined by
     * {@link com.greentree.model.services.manager.PropertyManager}; whereas,
     * here we instantiate
     * {@link import com.greentree.model.services.tokenservice.JDBCTokenServiceImpl}
     * regardless of the application properties.
     */
    static {
        try {
            Class<?> classy = Class.forName(CLASSNAME);
            service = (ITokenService) classy.newInstance();
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex) {
            fail(ex.getClass().getSimpleName() + " " + ex.getMessage());
        }
    }
    
    /**
     * This method tests {@link HibernateTokenServiceImpl#commit(Token)}.
     * 
     * @throws com.greentree.model.exception.TokenException when the 
     * {@link Token} object fails to initialize.
     * 
     * @throws com.greentree.model.exception.TokenServiceException when  
     * {@link ITokenService#commit(com.greentree.model.domain.Token)} fails.
     */
    @Test
    public void testCommit() throws TokenException, TokenServiceException {
        Token tk = new Token("The Rift");
        LOGGER.debug("Token initialized with keyId: " + tk.getKeyId());
        
        // insert
        assertTrue(
            "service.commit(Token) returned false", service.commit(tk)
        );
        LOGGER.debug("ITokenService insert done");
        
        // update
        assertTrue(
            "service.commit(Token) returned false", service.commit(tk)
        );
        LOGGER.debug("ITokenService update done");
    }
    
    /**
     * Test method for {@link HibernateTokenServiceImpl#selectToken(java.security.interfaces.RSAPublicKey)}
     * 
     * @throws com.greentree.model.exception.TokenServiceException if 
     * the static test token does not return a valid {@link RSAPublicKey}.
     * 
     * @throws com.greentree.model.exception.TokenException when the {@link 
     * Token} retrieved from JDBC returns false from {@link Token#validate()}.
     */
    @Test
    public void testSelectToken() throws TokenServiceException, TokenException, 
        AssertionError {
        Token token = new Token("Divinity Coast");
        LOGGER.debug("Token initialized with keyId: " + token.getKeyId());
        
        service.commit(token);
        LOGGER.debug("service.commit(Token) done");
        
        RSAPublicKey key = token.getPublicKey();
        LOGGER.debug("RSAPublicKey: " + key.getModulus().toString());
        
        token = null;
        LOGGER.debug("ohnoes, no more Token?!");
        
        token = service.selectToken(key);
        LOGGER.debug("service.selectToken(RSAPublicKey) done");
        
        assertTrue("selectToken(RSAPublicKey) FAILED", token.validate());
        LOGGER.debug("token.validate() PASSED");
    }
}
