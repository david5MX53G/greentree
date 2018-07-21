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
import com.greentree.model.exception.TokenException;
import com.greentree.model.services.exception.InvalidTokenException;
import com.greentree.model.services.manager.PropertyManager;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
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
     * @throws InvalidTokenException when the <code>Token</code> given for
     * storage is not found
     *
     * @throws IOException when the database cannot be reached
     */
    @Override
    public void commit(Token token) throws InvalidTokenException, IOException {
        try {
            if (token.validate() == false) {
                throw new InvalidTokenException(
                    "The given Token does not validate",
                    new Exception());
            } else {
                String keyId = this.getKeyId(token.getPublicKey());
                Connection conn = this.getConn();

                if (conn != null) {
                    String sql = "INSERT INTO token (key, token) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, keyId);

                    /**
                     * TODO: figure out the MySQL equivalent of...
                     * ObjectOutputStream out = new ObjectOutputStream(new
                     * FileOutputStream(filename)); out.writeObject(token);
                     */
                    stmt.executeUpdate();
                } else {
                    LOGGER.error(
                        "Fetching the db connection failed miserably."
                    );
                }
            }
        } catch (TokenException e) {

            String msg
                = "Token commit threw " + e.getClass().getName() + ": "
                + e.getMessage();

            LOGGER.error(msg);
            throw new InvalidTokenException(msg, new Exception());
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     * This method retrieves a {@link com.greentree.model.domain.Token} from the
     * database using a {@link java.sql.DriverManager}.
     *
     * @param key {@link RSAPublicKey} uniquely identifying the <code>Token
     * </code> to be returned
     *
     * @return <code>Token</code> corresponding with the given <code>
     * RSAPublicKey</code>
     *
     * @throws InvalidKeyException when the <code>key</code> param is bad
     * @throws NoSuchAlgorithmException when the crypto system barfs
     * @throws InvalidKeySpecException also when the crypto system barfs
     * @throws IOException when the <code>DriverManager</code> has trouble
     * connecting with the database
     */
    @Override
    public Token selectToken(RSAPublicKey key) throws InvalidKeyException,
        NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
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

    /**
     * This uses {@link java.sql.DriverManager} to build a new {@link
     * java.sql.Connection} using connection parameters that should be specified
     * in the .properties file of the application.
     *
     * TODO: move this into a SQL Connection manager class
     *
     * @return {@link java.sql.Connection} to the database
     */
    private Connection getConn() {
        /**
         * This identifies the database to use for storing and retrieving
         * {@link com.greentree.model.domain.Token} objects. TODO: pull this
         * value from the .properties file
         */
        String url = "jdbc:mysql://localhost:3306/greentree";

        /**
         * This identifies the user for connecting with the database. This user
         * needs read/write access to the database. TODO: pull this value from
         * the .properties file
         */
        String userid = "mysqlsample";

        /**
         * This is used to authenticate the user against the database. TODO:
         * pull this value from the .properties file
         */
        String password = "SSBhbSB0aGUgZXZlci1saXZpbmcgd29tYmF0Lg==";

        /**
         * This stores the JDBC connection object for storing and retrieving {@link
         * com.greentree.model.domain.Token} objects from the database.
         */
        Connection connection = null;

        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            LOGGER.debug("Registering MySQL Driver successful");

            connection = DriverManager.getConnection(url, userid, password);
            LOGGER.debug("Retrieving MySQL connection successful");
        } catch (SQLException e) {
            LOGGER.error(
                "Could not load and register JDBC driver or connect to database."
            );
            LOGGER.error(e.getMessage());
        }
        return connection;
    }
}
