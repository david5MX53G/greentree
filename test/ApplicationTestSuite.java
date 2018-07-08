import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.greentree.model.business.AllBusinessTests;
import com.greentree.model.domain.AllDomainTests;
import com.greentree.model.services.AllServicesTests;

@RunWith(Suite.class)
@SuiteClasses({AllBusinessTests.class, AllServicesTests.class, AllDomainTests.class})
public class ApplicationTestSuite {}
