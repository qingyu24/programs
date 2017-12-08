/**
 * LogUser.java 2012-10-23上午10:36:54
 */
package core.detail.impl.log;

import core.detail.UserBase;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LogUser extends UserBase
{

	LogUser()
	{
		super("[GlobalLog]");
	}
	
	/* (non-Javadoc)
	 * @see core.detail.UserBase#ExecuteKeyDataSQLRun()
	 */
	@Override
	public void ExecuteKeyDataSQLRun() throws Exception
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see core.detail.UserBase#OnDisconnect()
	 */
	@Override
	public void OnDisconnect() throws Exception
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see core.detail.UserBase#OnAllDataLoadFinish()
	 */
	@Override
	public void OnAllDataLoadFinish() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

}
