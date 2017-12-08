/**
 * TestDBMgr_AotuSaveGlobalData.java 2012-9-14上午11:14:26
 */
package db;

import static org.junit.Assert.*;
import logic.MyRoot;
import logic.userdata.globalData.GlobalData;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utility.*;

import core.*;
import core.db.sql.*;
import core.detail.Mgr;
import core.detail.impl.mgr.LogicMgrImpl;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestDBMgr_AotuSaveGlobalData
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		RootConfig.GetInstance().Init();
		DBMgr.Init();
		DBStores.GetInstance().RemoveAll();
		new MyRoot();
		LogicMgrImpl l = (LogicMgrImpl) Mgr.GetLogicMgr();
		new Thread(l).start();
	}

	@Before
	public void setUp() throws Exception
	{
		DBMgr.ExecuteSQL("DELETE FROM GLOBALDATA WHERE ROLEID>100000");
	}
	
	public static class GDataSaver extends DBLoaderEx<GlobalData>
	{
		private static long roleid = 100001;
		private static int lastlv = Rand.Get();
		private GlobalData m_Test;
		public GDataSaver()
		{
			super(new GlobalData(), true);
		}
		
		public void Add()
		{
			System.out.println("Add");
			GlobalData d = DBMgr.CreateGlobalRoleData(roleid++, new GlobalData());
			d.NickName.Set("test");
			lastlv = Rand.Get();
			d.RoleLV.Set(lastlv);
			d.LoginTime.Set(System.currentTimeMillis());
			
			Add(d);
			
			m_Test = d;
		}
		
		public void Read()
		{
			GlobalData[] gs = DBMgr.ReadSQL(new GlobalData(), "SELECT * FROM GLOBALDATA WHERE ROLEID="+(roleid-1));
			assertEquals(gs.length, 1);
			assertEquals(gs[0].RoleLV.Get(), lastlv);
		}
		
		public void Modify()
		{
			lastlv = Rand.Get();
			m_Test.RoleLV.Set(lastlv);
		}
	}

	@Test
	public void test() throws InterruptedException
	{
		GDataSaver s = new GDataSaver();
		DBMgr.LoadAll();
		s.Add();
		
		Thread.sleep(3500);
		
		s.Read();
		
		s.Modify();
		Thread.sleep(3500);
		s.Read();
	}

}
