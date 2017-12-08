/**
 * SaveMethod.java 2012-8-7上午10:51:48
 */
package core.detail.impl;

import core.*;
import core.detail.eUserLockType;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class SaveMethod extends CustomMethod
{

	public SaveMethod(User p_User)
	{
		super(p_User, new SaveSQLRun());
		p_User.GetState().SetLock(eUserLockType.LOCK_SAVE, true);
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.CustomMethodImpl#RunDirect()
	 */
	@Override
	public void RunDirect() throws Exception
	{
		m_User.GetState().SetLock(eUserLockType.LOCK_SAVE, false);
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
