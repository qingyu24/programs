/**
 * MySQLExecuteOper.java 2012-8-31下午2:35:38
 */
package core.db.mysql.oper;

import java.sql.*;

import core.db.mysql.SQLResultParse;
import core.detail.SystemFn;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class MySQLExecuteOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private PreparedStatement 	m_Statement;
	
	public MySQLExecuteOper(Connection conn, String sql)
	{
		super("", null);
		m_Connection = conn;
		try
		{
			m_Statement = m_Connection.prepareStatement(sql);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}

	public boolean Execute()
	{
		return Execute(null);
	}

	public boolean Execute(SQLResultParse parse)
	{
		try
		{
			long stm = System.currentTimeMillis();
			if (parse == null)
			{
				m_Statement.execute();
				m_Connection.commit();
			}
			else
			{
				ResultSet r = m_Statement.executeQuery();
				parse.Parse(r);
			}
			
			Use();
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			return true;
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
			return false;
		}
	}


	/* (non-Javadoc)
	 * @see core.db.mysql.oper.MySQLOper#Release()
	 */
	@Override
	public void Release()
	{
		m_Connection = null;
		SystemFn.ClosePreparedStatement(m_Statement);
		m_Statement = null;
	}

}
