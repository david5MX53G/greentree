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
package com.greentree.model.services.manager;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author david5MX53G
 */
public class JDBCPoolManagerTest {

    /**
     * log4j 2 logger
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * This tests {@link JDBCPoolManager#getConn()}.
     *
     * @throws java.beans.PropertyVetoException when <code>getConn()</code>
     * fails
     * @throws java.io.IOException when <code>getConn()</code> fails
     * @throws java.sql.SQLException when <code>getConn()</code> fails
     */
    @Test
    public void getConnTest() throws PropertyVetoException, IOException,
        SQLException, AssertionError {
        Connection conn = JDBCPoolManager.getConn();
        LOGGER.debug("JdbcConnection initialized from JDBCPoolManager");

        String sqlIns = "INSERT INTO token (keyId) VALUES (?) "
            + "ON DUPLICATE KEY UPDATE keyId=keyId";

        try (PreparedStatement stmt = conn.prepareStatement(sqlIns)) {
            stmt.setString(
                1, "248163264128256512"
            );
            stmt.executeUpdate();
        }
        LOGGER.debug("JdbcStatement complete: " + sqlIns);

        String sqlSel = "SELECT keyId FROM token";

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlSel);
            String result;
            while (rs.next()) {
                result = rs.getString("keyId");
                assertTrue(result instanceof String);
                LOGGER.debug("retrieved result: " + result);
                result = null;
            }
            LOGGER.debug("JdbcStatement complete: " + sqlSel);
        }
        LOGGER.debug("getConnTest() PASSED");
    }
}
