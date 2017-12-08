/**
 * DBInt.java 2012-6-19下午1:22:22
 */
package core.db;

import java.sql.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBInt implements DBValue
{
	private 	int 	m_Value;
	private 	boolean	m_Change = false;
	
	DBInt()
	{
		
	}
	
	DBInt(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getInt(fieldName);
	}
	
	DBInt(int v)
	{
		m_Value = v;
	}
	
	public void Set(int v)
	{
		if ( m_Value != v)
		{
			m_Value = v;
			m_Change = true;
		}
	}
	
	public void Add(int v)
	{
		m_Value += v;
		m_Change = true;
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
		s.setInt(index, m_Value);
	}
}
