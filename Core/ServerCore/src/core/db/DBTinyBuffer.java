/**
 * DBTinyBuffer.java 2012-6-29下午5:15:30
 */
package core.db;

import java.sql.*;

import javax.sql.rowset.serial.*;

import core.detail.impl.log.Log;
import core.exception.WrapRuntimeException;

import utility.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 总是255定长的buffer
 */
public class DBTinyBuffer implements DBValue
{
	private Blob 	m_Value;
	private boolean	m_Changed = false;
	
	DBTinyBuffer()
	{
		
	}
	
	DBTinyBuffer(ResultSet r, String fieldName) throws SQLException
	{
		m_Value = r.getBlob(fieldName);
	}
	
	public void Set(byte[] bs)
	{
		Debug.Assert(bs.length <= 255, "字符长度不超过255");
		try
		{
			m_Value = new SerialBlob(bs);
			m_Changed = true;
		}
		catch (SerialException e)
		{
			throw new WrapRuntimeException(e);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}
	
	public void Set(Blob b)
	{
		m_Value = b;
		m_Changed = true;
	}
	
	public Blob Get()
	{
		return m_Value;
	}
	
	public byte[] GetBytes()
	{
		try
		{
			return m_Value.getBytes(1, (int) m_Value.length());
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}
	
	public String toString()
	{
		long length = 0;
		try
		{
			if ( m_Value != null )
			{
				length = m_Value.length();
			}
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
		return super.toString() + " length[" + length + "]";
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
		s.setBlob(index, m_Value);
	}

}
