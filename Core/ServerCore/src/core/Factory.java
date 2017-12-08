/**
 * Factory.java 2012-6-11下午9:46:44
 */
package core;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface Factory
{
	
	/**
	 * 当一个连接创建时,这这个连接同时创建的{@link User}对象
	 *
	 * @return 获取{@link User}对象
	 */
	User NewUser();
	
	/**
	 * 注册网络消息的回调对象
	 */
	void OnRegAllCallBack();
	
	void OnServerCloseCallBack(int second) throws Exception;
	
	void OnLinkProcessMsgTooMuch(User p_User) throws Exception;
	
	void OnUserNumChange(int currnum, int maxnum);
	
	void OnLinkNumChange(int currnum, int maxnum);

	void NeedSendEchoMsg(User p_User);
	
	byte[] GetEncryptBuffer();
}
