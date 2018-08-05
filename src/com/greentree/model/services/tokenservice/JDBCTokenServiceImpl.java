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
import com.greentree.model.services.manager.JDBCPoolManager;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * This class provides the {@link java.sql} implementation necessary to
 * serialize and deserialize {@link com.greentree.model.domain.Token} from a
 * database.
 *
 * TODO: stop using the BLOB field type; storing binary data in the database
 * will drive your DBA up the wall for reasons including, but not limited to,
 * the fact that this data cannot be easily ported from one DBMS to another.
 *
 * @author david5MX53G
 */
public class JDBCTokenServiceImpl implements ITokenService {

    /**
     * This helps identify broken things.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * This method stores the given {@link com.greentree.model.domain.Token} in
     * the database using a JDBC connection.
     *
     * @param token {@link com.greentree.model.domain.Token} to be stored in the
     * database via JDBC
     *
     * @return boolean indicating success or failure
     *
     * @throws TokenServiceException when the <code>Token</code> given for
     * storage is not found, or when the database connection and/or statement(s)
     * get in trouble.
     */
    @Override
    public boolean commit(Token token) throws TokenServiceException {
        boolean valid;
        try {
            valid = token.validate();

            if (valid) {
                LOGGER.debug("Token is valid");
            } else {
                valid = false;
                LOGGER.error("Token param did not validate");
            }

            // serialize the Token to a byte[]
            byte[] byteArray = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(token);
            out.flush();
            byteArray = bos.toByteArray();
            bos.close();
            out.close();
            
            // serialize the byte[] in an InputStream
            if (byteArray == null) {
                LOGGER.error("failed to initialized byteArray");
            } else {
                LOGGER.debug("byte[] created from token");
            }

            ByteArrayInputStream boas = new ByteArrayInputStream(byteArray);
            LOGGER.debug("ByteArrayInputStream created from Token");

            Connection conn = JDBCPoolManager.getConn();
            LOGGER.debug("JDBC Connection acquired");
            conn.setAutoCommit(false);

            // create a new keyId, if none exists
            String sqlIns = "INSERT INTO token (keyId) VALUES (?) "
                + "ON DUPLICATE KEY UPDATE keyId=keyId";

            PreparedStatement stmtIns
                = conn.prepareStatement(sqlIns);

            stmtIns.setString(
                1, this.getKeyId(token.getPublicKey())
            );

            stmtIns.executeUpdate();
            stmtIns.close();

            conn.commit();

            LOGGER.debug(
                "keyId upsert complete "
                + String.valueOf(
                    this.getKeyId(token.getPublicKey())
                )
            );

            // update the new key record with Token binary
            String sqlUpd = "UPDATE token SET token = ? WHERE keyId = ?";

            PreparedStatement stmtUpd = conn.prepareStatement(sqlUpd);
            stmtUpd.setBinaryStream(1, boas);

            stmtUpd.setString(
                2, this.getKeyId(token.getPublicKey())
            );

            stmtUpd.executeUpdate();

            conn.commit();
            
            stmtUpd.close();
            conn.close();
            boas.close();

            LOGGER.debug("token binary upsert complete");
        } catch (IOException | SQLException | PropertyVetoException 
            | SAXException | ParserConfigurationException ex) {
            valid = false;
            throw new TokenServiceException(
                ex.getClass().getSimpleName() + " " + ex.getMessage(),
                LOGGER
            );
        }
        return valid;
    }

    /**
     * This method retrieves a {@link com.greentree.model.domain.Token} from the
     * database using a {@link java.sql.DriverManager}. Many thanks are due to
     * java2s.com for the tutorial Storeandretrieveanobjectfromatable.htm.
     *
     * @param key {@link RSAPublicKey} uniquely identifying the <code>Token
     * </code> to be returned
     *
     * @return <code>Token</code> corresponding with the given <code>
     * RSAPublicKey</code>
     *
     * @throws TokenServiceException
     */
    @Override
    public Token selectToken(RSAPublicKey key) throws TokenServiceException {
        Token token = null;
        String keyId = this.getKeyId(key);
        LOGGER.debug("fetching token KeyId " + keyId);

        try (Connection conn = JDBCPoolManager.getConn()) {
            LOGGER.debug("Connection initialized");
            String sql = "SELECT token FROM token WHERE keyId = ?";

            try (PreparedStatement stmt
                = conn.prepareStatement(sql)) {
                LOGGER.debug("PreparedStatement initialized: " + sql);

                stmt.setString(1, keyId);
                ResultSet rs = stmt.executeQuery();
                LOGGER.debug("ResultSet retrieved");

                while (rs.next()) {
                    byte[] st = (byte[]) rs.getObject(1);
                    LOGGER.debug("byte[] initialized");

                    try (ByteArrayInputStream baip
                        = new ByteArrayInputStream(st)) {
                        LOGGER.debug("ByteArrayInputStream initialized");

                        try (ObjectInputStream ois
                            = new ObjectInputStream(baip)) {
                            token = (Token) ois.readObject();
                            LOGGER.debug("Token initialized");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new TokenServiceException(ex.getMessage(), LOGGER, ex);
        } catch (PropertyVetoException | IOException | SQLException | SAXException | ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(JDBCTokenServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return token;
    }

    /**
     * This method is used to look up {@link com.greentree.model.domain.Token}
     * objects in the database.
     *
     * @param key {@link RSAPublicKey} used to generate the return value
     * @return string uniquely identifying the given key
     */
    private String getKeyId(RSAPublicKey key) {
        String result = String.valueOf(key.getModulus());
        result = result.substring(0, 9) + result.substring(result.length() - 9);
        return result;
    }
}
