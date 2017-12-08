/**
 * MethodExSQLImpl.java 2012-6-24上午10:55:43
 */
package logic.methodex;

import core.CustomMethod;
import logic.MyUser;
import logic.sqlrun.MySQLRun;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class MyMethodEx extends CustomMethod
{
	public MyMethodEx(MyUser p_User, MySQLRun p_SQLRun)
	{
		super(p_User, p_SQLRun);
	}

	/* (non-Javadoc)
	 * @see core.CustomMethod#RunDirect()
	 */
	@Override
	public void RunDirect() throws Exception
	{
		OnRunDirect((MyUser)m_User, (MySQLRun)m_SqlRun);
	}
	
	public abstract void OnRunDirect(MyUser p_User, MySQLRun p_SQLRun) throws Exception;

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetClassID()
	 */
	@Override
	public int GetClassID()
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#GetMethodID()
	 */
	@Override
	public int GetMethodID()
	{
		return 0;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodEx#CanExecute()
	 */
	@Override
	public boolean CanExecute()
	{
		return true;
	}
}
