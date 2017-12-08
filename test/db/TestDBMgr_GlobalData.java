/**
 * TestDBMgr_GlobalData.java 2012-9-3上午10:50:52
 */
package db;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.DBMgr;
import core.RootConfig;
import core.db.sql.DBStores;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestDBMgr_GlobalData
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		RootConfig.GetInstance().Init();
		DBMgr.Init();
		DBStores.GetInstance().RemoveAll();
	}
	
	@Before
	public void setUp() throws Exception
	{
		DBMgr.ExecuteSQL("TRUNCATE TABLE TESTGLOBALDATA");
		DBMgr.ExecuteSQL("TRUNCATE TABLE TESTREFDATA");
		DBMgr.ExecuteSQL("TRUNCATE TABLE TESTREFDATA1");
	}
	
	@Test
	public void TestGloabalRef()
	{
//		//全局数据的测试
//		DBMgr.ExecuteSQL("INSERT INTO TESTGLOBALDATA (GID,ROLEID,vStr,vInt) VALUES (1,10,'a',66)");
//		DBMgr.ExecuteSQL("INSERT INTO TESTGLOBALDATA (GID,ROLEID,vStr,vInt) VALUES (2,9,'b',77)");
//		DBMgr.ExecuteSQL("INSERT INTO TESTGLOBALDATA (GID,ROLEID,vStr,vInt) VALUES (3,8,'c',88)");
//		DBMgr.ExecuteSQL("INSERT INTO TESTGLOBALDATA (GID,ROLEID,vStr,vInt) VALUES (4,7,'d',99)");
//		
//		TestReadGlobalData g = new TestReadGlobalData();
//		
//		DBMgr.LoadAll();
//		
//		assertEquals(g.GetDatas().size(), 4);
//		assertEquals(g.GetDatas().peek().RoleID.Get(), 10);
//		
//		//引用数据的测试
//		
//		//创建一条数据，引用的是全局数据的第一条，根据TESTREFDATA.GID == TESTGLOBALDATA.GID == 1来确定数据
//		DBMgr.ExecuteSQL("INSERT INTO TESTREFDATA (GID,ROLEID) VALUES(1,9)");
//		TestRefData[] trds = DBMgr.ReadSQL(new TestRefData(), "SELECT * FROM TESTREFDATA");
//		assertEquals(trds.length, 1);
//		assertEquals(trds[0].GID.Get(), 1);
//		assertEquals(trds[0].RoleID.Get(), 9);
//		assertEquals(trds[0].vStr.Get(), "a");
//		assertEquals(trds[0].vInt.Get(), 66);
//		
//		g.GetDatas().peek().vInt.Set(67);
//		assertEquals(trds[0].vInt.Get(), 67);
//		trds[0].vInt.Set(68);
//		assertEquals(g.GetDatas().peek().vInt.Get(),68);
//		
//		g.GetDatas().peek().vStr.Set("KFC");
//		assertEquals(trds[0].vStr.Get(), "KFC");
//		trds[0].vStr.Set("QJD");
//		assertEquals(g.GetDatas().peek().vStr.Get(), "QJD");
//		
//		//创建一条数据，引用的是全局数据的第三条，根据TestRefData1.ROLEID == TESTGLOBALDATA.ROLEID == 8来确定数据
//		//这个和上面不同是因为Role字段出现在GID之前
//		DBMgr.ExecuteSQL("INSERT INTO TESTREFDATA1 (ROLEID,GID) VALUES(8,2)");
//		TestRefData1[] trds1 = DBMgr.ReadAllData(new TestRefData1());
//		assertEquals(trds1.length, 1);
//		assertEquals(trds1[0].RoleID.Get(), 8);
//		assertEquals(trds1[0].GID.Get(), 2);
//		assertEquals(trds1[0].vStr.Get(), "c");
//		assertEquals(trds1[0].vInt.Get(), 88);
	}

}
