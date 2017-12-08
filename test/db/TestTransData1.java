/**
 * TestTransData1.java 2012-9-6下午3:05:55
 */
package db;

import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestTransData1
{
	public DBLong 	RoleID;
	
	@RefField ( Bind = "TestGlobalData#vStr")
	public DBString	vString;
	
	@RefField ( Bind = "TestGlobalData")
	public DBInt	vInt;
}
