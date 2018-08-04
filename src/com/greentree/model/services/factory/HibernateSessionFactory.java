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
package com.greentree.model.services.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * This class is a {@link org.hibernate.SessionFactory} implementation that
 * builds and returns {@link org.hibernate.Session} instances.
 *
 * @author david5MX53G
 */
public class HibernateSessionFactory {

    /**
     * This {@link org.apache.logging.log4j.Logger} is for log messaging.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * This {@link org.hibernate.SessionFactory} will create
     * {@link org.hibernate.Session} instances.
     */
    private static org.hibernate.SessionFactory sessionFactory;

    /**
     * This returns the Hibernate <code>Session</code> Singleton. See
     * <a href="http://docs.jboss.org/hibernate/orm/current/quickstart/html_single/">
     * Hibernate Getting Started Guide</a>. This class relies heavily on config/
     * hibernate.cfg.xml.
     *
     * @return {@link org.hibernate.Session} Singleton
     */
    public static Session currentSession() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder builder = 
                    new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml");
                
                StandardServiceRegistry reg = builder.build();
                
                sessionFactory = new MetadataSources(reg).buildMetadata().buildSessionFactory();
                LOGGER.debug("new SessionFactory: " + sessionFactory.toString());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }

        LOGGER.debug("returning sessionFactory.getCurrentSession()");
        return sessionFactory.getCurrentSession();
    }

    /**
     * Closes the {@link org.hibernate.SessionFactory} of this class
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * Private constructor for building the {@link HibernateSessionFactory}
     * Singleton
     */
    private HibernateSessionFactory() {
    }
}
