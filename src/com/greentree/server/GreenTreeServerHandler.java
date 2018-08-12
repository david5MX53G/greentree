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

import com.greentree.model.business.manager.GreenTreeManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class connects {@link GreenTreeServer} to {@link GreenTreeManager}.
 * 
 * @author david5MX53G
 */
class GreenTreeServerHandler {
    /** This {@link org.apache.logging.log4j.Logger} is good for logging! */
    private static final Logger LOG = LogManager.getLogger();
    
    /** This {@link java.net.Socket} receives incoming client connections. */
    private final Socket socket;
    
    /**
     * This {@link GreenTreeManager} interfaces with all other layers of the 
     * application.
     */
    private static final GreenTreeManager MGR = GreenTreeManager.getInstance();

    GreenTreeServerHandler(Socket socket) {
        this.socket = socket;
    }

    void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            String command = (String) in.readObject();
            LOG.info("run() " + command);
            
            if ("registerToken(RSAPublicKey, String)".equals(command)) {
                RSAPublicKey key = (RSAPublicKey) in.readObject();
                String ciphertext = (String) in.readObject();
                out.writeObject(MGR.registerToken(key, ciphertext));
                out.flush();
            }
            
            if ("registerService(String)".equals(command)) {
                String service = (String) in.readObject();
                out.writeObject(MGR.registerService(service));
                out.flush();
            }
            
            LOG.info(command + " completed");
        } catch (IOException | ClassNotFoundException ex) {
            LOG.error("run() threw " + ex.getClass().getSimpleName() + ": " 
                + ex.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                LOG.error("run() ObjectInputStream.close() threw " 
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        }
    }
    
}
