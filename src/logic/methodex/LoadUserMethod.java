package logic.methodex;

import logic.MyUser;
import logic.sqlrun.MySQLRun;

public class LoadUserMethod extends MyMethodEx
{
	public LoadUserMethod(MyUser p_User, MySQLRun p_SQLRun)
	{
		super(p_User, p_SQLRun);
	}

	/* (non-Javadoc)
	 * @see logic.methodex.MyMethodEx#OnRunDirect(logic.MyUser, logic.sqlrun.MySQLRun)
	 */
	@Override
	public void OnRunDirect(MyUser p_User, MySQLRun p_SQLRun) throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}
