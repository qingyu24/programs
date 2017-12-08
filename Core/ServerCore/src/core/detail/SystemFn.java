/**
 * System.java 2012-8-22下午4:39:19
 */
package core.detail;

import java.io.*;
import java.lang.reflect.*;
import java.nio.channels.*;
import java.sql.*;
import java.util.*;

//import logic.userdata.login.LoginUserData;

import utility.*;

import core.db.*;
import core.db.sql.DBUniqueLongManager;
import core.detail.impl.log.Log;
import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 *          和utility提供的功能类似,一个系统用,一个是逻辑模块用
 */
/**
 * @author ddoq
 * @version 1.0.0
 * 
 */
public class SystemFn
{
	/**
	 * 清理所有改变标志,t所有的字段必须是DBValue类型
	 * 
	 * <br>
	 * 测试代码:{@link }
	 * 
	 * @param t
	 * @param setref
	 *            是否对引用字段也设置
	 */
	public static <T> void ClearChangeFlag(T t, boolean setref)
	{
		Field[] fs = t.getClass().getFields();
		for (Field f : fs)
		{
			RefField ref = f.getAnnotation(RefField.class);
			if (!setref && ref != null)
			{
				continue;
			}

			DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
			fd.Changed(false);
		}
	}

	public static void ClosePreparedStatement(PreparedStatement s)
	{
		if ( s == null )
		{
			return;
		}
		try
		{
			s.close();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
	}
	
	/**
	 * 关闭数据库的结果集
	 */
	public static void CloseResultSet(ResultSet resultSet)
	{
		if ( resultSet == null )
		{
			return;
		}
		try
		{
			resultSet.close();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
	}

	/**
	 * 关闭一个SocketChannel
	 * 
	 * @return true表示正常关闭
	 */
	public static boolean CloseSocketChannel(SocketChannel p_Sc)
	{
		if (p_Sc != null && p_Sc.isConnected())
		{
			try
			{
				p_Sc.close();
				return true;
			}
			catch (IOException e)
			{
				Log.out.LogException(e);
			}
		}
		return false;
	}

	public static Object CreateDBUniqueLong(long v)
	{
		Constructor<?> cs = GetOneParamConstructor(DBUniqueLong.class);
		try
		{
			cs.setAccessible(true);
			return cs.newInstance(v);
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBUniqueLong创建对象失败");
		}
		catch (InstantiationException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBUniqueLong创建对象失败");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBUniqueLong创建对象失败");
		}
		catch (InvocationTargetException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBUniqueLong创建对象失败");
		}
	}
	
	/**
	 * 根据c的类型创建一个DBValue的对象,使用的是无参数的构造函数
	 * 
	 * <br>
	 * 测试代码:{@link }
	 * 
	 * @param c
	 *            用来创建的类型,这个类型必须是DBValue的某个派生类型
	 */
	public static Object CreateDBValue(Class<?> c)
	{
		Debug.Assert(IsHaveInterface(c, DBValue.class), "类型[" + c + "]必须是DBValue的派生类型");
		Constructor<?> cs = GetNoParamConstructor(c);
		try
		{
			cs.setAccessible(true);
			return cs.newInstance();
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (InstantiationException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (InvocationTargetException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
	}

	/**
	 * 根据c的类型创建一个DBValue的对象,使用的是1参数的构造函数
	 * 
	 * @param c
	 *            用来创建的类型,这个类型必须是DBValue的某个派生类型
	 * @param v
	 *            要设置的值
	 * @return
	 */
	public static Object CreateDBValue(Class<?> c, Object v)
	{
		Debug.Assert(IsHaveInterface(c, DBValue.class), "类型[" + c + "]必须是DBValue的派生类型");
		Constructor<?> cs = GetOneParamConstructor(c);
		try
		{
			cs.setAccessible(true);
			return cs.newInstance(v);
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (InstantiationException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (InvocationTargetException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
	}

	public static Object CreateDBValue(Class<?> c, ResultSet resultSet, String fieldname)
	{
		Debug.Assert(IsHaveInterface(c, DBValue.class), "类型[" + c + "]必须是DBValue的派生类型");
		Constructor<?> cs = GetTwoParamConstructor(c);
		try
		{
			cs.setAccessible(true);
			return cs.newInstance(resultSet, fieldname);
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (InstantiationException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
		catch (InvocationTargetException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateDBValue 使用类型[" + c + "]创建对象失败");
		}
	}

	/**
	 * 给对象t的字段创建一个值(使用默认构造函数),字段的类型必须为DBValue
	 */
	public static <T> Object CreateFieldValue(T t, Field f)
	{
		Object v = SystemFn.CreateDBValue(f.getType());
		return SystemFn.SetFieldValue(t, f, v);
	}
	
	/**
	 * 给对象t的字段创建一个值(使用直接设值的构造函数),字段的类型必须为DBValue
	 */
	public static <T> Object CreateFieldValue(T t, Field f, Object v)
	{
		Object v1 = SystemFn.CreateDBValue(f.getType(), v);
		return SystemFn.SetFieldValue(t, f, v1);
	}

	/**
	 * 给对象t的字段创建一个值(使用数据库的返回结果),字段的类型必须为DBValue
	 */
	public static <T> Object CreateFieldValue(T t, Field f, ResultSet resultSet)
	{
		Object v = SystemFn.CreateDBValue(f.getType(), resultSet, f.getName());
		return SystemFn.SetFieldValue(t, f, v);
	}

	/**
	 * 创建一个DBUniqueLong的值,以角色id为基础递增而获取
	 */
	public static <T> Object CreateFieldValueUniqueLong(T t, Field f, long p_lMarkID)
	{
		String tbname = GetClassName(t.getClass());
		long setv = DBUniqueLongManager.GetInstance().GetNextValue(tbname, p_lMarkID);
		Object v = SystemFn.CreateDBUniqueLong(setv);
		return SystemFn.SetFieldValue(t, f, v);
	}

	/**
	 * 用类型c来创建一个对象
	 */
	public static <T> T CreateObject(Class<?> c)
	{
		try
		{
			@SuppressWarnings("unchecked")
			T t = (T) c.newInstance();
			return t;
		}
		catch (InstantiationException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateObject 类型[" + c + "]");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateObject 类型[" + c + "]");
		}
	}

	/**
	 * 创建v带Ref的版本对象
	 */
	public static Object CreateRefObject(Object v)
	{
		if ( v.getClass() == DBInt.class )
		{
			return new DBIntRef((DBInt) v);
		}
		Debug.Assert(false, "不支持的类型:"+v.getClass());
		return null;
	}

	/**
	 * 得到t中所有改变了的字段,t只能包含DBValue的类型
	 */
	public static <T> ArrayList<Field> GetAllChangeField(T t)
	{
		ArrayList<Field> all = new ArrayList<Field>();
		Field[] fs = t.getClass().getFields();
		for (Field f : fs)
		{
			RefField r = f.getAnnotation(RefField.class);
			if ( r != null )
			{
				continue;
			}
			DBValue v = (DBValue)SystemFn.GetFieldValue(t, f);
			if ( v.IsChanged() )
			{
				all.add(f);
			}
		}
		return all;
	}

	/**
	 * 得到t中所有改变了的字段名字集合,以逗号分隔,t只能包含DBValue的类型
	 */
	public static <T> String GetAllChangeFieldName(T t)
	{
		StringBuilder s = new StringBuilder();
		Field[] fs = t.getClass().getFields();
		for (Field f : fs)
		{
			RefField r = f.getAnnotation(RefField.class);
			if ( r != null )
			{
				continue;
			}
			DBValue v = (DBValue)SystemFn.GetFieldValue(t, f);
			if ( v.IsChanged() )
			{
				s.append(f.getName()).append(",");
			}
		}
		return s.toString();
	}
	
	/**
	 * 获取类型的简单名称
	 * 
	 * @param c
	 *            类型
	 */
	public static String GetClassName(Class<?> c)
	{
		return Str.GetLastStr(c.getName(), '.');
	}

	/**
	 * 得到"值"
	 */
	public static Object GetFieldValue(DBValue v)
	{
		Field f1;
		try
		{
			f1 = v.getClass().getDeclaredField("m_Value");
			f1.setAccessible(true);
			return f1.get(v);
		}
		catch (SecurityException e)
		{
			throw new WrapRuntimeException("SystemFn#GetFieldValue 获取值失败[" + v + "]");
		}
		catch (NoSuchFieldException e)
		{
			throw new WrapRuntimeException("SystemFn#GetFieldValue 获取值失败[" + v + "]");
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException("SystemFn#GetFieldValue 获取值失败[" + v + "]");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException("SystemFn#GetFieldValue 获取值失败[" + v + "]");
		}
	}

	public static Object GetFieldValue(Object o, String fieldName)
	{
		Field f1 = null;
		try
		{
			Field[] fs = o.getClass().getDeclaredFields();
			for ( Field f : fs )
			{
				if ( f.getName().equals(fieldName) )
				{
					f1 = f;
					f1.setAccessible(true);
					break;
				}
			}
		}
		catch (SecurityException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#GetFieldValue 对象[" + o + "] 字段[" + fieldName + "]");
		}
		if ( f1 != null )
		{
			return GetFieldValue(o, f1);
		}
		return null;
	}
	
	/**
	 * 得到字段的值
	 */
	public static <T> Object GetFieldValue(T t, Field f)
	{
		try
		{
			return f.get(t);
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#GetFieldValue 对象[ + " + t + " + ]字段[" + f + "]");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#GetFieldValue 对象[ + " + t + " + ]字段[" + f + "]");
		}
	}

	public static String GetIP(SocketChannel c)
	{
		try
		{
			if ( c != null )
			{
				return c.socket().getInetAddress().getHostAddress();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 得到无参数的构造函数
	 * 
	 * @return 如果无法得到这种构造函数,返回null
	 */
	public static Constructor<?> GetNoParamConstructor(Class<?> c)
	{
		Constructor<?>[] cs = c.getDeclaredConstructors();
		for (Constructor<?> c1 : cs)
		{
			if (c1.getGenericParameterTypes().length == 0)
			{
				c1.setAccessible(true);
				return c1;
			}
		}
		return null;
	}

	/**
	 * 得到只有一个参数的构造函数
	 * 
	 * @return 如果无法得到这种构造函数,返回null
	 */
	public static Constructor<?> GetOneParamConstructor(Class<?> c)
	{
		Constructor<?>[] cs = c.getDeclaredConstructors();
		for (Constructor<?> c1 : cs)
		{
			if (c1.getGenericParameterTypes().length == 1)
			{
				c1.setAccessible(true);
				return c1;
			}
		}
		return null;
	}

	/**
	 * 获取"SELECT FIELD1,FIELD2 FROM TBNAME VALUES(?,?)"格式的语句
	 * 
	 * @param c
	 *            从这个类型的字段获取
	 * @param tbname
	 *            表名
	 * @param jumpref
	 *            是否跳过引用类型
	 */
	public static String GetSQLInsertAll(Class<?> c, String tbname, boolean jumpref)
	{
		Field[] fs = c.getFields();
		StringBuilder querySQL = new StringBuilder();
		querySQL.append("INSERT INTO ").append(tbname).append("(");
		
		StringBuilder valueSQL = new StringBuilder();
		valueSQL.append(" VALUES(");
		for (int i = 0; i < fs.length; ++i)
		{
			Field f = fs[i];
			RefField ref = f.getAnnotation(RefField.class);
			if (jumpref && ref != null)
			{
				continue;
			}
			if (i != 0)
			{
				querySQL.append(",");
				valueSQL.append(",");
			}
			querySQL.append(f.getName());
			valueSQL.append("?");
		}
		querySQL.append(") ");
		valueSQL.append(") ");
		querySQL.append(valueSQL);
		return querySQL.toString();
	}

	/**
	 * 获取"INSERT INTO TBNAME (KEY) VALUES (?)"格式的语句
	 */
	public static String GetSQLInsertKey(String tbname, String key)
	{
		return String.format("INSERT INTO %s (%s) VALUES (?)", tbname, key);
	}

	/**
	 * 获取"SELECT * FROM TBNAME"格式的语句
	 * 
	 * @param tbname
	 *            要查询的表
	 */
	public static String GetSQLSelectAll(String tbname)
	{
		return "SELECT * FROM " + tbname;
	}
	
	/**
	 * 根据字段获取"SELECT FIELD1,FIELD2 FROM TBNAME WHERE KEY=?"的SQL语句
	 * 
	 * @param c
	 *            从这个类型的字段获取
	 * @param keyname
	 *            作为key的字段
	 */
	public static String GetSQLSelectAllFields(Class<?> c, String keyname)
	{
		Field[] fs = c.getFields();

		StringBuilder querySQL = new StringBuilder();
		querySQL.append("SELECT ");
		String tbname = GetClassName(c);

		for (int i = 0; i < fs.length; ++i)
		{
			Field f = fs[i];
			RefField ref = f.getAnnotation(RefField.class);
			if (ref != null)
			{
				continue;
			}
			querySQL.append(f.getName()).append(",");
		}
		querySQL.deleteCharAt(querySQL.length()-1);
//		querySQL = querySQL.substring(0, querySQL.length() - 1);
		querySQL.append(" FROM ").append(tbname).append(" WHERE ").append(keyname).append("=?");
		return querySQL.toString();
	}

	public static Pair<String,Boolean> GetSQLSelectAllFieldsByBaseRoleID(Class<?> c)
	{
		Field[] fs = c.getFields();
		
		boolean gid = false;
		for (int i = 0; i < fs.length; ++i)
		{
			Field f = fs[i];
			RefField ref = f.getAnnotation(RefField.class);
			if (ref != null)
			{
				continue;
			}
			if (f.getName().equals("GID"))
			{
				gid = true;
				break;
			}
		}
		
		StringBuilder querySQL = new StringBuilder();
		String tbname = GetClassName(c);
		querySQL.append("SELECT * FROM ").append(tbname).append(" WHERE ");
		if (gid)
		{
			querySQL.append("GID>=? AND GID<?");
		}
		else
		{
			querySQL.append("RoleID=?");
		}
		return Pair.makePair(querySQL.toString(), gid);
	}

	/**
	 * 根据字段获取"UPDATE TBNAME SET FIELD=?,FIELD=?"的SQL语句
	 * 
	 * @param c
	 *            从这个类型的字段获取
	 * @param tbname
	 *            表名
	 * @param key
	 *            作为key的字段
	 * @return
	 */
	public static String GetSQLUpdateAll(Class<?> c, String tbname, String key, boolean jumpref)
	{
		Field[] fs = c.getFields();
		Debug.Assert(fs.length >= 2, "");
		StringBuilder querySQL = new StringBuilder();
		querySQL.append("UPDATE ").append(tbname).append(" SET ");
		boolean add = false;
		for (Field f : fs)
		{
			RefField ref = f.getAnnotation(RefField.class);
			if (jumpref && ref != null)
			{
				continue;
			}
			String fname = f.getName();
			if (fname.equals(key))
			{
				continue;
			}
			if (add)
			{
				querySQL.append(",");
			}
			querySQL.append(fname).append(" = ?");
			add = true;
		}
		querySQL.append(" WHERE ").append(key).append(" = ?");
		return querySQL.toString();
	}

	/**
	 * 获取"UPDATE TBNAME SET FIELD=?,FIELD1=? WHERE KEY=?"格式的语句,只包含改变了的字段
	 * 
	 * @param t
	 *            改变的数据集
	 * @param tbname
	 *            对应的表名
	 * @param key
	 *            表的key
	 * @param jumpref
	 *            是否跳过引用字段
	 * @return 如果没有任何字段改变,则返回null
	 */
	public static <T> String GetSQLUpdateChange(T t, String tbname, String key, boolean jumpref)
	{
		Field[] fs = t.getClass().getFields();
		StringBuilder querySQL = new StringBuilder();
		querySQL.append("UPDATE ").append(tbname).append(" Set ");
		boolean add = false;
		for ( int i = 0; i < fs.length; ++i )
		{
			Field f = fs[i];
			RefField ref = f.getAnnotation(RefField.class);
			if ( jumpref && ref != null )
			{
				continue;
			}
			DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
			if ( f.getName().equals(key) )
			{
				continue;
			}
			if ( fd.IsChanged() )
			{
				if ( add )
				{
					querySQL.append(",");
				}
				querySQL.append(f.getName()).append(" = ?");
				add = true;
			}
		}
		querySQL.append(" WHERE ").append(key).append(" = ?");
		
		if ( add )
		{
			return querySQL.toString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * 得到2个参数的构造函数
	 * 
	 * @return 如果无法得到这种构造函数,返回null
	 */
	public static Constructor<?> GetTwoParamConstructor(Class<?> c)
	{
		Constructor<?>[] cs = c.getDeclaredConstructors();
		for (Constructor<?> c1 : cs)
		{
			if (c1.getGenericParameterTypes().length == 2)
			{
				c1.setAccessible(true);
				return c1;
			}
		}
		return null;
	}

	/**
	 * 从DBValue中取出值
	 */
	public static Object GetValueFromDBValue(DBValue v)
	{
		try
		{
			Field f = v.getClass().getDeclaredField("m_Value");
			f.setAccessible(true);
			return f.get(v);
		}
		catch (NoSuchFieldException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#GetValueFromDBValue");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#GetValueFromDBValue");
		}
	}

	/**
	 * 检查包含DBValue类型的对象是否有任意字段的值改变了
	 * 
	 * @param t
	 *            检查的对象
	 * @param checkref
	 *            是否检查应用字段
	 * @return 改变了返回true
	 */
	public static <T> boolean IsChanged(T t, boolean checkref)
	{
		Field[] fs = t.getClass().getFields();
		for (Field f : fs)
		{
			if (!checkref)
			{
				RefField ref = f.getAnnotation(RefField.class);
				if (ref != null)
				{
					continue;
				}
			}

			DBValue fd = (DBValue) SystemFn.GetFieldValue(t, f);
			if (fd.IsChanged())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查某个类型是否包含了某个字段
	 * 
	 * @param c
	 *            要检查的类型
	 * @param fieldname
	 *            要检查的字段
	 * @return 包含了则返回true
	 */
	public static boolean IsHaveField(Class<?> c, String fieldname)
	{
		Field[] fs = c.getFields();
		for (int i = 0; i < fs.length; ++i)
		{
			Field f = fs[i];
			if (f.getName() == fieldname)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * c类型是否有f的接口
	 * 
	 * @param c
	 *            要检测的类型
	 * @param f
	 *            是否有的类型
	 * @return 有f接口返回true
	 */
	public static boolean IsHaveInterface(Class<?> c, Class<?> f)
	{
		Debug.Assert(f.isInterface(), "类型[" + f + "]必须是接口类型");
		Class<?>[] cs = c.getInterfaces();
		for (Class<?> c1 : cs)
		{
			if (c1 == f)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 使用DBMgr.CreateRoleData()创建的都是内存数据,当这条数据刷入到sql后,内存数据标准就会去掉
	 */
	public static <T> boolean IsMemeoryData(T t)
	{
		Class<?> superClass = t.getClass().getSuperclass();
		if (superClass != RoleDataBase.class)
		{
			return false;
		}
		for (Field f : superClass.getDeclaredFields())
		{
			f.setAccessible(true);
			if (f.getName() == "Flag")
			{
				try
				{
					return f.getBoolean(t);
				}
				catch (IllegalAccessException e)
				{
					throw new WrapRuntimeException(e);
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否需要绑定其他的数据
	 * 
	 * @return 需要绑定则返回true
	 */
	public static boolean IsNeedBindExData(Class<?> c)
	{
		Field[] fs = c.getFields();
		for (int i = 0; i < fs.length; ++i)
		{
			Field f = fs[i];
			RefField ref = f.getAnnotation(RefField.class);
			if (ref != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 把o1的所有字段赋给o,必须是同一个类型
	 */
	public static void SetAllFieldValue(Object o, Object o1)
	{
		Debug.Assert(o.getClass() == o1.getClass(), "");
		Field[] fs = o1.getClass().getDeclaredFields();
		for ( Field f : fs )
		{
//			if (f.getType() == LoginUserData.class)
//			{
//				continue;
//			}
			//todo niuhao ,我注释掉这里了，为啥要单独跳过呢
			f.setAccessible(true);
			Object v = SystemFn.GetFieldValue(o1, f);
			SystemFn.SetFieldValue(o, f, v);
		}
	}

	/**
	 * 给对象t的字段f设置v值
	 */
	public static <T> Object SetFieldValue(T t, Field f, Object v)
	{
		try
		{
			f.set(t, v);
			return v;
		}
		catch (IllegalArgumentException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateFieldValue 对象[ + " + t + " + ]字段[" + f + "]");
		}
		catch (IllegalAccessException e)
		{
			throw new WrapRuntimeException(e, "SystemFn#CreateFieldValue 对象[ + " + t + " + ]字段[" + f + "]");
		}
	}

	/**
	 * 设置RoleDataBase类型中的Flag字段值为flag
	 */
	public static <T> void SetRoleDataBaseFlag(T t, boolean flag)
	{
		Class<?> superClass = t.getClass().getSuperclass();
		if (superClass != RoleDataBase.class)
		{
			return;
		}
		for (Field f : superClass.getDeclaredFields())
		{
			if (f.getName() == "Flag")
			{
				try
				{
					f.setAccessible(true);
					f.setBoolean(t, flag);
				}
				catch (IllegalArgumentException e)
				{
					throw new WrapRuntimeException(e, "SystemFn#SetRoleDataBaseFlag 对象[ + " + t + " + ] 设置字段[Flag]为[" + flag + "]");
				}
				catch (IllegalAccessException e)
				{
					throw new WrapRuntimeException(e, "SystemFn#SetRoleDataBaseFlag 对象[ + " + t + " + ] 设置字段[Flag]为[" + flag + "]");
				}
			}
		}
	}

	/**
	 * 线程sleep多少毫秒
	 */
	public static void Sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			Log.out.LogException(e);
		}
	}

	/**
	 * DB引用其他数据有两种写法 <br>
	 * 1.引用的记录类型#引用的记录字段 <br>
	 * 2.引用的记录类型,这种情况下引用的字段为本身的字段名称
	 * 
	 * @param splitstr
	 *            要拆分的字符
	 */
	public static Pair<String, String> SplitRefString(String splitstr, String defaultfieldname)
	{
		String[] bs = splitstr.split("#");
		if (bs.length == 1)
		{
			return Pair.makePair(bs[0], defaultfieldname);
		}
		else if (bs.length == 2)
		{
			return Pair.makePair(bs[0], bs[1]);
		}
		else
		{
			throw new WrapRuntimeException("字符串[" + splitstr + "]不符合SystemFn#SplitRefString的需求");
		}
	}
}
