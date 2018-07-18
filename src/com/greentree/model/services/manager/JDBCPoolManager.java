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
import java.sql.Connection;
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
    static Logger log = LogManager.getLogger();
    
    /**
     * {@link ComboPooledDataSource} is an implementation of the public 
     * interface PooledDataSource which extends {@link javax.sql.DataSource}. 
     * See comment on {@link JDBCPoolManager#createPool()}. The variable is 
     * static to facilitate the Singleton Pattern, as the whole objective of 
     * connection pools is to consolidate disparate data source connections.
     */
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();
    
    /** instantiates an implementation of {@link 
     * com.mchange.v2.c3p0.PooledDataSource}, a core part of the 
     * <a href="https://www.mchange.com/projects/c3p0/">c3p0 project</a>. 
     * The Project adapts, "traditional DriverManager-based JDBC drivers to the 
     * newer javax.sql.DataSource scheme for acquiring database Connections." 
     * and provides "Transparent pooling of Connection and PreparedStatements 
     * behind DataSources" 
     */
    public static void createPool() {
        // TODO: tbd
    }
    
    /** 
     * This leverages the {@link com.mchange.v2.c3p0.PoolBackedDataSource} 
     * interface to return a Java SQL Connection object.
     * 
     * @return {@link java.sql.Connection}
     */
    public static Connection getConnection() {
        // TODO: tbd
        return null;
    }
    
    /**
     * This is the opposite of {@link JDBCPoolManager#getConnection()} in that 
     * it shuts down {@link java.sql.Connection} instances opened by the above 
     * method.
     */
    public static void shutDown() {
        // TODO: tbd
    }
}
