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
package com.greentree.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class initializes a {@link java.net.ServerSocket} to manage 
 * connections from remote clients and pass data between these connections 
 * and additional layers of the GreenTree application.
 * 
 * @author david5MX53G
 */
public class GreenTreeServer {
    /** This {@link org.apache.logging.log4j.Logger} is good for logging! */
    private static final Logger LOG = LogManager.getLogger();
    
    /** TODO: move this into a properties file */
    private static final int PORT = 8189;
    
    /** This private constructor is used by {@link GreenTreeServer#start()}. */
    private GreenTreeServer() {}
    
    /** This opens a new {@link java.net.ServerSocket}. */
    public static void start() {
        LOG.info("started");
        ServerSocket s;
        GreenTreeServerHandler handler;
        try {
            s = new ServerSocket(PORT);
            LOG.debug("listening on port " + String.valueOf(PORT));
            while (true) {
                Socket socket = s.accept();
                LOG.debug("connection accepted");
                handler = new GreenTreeServerHandler(socket);
                handler.run();
                LOG.debug("GreenTreeServerHandler done");
            }
        } catch (IOException ex) {
            LOG.error("start() threw " + ex.getClass().getSimpleName() + ": " 
                + ex.getMessage());
        }
        LOG.info("stopped");
    }
    
    /**
     * This runs {@link GreenTreeServer#start()}.
     * @param args does nothing
     */
    public static void main(String[] args) {
        GreenTreeServer.start();
    }
}
