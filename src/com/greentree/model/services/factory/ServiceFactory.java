package com.greentree.model.services.factory;

import java.io.IOException;

import com.greentree.model.services.manager.PropertyManager;
import com.greentree.model.services.IService;
import com.greentree.model.exception.ServiceLoadException;

/**
 * Defines methods for returning <code>com.greentree.model.services</code>
 * implementation objects.
 *
 * @author david5MX53G
 *
 */
public class ServiceFactory {

    private static ServiceFactory serviceFactoryInstance;

    /**
     * Creates a new <code>ServiceFactory</code> using the Singleton Gang of
     * Four Pattern
     */
    private ServiceFactory() {
    }

    /**
     * Implements the Singleton Gang of Four pattern because multiple
     * <code>ServiceFactory</code> objects is wasteful
     *
     * @return new <code>{@link ServiceFactory}</code> instance
     */
    public static ServiceFactory getInstance() {
        if (serviceFactoryInstance == null) {
            serviceFactoryInstance = new ServiceFactory();
        }
        return serviceFactoryInstance;
    }

    /**
     * @param name {@link String} used to search the properties file for name of
     * the correct service implementation class
     * @return {@link IService} implementation class corresponding with the
     * given <code>name</code>
     * @throws ServiceLoadException when the named service cannot be found in
     * the properties file
     */
    public IService getService(String name) throws ServiceLoadException {
        IService service = null;
        try {
            Class<?> classy = Class.forName(getImplName(name));
            service = (IService) classy.newInstance();
        } catch (Exception e) {
            throw new ServiceLoadException(e.getMessage(), e);
        }
        return service;
    }

    /**
     * @return fully-qualified name of a com.greetree.model.services
     * implementation class
     * @throws IOException
     * @name used to look up the fully-qualified class in
     * <code>application.properties</code>
     */
    private String getImplName(String name) throws IOException {
        return PropertyManager.getProperty(name);
    }
}
