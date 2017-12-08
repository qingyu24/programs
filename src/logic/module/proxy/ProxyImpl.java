package logic.module.proxy;

import core.DBMgr;
import core.Root;
import core.User;
import core.detail.impl.socket.SendMsgBuffer;
import core.remote.PI;
import core.remote.PL;
import core.remote.PU;
import core.remote.RFC;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.loader.UserLoader;
import logic.userdata.account;
import manager.LoaderManager;

public class ProxyImpl implements ProxyInterface {

	@Override
	@RFC(ID = 1)
	public void GetAllUser(@PU(Index = 1) MyUser p_user, @PI int page) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@RFC(ID = 2)
	public void AddScore(@PU(Index = 1) MyUser p_user, @PL long roleId, @PI int score) {
		// TODO Auto-generated method stub
		UserLoader loader = (UserLoader)LoaderManager.getInstance().getLoader(LoaderManager.Users);
		int ret = loader.addScore(p_user.GetRoleGID(),roleId, score);
		User target = Root.GetInstance().GetUserByGid(roleId);
		String nickName = "";
		if(null != target){
			nickName = target.GetNick();
			SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.PROXY, ProxyInterface.MID_UPDATE_SCORE);
			buffer.Add(ret);
			buffer.Send(target);
		}else{
			account[] acc = DBMgr.ReadSQL(new account(), "select * from account where RoleID=" + roleId);
			nickName = acc[0].nickName.Get();
		}
		//添加充值的记录;
		p_user.getCenterData().addRecord(roleId, score, nickName);
		//
		SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.PROXY, ProxyInterface.MID_ADDSCORE);
		buffer.Add(1);
		buffer.Send(p_user);
	}

	@Override
	@RFC(ID = 3)
	public void GetUser(@PU(Index = 1) MyUser p_user, @PI int tid) {
		// TODO Auto-generated method stub
		UserLoader loader = (UserLoader)LoaderManager.getInstance().getLoader(LoaderManager.Users);
		account usr = loader.getUser(tid);
		SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.PROXY, ProxyInterface.MID_GETUSER);
		if(null != usr){
			buffer.Add(1);
			buffer.Add(usr.RoleID.Get());
			buffer.Add(usr.nickName.Get());
		}else{
			buffer.Add(0);
		}
		buffer.Send(p_user);
	}

	@Override
	@RFC(ID = 4)
	public void GeChargeRecord(@PU(Index = 1) MyUser p_user, @PI int page) {
		// TODO Auto-generated method stub
		SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.PROXY, ProxyInterface.MID_GET_CHARGE_RECORD);
		p_user.getCenterData().packRecordList(buffer, page);
		buffer.Send(p_user);
		
	}
	
	

}
