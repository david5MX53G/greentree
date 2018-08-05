package com.greentree.model.services.manager;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * PropertyManagerTest tests the {@link 
 * com.greentree.model.services.manager.PropertyManager} class.
 * 
 * @author david5MX53G
 */
public class PropertyManagerTest {
    boolean loadPropertiesTest = false;
    
    /** log4j 2 logger */
    Logger logger = LogManager.getLogger();
    
    /**
     * This tests {@link PropertyManager#loadProperties()}.
     * 
     * @throws IOException when "config/application.properties" cannot be loaded
     * 
     * @throws javax.xml.parsers.ParserConfigurationException if {@link 
     *         PropertyManager#loadProperties()} fails
     * 
     * @throws org.xml.sax.SAXException if {@link 
     *         PropertyManager#loadProperties()} fails
     */
    @Test
    public void loadPropertiesTest() 
        throws IOException, ParserConfigurationException, SAXException {
        assertTrue(PropertyManager.loadProperties());
        logger.debug("loadPropertiesTest() PASSED");
    }
    
    /** 
     * getPropertyTest() tests {@link 
     * PropertyManager#getProperty(java.lang.String)}.
     * 
     * @throws IOException from {@link PropertyManager#loadProperties()}
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     */
    @Test public void getPropertyTest() 
        throws IOException, ParserConfigurationException, SAXException {
        assertTrue(PropertyManager.getProperty("ITokenService")
                   instanceof String);
        
        assertTrue(PropertyManager.getProperty("tokenfilepath")
                   instanceof String);
        
        assertTrue(PropertyManager.getProperty("jdbc.url")
                   instanceof String);
        
        assertTrue(PropertyManager.getProperty("jdbc.user")
                   instanceof String);
        
        assertTrue(PropertyManager.getProperty("jdbc.pass")
                   instanceof String);
        
        assertTrue(PropertyManager.getProperty("jdbc.minpoolsize")
                   instanceof String);
        
        assertTrue(PropertyManager.getProperty("jdbc.maxpoolsize")
                   instanceof String);
        
        logger.debug("getPropertyTest() PASSED");
    }
}
