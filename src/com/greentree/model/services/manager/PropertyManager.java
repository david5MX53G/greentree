package com.greentree.model.services.manager;

import java.io.IOException;
import java.util.Properties;

/**
 * This class loads and provides access to {@link Properties} for business and service layers.
 * 
 * @author david.dietrich
 *
 */
public class PropertyManager {
	/** {@link java.util.Properties} are stored here */
	private static Properties properties;
	
	/** default file for reading {@link java.util.Properties} */
	private static final String PROPSPATH = "config/application.properties";
	
	/**
	 * Loads the static <code>{@link PropertyManager#properties}</code> variable from the given 
	 * file path.
	 * 
	 * @param filePath identifies the path to a file with property values.
	 * @throws IOException when no valid file is found at the given location
	 */
	public static void loadProperties(String filePath) throws IOException {
            properties = new java.util.Properties();
            try (java.io.FileInputStream file = new java.io.FileInputStream(filePath)) {
                    properties.load(file);
            } catch (IOException e) {
                    String msg = "PropertyManager " + e.getClass().getSimpleName() + ": "
                    + "new java.io.FileInputStream(" + filePath + ") :" + e.getMessage();
                    throw new IOException(msg);
            }
	}

	/**
	 * loads properties from the default location defined in the static variable of this class.
	 * 
	 * @throws IOException when the default file cannot be read
	 */
	public static void loadProperties() throws IOException {
            properties = new java.util.Properties();
            try (java.io.FileInputStream fis = new java.io.FileInputStream(PROPSPATH)) {
                properties.load(fis);
            } catch ( IOException e) {
                String msg = "PropertyManager:" + e.getClass().getSimpleName() + ": " + 
                properties.toString();
                throw new IOException(msg);
            }
	}
	
	/**
	 * Returns the value of the given {@link Property} from the properties file. The path to this 
	 * file is given {@link PropertyManager#PropertyManager()} construct.
	 * 
	 * @param propertyName identifies the key in the properties file from which to return the value
	 * @return value of the given key in the properties file
	 * @throws IOException when the default properties file fails to load
	 */
	public static String getProperty(String propertyName) throws IOException {
            if (!(properties instanceof java.util.Properties)) {
                loadProperties();
            }
            return properties.getProperty(propertyName);
	}
}
