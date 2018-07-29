package com.greentree.model.services;

import com.greentree.model.services.factory.HibernateSessionFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.greentree.model.services.factory.ServiceFactoryTest;
import com.greentree.model.services.manager.JDBCPoolManagerTest;
import com.greentree.model.services.manager.PropertyManagerTest;
import com.greentree.model.services.tokenservice.FileSystemTokenServiceImplTest;
import com.greentree.model.services.tokenservice.HibernateTokenServiceImplTest;
import com.greentree.model.services.tokenservice.JDBCTokenServiceImplTest;

/**
 * This aggregates JUnit test cases for <code>{@link com.greentree.model.services}</code>.
 * 
 * @author david5MX53G
 *
 */
@RunWith(Suite.class)

@SuiteClasses({
    ServiceFactoryTest.class,
    PropertyManagerTest.class, 
    FileSystemTokenServiceImplTest.class,
    //TODO: fix JDBC tests so they work with the HibernateTokenServiceImpl table schema
    //JDBCPoolManagerTest.class, 
    //JDBCTokenServiceImplTest.class,
    HibernateTokenServiceImplTest.class,
    HibernateSessionFactoryTest.class
})

public class AllServicesTests {

}
