package com.greentree.model.services.manager;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * PropertyManagerTest tests the {@link 
 * com.greentree.model.services.manager.PropertyManager} class.
 * 
 * @author david.dietrich
 */
public class PropertyManagerTest {
    boolean loadPropertiesTest = false;
    
    /** log4j 2 logger */
    Logger logger = LogManager.getLogger();
    
    /**
     * loadPropertiesTest() tests {@link PropertyManager#loadProperties()} and 
     * {@link PropertyManager#loadProperties(java.lang.String)}.
     * 
     * @throws IOException when "config/application.properties" cannot be loaded
     */
    @Test
    public void loadPropertiesTest() throws IOException {
        PropertyManager.loadProperties();
        PropertyManager.loadProperties("config/application.properties");
        loadPropertiesTest = true;
        assertTrue(loadPropertiesTest);
        logger.debug("loadPropertiesTest() PASSED");
    }
    
    /** 
     * getPropertyTest() tests {@link 
     * PropertyManager#getProperty(java.lang.String)}.
     * 
     * @throws IOException from {@link PropertyManager#loadProperties()}
     */
    @Test public void getPropertyTest() throws IOException {
        if (loadPropertiesTest == false) {
            loadPropertiesTest();
        }
        
        assertTrue(PropertyManager.getProperty("ITokenService") 
                   instanceof String);
        assertTrue(PropertyManager.getProperty("TokenFilesLocation")
                   instanceof String);
        logger.debug("getPropertyTest() PASSED");
    }
}
