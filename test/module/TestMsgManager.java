/**
 * TestMsgManager.java 2012-9-14下午4:59:07
 */
package module;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.MyUser;
import logic.module.common.MsgManager;
import logic.module.common.MsgManager.DataView;

import org.junit.Test;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestMsgManager
{
	public static class Data implements Comparable<Data>
	{
		public Integer id = 0;
		public Type type = Type.Empty;
		public boolean IsDel = false;
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Data d)
		{
			return id.compareTo(d.id) * -1;///<反id排序
		}
	}
	
	public static enum Type
	{
		Empty,
		All,
		Any,
		;
	}
	
	public static class TestClass extends MsgManager<Data,Type>
	{
		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#LoadSQL()
		 */
		@Override
		protected Data[] LoadSQL(MyUser p_User)
		{
			//0 1 2 3 4 9 属于All 
			//5 6 7 8 9 属于Any
			Data[] ds = new Data[10];
			for ( int i = 0; i < 10; ++i )
			{
				ds[i] = new Data();
				ds[i].id = i;
				ds[i].type = i < 5 ? Type.All : Type.Any;
			}
			return ds;
		}

		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#IsDeled(java.lang.Object)
		 */
		@Override
		protected boolean IsDeled(Data t)
		{
			return t.IsDel;
		}

		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#GetDataTypes(java.lang.Object)
		 */
		@Override
		protected Type[] GetDataTypes(Data t)
		{
			if ( t.id == 9 )
			{
				return new Type[]{Type.All, Type.Any};
			}
			return new Type[]{t.type};
		}

		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#IsDataLimit(java.lang.Object, int)
		 */
		@Override
		protected boolean IsDataLimit(Type e, int currnum)
		{
			switch(e)
			{
			case All:return currnum >= 3;
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#InitData(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void InitData(Data t, Data t1)
		{
			t1.id = t.id;
			t1.type = t.type;
		}

		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#InitRemoveData(java.lang.Object)
		 */
		@Override
		protected void InitRemoveData(Data t)
		{
			t.IsDel = true;
		}

		/* (non-Javadoc)
		 * @see logic.module.common.MsgManager#AutoSaveDB()
		 */
		@Override
		protected int AutoSaveDB()
		{
			return 0;
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void Test() throws Exception
	{
		TestClass c = new TestClass();
		c.GetSQLRun().Execute(null);
		
		DataView dv = c.GetDataView(Type.All);
		ArrayList<Data> ds = dv.GetAllData();
		assertEquals(ds.size(), 3);
		
		//9 4 3
		int id = ds.get(0).id;
		assertEquals(id, 9);
		id = ds.get(1).id;
		assertEquals(id, 4);
		id = ds.get(2).id;
		assertEquals(id, 3);
		
		dv = c.GetDataView(Type.Any);
		ds = dv.GetAllData();
		assertEquals(ds.size(), 5);
		//9 8 7 6 5
		id = ds.get(0).id;
		assertEquals(id, 9);
		id = ds.get(4).id;
		assertEquals(id, 5);
		
		//再加入一条id为9的数据
		Data add = new Data();
		add.id = 9;
		c.AddData(add);
		ds = c.GetDataView(Type.All).GetAllData();
		assertEquals(ds.size(), 2);	///<注意这,原来是3条记录,现在变成2条了,那么是把以前老的给挤出去一条
		//数据变成了9 4
		id = ds.get(0).id;
		assertEquals(id, 9);
		id = ds.get(1).id;
		assertEquals(id, 4);
		
		ds = c.GetDataView(Type.Any).GetAllData();
		assertEquals(ds.size(), 5);	///<注意这,原来是5条记录,现在还是5条
		
		add = new Data();
		add.id = 10;
		add.type = Type.All;
		c.AddData(add);
		ds = c.GetDataView(Type.All).GetAllData();
		assertEquals(ds.size(), 3);
		//数据变成了10 9 4
		id = ds.get(0).id;
		assertEquals(id, 10);
		id = ds.get(1).id;
		assertEquals(id, 9);
		id = ds.get(2).id;
		assertEquals(id, 4);
		
		Data del = ds.get(1);
		c.RemoveData(del);
		ds = c.GetDataView(Type.All).GetAllData();
		assertEquals(ds.size(), 2);
		//数据变成了10 4
		id = ds.get(0).id;
		assertEquals(id, 10);
		id = ds.get(1).id;
		assertEquals(id, 4);
	}
}
