/**
 * DBShort.java 2012-6-29下午5:13:17
 */
package core.db;

import java.sql.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBShort implements DBValue
{
	private short 	m_Value;
	private boolean	m_Changed = false;
	
	DBShort()
	{
		
	}
	
	DBShort(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getShort(fieldName);
	}
	
	DBShort(short v)
	{
		m_Value = v;
	}
	
	public void Set(short v)
	{
		if ( m_Value != v)
		{
			m_Value = v;
			m_Changed = true;
		}
	}
	
	public short Get()
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
		return m_Changed;
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#Changed(boolean)
	 */
	@Override
	public void Changed(boolean c)
	{
		m_Changed = c;
	}

	/* (non-Javadoc)
	 * @see core.db.DBValue#SetStatement(int, java.sql.PreparedStatement)
	 */
	@Override
	public void SetStatement(int index, PreparedStatement s)
			throws SQLException
	{
		s.setShort(index, m_Value);
	}

}
