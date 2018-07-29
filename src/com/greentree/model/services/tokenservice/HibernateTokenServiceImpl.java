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

import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenServiceException;
import com.greentree.model.services.factory.HibernateSessionFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * This class uses {@link org.hibernate} to persist {@link Token} objects to a 
 * database. Database config parameters are read from hibernate.cfg.xml.
 * 
 * @author david5MX53G
 */
public class HibernateTokenServiceImpl implements ITokenService {
    /** log4j 2 logger */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public boolean commit(Token token) throws TokenServiceException {
        boolean result = false;
        Session sess = null;
        Transaction tx;
        try {
            sess = HibernateSessionFactory.currentSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(token);
            tx.commit();
            result = tx.getStatus() == TransactionStatus.COMMITTED;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (sess != null) { sess.close(); }
        }
        return result;
    };

    @Override
    public Token selectToken(RSAPublicKey key) throws TokenServiceException {
        Token result = null;
        String keyId = String.valueOf(key.getModulus());
        keyId = keyId.substring(0, 9) + keyId.substring(keyId.length() - 9);
        Session sess = null;
        Transaction tx;
        try {
            sess = HibernateSessionFactory.currentSession();            
            tx = sess.beginTransaction();            

            // behold! the "Named Parameter", as seen on https://www.mkyong.com/hibernate/hibernate-parameter-binding-examples/
            String query 
                = "from Token t where t.keyId = :keyId";
            
            List resultList = sess.createQuery(query)
                .setParameter("keyId", keyId)
                .list();
            
            for (Token tk : (List<Token>) resultList) {
                if (tk instanceof Token) {
                    result = tk;
                    LOGGER.debug("returned Token keyId: " + tk.getKeyId());
                } else {
                    throw new TokenServiceException(
                        "A Token was not returned from Token where keyId = " 
                            + keyId, 
                        LOGGER
                    );
                }
            }
            
            tx.commit();
            LOGGER.debug("transaction committed");
        } 
        
        catch (Exception e) {
            LOGGER.error(e.getMessage());
        } 
        
        finally {
            if (sess != null) {
                sess.close();
                LOGGER.debug("session closed");
            }
        }
        return result;
    };
}
