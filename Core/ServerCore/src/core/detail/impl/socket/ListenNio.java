/**
 * ListenNio.java 2012-6-13下午10:20:27
 */
package core.detail.impl.socket;

import java.nio.channels.SocketChannel;

import core.detail.*;
import core.detail.impl.log.*;
import core.ex.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class ListenNio extends ListenNioBase
{
	private long m_RecordTime = System.currentTimeMillis();

	public ListenNio()
	{
		super(new LinkNio());
	}
    
	private void _Record()
    {
    	long tm = System.currentTimeMillis() - m_RecordTime;
		if ( tm > 1000 && LinkNio.m_ProcessNum > 0 )
		{
			_NetInfoLog.Log(LinkNio.m_ProcessNum, 
					LinkNio.m_ProcessNumTotal, 
					LinkNio.m_ProcessBufferNum, 
					LinkNio.m_ProcessBufferNumTotal, 
					GetLinkNum(), 
					Mgr.GetUserMgr().GetNum());
			
			if (ShowMgr.CanShow())
			{
				ShowMgr.GetInfo().setInputBuffer(LinkNio.m_ProcessBufferNum * 1000 / tm);
				ShowMgr.GetInfo().setNetNum(LinkNio.m_ProcessNum * 1000 / tm);
			}
			m_RecordTime = System.currentTimeMillis();
			LinkNio.m_ProcessBufferNum = 0;
			LinkNio.m_ProcessNum = 0;
			
			if (ShowMgr.CanShow())
			{
				ShowMgr.GetInfo().setReceiveMsgNumTotal(LinkNio.m_ProcessNumTotal);
				ShowMgr.GetInfo().setInputBufferTotal(LinkNio.m_ProcessBufferNumTotal);
				ShowMgr.GetInfo().setLinkNum(GetLinkNum());
			}
		}
    }

	/* (non-Javadoc)
	 * @see core.detail.impl.socket.ListenNioBase#_OnProcessOperCallBack()
	 */
	@Override
	protected void _OnProcessOperCallBack()
	{
		_Record();
	}

	/* (non-Javadoc)
	 * @see core.detail.impl.socket.ListenNioBase#_CheckIPLimit(java.nio.channels.SocketChannel)
	 */
	@Override
	protected boolean _CheckIPLimit(SocketChannel c)
	{
		return true;
	}
}
