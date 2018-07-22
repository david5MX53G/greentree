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

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author david5MX53G
 */
public class JDBCPoolManager {
    /**
     * log4j Logger for logging logs to the log
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * {@link ComboPooledDataSource} is an implementation of the public
     * interface PooledDataSource which extends {@link javax.sql.DataSource}.
     * See comment on {@link JDBCPoolManager#createPool()}. The variable is
     * static to facilitate the Singleton Pattern, as the whole objective of
     * connection pools is to consolidate disparate data source connections.
     */
    private static ComboPooledDataSource cpds = null;

    /**
     * instantiates an implementation of {@link
     * com.mchange.v2.c3p0.PooledDataSource}, a core part of the
     * <a href="https://www.mchange.com/projects/c3p0/">c3p0 project</a>. The
     * Project adapts, "traditional DriverManager-based JDBC drivers to the
     * newer javax.sql.DataSource scheme for acquiring database Connections."
     * and provides "Transparent pooling of Connection and PreparedStatements
     * behind DataSources"
     *
     * @throws java.beans.PropertyVetoException when connection pool driver
     * fails to set.
     *
     * @throws IOException when the properties file cannot be read
     */
    private static void createPool() throws PropertyVetoException, IOException {
        cpds = new ComboPooledDataSource();

        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");

            /**
             * This identifies the database to use for storing and retrieving
             * {@link com.greentree.model.domain.Token} objects.
             */
            String url = PropertyManager.getProperty("jdbcUrl");
            cpds.setJdbcUrl(url);

            /**
             * This identifies the user for connecting with the database. This
             * user needs read/write access to the database.
             */
            String userid = PropertyManager.getProperty("jdbcUser");
            cpds.setUser(userid);

            /**
             * This is used to authenticate the user against the database.
             */
            String password = PropertyManager.getProperty("jdbcPass");
            cpds.setPassword(password);
            
            /**
             * These values determine how many connections are allowed in the 
             * {@link ComboPooledDataSource}.
             */
            int poolSizeMin =
                Integer.valueOf(PropertyManager.getProperty("jdbcMinPoolSize"));
            cpds.setMinPoolSize(poolSizeMin);
            
            int poolSizeMax = 
                Integer.valueOf(PropertyManager.getProperty("jdbcMaxPoolSize"));
            cpds.setMaxPoolSize(poolSizeMax);
            
        } catch (PropertyVetoException ex) {
            LOGGER.error(ex.getClass().getSimpleName() + " " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * This leverages the {@link com.mchange.v2.c3p0.PoolBackedDataSource}
     * interface to return a Java SQL Connection object.
     *
     * @return {@link java.sql.Connection} to the database configured in the
     * application properties file, or null when there's an error
     *
     * @throws java.beans.PropertyVetoException when
     * {@link ComboPooledDataSource} fails to load.
     *
     * @throws java.io.IOException when {@link ComboPooledDataSource} fails to
     * load.
     *
     * @throws java.sql.SQLException when
     * {@link ComboPooledDataSource#getConnection()} fails.
     */
    public static Connection getConn() throws PropertyVetoException, IOException, SQLException {
        // populate the connection pool singleton, if necessary
        if (cpds == null) {
            createPool();
        }

        // grab a connection from the pool
        Connection conn = null;
        try {
            conn = cpds.getConnection();
        } catch (SQLException e) {
            LOGGER.error(e.getClass().getSimpleName() + " " + e.getMessage());
            throw e;
        }
        return conn;
    }

    /**
     * This is the opposite of {@link JDBCPoolManager#getConnection()} in that
     * it shuts down {@link java.sql.Connection} instances opened by the above
     * method.
     *
     * @throws java.sql.SQLException when
     * {@link DataSources#destroy(javax.sql.DataSource)} fails
     */
    public static void shutDown() throws SQLException {
        if (cpds != null) {
            LOGGER.debug("destroying ComboPooledDataSource");
            try {
                DataSources.destroy(cpds);
            } catch (SQLException ex) {
                LOGGER.error(
                    ex.getClass().getSimpleName() + " " + ex.getMessage()
                );
                throw ex;
            }
        }
    }
}
