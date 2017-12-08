/**
 * ListenNioGM.java 2012-11-6上午11:21:39
 */
package core.detail.impl.socket;

import java.nio.channels.SocketChannel;

import core.RootConfig;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ListenNioGM extends ListenNioBase
{
	public ListenNioGM()
	{
		super(new LinkNioGM());
	}


	/* (non-Javadoc)
	 * @see core.detail.impl.socket.ListenNioBase#_OnProcessOperCallBack()
	 */
	@Override
	protected void _OnProcessOperCallBack()
	{
	}
	
	protected SendBuffer _GetSendBuffer()
	{
		return SendBuffer.GetGM();
	}


	/* (non-Javadoc)
	 * @see core.detail.impl.socket.ListenNioBase#_CheckIPLimit(java.nio.channels.SocketChannel)
	 */
	@Override
	protected boolean _CheckIPLimit(SocketChannel c)
	{
		return RootConfig.GetInstance().CheckGMIPLimit(c);
	}
}
