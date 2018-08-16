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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class connects {@link GreenTreeServer} to {@link GreenTreeManager}.
 *
 * @author david5MX53G
 */
class GreenTreeServerHandler extends Thread {

    /**
     * This {@link org.apache.logging.log4j.Logger} is good for logging!
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * This is used for logging to identify which method is logging messages
     */
    private static String methodName;

    /**
     * This {@link java.net.Socket} receives incoming client connections.
     */
    private final Socket socket;

    /**
     * This {@link GreenTreeManager} interfaces with all other layers of the
     * application.
     */
    private static final GreenTreeManager MGR = GreenTreeManager.getInstance();

    GreenTreeServerHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * This reads the first line of input from the client as a command string.
     * This string is checked against a series of conditions, and one of the
     * {@link GreenTreeManager} methods is invoked accordingly.
     */
    @Override
    public void run() {
        methodName = "void run() ";
        LOG.debug(methodName + "started");
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            if (in instanceof ObjectInputStream
                && out instanceof ObjectOutputStream) {
                LOG.debug(methodName
                    + "ObjectInputStream and ObjectOutputStream acquired");
            } else {
                throw new IOException(methodName
                    + "failed to acquire ObjectInputStream/ObjectOutputStream");
            }

            String command = (String) in.readObject();
            LOG.info(methodName + command);

            if ("registerToken(RSAPublicKey, String)".equals(command)) {
                RSAPublicKey key = (RSAPublicKey) in.readObject();
                String ciphertext = (String) in.readObject();
                out.writeObject(MGR.registerToken(key, ciphertext));
                out.flush();
                LOG.info(methodName + "returned "
                    + "GreenTreeManager.registerToken(RSAPublicKey, String)");
            }

            else if ("registerService(String)".equals(command)) {
                String service = (String) in.readObject();
                out.writeObject(MGR.registerService(service));
                out.flush();
                LOG.info(methodName + "returned "
                    + "GreenTreeManager.registerService(String)");
            }

            else if ("registerToken(String)".equals(command)) {
                String plaintext = (String) in.readObject();
                out.writeObject(MGR.registerToken(plaintext));
                out.flush();
                LOG.info(methodName + "returned "
                    + "GreenTreeManager.registerToken(String)");
            }

            else if ("getPublicKey()".equals(command)) {
                out.writeObject(MGR.getPublicKey());
                out.flush();
                LOG.info(methodName + "returned "
                    + "GreenTreeManager.getPublicKey()");
            }
            
            else if ("getData(RSAPublicKey)".equals(command)) {
                RSAPublicKey key = (RSAPublicKey) in.readObject();
                out.writeObject(MGR.getData(key));
                LOG.info(methodName + " returned " 
                    + "GreenTreeManager.getData(RSAPublicKey)");
            }
            
            else if ("addBlock(String, RSAPublicKey, long, long)".equals(command)) {
                String msg = (String) in.readObject();
                RSAPublicKey key = (RSAPublicKey) in.readObject();
                long notBefore = (long) in.readObject();
                long notAfter = (long) in.readObject();
                out.writeObject(MGR.addBlock(msg, key, notBefore, notAfter));
                LOG.info(methodName + " returned " 
                    + "GreenTreeManager.addBlock(msg, key, notBefore, notAfter)");
            }
            
            else {
                LOG.error(methodName + "unrecognized command received: " 
                    + command);
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
                LOG.error(methodName + "threw " + ex.getClass().getSimpleName() 
                    + ": " + ex.getMessage());
            }
        }
    }

}
