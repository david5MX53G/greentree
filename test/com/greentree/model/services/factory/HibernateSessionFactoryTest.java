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
package com.greentree.model.services.factory;

import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author david5MX53G
 */
public class HibernateSessionFactoryTest {
    /** log4j 2 logger */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * This tests the method {@link HibernateSessionFactory#currentSession()} 
     * per jboss.org guidelines (https://docs.jboss.org/hibernate/orm/3.5/api/org/hibernate/Session.html):
     *
     * Session sess = factory.openSession();
     * Transaction tx;
     * try {
     *     tx = sess.beginTransaction();
     *     //do some work
     *     ...
     *     tx.commit();
     * }
     * catch (Exception e) {
     *     if (tx!=null) tx.rollback();
     *     throw e;
     * }
     * finally {
     *     sess.close();
     * }
     *
     *
     * @throws com.greentree.model.exception.TokenException when the
     * {@link com.greentree.model.domain.Token} fails to build
     */
    @Test
    public void testCurrentSession() throws TokenException {
        Session sess = null;
        Transaction tx;
        try {
            sess = HibernateSessionFactory.currentSession();
            LOGGER.debug("session: " + sess.toString());
            
            tx = sess.beginTransaction();
            LOGGER.debug("session transaction started");
            
            sess.save(new Token("Over The Shiverpeaks"));
            LOGGER.debug("new Token saved");
            
            List result = sess.createQuery("from Token t").list();
            for (Token tk : (List<Token>) result) {
                if (tk instanceof Token) {
                    assertTrue(true);
                    LOGGER.debug(tk.getKeyId() + " instanceof Token");
                } else {
                    fail();
                    LOGGER.debug("Token failed instanceof test");
                }
            }
            
            tx.commit();
            LOGGER.debug("transaction committed");
            
            assertTrue(tx.getStatus() == TransactionStatus.COMMITTED);
        } catch (Exception e) {
            fail(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            if (sess != null) {
                sess.close();
                LOGGER.debug("session closed");
            }
            
            HibernateSessionFactory.closeFactory();
            LOGGER.debug("HibernateSessionFactory closed");
        }
    }
}
