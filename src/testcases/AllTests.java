package testcases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ServerStart.class, WelcomeScreenTest.class,
		CentralBoardTest.class, ClanBoardTest.class })
// , ClanDirectoryTest.class})
public class AllTests {

}
