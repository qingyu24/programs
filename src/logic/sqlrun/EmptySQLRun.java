/**
 * EmptySQLRun.java 2012-8-10下午2:52:59
 */
package logic.sqlrun;

import logic.MyUser;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 用于在SQL线程空跑的SQLRun
 */
public class EmptySQLRun extends MySQLRun
{

	/* (non-Javadoc)
	 * @see logic.sqlrun.MySQLRun#Execute(logic.MyUser)
	 */
	@Override
	public void Execute(MyUser p_User) throws Exception
	{
	}
}
