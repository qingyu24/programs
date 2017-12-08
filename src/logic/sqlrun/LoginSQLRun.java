/**
 * LoginSQLRun.java 2012-6-24上午10:47:42
 */
package logic.sqlrun;

import core.DBMgr;
import core.RootConfig;
import core.db.RoleIDUniqueID;
//import core.detail.impl.log.*;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.eLogicCloseUserReason;
import logic.module.log.eLogicSQLLogType;
import logic.module.log.sql.eLoginDebugLogType;
import logic.module.login.Login;
import logic.module.login.eLoginErrorCode;
import logic.userdata.account;
import logic.userdata.logindata;
import utility.UniqueID;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginSQLRun extends MySQLRun
{
	private static logindata m_LoginData = new logindata();	///<这个只当成模板用,没实际效果
	private String m_sUserName;
	private String m_sPassword;
	private String m_sToken;		// 客户端段提交的accesstoken
	private String m_sServerToken;	// 服务器生成的accesstoken
	private boolean m_bForbid;
	private int m_nServerID;
	private String m_sDeviceIdentifier;
	private String m_sDeviceModel;
	private boolean m_bCreate;
	private String m_nickname; 
	private String m_pass;
	private String m_headurl;
	private int m_sex;
	private static final int USERTYPE1 = 1; // 管理员登录u;
	private static final int USERTYPE2 = 2; //盟商登录;
	private static final int USERTYPE3 = 3; // 会员登录；
	
	
//	private long stm = 0;
	private static UniqueID m_IDBuild = new UniqueID();
	private static final String m_CreateUserData = "INSERT INTO LoginData (`UserName`,`UserID`, `Password`, `ServerID`, `ChannelID`) VALUES ('%s', %d, '%s', %d, %d)";
	private static final String m_SelectUserData = "SELECT * FROM LoginData WHERE UserName = '%s' AND ServerID = %d";
	private static final String m_user_query = "select * from account where openid = '%s'";
	
	//这是只是注册账号的，以后还会有微信登录的；
	private static final String m_user_create = "insert into account(RoleID, nickName, Password, openid, headIcon, sex) values (%d, '%s', '%s','%s','%s',%d)";//
	private static final String m_MaxRoleData = "SELECT * FROM account ORDER BY ROLEID DESC LIMIT 1";
	
	public LoginSQLRun(MyUser p_user, 
			String p_sUserName, 
			String p_sToken, 
			String pass, 
			String p_sServerToken, 
			boolean forbid, 
			int serverid, 
			String deviceIdentifier, 
			String deviceModel, 
			boolean isCreate,
			String headurl,
			String sex)
	{
		m_sUserName = p_sUserName;
		m_sPassword = pass;
		m_sToken = p_sToken;
		m_sServerToken = p_sServerToken;
		m_bForbid = forbid;
		m_nServerID = serverid;
		m_sDeviceIdentifier = deviceIdentifier.substring(0, Math.min(deviceIdentifier.length(), 60));
		m_sDeviceModel = deviceModel.substring(0, Math.min(deviceModel.length(), 60));
		m_bCreate = isCreate;
		m_headurl = headurl;
		if(sex == ""){
			m_sex = 1;
		}else{
			m_sex = Integer.parseInt(sex);
		}
		
	}
	
	/* 
	 * 登录数据查询
	 */
	@Override
	public void Execute(MyUser p_User) throws Exception
	{
		_ExecuteUser(p_User);//todo switch;
	}
	
	private void _ExecuteUser(MyUser p_User) throws Exception
	{
		if(!this.m_sToken.equals("")){
			this.m_nickname = this.m_sUserName;
			this.m_sUserName = this.m_sToken;
			
		}
		account[] ds = DBMgr.ReadSQL(new account(), String.format(m_user_query, m_sUserName));
		account d = null;
		if (ds.length > 1)
		{
			//读取到多条数据,程序不处理,让玩家找GM协助
			eLoginDebugLogType.LOGINSQL_ERR_CREATE.Log(p_User);
			p_User.setLoginData(null, eLoginErrorCode.UNKNOW);
			return;
		}
		
		else if (ds.length == 1)
		{
			d = ds[0];
		}
		
		_CheckLoginUserByPassword(d, p_User);
		
	}
	
	
	private void _CheckLoginUserByPassword(account d, MyUser p_User)
	{
		if(d == null)
		{
			// 没有找到用户的数据，用户名尚未注册
			if(!m_bCreate)
			{
				// 没有请求创建新用户数据，则返回用户不存在
				
				if(!this.m_sToken.equals("")){
					System.out.println("* 第三方登录");
					d = _CreateUser(p_User);
					if(d == null)
						return;
				}else{
					System.out.println("* 用户不存在");
					eLoginDebugLogType.LOGIN_FAIL_USER_NOT_EXIST.Log(p_User);
					p_User.setLoginData(null, eLoginErrorCode.USERNAME_NOTEXIST);
					return;
				}
				
			}
			else
			{
				// 需要创建用户
				d = _CreateUser(p_User);
				if(d == null)
					return;
			}
		}
		else
		{
			// 找到用户的数据，用户名已经注册
			if(m_bCreate)
			{
				// 如果请求创建新用户数据，则返回用户已被注册
				System.out.println("* 用户名已被注册");
				//eLoginDebugLogType.REGISTER_FAIL_USERNAME_EXIST.Log(p_User);
				p_User.setLoginData(null, eLoginErrorCode.USERNAME_ALREADYEXIST);
				return;
			}
			else
			{
				//验证密码
				if(!_CheckPassword(d, p_User))
					return;
			}
		}
		// 验证是否封禁
		//if(!_CheckForbid(d, p_User))
		//	return;
		// 一切OK
		//d.LastLoginTime.Set(System.currentTimeMillis());
		if(m_bCreate){
			p_User.setLoginData(null, eLoginErrorCode.REGISTER_OK);
			//_Log(p_User, d, eLoginErrorCode.REGISTER_OK);
		}else{
			p_User.setLoginData(null, eLoginErrorCode.LOGIN_OK);
			//_Log(p_User, d, eLoginErrorCode.LOGIN_OK);
		}

	}
	
	
	private account _CreateUser(MyUser p_User)
	{
		System.out.println("* 没有登陆数据,创建登陆数据");
		boolean c = false;
		RoleIDUniqueID build = DBMgr.GetCreateRoleUniqueID();
		account[] maxrd = DBMgr.ReadSQL(new account(), m_MaxRoleData);
		if (maxrd.length == 0)
		{
			build.SetBaseValue(0);
		}
		else
		{
			build.SetBaseValue(maxrd[0].RoleID.Get());
		}
		
		//这里我还会添加微信登录的接口：
		//我现在哪roleid当openid来用；
		long userID = build.Get();
		String nickName = "random" + Math.random() * 10000;
		String uname = m_sUserName;
		if(!this.m_sToken.equals("")){
			nickName = this.m_nickname;
			uname = this.m_sToken;
		}
		
		c = DBMgr.ExecuteSQL(String.format(m_user_create, userID, nickName, m_sPassword, uname, m_headurl, m_sex));
		
		account d = null;
		if ( c )
			d = _GetLoginData();
		
		if ( !c || d == null )
		{
			Login.GetInstance().SetLoginFail(p_User);
			System.out.println("* 创建登陆数据失败");
			//eLoginDebugLogType.LOGINSQL_ERR_CREATE.Log(p_User);
			p_User.setLoginData(null, eLoginErrorCode.UNKNOW);
		}
		else
		{
			p_User.SetRoleGID(userID);
			p_User.SetNick(nickName);
			System.out.println("* 获取角色,进入世界:" + p_User.GetRoleGID());
			boolean c1 = Login.GetInstance().SetLoginSuccess(p_User);
			if ( !c1 )
			{
				p_User.Close(eLogicCloseUserReason.UPDATE_LOGINDATA_FAIL.ID(), 0);
				System.out.println("* 用户加载成功,但是登陆配置出错,断开用户:" + p_User.GetRoleGID());
				
			}
			else
			{
				//Root.GetInstance().AttachUser(p_User);
				PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN,2).
				Add(userID).
				Add(nickName).
				Send(p_User);
				System.out.println("* 创建登陆数据成功");
			}
			//p_User.Log(eLogicSQLLogType.LOGIC_SQL_REGISTER, 
			//		RootConfig.GetInstance().ServerUniqueID,
			//		eLogicSQLLogType.GetCurrTime(),
			//		userID,
			//		m_sUserName,
			//		p_User.GetLink().GetIP(),
			//		m_sDeviceIdentifier,
			//		m_sDeviceModel,
			//		1);
		}
		return d;
	}
	
	
	private boolean _CheckPassword(account data, MyUser p_User)
	{
		String pass = "";
		account p2 = (account)data;
		pass = p2.Password.Get();
		//todo;
		
		if ( !pass.equals(m_sPassword))
		{
			System.out.println("* 密码错误");
			//eLoginDebugLogType.LOGINSQL_PASSWORD_CHECK.Log(p_User);
			p_User.setLoginData(null, eLoginErrorCode.PASSWORD_ERR);
			//_Log(p_User, d, eLoginErrorCode.PASSWORD_ERR);
			Login.GetInstance().SetLoginFail(p_User);
			return false;
		}else{
			System.out.println("* 密码正确(重置)");
			//eLoginDebugLogType.LOGINSQL_PASSWORD_OK.Log(p_User);
			long roleId = 0;
			p_User.SetRoleGID(p2.RoleID.Get());
			p_User.SetNick(p2.nickName.Get());
			String roleName = data.nickName.Get();
			//
			boolean  ret = Login.GetInstance().SetLoginSuccess(p_User);
			if(ret){
				//Root.GetInstance().AttachUser(p_User);
				roleId = p2.RoleID.Get();
				
				System.out.println("* 获取角色,进入世界:" + p_User.GetRoleGID());
				PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN,2).
				Add(roleId).
				Add(roleName).
				Send(p_User);
			}
			
			return true;
		}
			
	}
	
	
	
	private boolean _CheckForbid(logindata d, MyUser p_User)
	{
		if (m_bForbid)
		{
			d.Forbid.Set(1);
			DBMgr.UpdateUserNameData(d);
//			Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, "_CheckForbid DBMgr.UpdateUserNameData");
			eLoginDebugLogType.LOGIN_FORBID_FAIL.Log(p_User);
			p_User.setLoginData(null, eLoginErrorCode.LOGIN_FORBID);
			return false;
		}
		if (d.Forbid.Get() == 0 )
		{
			eLoginDebugLogType.LOGIN_FORBID_OK.Log(p_User);
//			p_User.GetLoginUserData().SetLoginData(d, LoginUserData.eLoginRes.OK);
			return true;
		}
		else
		{
			eLoginDebugLogType.LOGIN_FORBID_FAIL.Log(p_User);
			p_User.setLoginData(null, eLoginErrorCode.LOGIN_FORBID);
			return false;
		}
//		Log.out.Log(eSystemInfoLogType.SYSTEM_INFO_SQL_STATEMENT, System.currentTimeMillis() - stm, "_CheckForbid Finish");
	}
	
	private account _GetLoginData()
	{
		
		account[] ds = DBMgr.ReadSQL(new account(), String.format(this.m_user_query, m_sUserName));
		if ( ds != null && ds.length == 1 )
		{
			return ds[0];
		}
		else
		{
			return null;
		}
	}

	private void _Log(MyUser p_User, logindata d, eLoginErrorCode c)
	{
		p_User.Log(eLogicSQLLogType.LOGIC_SQL_LOGIN, 
				RootConfig.GetInstance().ServerUniqueID,
				eLogicSQLLogType.GetCurrTime(),
				d != null ? d.UserID.Get() : 0,
				d != null ? d.UserName.Get() : "",
				p_User.GetLink().GetIP(),
				m_sDeviceIdentifier,
				m_sDeviceModel,
				c.ID());
	}
}
