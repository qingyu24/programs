/**
 * DBLong.java 2012-6-19下午1:23:23
 */
package core.db;

import java.sql.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBLong implements DBValue
{
	private 	long 	m_Value;
	private 	boolean	m_Change = false;
	
	DBLong()
	{
		
	}
	
	DBLong(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getLong(fieldName);
	}
	
	DBLong(long v)
	{
		m_Value = v;
	}
	
	public void Set(long v)
	{
		if ( m_Value != v)
		{
			m_Value = v;
			m_Change = true;
		}
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
		return m_Change;
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#Changed(boolean)
	 */
	@Override
	public void Changed(boolean c)
	{
		m_Change = c;
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
}
