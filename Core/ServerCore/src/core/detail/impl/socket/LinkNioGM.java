/**
 * LinkNioGM.java 2013-1-19上午11:01:01
 */
package core.detail.impl.socket;

import java.nio.channels.SocketChannel;

import core.RootConfig;
import core.User;
import core.detail.GMUser;
import core.detail.Mgr;
import core.detail.impl.mgr.methodmgr.MethodType;
import core.detail.interface_.Link;
import core.detail.interface_.Listen;
import core.detail.interface_.MethodEx;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LinkNioGM extends LinkNio
{
	public LinkNioGM()
	{
		
	}
	
	public LinkNioGM(Listen listen, SocketChannel sc)
	{
		super(listen, sc);
	}
	
	@Override
	public void _CheckLink()
	{
		
	}
	
	@Override
	protected String[] _GetCreateProtocol()
	{
		RootConfig r = RootConfig.GetInstance();
		String[] ss = new String[r.Protocol.length];
		
		for ( int i = 1; i < r.Protocol.length; ++i )
		{
			ss[i] = "core.detail.impl.socket.protocol." + r.Protocol[i].Name; 
		}
		return ss;
	}

	@Override
	public Link Clone(Listen listen, SocketChannel sc)
	{
		return new LinkNioGM(listen, sc);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#GetMethod(byte, byte)
	 */
	@Override
	public MethodEx GetMethod(byte classid, byte methodid)
	{
		return Mgr.GetRunMethodMgr(MethodType.GM).GetMethod(classid, methodid);
	}
	
	protected User _CreateUser(Link l)
	{
		User u = new GMUser();
		u.SetLink(l);
		return u;
	}
	
	@Override
	protected SendBuffer _GetSendBuffer()
	{
		return SendBuffer.GetGM();
	}
}
