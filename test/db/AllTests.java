/**
 * AllTests.java 2012-9-7下午4:43:52
 */
package db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@RunWith(Suite.class)
@SuiteClasses(
{ TestDBMgr.class, TestDBMgr_GlobalData.class, TestDBMgr_TransRead.class })
public class AllTests
{

}
