/**
 * MySQLReadByBaseRoleIDOper.java 2013-5-2下午9:58:32
 */
package core.db.mysql.oper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utility.Pair;
import core.db.RoleIDUniqueID;
import core.db.sql.ParseResult;
import core.detail.SystemFn;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemInfoLogType;
import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 角色数据读取的规则变成
 * 1.如果只有gid,那么以gid为key读取
 * 2.如果只有roleid,那么以roleid为key读取
 * 3.如果同时又gid和roleid,那么读取的时候以gid来读取,gid的数据为roleid,使用的语句为where gid>=roleid and gid < roleid+spaceid
 */
public class MySQLReadByBaseRoleIDOper extends MySQLOper
{
	private Connection			m_Connection;	///<数据库连接
	private PreparedStatement 	m_Statement;
	private ParseResult			m_ParseResult;
	private boolean				m_UseGID;
	
	private static int SPACENUM = RoleIDUniqueID.SPACENUM;
	
	public MySQLReadByBaseRoleIDOper(Connection conn, Class<?> c)
	{
		super("*", c);
		m_Connection = conn;
		
		m_ParseResult = new ParseResult(c);
		
		try
		{
			Pair<String,Boolean> r = SystemFn.GetSQLSelectAllFieldsByBaseRoleID(c);
			m_UseGID = r.second;
			m_Statement = m_Connection.prepareStatement(r.first);
		}
		catch (SQLException e)
		{
			throw new WrapRuntimeException(e);
		}
	}
	
	public <T> T[] Execute(long p_lRID)
	{
		ResultSet resultSet = null;
		T[] ts = null;
		try
		{
			long stm = System.currentTimeMillis();
			if ( m_UseGID )
			{
				m_Statement.setLong(1, p_lRID);
				m_Statement.setLong(2, p_lRID + SPACENUM);
			}
			else
			{
				m_Statement.setLong(1, p_lRID);
			}
			
			resultSet = m_Statement.executeQuery();
			ts = m_ParseResult.ReadAll(resultSet, p_lRID);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, m_Statement);
			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_READ_NUM, ts.length);
			Use();
		}
		catch (SQLException e)
		{
			Log.out.LogException(e);
		}
		finally
		{
			SystemFn.CloseResultSet(resultSet);
		}
		return ts;
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
		m_ParseResult.Release();
		m_ParseResult = null;
	}
}
