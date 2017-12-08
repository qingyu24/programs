/**
 * Link.java 2012-6-19上午10:55:08
 */
package core.detail.interface_;

import java.nio.*;
import java.nio.channels.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface Link
{
	/**
	 * 是否能立即断开了
	 */
	boolean _CanCloseImmediately();
	
	/**
	 * 检查连接
	 * 
	 * @tip 如果连接后在30s内没执行登陆消息,则踢了(很有可能是专门用来产生连接负担的ddos攻击)
	 * @tip 定时给客户端发送握手消息,以确保客户端没有正常释放时,服务器能清理死客户端
	 */
	void _CheckLink();
	
	/**
	 * 立即断开,只由Listen来调用
	 */
	void _CloseImmediately();
	
	/**
	 * 检查连接是否断开,用的是最简单的法子,xx时间内没有一条消息则认为断开了
	 */
	boolean CheckLink(int maxtm);
	
	/**
	 * 克隆一个Link对象
	 */
	Link Clone(Listen listen, SocketChannel sc);
	
	/**
	 * 断开连接,只是逻辑上的断开,实际断开得等消息发送完毕后
	 */
	void Close(int reason, int ex);

	/**
	 * 获取IP
	 */
	String GetIP();
	
	/**
	 * 获取要执行的Method
	 */
	MethodEx GetMethod(byte classid, byte methodid);

	/**
	 * user对象的hashcode
	 */
	int GetUserHashCode();

	/**
	 * 是否断开了
	 */
	boolean IsDisConnected();


	/**
	 * 读取数据
	 *
	 * @return 返回false表示读取失败,需要从Listen中移除
	 */
	boolean OnRead();
	
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_Buffer
	 */
	void Send(ByteBuffer m_Buffer);
	
	void SendBuffer(ByteBuffer p_Buffer);
}
