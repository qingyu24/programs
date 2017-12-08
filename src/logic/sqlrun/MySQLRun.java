/**
 * MySQLRun.java 2012-6-24上午10:40:52
 */
package logic.sqlrun;

import core.SQLRun;
import core.User;
import logic.MyUser;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class MySQLRun implements SQLRun
{

	/* (non-Javadoc)
	 * @see core.SQLRun#Execute(core.User)
	 */
	@Override
	public void Execute(User p_User) throws Exception
	{
		Execute((MyUser)p_User);
	}

	public abstract void Execute(MyUser p_User) throws Exception;
}
