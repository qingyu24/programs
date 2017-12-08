/**
 * User.java 2012-6-11下午9:35:53
 */
package core;

import java.util.*;

import core.detail.*;
import core.detail.interface_.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface User
{

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	UserState GetState();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_DataReadyIndex
	 * @return
	 */
	boolean DataReady();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 */
	void Close(int reason, int ex);

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	boolean KeyDataReady();
	
	boolean NickReady();
	
	boolean GIDReady();
	
	boolean UserNameReady();
	
	void SetLink(Link p_link);
	
	void SetNick(String p_sNick);
	
	void SetRoleGID(long p_lGID);
	
	void SetNetID(int p_nNetID);
	
	void SetUserName(String p_sUserName);
	
	String GetNick();
	
	String GetServerNick();
	
	long GetRoleGID();
	
	int GetNetID();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 * @throws Exception 
	 *
	 */
	void ExecuteKeyDataSQLRun() throws Exception;

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_DataReadyIndex
	 * @return
	 */
	ArrayList<SQLRun> GetSQLRun();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @return
	 */
	Link GetLink();

	/**
	 * 关键数据是否都准备好了
	 */
	boolean IsKeyDataReady();
	
	/**
	 * 昵称数据是否准备好了
	 */
	boolean IsNickReady();
	
	/**
	 * 角色id是否准备好了
	 */
	boolean IsGIDReady();
	
	/**
	 * 获取账号名
	 */
	String GetUserName();
	
	/**
	 * 获取带服务器id的账号名
	 */
	String GetServerUserName();

	/**
	 * 断开时的回调
	 */
	void OnDisconnect() throws Exception;
	
	/**
	 * 释放时的回调
	 */
	void OnRelease() throws Exception;

	/**
	 * 把u的数据合并进入本身
	 */
	void Merge(User u);

	/**
	 * 是否为断开状态
	 */
	boolean IsDisConnected();

	/**
	 * 把所有的UserData存入数据库
	 */
	void SaveAllToDB();
	
	void StartMethodMark(MethodEx mex);
	
	void EndMethodMark(MethodEx mex);
	
	void OnRunDirect();
	
	boolean IsDisabled();
	
	/**
	 * 是否急切的需要存一次数据
	 * 
	 * @return 如果进行过一次存储,那么就不再处于急切状态
	 */
	boolean IsNeedFirstSaveToDB();
	
	/**
	 * 写入日志
	 */
	void Log(LogType type, Object... params);
	
	/**
	 * 带运行堆栈的日志
	 * 
	 * @tip 带堆栈会有较大的消耗(相对于函数调用),慎用,在最终发布的时候同Log,也就是不会出现堆栈信息
	 */
	void LogTrace(LogType type, Object... params);

	/**
	 * 记录一个异常
	 */
	void LogException(Throwable e);
	
	/**
	 * 记录一个异常
	 */
	void LogException(Throwable e, boolean log);
	
	/**
	 * 记录一个异常
	 */
	void LogException(Throwable e, String s);
	
	void SetCloseReason(int reason, int ex);
	
	int GetCloseReason();
	
	int GetCloseReasonEx();

	void SetPassword(String m_sPassword);
	
	String GetPassword();
	
	void SetServerID(short serverid);
	
	short GetServerID();
}
