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
package com.greentree.model.business.manager;

import com.greentree.model.services.factory.ServiceFactory;

/**
 * This class provides methods for registering the GreenTree data access object 
 * (DAO).
 * 
 * @author david5MX53G
 */
public class DAOManager {
    /**
     * Stores the singleton instance of this class.
     */
    private static DAOManager _instance;
    
    /**
     * {@link ServiceFactory} for building DOAServices.
     */
    private final ServiceFactory factory = ServiceFactory.getInstance(); 
    
    /**
     * private constructor for the DOAManager Singleton
     */
    private DAOManager() {}
    
    public static DAOManager getInstance() {
        if (_instance == null) {
            _instance = DOAManager();
        }
        return _instance;
    }
}
