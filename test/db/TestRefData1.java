/**
 * TestRefData1.java 2012-9-3上午11:31:35
 */
package db;

import core.db.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestRefData1 extends RoleDataBase
{
	public DBLong RoleID;
	public DBLong GID;
	
	@RefField ( Bind = "TestGlobalData")
	public DBInt	vInt;
	
	@RefField ( Bind = "TestGlobalData" )
	public DBString vStr;
}