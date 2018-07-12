package com.greentree.model.business;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.greentree.model.business.manager.GreenTreeManagerTest;

/**
 * JUnit test suite for <code>{@link com.greentree.model.business}</code>
 * 
 * If you get this...
 * 
 * PropertyFileNotFoundException in ManagerSuperType class: unable to load properties from file 
 * path null
 * 
 * ...you need to pass in the path to the properties file in the config dir of this project using 
 * -D, like this:
 * 
 * -Dprop_location="C:\Users\david\Documents\eclipse-workspace\GreenTree\config\application
 * .properties"
 * 
 * @author david5MX53G
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ GreenTreeManagerTest.class})
public class AllBusinessTests {

}
