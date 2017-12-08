/**
 * TestDBMgr_TransRead.java 2012-9-6下午3:01:04
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
public class TestDBMgr_TransRead
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
		DBMgr.ExecuteSQL("TRUNCATE TABLE TESTTRANSDATA");
	}

	@Test
	public void Test()
	{
//		//全局数据准备
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
//		
//		//转换数据准备
//		DBMgr.ExecuteSQL("INSERT INTO TESTTRANSDATA (ROLEID,VSTRING,VINT) VALUES(9,'AAA',8888)");
//		DBMgr.ExecuteSQL("INSERT INTO TESTTRANSDATA (ROLEID,VSTRING,VINT) VALUES(10,'BBB',9999)");
//		TestTransData[] trds = DBMgr.ReadSQL(new TestTransData(), "SELECT * FROM TESTTRANSDATA");
//		assertEquals(trds.length, 2);
//		assertEquals(trds[0].vString.Get(), "AAA");
//		assertEquals(trds[0].vInt.Get(), 8888);
//		assertEquals(trds[1].vString.Get(), "BBB");
//		assertEquals(trds[1].vInt.Get(), 9999);
//		
//		//使用转换结构来读取数据
//		TestTransData[] trds1 = DBMgr.ReadTransSQL(new TestTransData(), new TestTransData1(), "SELECT * FROM TESTTRANSDATA");
//		assertEquals(trds1.length, 2);
//		assertEquals(trds1[0].RoleID.Get(), 9);
//		assertEquals(trds1[0].vString.Get(), "b");
//		assertEquals(trds1[0].vInt.Get(), 77);
//		assertEquals(trds1[1].RoleID.Get(), 10);
//		assertEquals(trds1[1].vString.Get(), "a");
//		assertEquals(trds1[1].vInt.Get(), 66);
//		
//		//测试重复读取
//		TestTransData[] trds2 = DBMgr.ReadTransSQL(new TestTransData(), new TestTransData1(), "SELECT * FROM TESTTRANSDATA WHERE ROLEID=9");
//		assertEquals(trds2.length, 1);
//		assertEquals(trds2[0].RoleID.Get(), 9);
//		assertEquals(trds2[0].vString.Get(), "b");
//		assertEquals(trds2[0].vInt.Get(), 77);
	}

}
