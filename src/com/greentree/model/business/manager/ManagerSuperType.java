package com.greentree.model.business.manager;

import java.io.IOException;

import com.greentree.model.services.IService;
import com.greentree.model.services.manager.PropertyManager;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * provides behavior for getting <code>{@link IService}</class> objects.
 *
 * @author david5MX53G
 *
 */
public abstract class ManagerSuperType {
    /** 
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    protected static Logger LOG = LogManager.getLogger();

    /**
     * static initializer block for getting <code>{@link Properties}</code>
     */
    static {
        ManagerSuperType.loadProperties();
    }

    /**
     * This is simply a wrapper for {@link PropertyManager#loadProperties()}
     * that returns booleans instead of <code>Exception</code> objects to avoid
     * leaking system internals to the UI layer.
     *
     * @return false when trouble occurs, such as when the {@link Property} file
     * fails
     */
    public static boolean loadProperties() {
        boolean success;
        try {
            PropertyManager.loadProperties();
            success = true;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            String msg = e.getClass().getSimpleName() + ": " + e.getMessage();
            LOG.debug(msg);
            success = false;
        }
        return success;
    }

    /**
     * Loads a <code>{@link IService}</code> object based on the given command.
     *
     * @param name identifies the <code>IService</code> to load
     * @return {@link boolean} indicating whether {@link IService} registration
     * was successful
     */
    public abstract boolean registerService(String name);
}
