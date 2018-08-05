package com.greentree.model.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This aggregates JUnit test cases for <code>{@link com.greentree.model.domain}</code>.
 * 
 * @author david5MX53G
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ 
    BlockTest.class, 
    ClaimTest.class, 
    TokenTest.class 
})
public class AllDomainTests {

}
