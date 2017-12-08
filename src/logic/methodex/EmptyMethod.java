/**
 * EmptyMethod.java 2012-8-10下午2:47:40
 */
package logic.methodex;

import core.Root;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.sqlrun.EmptySQLRun;
import logic.sqlrun.MySQLRun;

/**
 * @author ddoq
 * @version 1.0.0
 *
 * 用来测试空跑SQL线程的Method对象
 */
public class EmptyMethod extends MyMethodEx
{
	private int m_nHashcode = 0;
	private int m_nNum = 0;
	
	public EmptyMethod(MyUser p_User, int p_nHashcode, int p_nNum)
	{
		super(p_User, new EmptySQLRun());
		m_nHashcode = p_nHashcode;
		m_nNum = p_nNum;
	}

	/* (non-Javadoc)
	 * @see logic.methodex.MyMethodEx#OnRunDirect(logic.MyUser, logic.sqlrun.MySQLRun)
	 */
	@Override
	public void OnRunDirect(MyUser p_User, MySQLRun p_SQLRun) throws Exception
	{
		if ( --m_nNum <= 0 )
		{
			PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN, 58).Add(m_nHashcode).Send(p_User);
			return;
		}
		else
		{
			Root.GetInstance().AddSQLRun(this);
		}
	}
}
