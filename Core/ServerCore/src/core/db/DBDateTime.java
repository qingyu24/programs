/**
 * DBDateTime.java 2012-6-19下午12:44:00
 */
package core.db;

import java.sql.*;
import java.util.*;

import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class DBDateTime implements DBValue
{
//	private Calendar	m_Value = Calendar.getInstance();
	private boolean 	m_Changed = false;
	private long		m_Value = 0;
	
	DBDateTime()
	{
//		m_Value.setTimeInMillis(0);
	}
	
	DBDateTime(ResultSet r, String fieldName) throws SQLException
	{
		Calendar v = Calendar.getInstance();
		Timestamp ts = r.getTimestamp(fieldName, v);
		if ( ts != null )
		{
//			m_Value.setTimeInMillis(ts.getTime());
			m_Value = ts.getTime();
		}
	}
	
//	public void Set(Calendar c)
//	{
//		if ( c.getTimeInMillis() != m_Value.getTimeInMillis() )
//		{
//			m_Value = c;
//			m_Changed = true;
//		}
//	}
	
	public void Set(long millis)
	{
		if ( millis != m_Value )
		{
			m_Value = millis;
			m_Changed = true;
		}
	}
	
//	public Calendar Get()
//	{
//		return m_Value;
//	}
	
	public long GetMillis()
	{
		return m_Value;
	}
	
	public String toString()
	{
		return super.toString() + "[" + Debug.GetShowTime(m_Value) + "][" + m_Value + "]";
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
		s.setTimestamp(index, new Timestamp(m_Value));
	}

}
