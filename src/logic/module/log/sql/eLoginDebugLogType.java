/**
 * eLoginDebugLogType.java 2013-1-23下午3:22:26
 */
package logic.module.log.sql;

import logic.MyUser;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
@SuppressWarnings("unused")
public enum eLoginDebugLogType
{
	RECEIVE,						///<1.收到登陆消息
	ERR_TOOLONG,					///<2.发现登陆数据,但是登陆时间过长(系统bug)
	REPEAT,							///<3.重复登陆消息,忽略
	REPLACE,						///<4.有另外一个用户挤掉了相同账号的用户
	ADD_RECORD,						///<5.添加了登陆记录,准备进行登陆操作
	LOGINSQL_ERR_CREATE,			///<6.执行LoginSQL,没有登陆数据,但是创建的时候出错了
	LOGINSQL_PASS,					///<7.执行LoginSQL,设置密码成功,登陆成功,说明这是第一次登陆
	LOGINSQL_ERR_CREATE_PASSWORD,	///<8.执行LoginSQL,设置密码时数据库出错了,无法登陆
	LOGINSQL_PASSWORD_CHECK,		///<9.执行LoginSQL,密码验证失败,拒绝登陆
	LOGINSQL_PASSWORD_OK,			///<10.执行LoginSQL,密码验证成功
	LOGINSQL_LISTROLE,				///<11.执行LoginMethod,LoginSQL成功完成,继续ListRole
	LOGIN_FAIL_ERR_FIND_RECORD,		///<12.登陆失败,寻找登陆记录,但是无法发现
	LOGIN_FAIL_ERR_CHANGE_RECORD,	///<13.登陆失败,修改登陆记录时发现,记录已经属于另外一个用户,不做修改
	LOGIN_FAIL_SET,					///<14.登陆失败,设置登陆记录状态为失败
	LISTROLE_ERR_LOGINDATA,			///<15.执行ListRole,出现错误,无法得到LoginData
	LISTROLE_NO_ROLE,				///<16.执行ListRole,没有发现角色数据
	LISTROLE_ROLE_OK,				///<17.执行ListRole,读取并设置了角色数据
	LISTROLE_WAIT_CREATE,			///<18.执行ListMethod,没有角色,等待用户创建角色
	LISTROLE_ERR_ROLEDATA,			///<19.执行ListMethod,无法找到用户角色数据,未知问题
	LOGIN_OK_ERR_NO_RECORD,			///<20.登陆成功,寻找登陆记录,但是无法发现,踢掉现在登陆的用户
	LOGIN_OK_ERR_CHANGE_RECORD,		///<21.登陆成功,修改登陆记录时发现,记录已经属于另外一个用户,踢掉现在登陆的用户
	LOGIN_OK_ERR_TOOLONG,			///<22.登陆成功,检查登陆时间时发现过长,踢掉现在登陆的用户
	LOGIN_OK_OK,					///<23.登陆成功,同时成功设置登陆记录状态为登陆成功
	LOGIN_SAME_FAILED,				///<24.执行登陆检查时,发现数据已经存在,并且是失败状态
	LOGIN_FORBID_OK,				///<25.检测禁止登陆标志通过(允许登陆)
	LOGIN_FORBID_FAIL,				///<26.检测禁止登陆标志失败(不允许登陆)
	LOGIN_FAIL_USER_NOT_EXIST,		///<27.登录失败，用户不存在(只在登录和注册逻辑分离的时候)
	REGISTER_FAIL_USERNAME_EXIST,	///<28.注册失败，用户名已被注册(只在登录和注册逻辑分离的时候)
	
	LOGIN_DIS_ERR_FIND_USERNAME,	///<50.用户下线,无法获取用户名,也就无法清除登陆记录,直接返回
	LOGIN_DIS_ERR_FIND_RECORD,		///<51.用户下线,无法找到登陆记录,跳过(不太正常)
	LOGIN_DIS_ERR_CHANGE_RECORD,	///<52.用户下线,登陆记录不是自己的,跳过(不太正常)
	LOGIN_DIS_OK,					///<53.用户下线,成功清理自己的登录记录
	
	LOGIN_MANULA_CLEAR,				///<100.手动清理了一个用户的登陆记录
	;
	
	public int ID()
	{
		switch (this)
		{
		case RECEIVE:return 1;						///<1.收到登陆消息
		case ERR_TOOLONG:return 2;					///<2.发现登陆数据,但是登陆时间过长(系统bug)
		case REPEAT:return 3;						///<3.重复登陆消息,忽略
		case REPLACE:return 4;						///<4.有另外一个用户挤掉了相同账号的用户
		case ADD_RECORD:return 5;					///<5.添加了登陆记录,准备进行登陆操作
		case LOGINSQL_ERR_CREATE:return 6;			///<6.执行LoginSQL,没有登陆数据,但是创建的时候出错了
		case LOGINSQL_PASS:return 7;				///<7.执行LoginSQL,设置密码成功,登陆成功,说明这是第一次登陆
		case LOGINSQL_ERR_CREATE_PASSWORD:return 8;	///<8.执行LoginSQL,设置密码时数据库出错了,无法登陆
		case LOGINSQL_PASSWORD_CHECK:return 9;		///<9.执行LoginSQL,密码验证失败,拒绝登陆
		case LOGINSQL_PASSWORD_OK:return 10;		///<10.执行LoginSQL,密码验证成功
		case LOGINSQL_LISTROLE:return 11;			///<11.执行LoginMethod,LoginSQL成功完成,继续ListRole
		case LOGIN_FAIL_ERR_FIND_RECORD:return 12;	///<12.登陆失败,寻找登陆记录,但是无法发现
		case LOGIN_FAIL_ERR_CHANGE_RECORD:return 13;///<13.登陆失败,修改登陆记录时发现,记录已经属于另外一个用户,不做修改
		case LOGIN_FAIL_SET:return 14;				///<14.登陆失败,设置登陆记录状态为失败
		case LISTROLE_ERR_LOGINDATA:return 15;		///<15.执行ListRole,出现错误,无法得到LoginData
		case LISTROLE_NO_ROLE:return 16;			///<16.执行ListRole,没有发现角色数据
		case LISTROLE_ROLE_OK:return 17;			///<17.执行ListRole,读取并设置了角色数据
		case LISTROLE_WAIT_CREATE:return 18;		///<18.执行ListMethod,没有角色,等待用户创建角色
		case LISTROLE_ERR_ROLEDATA:return 19;		///<19.执行ListMethod,无法找到用户角色数据,未知问题
		case LOGIN_OK_ERR_NO_RECORD:return 20;		///<20.登陆成功,寻找登陆记录,但是无法发现,踢掉现在登陆的用户
		case LOGIN_OK_ERR_CHANGE_RECORD:return 21;	///<21.登陆成功,修改登陆记录时发现,记录已经属于另外一个用户,踢掉现在登陆的用户
		case LOGIN_OK_ERR_TOOLONG:return 22;		///<22.登陆成功,检查登陆时间时发现过长,踢掉现在登陆的用户
		case LOGIN_OK_OK:return 23;					///<23.登陆成功,同时成功设置登陆记录状态为登陆成功
		case LOGIN_FORBID_OK:return 25;				///<25.检测禁止登陆标志通过(允许登陆)
		case LOGIN_FORBID_FAIL:return 26;			///<26.检测禁止登陆标志失败(不允许登陆)
		case LOGIN_SAME_FAILED:return 24;
		case LOGIN_FAIL_USER_NOT_EXIST:return 27;	///<27.登录失败，用户不存在(只在登录和注册逻辑分离的时候)
		case REGISTER_FAIL_USERNAME_EXIST:return 28;///<28.注册失败，用户名已被注册(只在登录和注册逻辑分离的时候)

		case LOGIN_DIS_ERR_FIND_USERNAME:return 50;	///<50.用户下线,无法获取用户名,也就无法清除登陆记录,直接返回
		case LOGIN_DIS_ERR_FIND_RECORD:return 51;	///<51.用户下线,无法找到登陆记录,跳过(不太正常)
		case LOGIN_DIS_ERR_CHANGE_RECORD:return 52;	///<52.用户下线,登陆记录不是自己的,跳过(不太正常)
		case LOGIN_DIS_OK:return 53;				///<53.用户下线,成功清理自己的登录记录
		
		case LOGIN_MANULA_CLEAR:return 100;			///<100.手动清理了一个用户的登陆记录
		}
		return 0;
	}
	
	public void Log(MyUser p_user)
	{
		Log(p_user, p_user.toString());
	}
	
	public void Log(MyUser p_user, String exdata)
	{
//		p_user.Log(eLogicSQLLogType.LOGIC_SQL_LOGINDEBUG, 
//				RootConfig.GetInstance().ServerUniqueID,
//				eLogicSQLLogType.GetCurrTime(),
//				p_user.GetUserName() == null ? "" : p_user.GetUserName(),
//				p_user.GetLink() == null ? "" : p_user.GetLink().GetIP(),
//				ID(),
//				exdata
//				);
	}
}
