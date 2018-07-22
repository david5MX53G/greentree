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

import com.greentree.model.business.exception.TokenServiceException;
import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenException;
import com.mysql.cj.jdbc.JdbcPreparedStatement;
import com.mysql.cj.jdbc.JdbcConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPublicKey;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                byte[] byteArray = null;
                LOGGER.debug("Token is valid");
                // serialize the Token to a byte[]
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    ObjectOutputStream out = new ObjectOutputStream(bos);
                    out.writeObject(token);
                    out.flush();
                    byteArray = bos.toByteArray();
                    LOGGER.debug("byte[] created from token");
                } catch (IOException e) {
                    valid = false;
                    throw new TokenServiceException(
                        "ByteArrayOutputStream error",
                        LOGGER
                    );
                }

                // serialize the byte[] as an InputStream
                if (byteArray != null) {
                    try (
                        ByteArrayInputStream boas
                        = new ByteArrayInputStream(byteArray)) {
                        LOGGER.debug("ByteArrayInputStream created from Token");

                        // insert the InputStream into the database
                        try (JdbcConnection conn = this.getConn()) {
                            LOGGER.debug("JDBC Connection acquired");
                            conn.setAutoCommit(false);

                            /**
                             * Positional params are used here to avoid breaking
                             * the INSERT when the column names change.
                             */
                            String sql
                                = "INSERT INTO token (keyId, token) VALUES (?, ?)";

                            try (JdbcPreparedStatement stmt
                                = (JdbcPreparedStatement) conn.prepareStatement(sql)) {
                                LOGGER.debug(
                                    "PreparedStatement initialized: " + sql
                                );

                                stmt.setString(
                                    1, this.getKeyId(token.getPublicKey())
                                );

                                LOGGER.debug(
                                    "PreparedStatement setString done"
                                );

                                stmt.setBinaryStream(2, boas);

                                LOGGER.debug(
                                    "PreparedStatement setBinaryStream done"
                                );

                                stmt.executeUpdate();
                                LOGGER.debug(
                                    "PreparedStatement executeUpdate() done"
                                );
                            }

                            conn.commit();
                            LOGGER.debug("token committed to database");
                        }
                    }
                }
            } else {
                valid = false;
                throw new TokenException("Token param did not validate");
            }
        } catch (TokenException | SQLException | IOException ex) {
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

        try (JdbcConnection conn = this.getConn()) {
            LOGGER.debug("Connection initialized");
            String sql = "SELECT token FROM token WHERE keyId = ?";

            try (JdbcPreparedStatement stmt
                = (JdbcPreparedStatement) conn.prepareStatement(sql)) {
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
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            throw new TokenServiceException(ex.getMessage(), LOGGER, ex);
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

    /**
     * This uses {@link java.sql.DriverManager} to build a new {@link
     * java.sql.Connection} using connection parameters that should be specified
     * in the .properties file of the application.
     *
     * TODO: move this into a SQL Connection manager class
     *
     * @return {@link java.sql.Connection} to the database
     */
    private JdbcConnection getConn() throws SQLException {
        /**
         * This identifies the database to use for storing and retrieving
         * {@link com.greentree.model.domain.Token} objects. TODO: pull this
         * value from the .properties file
         *
         * TODO: set server timezone a a key/value pair in java.util.Properties
         * passed to DriverManager.getConnection() or Driver.connect() as
         * described in connector-j-reference-configuration-properties.html
         */
        String url = "jdbc:mysql://localhost:3306/greentree?serverTimezone=UTC";

        /**
         * This identifies the user for connecting with the database. This user
         * needs read/write access to the database. TODO: pull this value from
         * the .properties file
         */
        String userid = "ITokenService";

        /**
         * This is used to authenticate the user against the database. TODO:
         * pull this value from the .properties file
         */
        String password = "SSBhbSB0aGUgZXZlci1saXZpbmcgd29tYmF0Lg==";

        /**
         * This stores the JDBC connection object for storing and retrieving {@link
         * com.greentree.model.domain.Token} objects from the database.
         */
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        LOGGER.debug("Registering MySQL Driver successful");

        JdbcConnection conn
            = (JdbcConnection) DriverManager.getConnection(url, userid, password);
        LOGGER.debug("Retrieving MySQL connection successful");
        return conn;
    }
}
