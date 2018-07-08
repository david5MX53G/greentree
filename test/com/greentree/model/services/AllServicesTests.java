package com.greentree.model.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.greentree.model.services.factory.ServiceFactoryTest;
import com.greentree.model.services.manager.PropertyManagerTest;
import com.greentree.model.services.tokenservice.FileSystemTokenServiceImplTest;

/**
 * This aggregates JUnit test cases for <code>{@link com.greentree.model.services}</code>.
 * 
 * @author david.dietrich
 *
 */
@RunWith(Suite.class)
@SuiteClasses({FileSystemTokenServiceImplTest.class, ServiceFactoryTest.class, 
               PropertyManagerTest.class})
public class AllServicesTests {

}
