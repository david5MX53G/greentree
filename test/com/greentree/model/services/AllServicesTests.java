package com.greentree.model.services;

import com.greentree.model.services.factory.HibernateSessionFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.greentree.model.services.factory.ServiceFactoryTest;
//import com.greentree.model.services.manager.JDBCPoolManagerTest;
//import com.greentree.model.services.tokenservice.JDBCTokenServiceImplTest;
import com.greentree.model.services.manager.PropertyManagerTest;
import com.greentree.model.services.tokenservice.FileSystemTokenServiceImplTest;
import com.greentree.model.services.tokenservice.HibernateTokenServiceImplTest;

/**
 * This aggregates JUnit test cases for <code>{@link com.greentree.model.services}</code>.
 * 
 * @author david5MX53G
 *
 */
@RunWith(Suite.class)

@SuiteClasses({
    HibernateSessionFactoryTest.class,
    ServiceFactoryTest.class,
    //JDBCPoolManagerTest.class, //TODO: fix so this works with the HibernateTokenServiceImpl table schema
    PropertyManagerTest.class, 
    FileSystemTokenServiceImplTest.class,
    HibernateTokenServiceImplTest.class
    //JDBCTokenServiceImplTest.class, //TODO: fix so this works with the HibernateTokenServiceImpl table schema
})

public class AllServicesTests {

}
