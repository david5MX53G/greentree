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
import com.greentree.model.services.manager.PropertyManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.xml.sax.SAXException;

/**
 * This class initializes a {@link java.net.ServerSocket} to manage connections
 * from remote clients and pass data between these connections and additional
 * layers of the GreenTree application.
 *
 * @author david5MX53G
 */
public class GreenTreeServer {

    /**
     * This {@link org.apache.logging.log4j.Logger} is good for logging!
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * This private constructor is not used because the class has only a single
     * static method, and static methods do not require constructors.
     */
    private GreenTreeServer() {
    }

    /**
     * This opens a new {@link java.net.ServerSocket}.
     */
    public static void start() {
        LOG.info("started");
        int i = 1;
        ServerSocket s;
        GreenTreeServerHandler handler;
        int port;
        
        try {
            port = Integer.valueOf(PropertyManager.getProperty("port"));
            s = new ServerSocket(port);
            LOG.debug("listening on port " + String.valueOf(port));

            // annotated Spring Inversion of Control (IoC) Container
            //ApplicationContext annoctx = 
            //  new AnnotationConfigApplicationContext(AppConfig.class);
            // XML Spring Inversion of Control (IoC) Container
            ApplicationContext fsxmlctx = new FileSystemXmlApplicationContext(
                "config/applicationContext.xml"
            );

            LOG.debug(" ApplicationContext "
                + (fsxmlctx instanceof ApplicationContext));

            GreenTreeManager mngr = fsxmlctx.getBean(GreenTreeManager.class);

            LOG.debug(" GreenTreeManager " 
                + (mngr instanceof GreenTreeManager));

            while (true) {
                Socket socket = s.accept();
                LOG.debug("Spawning thread " + i);
                handler = new GreenTreeServerHandler(socket, mngr);
                handler.start();
                LOG.debug("GreenTreeServerHandler done");
                i++;
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            LOG.error("start() threw " + ex.getClass().getSimpleName() + ": "
                + ex.getMessage());
        }
        LOG.info("stopped");
    }

    /**
     * This runs {@link GreenTreeServer#start()}.
     *
     * @param args does nothing
     */
    public static void main(String[] args) {
        GreenTreeServer.start();
    }
}
