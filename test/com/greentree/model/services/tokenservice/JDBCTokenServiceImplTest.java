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
 * This class tests the class
 * {@link com.greentree.model.services.tokenservice.JDBCTokenServiceImpl}
 *
 * @author david5MX53G
 */
public class JDBCTokenServiceImplTest {

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
        "com.greentree.model.services.tokenservice.JDBCTokenServiceImpl";

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
     * Test method for {@link JDBCTokenServiceImpl#commit(Token)}.
     *
     * @throws com.greentree.model.exception.TokenServiceException when
     * @throws com.greentree.model.exception.TokenException
     */
    @Test
    public void testCommit()
        throws AssertionError, TokenServiceException, TokenException {        
        Token token = new Token("Ase's Death by Edvard Grieg");
        LOGGER.debug("Token 0 initialized");
        
        // insert initial Token record
        assertTrue(
            "service.commit(token) returned false", service.commit(token)
        );
        
        // update previous Token record
        assertTrue(
            "service.commit(token) returned false", service.commit(token)
        );
        
        // TODO: validate the db has only one record for the Token
        
        LOGGER.debug("commit(token) test PASSED");
    }
    
    /**
     * Test method for {@link JDBCTokenServiceImpl#selectToken(RSAPublicKey)}
     * @throws com.greentree.model.exception.TokenServiceException if 
     * the static test token does not return a valid {@link RSAPublicKey}.
     * @throws com.greentree.model.exception.TokenException when the {@link 
     * Token} retrieved from JDBC returns false from {@link Token#validate()}.
     */
    @Test
    public void testSelectToken() throws TokenServiceException, TokenException, 
        AssertionError {
        Token token = new Token("Turandot, Act III by Giacomo Puccini");
        LOGGER.debug("Token initialized");
        service.commit(token);
        RSAPublicKey key = token.getPublicKey();
        token = null; // ohnoes, no more Token?!
        token = service.selectToken(key); // No worries, we can get it back!
        assertTrue("selectToken(RSAPublicKey) FAILED", token.validate());
        LOGGER.debug("testSelectToken() PASSED");
    }
}
