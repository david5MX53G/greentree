package com.greentree.model.business.manager;

import java.io.IOException;

import com.greentree.model.services.IService;
import com.greentree.model.services.manager.PropertyManager;

/**
 * provides behavior for getting <code>{@link IService}</class> objects.
 * 
 * @author david.dietrich
 *
 */
public abstract class ManagerSuperType {
	/** identifies log messages from this class */
	private static String logPrefix = "ManagerSuperType";
	
	/** static initializer block for getting <code>{@link Properties}</code> */ 
	static {
		ManagerSuperType.loadProperties();
	}
	
	/**
	 * Loads a <code>{@link IService}</code> object based on the given command.
	 * 
	 * @param name identifies the <code>IService</code> to load
	 * @return {@link boolean} indicating whether {@link IService} registration was successful
	 */
	public abstract boolean registerService(String name);
	
	/**
	 * This is simply a wrapper for {@link PropertyManager#loadProperties()} that returns booleans 
	 * instead of <code>Exception</code> objects to avoid leaking system internals to the UI layer.
	 * 
	 * @return false when trouble occurs, such as when the {@link Property} file fails
	 */
	public static boolean loadProperties() {
		boolean success = false;
		try {
			PropertyManager.loadProperties();
			success = true;
		} catch (IOException e) {
			String msg = e.getClass().getSimpleName() + ": " + e.getMessage();
			System.out.println(logPrefix + ": " + msg);
		}
		return success;
	}
}