/**
 * DBAutoLong.java 2012-6-29下午5:04:48
 */
package core.db;

import java.sql.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBAutoLong implements DBValue
{
	private 	long 	m_Value;
	
	DBAutoLong()
	{
		
	}
	
	DBAutoLong(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getLong(fieldName);
	}
	
	DBAutoLong(long v)
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
	public void SetStatement(int index, PreparedStatement s)
			throws SQLException
	{
		s.setLong(index, m_Value);
	}
}
