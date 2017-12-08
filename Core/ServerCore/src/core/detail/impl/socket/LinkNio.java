/**
 * LinkNio.java 2012-6-13下午10:30:49
 */
package core.detail.impl.socket;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;

import utility.*;

import core.*;
import core.detail.Mgr;
import core.detail.SystemFn;
import core.detail.impl.log.Log;
import core.detail.impl.log.eSystemErrorLogType;
import core.detail.impl.log.eSystemInfoLogType;
import core.detail.impl.mgr.methodmgr.MethodType;
import core.detail.interface_.*;
import core.exception.WrapRuntimeException;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LinkNio implements Link
{
	private Listen			m_Nio;
	private int				m_HashCode;
	private SocketChannel 	m_Sc;
	private ReceiveBuffer 	m_ReceiveBuffer = new ReceiveBuffer(1024);
	private User			m_User;
	private boolean			m_Closed = false;
	private Protocol[] 		m_Protocols;
	private TransBuffer		m_TransBuffer = new TransBuffer();
	private boolean			m_AllProtocolsInit = false;
	private long			m_LastMsgTime = System.currentTimeMillis();
	
	private int				m_ReceiveNum = 0;
	private long			m_CheckReceiveTime = 0;
	private ArrayList<String> m_ReceiveMsg = new ArrayList<String>();
	
	private int				m_ReceiveNumTotal = 0;
	private long			m_CreateTime = System.currentTimeMillis();
	
	private long			m_SendEchoMsg = System.currentTimeMillis();

	public static long m_ProcessNum;
	public static long m_ProcessBufferNum;
	public static long m_ProcessNumTotal;
	public static long m_ProcessBufferNumTotal;
	
	public LinkNio()
	{
	}
	
	public LinkNio(Listen l, SocketChannel p_sc)
	{
		m_Nio = l;
		m_Sc = p_sc;
		m_HashCode = m_Sc.hashCode();
		m_User = _CreateUser(this);
		m_User.Log(eSystemInfoLogType.SYSTEM_INFO_LINK_USER, this, m_User);
//		m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime:" + Debug.GetShowTime(m_CheckReceiveTime));
		m_CreateTime = System.currentTimeMillis();
		m_SendEchoMsg = System.currentTimeMillis();
		_CreateProtocols();
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#_CanCloseImmediately()
	 */
	@Override
	public boolean _CanCloseImmediately()
	{
		return !_GetSendBuffer().HaveAnySendMsg(m_Sc);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#_CheckLink()
	 */
	@Override
	public void _CheckLink()
	{
		if ( RootConfig.GetInstance().LinkMaxWaitFirstMsgTime > 0 && 
			 m_ReceiveNumTotal == 0 && 
			 System.currentTimeMillis() - m_CreateTime > RootConfig.GetInstance().LinkMaxWaitFirstMsgTime * 1000 )
		{
			long stm = System.currentTimeMillis();
			Close(eCloseReasonType.DDOS.ID(), (int) m_CreateTime);
			if ( System.currentTimeMillis() - stm > 50 )
			{
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LinkNio::_CheckLink[[[:" + (System.currentTimeMillis() - stm));
			}
			return;
		}
		if ( RootConfig.GetInstance().LinkEchoSpaceTime > 0 &&
			 System.currentTimeMillis() - m_SendEchoMsg > RootConfig.GetInstance().LinkEchoSpaceTime * 1000 )
		{
			m_SendEchoMsg = System.currentTimeMillis();
			Root.GetInstance().GetFactory().NeedSendEchoMsg(m_User);
		}
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#_CloseImmediately()
	 */
	@Override
	public void _CloseImmediately()
	{
		Debug.Assert(m_Closed, "要求是断开状态");
		
		SystemFn.CloseSocketChannel(m_Sc);
		m_Sc = null;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#CheckLink(int)
	 */
	@Override
	public boolean CheckLink(int maxtm)
	{
		m_User.Log(eSystemInfoLogType.SYSTEM_INFO_LINK_CHECK_TIME, Debug.GetCurrTime(), Debug.GetShowTime(m_LastMsgTime), maxtm);
		return System.currentTimeMillis() - m_LastMsgTime < maxtm;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#Clone(core.detail.interface_.Listen, java.nio.channels.SocketChannel)
	 */
	@Override
	public Link Clone(Listen listen, SocketChannel sc)
	{
		return new LinkNio(listen, sc);
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#Close()
	 */
	@Override
	public synchronized void Close(int reason, int ex)
	{
		if ( !m_Closed )
		{
			m_Closed = true;
			
			m_Nio.AddRemoveLinkList(this);
			m_User.Log(eSystemInfoLogType.SYSTEM_INFO_LINK_CLOSE, this);
			m_User.Log(eSystemInfoLogType.SYSTEM_INFO_LINK_READY_REMOVE, this, m_User);
			m_User.SetCloseReason(reason, ex);
			Mgr.GetUserMgr().OnUserDisconnected(m_User);
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#GetIP()
	 */
	@Override
	public String GetIP()
	{
		return SystemFn.GetIP(m_Sc);
	}

	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#GetMethod(byte, byte)
	 */
	@Override
	public MethodEx GetMethod(byte classid, byte methodid)
	{
		MethodEx m = Mgr.GetRunMethodMgr(MethodType.NORMAL).GetMethod(classid, methodid);
		if ( m == null && RootConfig.GetInstance().RobotService )
		{
			m = Mgr.GetRunMethodMgr(MethodType.TEST).GetMethod(classid, methodid);
		}
		return m;
	}

	/**
	 * user对象的hashcode
	 */
	public int GetUserHashCode()
	{
		if ( m_User != null )
		{
			return m_User.hashCode();
		}
		return 0;
	}
	
	public int hashCode()
	{
		return m_HashCode;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#IsDisConnected()
	 */
	@Override
	public boolean IsDisConnected()
	{
		return m_Closed;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#OnRead()
	 */
	@Override
	public boolean OnRead()
	{
		try
		{
			int count = m_ReceiveBuffer.Read(m_Sc);
			if ( count <= 0 )
			{
				throw new Exception("# 连接出错["+m_Sc+"]");
			}
			
			if ( RootConfig.GetInstance().CheckDeadLink > 0 && !CheckLink(RootConfig.GetInstance().CheckDeadLink*1000) )
			{
				throw new Exception("# 连接等待消息的时间过长");
			}
			
			m_LastMsgTime = System.currentTimeMillis();
			m_ProcessBufferNum += count;
			m_ProcessBufferNumTotal += count;
			_InitProtocols();
			
			if ( !m_AllProtocolsInit )
			{
				return true;
			}
			
			while(true)
			{
				MsgBuffer buffer = _GetMsgBuffer();
				if ( buffer == null )
				{
					break;
				}
				
				byte classid = buffer.GetByte();
				byte methodid = buffer.GetByte();
				MethodEx mex = GetMethod(classid, methodid);
				if ( mex == null )
				{
					throw new Exception("# 无法找到远程调用函数 classid[" + classid + "]" + " methodid[" + methodid + "]");
				}
				m_User.StartMethodMark(mex);
				
				mex.ParseMsg(buffer, m_User);
				if ( !buffer.Check() )
				{
					throw new Exception("# 解析buffer的长度出错 使用的长度["+buffer.GetUseLength()+"] 应该使用的长度[" + buffer.GetLength() + "]");
				}
				Mgr.GetLogicMgr().AddNetTask(mex);
				m_ReceiveBuffer.Use();
				m_ProcessNum++;
				m_ProcessNumTotal++;
				
				m_ReceiveNum++;
				m_ReceiveNumTotal++;
				m_ReceiveMsg.add(""+classid + "," + methodid + Debug.GetCurrTime());
				
				m_User.EndMethodMark(mex);
				
				_CheckNetNum();
			}
			
			_CheckSecondNetNum();
		}
		catch (Exception e)
		{
			m_User.Log(eSystemErrorLogType.SYSTEM_ERROR_READ_NET_ERROR, m_User.GetRoleGID(), m_User,e.getMessage());
			long stm = System.currentTimeMillis();
			Close(eCloseReasonType.READ_EXCEPTION.ID(), 0);
			if ( System.currentTimeMillis() - stm > 50 )
			{
				Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LinkNio::OnRead用时[[[:" + (System.currentTimeMillis() - stm));
			}
			return false;
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#Send(java.nio.ByteBuffer)
	 */
	@Override
	public void Send(ByteBuffer buffer)
	{
		if ( IsDisConnected() )
		{
			return;
		}
		SocketChannel sc = m_Sc;
		if ( sc != null )
		{
			for ( int i = m_Protocols.length - 1; i >= 0; i-- )
			{
				if ( m_Protocols[i] != null )
				{
					buffer = m_Protocols[i].Pack(buffer);
				}
			}
			long stm = System.currentTimeMillis();
			_GetSendBuffer().AppendBuffer(buffer, m_Sc);
			if ( System.currentTimeMillis() - stm > 50 )
			{
				m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "SendBuffer用时[[[:" + (System.currentTimeMillis() - stm));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see core.detail.interface_.Link#SendBuffer(java.nio.ByteBuffer)
	 */
	@Override
	public void SendBuffer(ByteBuffer buffer)
	{
		if ( IsDisConnected() )
		{
			return;
		}
		SocketChannel sc = m_Sc;
		if ( sc != null )
		{
			try
			{
				sc.write(buffer);
				Debug.Assert(buffer.hasRemaining() == false, "我要看看会有异常吗?");
			}
			catch (IOException e)
			{
				m_User.LogException(e, "Link::SendBuffer::" + buffer.remaining());
				
				long stm = System.currentTimeMillis();
				Close(eCloseReasonType.WRITE_EXCEPTION.ID(), 0);
				if ( System.currentTimeMillis() - stm > 50 )
				{
					Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "LinkNio::SendBuffer用时[[[:" + (System.currentTimeMillis() - stm));
				}
			}
		}
	}
	

	private void _CheckNetNum()
	{
		if ( RootConfig.GetInstance().LinkMsgLimit > 0 )
		{
//			m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime::_CheckNetNum:" + Debug.GetShowTime(m_CheckReceiveTime) + "," + Debug.GetCurrTime() + ",ReceiveNum:" + m_ReceiveNum);
			if ( System.currentTimeMillis() - m_CheckReceiveTime > 1000 )
			{
				m_ReceiveNum = 0;
				m_CheckReceiveTime = System.currentTimeMillis();
				m_ReceiveMsg.clear();
				
//				m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime::_CheckNetNum_Clear:" + Debug.GetShowTime(m_CheckReceiveTime) + "," + Debug.GetCurrTime() + ",ReceiveNum:" + m_ReceiveNum);
			}
			if (m_ReceiveNum >= RootConfig.GetInstance().LinkMsgLimit)
			{
//				m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime::_CheckNetNum_Kick:" + ",ReceiveNum:" + m_ReceiveNum + ",limit:" + RootConfig.GetInstance().LinkMsgLimit);
				_ExecuteKickTooMuch();
			}
		}
	}

	
	private void _CheckSecondNetNum()
	{
		if ( RootConfig.GetInstance().LinkMsgLimit > 0 )
		{
//			m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime::_CheckSecondNetNum:" + Debug.GetShowTime(m_CheckReceiveTime) + "," + Debug.GetCurrTime() + ",ReceiveNum:" + m_ReceiveNum);
			if ( System.currentTimeMillis() - m_CheckReceiveTime > 1000 )
			{
				if ( m_ReceiveNum * 1000 >= RootConfig.GetInstance().LinkMsgLimit * (System.currentTimeMillis() - m_CheckReceiveTime) )
				{
//					m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime::_CheckSecondNetNum_Kick:" + ",ReceiveNum:" + m_ReceiveNum + ",tm1:" + System.currentTimeMillis() + ",tm2:" + m_CheckReceiveTime);
					_ExecuteKickTooMuch();
				}
				m_ReceiveNum = 0;
				m_CheckReceiveTime = System.currentTimeMillis();
				m_ReceiveMsg.clear();
//				m_User.Log(eSystemInfoLogType.SYSTEM_INFO_NORMAL, "CheckTime::_CheckSecondNetNum_Clear:" + Debug.GetShowTime(m_CheckReceiveTime) + "," + Debug.GetCurrTime() + ",ReceiveNum:" + m_ReceiveNum);
			}
		}
	}
	
	protected String[] _GetCreateProtocol()
	{
		RootConfig r = RootConfig.GetInstance();
		String[] ss = new String[r.Protocol.length];
		
		for ( int i = 0; i < r.Protocol.length; ++i )
		{
			if ( r.Protocol[i].Name.isEmpty() )
			{
				continue;
			}
			ss[i] = "core.detail.impl.socket.protocol." + r.Protocol[i].Name; 
		}
		return ss;
	}

	private void _CreateProtocols()
	{
		RootConfig r = RootConfig.GetInstance();
		m_Protocols = new Protocol[r.Protocol.length];
		
		String[] ss = _GetCreateProtocol();
		
		for ( int i = 0; i < ss.length; ++i )
		{
			if ( ss[i] == null || ss[i].isEmpty() )
			{
				continue;
			}
			
			try
			{
				m_Protocols[i] = (Protocol) Class.forName(ss[i]).newInstance();
				m_Protocols[i].Create(this);
			}
			catch (Exception e)
			{
				m_User.LogException(e);
				Debug.Assert(false, "创建协议类[" + ss[i] + "]失败!");
			}
		}
	}

	private void _ExecuteKickTooMuch()
	{
		try
		{
			Root.GetInstance().GetFactory().OnLinkProcessMsgTooMuch(m_User);
		}
		catch (Exception e1)
		{
		}
		StringBuilder sb = new StringBuilder();
		sb.append("玩家[").append(m_User.GetRoleGID())
		  .append("]同时发送[").append(m_ReceiveNum).append("]消息,超过上限[").append(RootConfig.GetInstance().LinkMsgLimit)
		  .append("],踢掉,当前时间:").append(Debug.GetCurrTime())
		  .append(",上次检查时间").append(Debug.GetShowTime(m_CheckReceiveTime));
		WrapRuntimeException e = new WrapRuntimeException(sb.toString());
		for (String s : m_ReceiveMsg)
		{
			System.err.println("执行的消息:" + s);
		}
		m_User.LogException(e);
		throw e;
	}

	private MsgBuffer _GetMsgBuffer() throws Exception
	{
		m_TransBuffer.Init(m_ReceiveBuffer);
		for ( Protocol p : m_Protocols )
		{
			if ( p != null )
			{
				boolean c = p.Resolve(m_TransBuffer, this);
				//如果null则表示这层协议还没通过
				if ( !c )
				{
					return null;
				}
			}
		}
//		m_ReceiveBuffer.SetPreEnd(m_TransBuffer.GetPreEndPos());
		return m_TransBuffer.ToMsgBuffer();
	}

	private void _InitProtocols() throws Exception
	{
		if ( !m_AllProtocolsInit )
		{
			//在功能前要进行协议的初始化,所有的协议初始化后才能进入逻辑层
			m_TransBuffer.Init(m_ReceiveBuffer);
			int num = 0;
			for ( Protocol p : m_Protocols )
			{
				if ( p == null )
				{
					num++;
					continue;
				}
				boolean c = p.Init();
				if ( !c )
				{
					c = p.Resolve(m_TransBuffer, this);
					if ( !c )
					{
						break;
					}
					else
					{
						m_ReceiveBuffer.Use();
					}
				}
				num++;
			}
			
			if ( num == m_Protocols.length )
			{
				m_AllProtocolsInit = true;
				m_CheckReceiveTime = System.currentTimeMillis();
				m_ReceiveNum = 0;
			}
		}
	}

	protected User _CreateUser(Link l)
	{
		return Mgr.GetUserMgr().CreateUser(l);
	}
	
	protected SendBuffer _GetSendBuffer()
	{
		return SendBuffer.GetNormal();
	}
}
