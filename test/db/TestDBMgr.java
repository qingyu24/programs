/**
 * TestDBMgr.java 2012-8-30下午3:55:47
 */
package db;

import static org.junit.Assert.*;

import org.junit.*;

import utility.Rand;

import core.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestDBMgr
{
	private static final TestUData m_Seed = new TestUData();
	private static final TestUData_Role m_SeedRole = new TestUData_Role();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		RootConfig.GetInstance().Init();
		DBMgr.Init();
	}
	
	@Before
	public void setUp() throws Exception
	{
		DBMgr.ExecuteSQL("TRUNCATE TABLE TestUData");
		DBMgr.ExecuteSQL("TRUNCATE TABLE TestUData_ROLE");
	}
	
	/**
	 * 用UserID读写数据库
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void TestUserIDData()
	{
		//创建一个结果
		int userid = 23423;
			
		TestUData d = DBMgr.CreateUserData(userid, m_Seed);
		assertNotNull(d);
		
		assertEquals(d.UserID.Get(), userid);
		assertEquals(d.AutoInt.Get(), 1);
		
		//读取出来的结果和创建的比较
		TestUData d1 = DBMgr.ReadUserData(userid, m_Seed);
		assertEquals(d.UserID.Get(), d1.UserID.Get());
		assertEquals(d.AutoInt.Get(), d1.AutoInt.Get());
		
		//修改创建的记录
		byte[] bs = Rand.CreateBytes(65535);
		d.Buffer.Set(bs);
		
		long tm = System.currentTimeMillis();
		d.DataTime.Set(tm);
		
		float f = 3.1415926f;
		d.vFloat.Set(f);
		
		long v = Rand.Get();
		d.vLong.Set(v);
		
		short v1 = (short) Rand.GetIn100();
		d.vShort.Set(v1);
		
		String s = "的说法AAAAAA爱的发的";
		d.UserName.Set(s);
		
		byte[] bs1 = Rand.CreateBytes(255);
		d.TBuffer.Set(bs1);
		
		//保存记录
		boolean c = DBMgr.UpdateUserIDData(d);
		assertTrue(c);
		
		//重新读取
		d1 = DBMgr.ReadUserData(userid, m_Seed);
		
		//再次比较
		assertEquals(d1.UserName.Get(), s);
		assertArrayEquals(d1.Buffer.GetBytes(), bs);
		assertTrue(Math.abs(f - d1.vFloat.Get()) < 0.0001);
		assertTrue(v == d1.vLong.Get());
		assertTrue(v1 == d1.vShort.Get());
		assertArrayEquals(bs1, d1.TBuffer.GetBytes());
		//这个地方需要注意，把一个时间保存进入数据库后，会把毫秒级的数据去掉
		assertTrue( Math.abs(d1.DataTime.GetMillis()-tm) < 1000);
		//DBUniqueLong只有作为key的时候才会插入数据库，所以重新读取上来就会丢失原数据
		assertEquals(d1.ULong.Get(), 0);
	}
	
	/**
	 * 用UserName读写数据库
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void TestUserNameData()
	{
		//创建一个结果
		String username = "23423";
		
		TestUData d = DBMgr.CreateUserData(username, m_Seed);
		assertNotNull(d);
		
		assertEquals(d.UserName.Get(), username);
		assertEquals(d.AutoInt.Get(), 1);
		
		//读取出来的结果和创建的比较
		TestUData d1 = DBMgr.ReadUserData(username, m_Seed);
		assertEquals(d.UserID.Get(), d1.UserID.Get());
		assertEquals(d.AutoInt.Get(), d1.AutoInt.Get());
		
		//修改创建的记录
		byte[] bs = Rand.CreateBytes(65535);
		d.Buffer.Set(bs);
		
		long tm = System.currentTimeMillis();
		d.DataTime.Set(tm);
		
		float f = 3.1415926f;
		d.vFloat.Set(f);
		
		long v = Rand.Get();
		d.vLong.Set(v);
		
		short v1 = (short) Rand.GetIn100();
		d.vShort.Set(v1);
		
		byte[] bs1 = Rand.CreateBytes(255);
		d.TBuffer.Set(bs1);
		
		//保存记录
		boolean c = DBMgr.UpdateUserNameData(d);
		assertTrue(c);
		
		//重新读取
		d1 = DBMgr.ReadUserData(username, m_Seed);
		
		//再次比较
		assertArrayEquals(d1.Buffer.GetBytes(), bs);
		assertTrue(Math.abs(f - d1.vFloat.Get()) < 0.0001);
		assertTrue(v == d1.vLong.Get());
		assertTrue(v1 == d1.vShort.Get());
		assertArrayEquals(bs1, d1.TBuffer.GetBytes());
		//这个地方需要注意，把一个时间保存进入数据库后，会把毫秒级的数据去掉
		assertTrue( Math.abs(d1.DataTime.GetMillis()-tm) < 1000);
		//DBUniqueLong只有作为key的时候才会插入数据库，所有重新读取上来就会丢失原数据
		assertEquals(d1.ULong.Get(), 0);
	}
	
	/**
	 * 测试用Role读写数据库
	 */
	@Test
	public void TestRoleData()
	{
		//创建一个结果
		long roleid = 78947893432L;
		
		TestUData_Role d = DBMgr.CreateRoleData(roleid, m_SeedRole);
		assertNotNull(d);
		
		assertEquals(d.RoleID.Get(), roleid);
		
		//这时候从db里肯定无法读取出来数据
		TestUData_Role[] d1 = DBMgr.ReadRoleData(roleid, m_SeedRole);
		assertEquals(d1.length, 0);
		
		//修改创建的记录
		byte[] bs = Rand.CreateBytes(65535);
		d.Buffer.Set(bs);
		
		long tm = System.currentTimeMillis();
		d.DataTime.Set(tm);
		
		float f = 3.1415926f;
		d.vFloat.Set(f);
		
		long v = Rand.Get();
		d.vLong.Set(v);
		
		short v1 = (short) Rand.GetIn100();
		d.vShort.Set(v1);
		
		byte[] bs1 = Rand.CreateBytes(255);
		d.TBuffer.Set(bs1);
		
		long uv = d.ULong.Get();
		
		//保存记录
		boolean c = DBMgr.UpdateRoleData(d);
		assertTrue(c);
		
		//重新读取
		TestUData_Role[] ds = DBMgr.ReadRoleData(roleid, m_SeedRole);
		
		//再次比较
		assertArrayEquals(d.Buffer.GetBytes(), bs);
		assertTrue(Math.abs(f - ds[0].vFloat.Get()) < 0.0001);
		assertTrue(v == d.vLong.Get());
		assertTrue(v1 == d.vShort.Get());
		assertArrayEquals(bs1, d.TBuffer.GetBytes());
		//这个地方需要注意，把一个时间保存进入数据库后，会把毫秒级的数据去掉
		assertTrue( Math.abs(ds[0].DataTime.GetMillis()-tm) < 1000);
		assertEquals(ds[0].ULong.Get(), uv);
	}
	
	@Test
	public void TestGID()
	{
		DBMgr.ExecuteSQL("TRUNCATE TABLE TestGID");
		
		TestGID d = DBMgr.CreateRoleDataByGID(new TestGID());
		assertNotNull(d);
		
		d.vStr.Set("abcd");
		long v = d.GID.Get();
		
		boolean c = DBMgr.UpdateRoleData(d);
		assertTrue(c);
		
		TestGID[] ds = DBMgr.ReadSQL(new TestGID(), "SELECT * FROM TestGID");
		assertEquals(ds.length, 1);
		assertEquals(ds[0].GID.Get(), v);
		assertEquals(ds[0].vStr.Get(), "abcd");
	}
}
