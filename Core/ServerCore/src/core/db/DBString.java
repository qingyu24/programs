/**
 * DBString.java 2012-6-19下午12:43:50
 */
package core.db;

import java.sql.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBString implements DBValue
{
	protected 	String 	m_Value;
	private 	boolean	m_Change = false;
	
	DBString()
	{
		m_Value = "";
	}
	
	DBString(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getString(fieldName);
		if ( m_Value == null )
		{
			m_Value = "";
		}
	}
	
	DBString(String s)
	{
		m_Value = s;
	}
	
	public void Set(String v)
	{
		if ( m_Value == null || !m_Value.equals(v) )
		{
			m_Value = v;
			m_Change = true;
		}
	}
	
	public String Get()
	{
		return m_Value;
	}
	
	public String toString()
	{
		return super.toString() + "[" + m_Value + "]";
	}
	
	public boolean equals(Object obj)
	{
		return m_Value.equals(obj);
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
		s.setString(index, m_Value);
	}
}
