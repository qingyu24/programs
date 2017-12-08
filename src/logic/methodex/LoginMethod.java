/**
 * MethodLogin.java 2012-6-24上午11:33:59
 */
package logic.methodex;

import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.module.log.sql.eLoginDebugLogType;
import logic.module.login.Login;
import logic.module.login.eLoginErrorCode;
import logic.sqlrun.MySQLRun;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class LoginMethod extends MyMethodEx
{
	public LoginMethod(MyUser p_User, MySQLRun p_SQLRun)
	{
		super(p_User, p_SQLRun);
	}

	// 在SqlRun执行完毕之后调用，也就是LoginSQLRun执行完之后，根据执行的结果进行后续处理
	@Override
	public void OnRunDirect(MyUser p_User, MySQLRun p_SQLRun) throws Exception
	{
		if ( p_User.IsDisabled() )
		{
			Login.GetInstance().SetLoginFail(p_User);
			Login.GetInstance().RemoveLoginInfo(p_User);
			return;
		}
		
		eLoginErrorCode r = p_User.getLoginRes();
		System.out.println("* LoginMethod result:" + r);
		int res = r.ID();
		PackBuffer.GetInstance().Clear().AddID(Reg.LOGIN,0).Add(res).Send(p_User);		// 通知客户端登录结果
		if ( r == eLoginErrorCode.LOGIN_OK ||  r == eLoginErrorCode.REGISTER_OK)
		{
			
			// 登录成功的话，请求列出用户所拥有的角色
			System.out.println("* LoginMethod : %d");
			eLoginDebugLogType.LOGINSQL_LISTROLE.Log(p_User);
			Login.GetInstance().RemoveLoginInfo(p_User);
			//登录成功了.//todo;
			
			
		}else
		{
			Login.GetInstance().SetLoginFail(p_User);
			Login.GetInstance().RemoveLoginInfo(p_User);
		}
	}
}
