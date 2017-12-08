/**
 * LoginRecord.java 2013-2-26上午11:37:38
 */
package logic.module.login;

import utility.Debug;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginRecord
{
	public enum LoginState
	{
		LOGINING, 			///<正在登陆
		LOGIN_CREATE,		///<在创建角色
		LOGIN_CREATEFINISH,	///<创建角色完成
		LOGINED_SUCCESS,	///<登陆完成,此状态在用户加入用户管理后设置
		LOGINED_FAIL,		///<登陆失败,此状态在用户加入用户管理前的任意失败后设置
	}
	
	private long m_LoginTime;
	private LoginState m_State;
	private int m_HashCode;	///<MyUser对象的hashcode
	private long m_CreateStartTime;
	private long m_CreateFinishTime;
	
	public LoginRecord(int hashcode)
	{
		Init(hashcode);
	}
	
	public void Init(int hashcode)
	{
		m_LoginTime = System.currentTimeMillis();
		m_State = LoginState.LOGINING; 
		m_HashCode = hashcode;
		m_CreateStartTime = 0;
		m_CreateFinishTime = 0;
		System.out.println("* 初始化登陆记录,用户:" + m_HashCode + " 登陆时间:" + Debug.GetShowTime(m_LoginTime) );
	}
	
	public long GetLoginUseTime()
	{
		return System.currentTimeMillis() - m_LoginTime - (m_CreateFinishTime - m_CreateStartTime);
	}
	
	public long GetLoginTime()
	{
		return m_LoginTime;
	}
	
	public int GetHashCode()
	{
		return m_HashCode;
	}

	public void SetState(LoginState s)
	{
		m_State = s;
		switch(s)
		{
		case LOGIN_CREATE:
			m_CreateStartTime = System.currentTimeMillis();
			m_CreateFinishTime = m_CreateStartTime;
			break;
		case LOGIN_CREATEFINISH:
			m_CreateFinishTime = System.currentTimeMillis();break;
		}
	}
	
	public LoginState GetState()
	{
		return m_State;
	}
	
	public String toString()
	{
		return super.toString() + 
				" m_LoginTime:" + Debug.GetShowTime(m_LoginTime) + 
				" m_State:" + m_State + 
				" m_HashCode:" + m_HashCode + 
				" m_CreateStartTime:" + Debug.GetShowTime(m_CreateStartTime) + 
				" m_CreateFinishTime:" + Debug.GetShowTime(m_CreateFinishTime) ;
	}
}
