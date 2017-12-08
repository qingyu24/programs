package logic.module.character;

import core.Root;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.PackBuffer;
import logic.Reg;
import logic.eGameState;
import logic.module.room.Room;
import manager.RoomManager;

public class CharacterImpl implements CharacterInterface
{

    @Override
    public void RequestBaseInfo(MyUser p_user)
    {
        p_user.client_DataReady = true;
        Send_BaseData(p_user);
    }

    public void ServerDataReady(MyUser p_user)
    {
        p_user.server_DataReady = true;
        Send_BaseData(p_user);
    }

    public void Send_BaseData(MyUser p_user)
    {
    	
        if (false == p_user.client_DataReady || false == p_user.server_DataReady)
            return;
        // 仅发送一次
        p_user.client_DataReady = false;
        System.out.println("发送玩家的基础数据到客户端:");
        this._SToC_PlayerUserData(p_user);
        
    }
    private void _SToC_PlayerUserData(MyUser p_user)
    {
    	System.out.println("Send Msg _SToC_PlayerUserData");
		SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.CHARACTER, 0);
		Room r = RoomManager.getInstance().getRoom(p_user.GetRoleGID());
		if(null != r){
			if(r.getState()==eGameState.GAME_ALL_OVER){
				p.Add(-1);
			}else{
			p.Add(r.getId());}
		}else{
			p.Add(-1);
		}
		boolean b = p_user.packBaseData(p);
		if (b) 
		{
			p.Send(p_user);
		}
		Root.GetInstance().AttachUser(p_user);
    }

    private static CharacterImpl s_instance = new CharacterImpl();

    public static CharacterImpl GetInstance()
    {
        return s_instance;
    }

}
