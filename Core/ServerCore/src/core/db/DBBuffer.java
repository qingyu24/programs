/**
 * DBBuffer.java 2012-6-29下午5:24:20
 */
package core.db;

import java.sql.*;

import javax.sql.rowset.serial.*;

import utility.Debug;

import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 * 
 *          最长65535的buffer
 */
public class DBBuffer implements DBValue
{
	private Blob	m_Value;
	private boolean	m_Changed	= false;

	DBBuffer()
	{

	}

	DBBuffer(ResultSet r, String fieldName)
	{
		try
		{
			m_Value = r.getBlob(fieldName);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}

	public void Set(byte[] bs)
	{
		Debug.Assert(bs.length <= 65535, "设置的字符长度必定不大于65535");
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
			if (m_Value != null)
			{
				length = m_Value.length();
			}
		}
		catch (SQLException e)
		{
		}
		return super.toString() + " length[" + length + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see core.db.DBValue#IsChanged()
	 */
	@Override
	public boolean IsChanged()
	{
		return m_Changed;
	}

	/*
	 * (non-Javadoc)
	 * @see core.db.DBValue#Changed(boolean)
	 */
	@Override
	public void Changed(boolean c)
	{
		m_Changed = c;
	}

	/*
	 * (non-Javadoc)
	 * @see core.db.DBValue#SetStatement(int, java.sql.PreparedStatement)
	 */
	@Override
	public void SetStatement(int index, PreparedStatement s) throws SQLException
	{
		s.setBlob(index, m_Value);
	}
}
