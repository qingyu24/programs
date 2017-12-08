/**
 * MethodEx.java 2012-6-12下午10:16:42
 */
package core.detail.interface_;

import core.User;
import core.detail.impl.socket.MsgBuffer;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface MethodEx
{
	enum RunResult
	{
		RUNDIRECT,	//已经直接运行了
		DATA,		//因为有未加载的数据,需要进行数据加载
		BLOCK,		//对象因为某种原因(数据未准备好),先不进行逻辑处理
	}
	
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	MethodEx Clone();
	
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param buffer
	 * @param m_User 
	 * @throws Exception 
	 */
	void ParseMsg(MsgBuffer buffer, User m_User) throws Exception;

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 * @throws Exception 
	 *
	 */
	RunResult Run() throws Exception;
	
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 * @throws Exception 
	 *
	 */
	void RunDirect() throws Exception;

	void SqlRun() throws Exception;

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 */
	void Close(int reason);

	/**
	 * 所有的User数据是否都准备好了
	 */
	boolean IsAllDataReady();

	/**
	 * 得到其上绑定的User对象
	 */
	User GetUser();

	/**
	 * 通知MethodEx对象所有的操作都完成了
	 */
	void Finish();
	
	int GetClassID();
	
	int GetMethodID();
	
	/**
	 * 能否执行逻辑
	 */
	boolean CanExecute();
}
