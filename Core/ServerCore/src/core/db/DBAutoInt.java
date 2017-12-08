/**
 * DBAutoInt.java 2012-6-29下午5:00:32
 */
package core.db;

import java.sql.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBAutoInt implements DBValue
{
	private	int m_Value;
	
	/**
	 * 第一个构造函数用来创建内存数据
	 */
	DBAutoInt()
	{
	}
	
	/**
	 * 第二个构造函数用来读取sql数据
	 */
	DBAutoInt(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getInt(fieldName);
	}
	
	DBAutoInt(int v)
	{
		m_Value = v;
	}
	
	public int Get()
	{
		return m_Value;
	}
	
	public String toString()
	{
		return super.toString() + "[" + m_Value + "]";
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#IsChanged()
	 */
	@Override
	public boolean IsChanged()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#Changed(boolean)
	 */
	@Override
	public void Changed(boolean c)
	{
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#SetStatement(int, java.sql.PreparedStatement)
	 */
	@Override
	public void SetStatement(int index, PreparedStatement s) throws SQLException
	{
		s.setInt(index, m_Value);
	}
}
