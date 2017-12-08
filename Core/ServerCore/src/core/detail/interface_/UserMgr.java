/**
 * UserMgr.java 2012-6-27上午10:21:58
 */
package core.detail.interface_;

import java.util.ArrayList;

import core.User;
import core.UserSelector;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public interface UserMgr
{

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param p_User
	 */
	void AttachUser(User p_User);

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param p_Link
	 * @return
	 */
	User CreateUser(Link p_Link);

	/**
	 * 所有的User是否都空了
	 */
	boolean Empty();

	/**
	 * 检测是否保存所有的用户数据任务是否执行完成
	 */
	boolean ExecuteSaveAllUserDataFinish();
	
	ArrayList<User> GetAllOnlineUser();

	int GetNum();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_lGid
	 * @return
	 */
	User GetUserByGid(long m_lGid, boolean create);

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_NetID
	 * @return
	 */
	User GetUserByNetID(int m_NetID);

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_sRoleNick
	 * @return
	 */
	User GetUserByNick(String m_sRoleNick, short serverid, boolean create);

	ArrayList<User> GetUserBySelector(UserSelector s);
	
	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param p_username
	 * @return
	 */
	User GetUserByUserName(String p_username);
	
	User GetUserByUserName(String m_sUserName, String m_sPassword, short serverid, boolean b);

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 * @param m_User
	 */
	void OnUserDisconnected(User m_User);

	void RegUserSelector(UserSelector s);

	/**
	 * 保存所有的用户数据
	 */
	void SaveAllUserData();

	/**
	 * 
	 *
	 * <br>测试代码:{@link }
	 *
	 */
	void Update();
}
