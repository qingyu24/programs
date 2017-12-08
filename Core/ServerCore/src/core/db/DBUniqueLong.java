/**
 * DBUniqueLong.java 2012-6-19下午1:22:16
 */
package core.db;

import java.sql.*;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBUniqueLong implements DBValue
{
	private static UniqueID m_IDBuild = new UniqueID();
	private long 	m_Value;
	
	DBUniqueLong()
	{
		m_Value = m_IDBuild.Get();
	}
	
	DBUniqueLong(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getLong(fieldName);
	}
	
	DBUniqueLong(long v)
	{
		m_Value = v;
	}
	
	public long Get()
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
	 * @see core.db.DBValue#SetChanged(boolean)
	 */
	@Override
	public void Changed(boolean c)
	{
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#SetStatement(int, java.sql.PreparedStatement)
	 */
	@Override
	public void SetStatement(int index, PreparedStatement s)
			throws SQLException
	{
		s.setLong(index, m_Value);
	}

	public void Set(long v)
	{
		m_Value = v;
	}
}
