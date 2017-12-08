/**
 * TestRefData.java 2012-9-3上午11:01:35
 */
package db;

import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestRefData extends RoleDataBase
{
	public DBLong GID;
	public DBLong RoleID;
	
	@RefField ( Bind = "TestGlobalData")
	public DBInt	vInt;
	
	@RefField ( Bind = "TestGlobalData" )
	public DBString vStr;
}
