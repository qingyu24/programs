package logic.module.room;

import java.util.ArrayList;

import core.remote.PI;
import core.remote.PS;
import core.remote.PU;
import core.remote.PVI;
import core.remote.RCC;
import core.remote.RFC;
import logic.MyUser;
import logic.Reg;

@RCC(ID = Reg.ROOM)
public interface RoomInterface {
    static final int MID_CREATE = 0; //创建;
    static final int MID_ENTER = 1; //进入房间;
    static final int MID_BROADCAST_ENTER = 2; // 广播进入房间;
    static final int MID_BROADCAST_LEAVE = 3; //广播离开房间;
    static final int MID_INITCARD = 4; //接收初始化牌;
    static final int MID_OPER_CARD = 5; //如何处理牌;
    static final int MID_READY = 6; // 游戏准备;
    static final int MID_BROADCAST_POS = 7; //广播出牌的位置;
    static final int MID_LEAVEROOM = 8; //广播出牌的位置;

    static final int MID_BROADCAST_HU = 9; //广播胡牌;
    static final int MID_CHANGE_ROOMCARD = 10; // 房卡变化;
    static final int MID_BROADCAST_LIUJU = 11; ///广播流局/

    static final int MID_RECONNECT = 12; // 重新进入房间;
    static final int MID_BROADCAST_APPLYDISMISS = 13; //申请解散；
    static final int MID_BROADCAST_AGREE = 14; // 广播同意解散;
    static final int MID_BROADCAST_DISMISS = 15; // 房间解散了;
    static final int MID_RETURN_ROOM = 16; // 返回房间；
    static final int MID_TEST_CARD = 17; // 返回房间；
    static final int MID_USER_STATE = 18; //更新用的状态：
    static final int MID_CHAT_MSG = 19; //
    static final int MID_BROADCAST_CHAT = 20;

    static final int MID_ROUND_OVER = 21; //整个牌局结束;
    static final int MID_FA_CARD = 22; //发牌的消息;
    static final int MID_KOU_CARD = 23; //扣牌的消息;
    static final int MID_BROAD_KOU_CARD = 24; //广播口排；
    static final int MID_KOU_OVER = 25; //扣牌结束;
    static final int MID_KOU_ROUND_OVER = 26; //扣牌结束;
    static final int MID_LAST_CARD = 27; //剩余牌数;

    static final int MID_BROADCAST_HUI = 28; //广播会儿牌；
    static final int MID_SEND_PIAO = 29; // 飘;
    static final int MID_BROADCAST_PIAO = 30; //广播飘;
    static final int MID_GET_LIST = 31; //获取排行榜;
    static final int MID_BROADCAST_STARTPIAO = 32; //开始飘;

    static final int MID_BROADCAST_SOMEBODYHU = 33; // 有人胡牌;

    static final int MID_SEND_YOU = 34; //通知客户端可以悠了；

    static final int MID_GET_YOU = 35; //获得客户端是否悠的通知;

    static final int MID_TELL_OPERATION = 36; //通知客户端执行什么操作;(1(吃),3（碰）,5（杠）,7（胡)

    static final int MID_TELL_BAO = 37; //通知宝牌;


    static final int MID_TEST_JIESUAN = 38; //结算接口测试；

    static final int MID_BROADCAST_SATTUS = 39; //玩家的状态广播;

    static final int MID_CARD_AUTO = 40; //自动摸牌出牌(当用户在能够悠牌);

    static final int MID__BROADCAST_CENCELDIS = 41; //取消解散房间

    static final int MID_BROADCAST_HDLY = 42; //开始海底捞月;

    //----------------------------------------------------------------

    static final int MID_BROADCAST_CHUBAO = 43; //打出一个没用的宝牌，继续摸宝

    static final int MID_BROADCAST_TING = 44; //广播听牌;

    static final int MID_TELL_TING = 45; //通知某个用户听牌了;


    @RFC(ID = MID_CREATE)
    void CreateRoom(@PU(Index = Reg.ROOM) MyUser p_user,
                    @PI int round, @PI int fan,
                    @PI int wanfa, @PVI ArrayList<Integer> list, @PI int jifen, @PVI ArrayList<Integer> list2);

    @RFC(ID = MID_ENTER)
    void EnterRoom(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PS String ip);

    @RFC(ID = MID_OPER_CARD)
    void OperCard(@PU(Index = Reg.ROOM) MyUser p_user, @PI int type, @PVI ArrayList<Integer> list);

    @RFC(ID = MID_INITCARD)
    void InitCard(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PS String ip);

    @RFC(ID = MID_READY)
    void GameReady(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);

    @RFC(ID = MID_LEAVEROOM)
    void LeaveRoom(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);

    @RFC(ID = MID_RECONNECT)
    void Reconnect(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);

    @RFC(ID = MID_BROADCAST_APPLYDISMISS)
    void ApplyDismiss(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);

    @RFC(ID = MID_BROADCAST_AGREE)
    void AgreeDismiss(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PI int result);

    @RFC(ID = MID_BROADCAST_DISMISS)
    void DismissRoom(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);

    @RFC(ID = MID_TEST_CARD)
    void TestCard(@PU(Index = Reg.ROOM) MyUser p_user, @PVI ArrayList<Integer> list, @PVI ArrayList<Integer> list2);

    @RFC(ID = MID_CHAT_MSG)
    void SendChat(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PI int type, @PS String id);


    @RFC(ID = MID_KOU_CARD)
    void KouCard(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PVI ArrayList<Integer> list);

    @RFC(ID = MID_GET_LIST)
    void GetList(@PU(Index = Reg.ROOM) MyUser p_user, @PI int page);

    @RFC(ID = MID_SEND_PIAO)
    void SendPiao(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PI int type);

    @RFC(ID = MID_GET_YOU)
    void GetYou(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId, @PI int ret);

    @RFC(ID = MID_TEST_JIESUAN)
    void TestJieSuan(@PU(Index = Reg.ROOM) MyUser p_user,
                     @PI int round,
                     @PI int fan,
                     @PI int wanfa,
                     @PVI ArrayList<Integer> list,
                     @PI int jifen,
                     @PVI ArrayList<Integer> list2,
                     @PI int zimo,
                     @PI int dianpao,
                     @PI int ytl,
                     @PI int qys,
                     @PI int qysytl,
                     @PI int hy,
                     @PI int mg,
                     @PI int ag,
                     @PI int piao

    );

    @RFC(ID = MID_CARD_AUTO)
    void AutoCard(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);

    @RFC(ID = MID__BROADCAST_CENCELDIS)
//取下
    void cenceldis(@PU(Index = Reg.ROOM) MyUser p_user, @PI int roomId);


}
