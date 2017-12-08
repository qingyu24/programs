/**
 * PackBuffer.java 2012-6-26下午4:03:29
 */
package logic;

import core.Root;
import core.Tick;
import core.detail.impl.socket.SendMsgBuffer;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class PackBuffer extends SendMsgBuffer implements Tick
{
	private static PackBuffer m_Instance = new PackBuffer();
	
	public PackBuffer()
	{
		super(2048);
		Root.GetInstance().AddLoopMilliTimer(this, 300, null);
	}
	
	public static PackBuffer GetInstance()
	{
		return m_Instance;
	}

	/* (non-Javadoc)
	 * @see core.Tick#OnTick(long)
	 */
	@Override
	public void OnTick(long p_lTimerID) throws Exception
	{
		OnRelaxSendBuffer();
	}
}
