/**
 * MethodParamBase.java 2012-6-15上午11:12:42
 */
package core.detail.impl.methodparam;

import core.detail.eUserLockType;
import core.detail.impl.socket.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public abstract class MethodParamBase implements MethodParam
{

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Clone()
	 */
	@Override
	public abstract MethodParam Clone();

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#ParseMsg(core.detail.impl.socket.MsgBuffer)
	 */
	@Override
	public abstract void ParseMsg(MsgBuffer buffer) throws Exception;

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#DataReady()
	 */
	@Override
	public boolean DataReady()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#IsLock()
	 */
	@Override
	public boolean IsLock()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#Get()
	 */
	@Override
	public abstract Object Get();

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#SQLRun()
	 */
	@Override
	public void SQLRun()
	{
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.MethodParam#SetLock(core.detail.eUserLockType, boolean)
	 */
	@Override
	public void SetLock(eUserLockType type, boolean b)
	{
	}
}
