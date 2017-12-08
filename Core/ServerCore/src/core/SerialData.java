/**
 * SerialData.java 2012-7-5上午11:06:06
 */
package core;

import core.detail.impl.socket.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface SerialData
{
	/**
	 * 序列化数据,由逻辑层在发送这类数据给客户端时会调用
	 * 
	 * @exception 这个接口不理会产生的异常,如果出现,那么会由逻辑层入口函数捕获,然后导致用户断线
	 */
	void OnSerialData(SendMsgBuffer p_Send);

	/**
	 * 从消息流中反序列化出对象
	 * 
	 * @exception Exception 抛出所有可能的异常
	 */
	void DeSerialData(MsgBuffer buffer) throws Exception;
}
