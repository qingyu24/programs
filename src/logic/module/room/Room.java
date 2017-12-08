package logic.module.room;

import core.Root;
import core.Tick;
import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.*;
import logic.config.MahJongConfig;
import logic.config.handler.MahJongHandler;
import logic.module.log.eLogicDebugLogType;
import logic.module.log.eLogicSQLLogType;
import logic.module.room.RoomUser.piaoTag;
import logic.userdata.HLDDeskBalance;
import logic.userdata.IDeskBalance;
import logic.userdata.JZDeskBalance;
import logic.userdata.XADeskBalance;
import manager.CardManager;
import manager.ConfigManager;
import manager.RoomManager;

import java.util.*;
import java.util.Map.Entry;

public class Room implements Tick {

    private int roomId;
    private eGameState roomState;
    private ArrayList<RoomUser> userInfo = new ArrayList<RoomUser>();
    private Map<Long, RoomUser> m_users = new HashMap<Long, RoomUser>();
    private Map<Integer, RoomUser> m_posusers = new HashMap<Integer, RoomUser>();
    private RoomRule m_rule;
    private ArrayList<CardInfo> m_cards;
    private ArrayList<CardInfo> m_allCards;
    private long m_timeid;
    private int mTime1; // 开始倒计时;
    private int mTime2; // 开始倒计时;
    private static int max_time = 0; // 摸牌等待时间;
    private static int max_chutime = 18000000; // 出牌等待时间；
    private boolean startRecord;
    private int m_position; // 当前的出牌的位置;
    private int m_round;
    private int m_ju;
    private long master_roleId; // 房主的id;
    public static int ms_lastcard = 12;
    private int mTime3; // 解散房间的倒计时;
    private ArrayList<CardInfo> m_cardList = new ArrayList<CardInfo>(); // 出牌的队列；
    private ArrayList<CardInfo> m_deskcards = new ArrayList<CardInfo>(); // 牌桌上的可见牌的队列；
    private int m_destroyRoomTime = 0;
    private int m_kouRound; // 扣牌的轮次;
    private int max_kouRound = 4; // 最多扣牌的人次;
    private int max_lastKouRound = 4; // 剩余最大轮数；
    private int m_curKou; // 当前的扣牌人次;
    private int m_anGangCount; // 暗杠的数量;
    private boolean m_ting; // 是否已经有人听牌;
    private ArrayList<Integer> m_huilist = new ArrayList<Integer>();
    private HashMap<Long, ArrayList<eCardState>> m_pgh_list = new HashMap<Long, ArrayList<eCardState>>();
    private Map<Long, Integer> m_userState = new HashMap<Long, Integer>(); // 记录玩家对房间是否解散的态度;0
    private List<Integer> list = new ArrayList<>();
    private Map<Integer, RoomUser> m_userlist = new HashMap<Integer, RoomUser>();
    // 便是拒绝;
    public int guo;// 锅里的分
    public boolean gameOver = false;
    public ArrayList m_guoScore;
    public long m_firstZhuang;
    private CardInfo m_mopai;
    private MyUser mWinner; // 当前这个房间的胜利者;
    public CardInfo lastCard;
    public boolean noticeTing;
    private int huiCard;
    private boolean hdlBegin;

    // 记录当前出牌的那个人;
    public Room(int id, long roleId) {
        huiCard = 0;
        roomId = id;
        master_roleId = roleId;//房主,创建房间的人
        this.m_timeid = Root.GetInstance().AddLoopTimer(this, 1, null);
        this.m_position = -1;
        this.roomState = eGameState.GAME_START;
        m_round = 1;
        lastCard = null;
        m_ju = 0;
        m_destroyRoomTime = 0;
        m_kouRound = 0;
        m_curKou = 0;
        m_anGangCount = 0;
        m_userState.clear();
        list = this.random();
        gameOver = false;
        this.guo = 0;
        m_guoScore = new ArrayList<>();
        this.mWinner = null;
        this.ms_lastcard = CardManager.MS_BASE_LASTCARD;
        this.m_ting = false;
        m_allCards = new ArrayList<CardInfo>();
        m_mopai = null;
        hdlBegin = false;
    }

    public boolean isHdlBegin() {
        return hdlBegin;
    }

    public void setHdlBegin(boolean hdlBegin) {
        this.hdlBegin = hdlBegin;
    }

    public MyUser getWinner() {
        return mWinner;
    }

    public int getId() {
        return roomId;
    }

    public eGameState getState() {
        return roomState;
    }

    public boolean checkPiaoState() {
        Iterator<RoomUser> it = this.userInfo.iterator();
        while (it.hasNext()) {
            if (it.next().getPiao() == piaoTag.NONE) {
                String fstr1 = String.format("有一家没有操作飘 \n");
                LogRecord(null, fstr1);
                return false;
            }
        }
        it = this.userInfo.iterator();
        while (it.hasNext()) {
            RoomUser ru = it.next();
            String fstr1 = String.format("飘的最终结果：%s \n", ru.getPiao());
            LogRecord(ru.getUser(), fstr1);
        }
        return true;
    }

    public void changeRoomState(eGameState state) {
        eGameState pre = this.roomState;
        this.roomState = state;
        if (pre == eGameState.GAME_KOU && state == eGameState.GAME_PLAYING) {
            // 开始倒计时出牌了;
            this.getZhuang().setState(eUserState.CHUPAI);
        }
    }

    public boolean dismissRoom(MyUser user, int agree) {
        this.m_userState.put(user.GetRoleGID(), agree);
        this.broadcast(RoomInterface.MID_BROADCAST_AGREE, user.GetRoleGID(), agree);
        return this.processDismissRoom();
    }

    private boolean processDismissRoom() {
        int count1 = 0;
        int count2 = 0;
        Iterator<Entry<Long, Integer>> it = this.m_userState.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Long, Integer> s = it.next();
            if (s.getValue() == 1) {
                count1++;
            }
            if (s.getValue() == 2) {
                count2++;
            }
        }

        if (count1 >= 3) {
            // 同意解散；//广播解散成功;
            this.broadcast(RoomInterface.MID_BROADCAST_DISMISS, 1);
            // todo 把房间删除掉;
            RoomManager.getInstance().removeRoom(this.roomId);
            return true;
        }
        // 两个人不同意的话，解散失败;
        if (count2 >= 2) {
            // 拒绝解散; //广播不能解散;
            m_userState.clear();
            this.broadcast(RoomInterface.MID_BROADCAST_DISMISS, 2);
            this.changeRoomState(eGameState.GAME_PLAYING);

        }
        return false;
    }

    public RoomRule getRoomRule() {
        return m_rule;
    }

    public void setRoomRule(RoomRule rule) {

        m_rule = rule;
    }

    public int getUserCount() {
        return userInfo.size();
    }

    public boolean isFull() {
        return userInfo.size() >= 4;
    }

    public void clearUser() {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomManager.getInstance().removeRoomUser(it.next().getRoleId());
            it.remove();
        }
    }

    private List<Integer> random() {
        ArrayList<Integer> list = new ArrayList<>();
        while (list.size() < 3) {
            int i = (int) (Math.random() * 3) + 1;
            Integer j = Integer.valueOf(i);
            if (!list.contains(j)) {
                list.add(j);
            }
        }
        return list;

    }

    public RoomUser addUser(MyUser u, String ip) {
        RoomUser user = new RoomUser(u, ip);
        if ((CardManager.getInstance().ishuludao || CardManager.getInstance().isjinzhou) && userInfo.size() != 0) {
            int i = userInfo.size() - 1;
            if (i == -1) {

            }
            user.setPos(this.list.get(i).intValue());
            user.setRoomId(roomId);
            m_posusers.put(this.list.get(i), user);
            userInfo.add(user);
            m_userlist.put(this.list.get(i), user);
            String fstr = String.format("%d 进入房间:%d", u.GetRoleGID(), roomId);
            LogRecord(u, fstr);
            m_users.put(u.GetRoleGID(), user);
        } else {
            user.setPos(userInfo.size());
            user.setRoomId(roomId);
            m_posusers.put(userInfo.size(), user);
            userInfo.add(user);
            m_userlist.put(0, user);
            String fstr = String.format("%d 进入房间:%d", u.GetRoleGID(), roomId);
            LogRecord(u, fstr);
            m_users.put(u.GetRoleGID(), user);
        }

        if (userInfo.size() == 1 && m_userlist.size() == 1) {
            m_firstZhuang = user.getRoleId();
            if (CardManager.ishuludao) {
                //随机一个庄家;
            } else {
                user.setZhuang(1); // 第一个进入的房间是房主，需要扣他的房卡;
            }
    /*		this.master_roleId = user.getUser().GetRoleGID();*/
            this.m_position = 0; // todo 默认是庄先出牌;

        } else {
            user.setZhuang(0);
            if (CardManager.getInstance().ishuludao || CardManager.getInstance().isjinzhou) {
                if (m_userlist.size() == 4) {
                    userInfo.clear();
                    for (int i = 0; i < m_userlist.size(); i++) {
                        RoomUser roomUser = new RoomUser();
                        if (i == 3) {
                            roomUser = m_userlist.get(i);
                            RoomUser roomUser2 = m_userlist.get(0);
                            roomUser.setNext(roomUser2);
                        } else {

                            roomUser = m_userlist.get(i);
                            RoomUser roomUser2 = m_userlist.get(i + 1);
                            roomUser.setNext(roomUser2);
                        }
                        userInfo.add(roomUser);
                    }
                }
            } else {
                int index = userInfo.size() - 1;
                RoomUser preUser = userInfo.get(index - 1);
                String fstr1 = String.format("%d 的上家是:%d 的下家是 %d \n", u.GetRoleGID(), preUser.getRoleId(),
                        user.getRoleId());
                LogRecord(null, fstr1);
                user.setPreUser(preUser);
                preUser.setNextUser(user);

                if (userInfo.size() == 4) {
                    userInfo.get(0).setPreUser(user);
                    user.setNextUser(userInfo.get(0));
                    String fstr2 = String.format("%d 的上家是 %d \n", userInfo.get(0).getRoleId(), user.getRoleId());
                    LogRecord(null, fstr2);
                }
            }
            if (userInfo.size() == 4) {
                /*
				 * ScoreLoader loader = (ScoreLoader)
				 * ScoreManager.getInstance().getLoader(ScoreManager.Scores);
				 * calScore calScore = new calScore();
				 */
                if (CardManager.ishuludao) {
                    this.LogRecord(null, "开始设置庄");
                    Iterator<RoomUser> it = userInfo.iterator();
                    int index = (int) (Math.random() * userInfo.size());
                    this.m_position = index;
                    for (int i = 0; i < userInfo.size(); ++i) {
                        if (index == i) {
                            userInfo.get(i).setZhuang(1);

                        } else {
                            userInfo.get(i).setZhuang(0);
                        }
                    }
                }
            }
        }

        return user;
    }

    public void initCard() {
        // todo 开始发牌;
        // 先洗牌;
        String fstr2 = String.format("-----------------------------------开始发牌 \n");
        LogRecord(null, fstr2);
        this.m_cardList.clear();// 清空上一局的出牌;
        this.m_deskcards.clear();
        this.mWinner = null;
        this.m_ting = false;
        this.m_mopai = null;
        noticeTing = true;
        Iterator<RoomUser> it = userInfo.iterator();
        int hui = -1;
        if (CardManager.ishuludao) {
            CardManager.canhui = this.m_rule.hasWanFaHLD(eWanFaType_hld.SHANGFANHUI);
            CardManager.qufengpai = this.m_rule.hasWanFaHLD(eWanFaType_hld.QUFENGPAI);
            if (CardManager.qufengpai) {
                ms_lastcard = CardManager.MS_BASE_LASTCARD = 12;
            } else {
                ms_lastcard = CardManager.MS_BASE_LASTCARD = 20;
            }
        }

        if (CardManager.isshanxi) {
            CardManager.qufengpai = this.m_rule.hasWanFaSX(eWanFaType_sx.BUDAIFENG);
            CardManager.canchi = this.m_rule.hasWanFaSX(eWanFaType_sx.CHIPAI);
            CardManager.guo = this.m_rule.hasWanFaSX(eWanFaType_sx.SHIGUO);

        }
        // if (CardManager.isshanxi) {
        // if (CardManager.qufengpai) {
        // CardManager.MS_BASE_LASTCARD = 12;
        // } else {
        // CardManager.MS_BASE_LASTCARD = 20;
        // }
        // }
        // 如果是益州 的;
        if (CardManager.isyizhou) {
            CardManager.canhui = this.m_rule.hasWanFaYZ(eWanFaType_yz.SHANGXIAHUI);
            CardManager.cankou = this.m_rule.hasWanFaYZ(eWanFaType_yz.KOUPAI);
            CardManager.siganghuangzhuang = this.m_rule.hasWanFaYZ(eWanFaType_yz.SIGANGHUANGZHUABG);
        }
        // 如果是保定的;判断能够扣牌;
        if (CardManager.isbaoding) {
            CardManager.cankou = this.m_rule.hasWanFa(eWanFaType.KOUPAI);
        }

        if (CardManager.isjinzhou) {
            CardManager.canhui = this.m_rule.hasWanFaJZ(eWanFaType_jz.SHANGFANHUI);
            ms_lastcard = CardManager.MS_BASE_LASTCARD = 8; //锦州剩余八张牌;
        }

        if (CardManager.canhui) {
            hui = CardManager.getInstance().randomHuiCard();
            MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance()
                    .getHandler(ConfigManager.MahJongConfig);
            int pre = handler.getPreHuiCard(hui);

            int nex = handler.getNextHuiCard(hui);
            huiCard = nex;
            if (CardManager.qufengpai && pre == 125) {
                hui = pre;
            }
            if (-1 == pre) {
                fstr2 = String.format("没有找到会儿牌：%d\n", hui);
                LogRecord(null, fstr2);
            } else {
                fstr2 = String.format("\n会儿牌：%d,%d,%d\n", pre, nex, hui);
                LogRecord(null, fstr2);
            }
            this.m_huilist.clear();
            this.m_huilist.add(pre);
            this.m_huilist.add(nex);
            this.m_huilist.add(hui);
            this.broadcast(RoomInterface.MID_BROADCAST_HUI, pre, nex, hui);
        }

        m_cards = CardManager.getInstance().createCardGroup(hui);
        m_allCards.clear();
        m_allCards.addAll(m_cards);

        this.m_kouRound = 0;
        this.m_curKou = 0;
        this.max_kouRound = 4;
        this.max_lastKouRound = 4;
        this.m_anGangCount = 0;
        int msgid = RoomInterface.MID_INITCARD;
        if (CardManager.cankou) {// 只有保定的麻将才有扣牌的功能;
            this.roomState = eGameState.GAME_KOU;
            while (it.hasNext()) {
                RoomUser u = it.next();
                if (u.getNextUser() == null || u.getPreUser() == null) {
                    assert (false);
                }
                SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_FA_CARD);
                this.packOtherUser(u, buffer);
                int num = 13;
                if (u.getZhuang() == 1) {
                    num = 14;
                }
                ArrayList<CardInfo> list = this.packCards(buffer, num, u);
                u.initCard(list);
                buffer.Send(u.getUser());
            }

        } else {//
            this.roomState = eGameState.GAME_PLAYING;
            while (it.hasNext()) {
                RoomUser u = it.next();
                if (u.getNextUser() == null || u.getPreUser() == null) {
                    assert (false);
                }
                SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgid);
                this.packOtherUser(u, buffer);
                int num = 13;
                if (u.getZhuang() == 1) {
                    num = 14;
                    this.changUserState(u.getUser(), eUserState.CHUPAI, false);

                }
                ArrayList<CardInfo> list = this.packCards(buffer, num, u);
                u.initCard(list);
                if (CardManager.isshanxi) {
                    // 添加剩余牌的数量；
                    int __temp = 136;
                    if (CardManager.qufengpai) {
                        __temp = 136 - 28;
                    }
                    buffer.Add(__temp - 4 * 13 - 1);
                }
                buffer.Add(m_position);
                buffer.Send(u.getUser());
                CardInfo ag = u.checkAnGang();
                if (u.getZhuang() == 1) {

                    boolean flag = u.getHuCard(null, true, false, false);
                    if (null != ag || flag) {
                        LogRecord(u.getUser(), "起手牌里有暗杠或者天湖");
                        SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM,
                                RoomInterface.MID_TELL_OPERATION);
                        if (null != ag) {
                            LogRecord(u.getUser(), "起手牌里有暗杠");
                            this.collectCardJudge(u.getRoleId(), eCardState.ANGANG);
                        }
                        if (flag) {
                            this.collectCardJudge(u.getRoleId(), eCardState.HU);
                            LogRecord(u.getUser(), "起手牌里有天胡");
                            u.tianhu = true;
                        }
                        LogRecord(u.getUser(), "发送起手牌里有暗杠或者天湖消息");
                        this.SendUserOper(p, u.getUser(), null, ag);
                    }

                }
            }
        }

        RoomUser ru = this.getZhuang();
        _Log(ru.getUser(), this.roomId, this.master_roleId);

    }

    // 获得桌面上最后一张打出的牌;
    // 本来这个牌是由前端给我的，现在由我自己来取吧；
    public int getLastCardInDesk() {
        return 0;
    }

    private void packOtherUser(RoomUser user, SendMsgBuffer buffer) {
        Iterator<RoomUser> it = userInfo.iterator();
        buffer.Add((short) 3);
        while (it.hasNext()) {
            RoomUser u = it.next();
            if (u != user) {
                buffer.Add(u.getUser().GetRoleGID());
            }
        }
        // 这里不太好;
        Iterator<RoomUser> it2 = userInfo.iterator();
        buffer.Add((short) 3);
        while (it2.hasNext()) {
            RoomUser u = it2.next();
            if (u != user) {
                int num = 13;
                if (u.getZhuang() == 1) {
                    num = 14;
                }
                buffer.Add(num);
            }
        }
    }

    // 出牌;
    public boolean pushCard(MyUser user, CardInfo ci) {
        RoomUser ru = m_users.get(user.GetRoleGID());
        if (null != ru) {
            ru.pushCard(ci.getTid());
        }
        this.m_cardList.add(ci); // 记录到一个房间的出牌的列表里面;
        this.m_deskcards.add(ci);
        Iterator<RoomUser> it2 = userInfo.iterator();
        while (it2.hasNext()) {
            RoomUser ru2 = it2.next();
            if (ru2.getUser() != user) {
                ru2.setState(eUserState.WAIT);
            }
        }

        String fstr2 = String.format("打出一张牌：%s \n", ci.getName());
        lastCard = ci;
        LogRecord(ru.getUser(), fstr2);
        return false;
    }

    // 摸牌;
    public CardInfo pullCard(MyUser user) {
        if (m_cards.size() > ms_lastcard) {
            CardInfo card = m_cards.get(0);
            m_cards.remove(0);
            m_mopai = card;
            return card;
        }
        return null;
    }

    public int getCardCount() {
        return this.m_cards.size();
    }

    // 吃碰杠；
    public boolean operCard(MyUser user, ArrayList<Integer> list, int type) {
        RoomUser ru = this.getRoomUser(user);
        ru.checkHasCard(list);
        ru.setPreOper(type);
        if (type == eCardOper.MID_SENDGANG || type == eCardOper.MID_SENDPENG || type == eCardOper.MID_ANGANG) {
            // 如果是碰和杠就改变位置;
            if (this.m_cardList.size() == 0 && type == eCardOper.MID_ANGANG && ru.getZhuang() != 1) {
                String str1 = String.format("首轮出牌不能暗杠\n");
                LogRecord(user, str1);
                return false;
            }
            if (CardManager.ishuludao) {
                if (type == eCardOper.MID_SENDPENG) {

                }
            }

            // 如果有人能胡牌，你的碰和杠会失败的;
            if (this.someBodyHu(user)) {
                return false;
            }
            // 如果在人别出了牌的情况下，有人胡牌的话，你再暗杠会失败的;
            if (type == eCardOper.MID_ANGANG || type == eCardOper.MID_SENDGANG) {
                this.m_anGangCount++; // 累计暗杠，用来对最后的剩余牌进行限制;
                if (!CardManager.isshanxi) {// 如果不是陕西的话，只能剩余20张
                    this.ms_lastcard = CardManager.MS_BASE_LASTCARD + this.m_anGangCount % 2;
                }
                // 四缸荒局
                if (gangOver()) {
                    gameOver(ru);
                    LogRecord(null, "四缸黄庄了");
                }

            }
            // 如果上次的暂停是由于你的杠引起的，你杠玩以后，我需要把上次检测到的杠给去掉，允许你继续摸牌；
            // this.m_pgh_list.remove(user.GetRoleGID());
            // 还是需要删除那些其他人的吃牌的提示;否则当前的玩家不能摸牌;
            this.m_pgh_list.clear();
            // 杠玩了，可以继续摸牌;
            String str1 = String.format("你对当前的牌做了选择，删除所有的预先判定\n");
            LogRecord(user, str1);

            this.m_position = ru.getPos();
        }
        if (type == eCardOper.MID_SENDEAT) {

            if (!this.canChi(ru)) {
                String str1 = String.format("吃牌失败了，后面有玩家能碰和杠胡\n");
                LogRecord(user, str1);
                // 保定麻将没这个;
                return false;
            }
        }
        CardInfo lastPushCard = this.m_cardList.size() > 0 ? this.m_cardList.get(this.m_cardList.size() - 1) : null;

        ru.operCard(list, type, lastPushCard);
        return true;
    }

    public CardInfo getM_mopai() {
        return m_mopai;
    }

    // 选择听牌了;
    public void TingCard(MyUser user) {
        noticeTing = true;
        this.m_ting = true;
        // 开始摸宝
        RoomUser ru = this.getRoomUser(user);
        assert (ru != null);
        CardInfo bao = m_cards.get(m_cards.size() - 1);
        ru.ting = false;
        ru.hasTing = true;
        // 检查宝牌是否有效;
        Iterator<RoomUser> it = this.userInfo.iterator();
        int total = 0;
        while (it.hasNext()) {
            RoomUser ru2 = it.next();
            total += ru2.isIncludeSomeCard(bao);
        }
        while (total >= 3) {
            // 打出那张宝牌;
            this.broadcast(RoomInterface.MID_BROADCAST_CHUBAO, bao.getTid());
            this.m_cards.remove(this.m_cards.size() - 1);// 删除最后一张牌;

            bao = m_cards.get(m_cards.size() - 1);
            it = this.userInfo.iterator();
            total = 0;
            while (it.hasNext()) {
                RoomUser ru2 = it.next();
                total += ru2.isIncludeSomeCard(bao);
            }

        }

        if (ru.getHuCard(bao, true, false, false)) { // 如果是摸宝胡；

            LogRecord(user, "听牌莫宝牌");
            this.collectCardJudge(user.GetRoleGID(), eCardState.HU);
            SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_OPERATION);
            this.SendUserOper(p, user, bao, null);
        } else if (ru.checkGangCard(bao, true)) {// 如果是杠
            LogRecord(user, "听牌杠牌");
            this.collectCardJudge(user.GetRoleGID(), eCardState.GANG);
            SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_OPERATION);
            this.SendUserOper(p, user, bao, null);
        } else {
            ru.setBao(bao.getTid());

            LogRecord(user, "听牌通知宝牌id" + bao.getTid());
            // 通知宝牌;

            SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_BAO);
            p.Add(bao.getTid());
            p.Send(user);
        }
    }

    public void GuoCard(MyUser user) {

        // this.m_pgh_list.remove(0); //去掉一个碰杠胡的人，因为他选择了放弃碰杠胡;
        // eCardState cs = this.m_pgh_list.get(user.GetRoleGID());
        this.m_pgh_list.remove(user.GetRoleGID());
        RoomUser myRu = this.getRoomUser(user);
        String str = String.format("%s过牌了还有%d个人没点过", user.GetNick(), this.m_pgh_list.size());
        LogRecord(user, str);
        RoomUser ru = this.m_posusers.get(this.m_position); // 当前的摸牌用户;
        ru.tianhu = false;//点过之后就不可能天胡了
        //如果悠牌状态下点过
        if (myRu.getYou() == eYouState.NOTICE_YOU) {
            myRu.setYou(eYouState.NONE);
        }
        if (this.m_pgh_list.isEmpty()) {
            if (ru.getState() == eUserState.WAIT) { // 如果是等待状态，就让他摸牌;
                if (ru.getYou() == eYouState.CANYOU) {
                    String fstr = String.format("通知客户端可以悠了\n");
                    LogRecord(ru.getUser(), fstr);
                    ru.setYou(eYouState.NOTICE_YOU);
                    SendMsgBuffer p1 = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_SEND_YOU);
                    p1.Add(1);
                    p1.Send(ru.getUser());
                } else {
                    this.changUserState(ru.getUser(), eUserState.MOPAI, false);
                }

            } else if (ru.getState() == eUserState.MOPAI) {// 如果是摸牌，说明刚才的过是自己需要过的，所以就要出牌;
                this.changUserState(ru.getUser(), eUserState.CHUPAI, false);
            }
        } else {
            CardInfo lastCard = this.m_cardList.get(this.m_cardList.size() - 1);
            this.NoticeUserOper(ru, lastCard);
        }
    }

    public RoomUser getZhuang() {
        Iterator<RoomUser> it = this.userInfo.iterator();
        while (it.hasNext()) {
            RoomUser obj = it.next();
            if (obj.getZhuang() == 1) {
                return obj;
            }
        }
        return null;
    }

    public boolean changeZhuang(MyUser winner) {
        Iterator<RoomUser> it = this.userInfo.iterator();
        while (it.hasNext()) {
            RoomUser obj = it.next();
            if (obj.getZhuang() == 1) {
                // 连续坐庄；
                if (null != winner && obj.getRoleId() == winner.GetRoleGID()) {
                    this.m_position = obj.getPos();
                    return false;
                } else {

                    obj.setZhuang(0);
                    RoomUser ru = this.getNextUser(obj.getUser());
                    ru.setZhuang(1);
                    this.m_position = ru.getPos();
                    // 一圈结束了;
                    if (ru.getUser().GetRoleGID() == m_firstZhuang) {
                        saveRound();
                        m_round++;
                    }


                    LogRecord(null, "换庄了第" + m_round + "个人结束");
                    return true;
                }
            }
        }
        return false;
    }

    private void saveRound() {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser roomUser = (RoomUser) it.next();
            roomUser.m_roundScore.add(roomUser.getUser().getCenterData().changeScore(0) - roomUser.m_preScore);
            roomUser.m_preScore = roomUser.getUser().getCenterData().changeScore(0);
        }
    }

    // 计算牌局的分数；
    public void processResult(MyUser user, int tid, boolean isyou) {

        Iterator<RoomUser> it = this.userInfo.iterator();
        while (it.hasNext()) {
            RoomUser obj = it.next();
            obj.getDeskBalance().reset();
        }
        int bei = 1;
        CardInfo lastCard = null;
        if (-1 != tid) {
            lastCard = this.m_cardList.get(this.m_cardList.size() - 1);
            assert (lastCard.getTid() == tid);
        }
        RoomUser ru = this.getRoomUser(user);
        ru.isYouhu = isyou;
        IDeskBalance db = ru.getDeskBalance();
        db.reset();
        db.processHu(lastCard, this.m_cards.size(), this.userInfo, ru);
        if (CardManager.isshanxi && m_rule.hasWanFaSX(eWanFaType_sx.DIANPAOSHIXIANG)) {
            LogRecord(null, "点炮多响");
            Iterator<RoomUser> it2 = userInfo.iterator();
            while (it2.hasNext()) {
                RoomUser roomUser = (RoomUser) it2.next();
                if (roomUser.isHu && roomUser.getRoleId() != ru.getRoleId() && lastCard.getOwner().getRoleId() != roomUser.getRoleId()) {
                    IDeskBalance dbs = roomUser.getDeskBalance();
                    dbs.processHu(lastCard, this.m_cards.size(), this.userInfo, roomUser);
                }
            }
        }
        if (CardManager.isshanxi) {

            db.processGang(lastCard, this.userInfo, ru);
        } else {

            db.processGang(lastCard, this.userInfo, ru);
        }
        int round = processGenZhuang(); // 跟庄的圈数；
        // 有人胡牌，所有的人都要计算杠;
        if (!CardManager.isshanxi) {
            it = this.userInfo.iterator();
            while (it.hasNext()) {
                RoomUser obj = it.next();
                db = obj.getDeskBalance();
                if (obj.getRoleId() != user.GetRoleGID()) {
                    // 其他人都是输家;只计算是否有杠牌
                    db.processGang(null, this.userInfo, ru);
                }
                if (obj.getZhuang() == 0) { // 跟庄的其他玩家都+1 * round;
                    db.genzhuang = round;
                } else {
                    db.genzhuang = -round * 3;
                }
            }
        }
        it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser u = it.next();
            u.updateScore(ru);

        }
        // 扣房卡:
        if (m_ju == 1) {
            // 第一局结束后开始扣房卡;
            RoomUser r = m_users.get(this.master_roleId);
            if (null != r) {
                // 默认的扣除两张，这个地方以后需要改;
                // 如果房主断线了，要去数据库里面扣;

                int cost = this.m_rule.getCostKa();
                if (CardManager.isshanxi) {
                    ru.getUser().getCenterData().changeRoomCard(-cost);
                } else {
                    ru.getUser().getCenterData().changeRoomCard(-cost);
                }

            }
        }
        // 记录赢的局数;
        user.getCenterData().updateWinCount();

    }

    public ArrayList<RoomUser> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ArrayList<RoomUser> userInfo) {
        this.userInfo = userInfo;
    }

    private int processGenZhuang() {
        Iterator<CardInfo> it = this.m_cardList.iterator();
        int count = 0;
        int current = -1;
        int total = 0;
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (current == ci.getNumber()) {
                count++;
            } else if (current != -1) {
                break;
            }
            if (count == 4) {
                total++;
                count = 0;
                current = -1;
            }
            if (current == -1) {
                current = ci.getNumber();
                count = 1;
            }
        }
        return total;
    }

    // 检查是否胡牌了;
    public eCardState huCard(MyUser user) {
        // CardInfo ci = null;
        // boolean ret = false;
        // if(-1 != tid){
        // ci = this.m_cardList.get(this.m_cardList.size() - 1);
        // assert(tid == ci.getTid());
        // RoomUser myRu = ci.getOwner().getNextUser();
        // RoomUser nextUser = myRu.getNextUser();
        // RoomUser nextUser2 = nextUser.getNextUser();
        //
        // if(myRu.getRoleId() != user.GetRoleGID()){
        // boolean hu = this.canHu(myRu);
        // if(hu){
        // String str = "前面有玩家能够胡牌，你暂时不能胡牌";
        // LogRecord(user, str);
        // return false;
        // }
        // }else if(nextUser.getRoleId() != user.GetRoleGID()){
        // boolean hu = this.canHu(nextUser);
        // if(hu){
        // String str = "前面有玩家能够胡牌，你暂时不能胡牌";
        // LogRecord(user, str);
        // return false;
        // }
        // }else if(nextUser2.getRoleId() != user.GetRoleGID()){
        // assert(false);
        // }

        // while(myRu.getRoleId() != user.GetRoleGID()){
        // eCardState hu = this.canHu(myRu);
        // if(null != hu){
        // String str = "前面有玩家能够胡牌，你暂时不能胡牌";
        // LogRecord(user, str);
        // return false;
        // }
        // myRu = myRu.getNextUser();
        // }
        // }
        // if(checkOther){
        // int index = 0;
        // System.err.print("顺序检测后面的三家是否胡牌\n");
        //
        // /*
        // RoomUser huUser = this.getZhuang();
        // if(huUser.getHuCard(tid)){
        // ArrayList<Integer> arg = new ArrayList<Integer>();
        // arg.add(tid);
        // broadcast(huUser.getUser(), RoomInterface.MID_OPER_CARD,
        // eCardOper.MID_BROADCAST_HUCARD,arg);
        // }
        // */
        //
        // while(index < 3){
        // RoomUser huUser = this.getNextUser(user);
        // if(huUser.getHuCard(ci, false)){
        // ArrayList<Integer> arg = new ArrayList<Integer>();
        // arg.add(tid);
        // broadcast(huUser.getUser(), RoomInterface.MID_OPER_CARD,
        // eCardOper.MID_BROADCAST_HUCARD,arg);
        // ret = true;
        // break;
        // }
        // user = huUser.getUser();
        // index ++;
        // }
        //
        // }else
        {
            RoomUser ru = this.getRoomUser(user);

            if (ru.getYou() == eYouState.NOTICE_YOU) {

                return eCardState.ZIMO;
            }

            return this.canHu(ru);
        }

    }

    // 将需要发放的牌放到
    public ArrayList<CardInfo> packCards(SendMsgBuffer buffer, int num, RoomUser ru) {
        ArrayList<CardInfo> list = new ArrayList<CardInfo>();
        buffer.Add((short) num);
        int index = 0;

        // *************************************************
        if (CardManager.isTest) {
            if (num == 14) {
                num = 14 - CardManager.getInstance().temp.size();
            }
        }

        // *************************************************

        while (index < num) {
            CardInfo info = m_cards.get(index);
            info.setOwner(ru);
            list.add(info);
            buffer.Add(info.getTid());
            m_cards.remove(index);
            index++;
        }

        // *************************************************
        // *************************************************
        // *************************************************
        // *************************************************
        // *************************************************
        // *************************************************
        if (CardManager.isTest) {
            if (num == 14 - CardManager.getInstance().temp.size()) {
                for (int i = 0; i < CardManager.getInstance().temp.size(); ++i) {
                    MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance()
                            .getHandler(ConfigManager.MahJongConfig);
                    int id = CardManager.getInstance().temp.get(i);
                    MahJongConfig conf1 = handler.getConfigById(id);
                    CardInfo ci1 = new CardInfo(ru);
                    ci1.init(conf1);
                    list.add(ci1);
                    buffer.Add(ci1.getTid());
                }

            }
        }

        // *************************************************/
        // *************************************************
        // *************************************************
        // *************************************************
        // *************************************************
        // *************************************************

        return list;
    }

    public RoomUser getRoomUser(MyUser user) {
        return m_users.get(user.GetRoleGID());

    }

    // 改变用户的状态;
    public void changUserState(MyUser user, eUserState state, boolean ispiao) {
        RoomUser ru = this.getRoomUser(user);
        ru.setState(state);
        Iterator<RoomUser> it = userInfo.iterator();
        int readyCount = 0;

        while (it.hasNext()) {
            RoomUser ru1 = it.next();
            if (ru1.getState() == eUserState.READY) {
                readyCount++;
                // 需要重置飘牌的用户状态;
                if (ispiao) {
                    ru1.setPiao(piaoTag.NONE);
                    ru1.isYouhu = false;
                }
                // ru1.setPiao(piaoTag.PIAO);
            }
        }
        if (state == eUserState.READY) {
            Iterator<RoomUser> its = userInfo.iterator();
            while (its.hasNext()) {
                RoomUser rus = (RoomUser) its.next();
                int i = 0;
                if (rus.getState() == eUserState.READY) {
                    i++;
                }
                if (i == 4) {
                    this.roomState = roomState.GAME_READY;
                }
            }

        }
        if (CardManager.isyizhou) {// 只有益州的能飘
            CardManager.canpiao = true;
        }
        if (readyCount >= 4) {
            boolean cp = CardManager.canpiao;
            if (cp) {
                // 广播开始飘牌;
                // 初始化

                this.broadcast(RoomInterface.MID_BROADCAST_STARTPIAO, 1);
            } else {
                this.initCard();
            }

        }

        if (state == eUserState.MOPAI || state == eUserState.CHUPAI) {
            // 将其他的用户重置为等待状态,并开启倒计时;
            Iterator<RoomUser> it2 = userInfo.iterator();
            while (it2.hasNext()) {
                RoomUser ru2 = it2.next();
                if (ru2.getRoleId() != user.GetRoleGID()) {
                    ru2.setState(eUserState.WAIT);
                }
            }

            Iterator<RoomUser> it3 = userInfo.iterator();
            if (state == eUserState.MOPAI) {
                // 检查其他的后面的两个人有没有碰和杠，如果都没有就直接摸牌，跳过倒计时;

                if (this.m_cardList.size() > 0) {

                    // if(!ret1 && !ret2 && !ret3 && !ret4 && !ret6 && !ret7 &&
                    // !ret8){
                    // 直接开始摸牌：
                    // System.err.printf("自己没有吃碰杠，后面的两家没有碰和杠，直接摸牌\n");
                    // this.startPullCard(ru);
                    // }else
                    {
                        // System.err.printf("%d 开始倒计时摸牌\n", user.GetRoleGID());
                        this.startRecord();

                    }
                } else {
                    this.startRecord();
                }
            } else {
                // System.err.printf("%d 开始倒计时出牌\n", user.GetRoleGID());
                this.startRecord();
            }

        }

    }

    // 能否针对当前的牌机型碰杠胡;
    public boolean canPengGangCard(RoomUser nextUser) {
        if (this.m_cardList.isEmpty()) {
            return false;
        }
        CardInfo lastCard = this.m_cardList.get(this.m_cardList.size() - 1);
        boolean ret1 = nextUser.checkPengCard(lastCard);
        boolean ret2 = nextUser.checkGangCard(lastCard, false);
        boolean ret3 = nextUser.getHuCard(lastCard, false, false, false);
        if (nextUser.hasTing) {
            LogRecord(nextUser.getUser(), "检测已经听过牌了不能吃碰杠" + lastCard.getName());
            ret1 = false;
            ret2 = false;
        }
        if (CardManager.ishuludao || CardManager.isjinzhou || CardManager.ischaoyang) {
            if (!nextUser.checkOne()) {
                ret1 = false;
                ret2 = false;
            }
            ;
        }
        if (ret1) {
            LogRecord(nextUser.getUser(), "检测到能够碰牌，需要点过:" + lastCard.getName());
            this.collectCardJudge(nextUser.getRoleId(), eCardState.PENG);

        }
        if (ret2) {
            LogRecord(nextUser.getUser(), "检测到能够杠牌，需要点过" + lastCard.getName());
            this.collectCardJudge(nextUser.getRoleId(), eCardState.GANG);

        }
        if (ret3) {
            LogRecord(nextUser.getUser(), "检测到能够胡牌，需要点过" + lastCard.getName());
            this.collectCardJudge(nextUser.getRoleId(), eCardState.HU);
            this.broadcast(RoomInterface.MID_BROADCAST_SOMEBODYHU, nextUser.getRoleId());

        }
        return false;
    }


    // 是否有人能胡牌;
    public boolean someBodyHu(MyUser user) {
        Iterator<Entry<Long, ArrayList<eCardState>>> it = this.m_pgh_list.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Long, ArrayList<eCardState>> entity = it.next();
            if (user.GetRoleGID() != entity.getKey()) {
                ArrayList<eCardState> list = entity.getValue();
                Iterator<eCardState> it2 = list.iterator();
                while (it2.hasNext()) {
                    if (it2.next() == eCardState.HU) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 判断某一个人能否胡牌；
    public eCardState canHu(RoomUser ru) {
        ArrayList<eCardState> list = this.m_pgh_list.get(ru.getRoleId());
        if (null == list) {
            return null;
        }

        Iterator<eCardState> it2 = list.iterator();
        while (it2.hasNext()) {
            eCardState es = it2.next();
            if (es == eCardState.HU || es == eCardState.ZIMO) {
                return es;
            }
        }
        return null;
    }

    public boolean canPeng(RoomUser ru) {
        ArrayList<eCardState> list = this.m_pgh_list.get(ru.getRoleId());
        if (null == list) {
            return false;
        }
        Iterator<eCardState> it2 = list.iterator();
        while (it2.hasNext()) {
            if (it2.next() == eCardState.PENG) {
                return true;
            }
        }
        return false;
    }

    public boolean canGang(RoomUser ru) {
        ArrayList<eCardState> list = this.m_pgh_list.get(ru.getRoleId());
        if (null == list) {
            return false;
        }
        Iterator<eCardState> it2 = list.iterator();
        while (it2.hasNext()) {
            if (it2.next() == eCardState.GANG) {
                return true;
            }
        }
        return false;
    }

    public boolean canChi(RoomUser ru) {
        ArrayList<eCardState> list = this.m_pgh_list.get(ru.getRoleId());
        if (null == list) {
            return false;
        }
        Iterator<eCardState> it2 = list.iterator();
        while (it2.hasNext()) {
            if (it2.next() == eCardState.CHI) {
                return true;
            }
        }
        return false;
    }

    // 是否能够摸牌;
    public boolean canMoCard(RoomUser ru) {
        return this.m_pgh_list.size() == 0 && noticeTing;
    }

    public void resumeCardState(RoomUser ru) {
        if (this.m_cardList.isEmpty()) {
            return;
        }
        CardInfo lastCard = this.m_cardList.get(this.m_cardList.size() - 1);
        this.NoticeSelfOper(ru, lastCard);
        this.broadcast(RoomInterface.MID_BROADCAST_SATTUS, ru.getRoleId(), 2);
    }

    // 针对当前出的牌，后面三家的情况做预先的判定;
    public void updateCardState(RoomUser ru) {
        this.m_pgh_list.clear();
        CardInfo lastCard = this.m_cardList.get(this.m_cardList.size() - 1);

        this.canPengGangCard(ru);
        if (CardManager.canchi) {
            boolean chi = ru.checkChiCard(lastCard);
            if (chi && !ru.hasTing && ru.checkOne() && canChicard(ru)) {
                LogRecord(ru.getUser(), "检测到能够吃牌，需要点过:" + lastCard.getName());
                this.collectCardJudge(ru.getRoleId(), eCardState.CHI);
            }
        }
        RoomUser nextUser = this.getNextUser(ru.getUser());
        this.canPengGangCard(nextUser);

        RoomUser nextUser2 = this.getNextUser(nextUser.getUser());
        this.canPengGangCard(nextUser2);

        this.NoticeUserOper(ru, lastCard);

        // 给最近的一家发送消息;

    }

    //葫芦岛吃牌过后剩余的牌数大于1
    private boolean canChicard(RoomUser ru) {
        if (CardManager.ishuludao || CardManager.isjinzhou || CardManager.ischaoyang) {
            if (ru.getHandCardCount() <= 4) {
                return false;
            }
        }
        return true;
    }

    public void startRecord() {
        this.mTime1 = 0;
        this.mTime2 = 0;
        this.startRecord = true;

    }

    public void stopRecord() {
        this.mTime1 = 0;
        this.mTime2 = 0;
        this.startRecord = false;
    }

    public RoomUser getPreUser(MyUser user) {
        RoomUser ru = this.getRoomUser(user);
        int pos = this.getPrePosition(ru.getPos());
        return m_posusers.get(pos);
    }

    public RoomUser getNextUser(MyUser user) {
        RoomUser ru = this.getRoomUser(user);
        int nextPos = this.getNextPosition(ru.getPos());
        return m_posusers.get(nextPos);
    }

    // 获得出牌位置;
    public int getNextPosition(int pos) {
        if (pos >= 3) {
            return 0;
        } else {
            return pos + 1;
        }
    }

    public int getPrePosition(int pos) {
        if (pos == 0) {
            return 3;
        } else {
            return pos - 1;
        }
    }

    // 获得出牌位置;
    public int changeNextPosition() {
        if (this.m_position >= 3) {
            this.m_position = 0;
        } else {
            this.m_position++;
        }
        return this.m_position;
    }

    public void removeUser(MyUser user) {
        RoomUser ru = m_users.get(user.GetRoleGID());
        userInfo.remove(ru);
        m_users.remove(ru);
        m_posusers.remove(ru);
        RoomManager.getInstance().removeRoomUser(user.GetRoleGID());

    }

    public void resolveUser(RoomUser ru) {
        userInfo.add(ru);
        m_users.put(ru.getRoleId(), ru);
        m_posusers.put(ru.getPos(), ru);
        RoomManager.getInstance().joinRoom(ru.getUser(), ru.getRoomId());
    }

    public void packData(SendMsgBuffer buffer) {
        buffer.Add(roomId);
        buffer.Add(roomState.ID());
    }

    public void packUserData(SendMsgBuffer buffer) {
        buffer.Add((short) userInfo.size());
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            it.next().packUserData(buffer, false);
        }
        this.m_rule.packData(buffer);
    }

    public void broadcast(MyUser sender, int msgId, int type, ArrayList<Integer> list) {

        int pos = -1;
        RoomUser ru = this.getRoomUser(sender);
        if (!list.isEmpty()) {
            ru.savePreCard(list.get(0));
        }
        if (type == eCardOper.MID_BROADCAST_PENGCARD) {
            // 如果这个用户是碰，就到了出牌的状态：
            pos = this.m_position;

            // 他的手里有可能还有能杠的牌，所以还不能打出牌的状态;
            this.changUserState(sender, eUserState.CHUPAI, false);

        } else if (type == eCardOper.MID_BROADCAST_GANGCARD || type == eCardOper.MID_BROADCAST_ANGANG) {
            // 如果是杠牌，这个用户就开始倒计时摸牌;
            pos = this.m_position;

            this.changUserState(sender, eUserState.MOPAI, false);
            //
        } else if (type == eCardOper.MID_BROADCAST_EADCARD) {
            // 如果这个用户是吃牌,那就到了出牌的阶段；
            pos = this.m_position;
            this.changUserState(sender, eUserState.CHUPAI, false);
            //
        } else if (type == eCardOper.MID_BROADCAST_HUCARD) {
            // 如果这个用户胡牌了，
            pos = this.changeNextPosition();
            this.changUserState(sender, eUserState.HU, false);
            list.clear();
            this.stopRecord();
            // 把四个人手里的牌都广播出去;
            this.roomState = eGameState.GAME_OVER;
            this.changeZhuang(sender);
            m_ju++;
            if (CardManager.isshanxi) {
                saveRound();
            }
            System.err.print("游戏结束。。。。");
            this.checkGameOver();
            if (CardManager.isshanxi && m_rule.hasWanFaSX(eWanFaType_sx.SHIGUO)) {
                if (checkover(true)) {
                    backGuo();
                }
            }
            this.mWinner = sender;
            Iterator<RoomUser> it = userInfo.iterator();
            while (it.hasNext()) {
                RoomUser user = it.next();
                this.TellUserHu(user.getUser(), sender);
            }

            return;

        } else {
            pos = this.changeNextPosition();
        }

        RoomUser posUser = this.m_posusers.get(pos);

        if (type == eCardOper.MID_BROADCAST_SENDCARD) {
            // 如果是出牌，下个用户就开始倒计时摸牌;
            // 记录当前河里出的最新的牌;
            // 在这里提前判定下一家是否能够摸牌，不能在这个用户摸牌的时候才判定。
            this.updateCardState(posUser);
            if (CardManager.isyizhou) { // ru是当前出牌的用户
                boolean you = ru.getHuCard(null, true, false, true);
                LogRecord(ru.getUser(), "能不能悠" + you);
                if (you) {
                    eYouState you2 = ru.getYou();
                    ru.setYou(eYouState.CANYOU);
                    String fstr = String.format("设置该用户为悠牌状态2\n");
                    LogRecord(sender, fstr);
                }

				/*
				 * else { eYouState you2 = ru.getYou();
				 * ru.setYou(eYouState.NONE); String fstr =
				 * String.format("设置该用户为不可悠牌状态2\n"); LogRecord(sender, fstr); }
				 */

            }
            //只有朝阳麻将有听牌的功能；
            if (CardManager.ischaoyang) {
                LogRecord(ru.getUser(), "即将开始检查听牌");
                if (CardManager.ischaoyang) {
                    LogRecord(ru.getUser(), "开始检查听牌");
                    if (ru.checkTingCard()) {
                        LogRecord(ru.getUser(), "开始检查听牌.....听牌了");
                        // 通知谁听牌了;
                        this.collectCardJudge(ru.getRoleId(), eCardState.TING);
                        noticeTing = false;
                        SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_TING);
                        p.Add(1);
                        p.Send(sender);
                    }

                }
            }


            if (this.canMoCard(posUser)) {
                if (posUser.getYou() == eYouState.CANYOU) {
                    String fstr = String.format("通知客户端可以悠了\n");
                    LogRecord(posUser.getUser(), fstr);
                    posUser.setYou(eYouState.NOTICE_YOU);
                    SendMsgBuffer p1 = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_SEND_YOU);
                    p1.Add(1);
                    p1.Send(posUser.getUser());
                } else {
                    // 通知摸牌的消息;
                    this.changUserState(posUser.getUser(), eUserState.MOPAI, false);
                }
            }

            // todo 这里要检测是否胡牌。
            //
        }
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(sender.GetRoleGID());
            buffer.Add(type);
            Iterator<Integer> it2 = list.iterator();

            buffer.Add((short) list.size());
            while (it2.hasNext()) {
                buffer.Add(it2.next());
            }
            buffer.Add(posUser.getUser().GetRoleGID());
            buffer.Send(user.getUser());
        }
    }
    // 告诉某个人你胡牌了;
    public void TellUserHu(MyUser user, MyUser sender) {
        SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_BROADCAST_HU);
        buffer.Add(sender.GetRoleGID());
        buffer.Add(eCardOper.MID_BROADCAST_HUCARD);
        buffer.Add((short) this.userInfo.size());
        Iterator<RoomUser> it2 = this.userInfo.iterator();

        while (it2.hasNext()) {
            RoomUser u = it2.next();
            buffer.Add(u.getUser().GetRoleGID());
            u.packAllCard(buffer);
            if (CardManager.isyizhou) {
                //buffer.Add(u.getUser().getCenterData().changeScore(0));
            }
        }
        Iterator<RoomUser> it3 = this.userInfo.iterator();

        while (it3.hasNext()) {
            RoomUser u = it3.next();

            if (CardManager.ishuludao || CardManager.isjinzhou || CardManager.ischaoyang) {
                IDeskBalance deskBalance = u.getDeskBalance();
                if (deskBalance != null) {
                    deskBalance.logScore();
                }
            }
        }
        buffer.Add(this.getZhuang().getRoleId());
        if (CardManager.isshanxi) {
            buffer.Add(m_ju + 1);
            LogRecord(null, "广播当前局数" + m_ju);
        } else {
            buffer.Add(m_round);
        }
        if (CardManager.ishuludao || CardManager.isjinzhou) {
            if (lastCard != null) {
                buffer.Add(lastCard.getTid());
                LogRecord(user, "添加最后一张牌" + lastCard);
            } else {
                buffer.Add(-1);
            }
        }
        buffer.Send(user);
    }

    // 将该用户的数据广播给其他的人；
    public void broadcast(int msgId, RoomUser ru) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            packData(buffer); // 房间的数据;
            ru.packUserData(buffer, false); // 该用户的数据;
            buffer.Send(user.getUser());
        }
    }

    // 将该用户的票数据广播给其他的人；
    public void broadcasts(int msgId, RoomUser ru, int piao) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
			/*
			 * packData(buffer); // 房间的数据; ru.packUserData(buffer); // 该用户的数据;
			 */
            buffer.Add(ru.getRoleId());
            buffer.Add(piao);
            buffer.Send(user.getUser());
        }
    }

    // 广播出牌的位置;
    public void broadcast(int msgId, int pos) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(pos);
            buffer.Send(user.getUser());
        }

    }

    // 广播票的位置;
    public void broadcast(int msgId, RoomUser ru, int pos) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(ru.getRoleId());
            buffer.Add(pos);
            System.out.println("====================" + pos);
            buffer.Send(user.getUser());
        }

    }

    // 广播出牌的位置;
    public void BroadcastChat(int msgId, long roleId, int arg, int id) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(roleId);
            buffer.Add(arg);
            buffer.Add(id);
            buffer.Send(user.getUser());
        }
    }

    public void BroadcastChat(int msgId, long roleId, int arg, String id) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(roleId);
            buffer.Add(arg);
            buffer.Add(id);
            buffer.Send(user.getUser());
        }
    }

    // 广播出牌的位置;
    public void broadcast(int msgId, long roleId, int arg) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(roleId);
            buffer.Add(arg);
            buffer.Send(user.getUser());
        }
    }

    // 广播出牌的位置;
    public void broadcast(int msgId, int arg1, int arg2) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(arg1);
            buffer.Add(arg2);
            buffer.Send(user.getUser());
        }
    }

    // 广播出牌的位置;
    public void broadcast(int msgId, int arg1, int arg2, int arg3) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(arg1);
            buffer.Add(arg2);
            buffer.Add(arg3);
            buffer.Send(user.getUser());
        }
    }

    // 广播
    public void broadcast(int msgId, long roleId) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(roleId);
            buffer.Send(user.getUser());
        }
    }

    //流局广播
    public void broadcast(int msgId, long roleId, boolean flag) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(roleId);
            this.packalluser(buffer);
            buffer.Send(user.getUser());
        }
    }

    private void packalluser(SendMsgBuffer b) {
        // TODO Auto-generated method stub

        Iterator<RoomUser> iterator = userInfo.iterator();
        b.Add((short) userInfo.size());
        while (iterator.hasNext()) {
            RoomUser ru = (RoomUser) iterator.next();
            ru.packLiuju(b);
        }
    }

    // 广播
    public void broadcast(int msgId, long roleId, ArrayList<Integer> list) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            buffer.Add(roleId);
            buffer.Add((short) Math.max(list.size(), 1));
            if (list.size() > 0) {
                Iterator<Integer> it2 = list.iterator();
                while (it2.hasNext()) {
                    buffer.Add(it2.next());
                }
            } else {
                buffer.Add(0);
            }

            buffer.Send(user.getUser());
        }
    }

    // 广播
    public void broadcast(int msgId, ArrayList<Integer> list) {
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser user = it.next();
            SendMsgBuffer buffer = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, msgId);
            // buffer.Add((short) Math.max(list.size(), 1));
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); ++i) {
                    buffer.Add(list.get(i));
                }
            }

            buffer.Send(user.getUser());
        }
    }

    @Override
    public void OnTick(long p_lTimerID) throws Exception {
        // TODO Auto-generated method stub
        if (this.roomState == eGameState.GAME_PAUSE || this.roomState == eGameState.GAME_WAIT
                || this.roomState == eGameState.GAME_OVER) {
            if (this.roomState == eGameState.GAME_WAIT) {
                // 一分钟之内没有处理是否解散房间的消息，就视为同意解散;
                mTime3++;
                if (mTime3 > 60) {
                    mTime3 = 0;
                    Iterator<RoomUser> it = userInfo.iterator();
                    while (it.hasNext()) {
                        RoomUser ru = it.next();
                        if (null == this.m_userState.get(ru.getRoleId())) {
                            System.err.print("一分钟之内没有处理是否解散房间的消息，就视为同意解散;\n");
                            if (this.dismissRoom(ru.getUser(), 1)) {
                                break;
                            }
                        }
                    }
                }
            }

            return;
        }
        int leaveCount = 0;
        Iterator<RoomUser> it2 = this.userInfo.iterator();
        while (it2.hasNext()) {
            RoomUser ru = it2.next();
            if (ru.getTagLeaveRoom()) {
                leaveCount++;
            }
        }
        if (leaveCount == 4) {
            this.m_destroyRoomTime++;
            if (this.m_destroyRoomTime > 600) {
                RoomManager.getInstance().removeRoom(this.roomId);
                System.err.printf("房间超时被删除掉 : %d \n", roomId);
                this.m_destroyRoomTime = 0;
            }
            return;
        } else {
            this.m_destroyRoomTime = 0;
        }
/*		LogRecord(null, "当前房间状态"+this.getState());*/
        if (this.startRecord) {
            this.mTime1++;
            if (this.mTime1 > max_time) {
                // process；
                this.mTime1 = 0;
                if (this.roomState == eGameState.GAME_PLAYING) {
                    Iterator<RoomUser> it = userInfo.iterator();
                    while (it.hasNext()) {
                        RoomUser ru = it.next();
                        ru.getState();
			/*			LogRecord(ru.getUser(), "当前状态2"+ru.getState());*/
                        if (ru.getState() == eUserState.MOPAI) {
                            // 1 - 如果这个人后面的人能碰牌或者杠牌，就等五秒再让他选择;
                            // 2 - 如果这个人能吃牌，就给他五秒的时间选择是否吃牌，过时之后自动摸牌.
                            if (!canMoCard(ru)) {
                                String fstr = String.format("禁止自动摸牌，下一家有碰杠胡的牌 \n");
                                LogRecord(ru.getUser(), fstr);
                                this.stopRecord();
                                return;
                            }
                            this.startPullCard(ru);

                        }
                    }
                }
            }
            this.mTime2++;
            if (this.mTime2 > max_chutime) {
                // process；
                this.mTime2 = 0;
                if (this.roomState == eGameState.GAME_PLAYING) {
                    Iterator<RoomUser> it = userInfo.iterator();
                    while (it.hasNext()) {
                        RoomUser ru = it.next();
                        if (ru.getState() == eUserState.CHUPAI) {
                            // 找到那个需要出牌的用户，并强制他广播出牌;
                            this.startPushCard(ru, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * 摸一张牌
     *
     * @param ru
     */
    public void startPullCard(RoomUser ru) {
        CardInfo card = pullCard(ru.getUser());
        if (card != null) {
            String fstr = String.format("自动摸一张：%s \n", card.getName());
            lastCard = card;
            LogRecord(ru.getUser(), fstr);

        }

        if (ru.getYou() == eYouState.NOTICE_YOU) {
			/*
			 * card.setOwner(ru); ru.setCurrentCard(card); //
			 * 如果我已经通知过你悠牌，但是你点的是胡牌(走过的消息)，那么在摸牌后就直接通知胡牌了;
			 * this.processResult(ru.getUser(), -1, false);// 这里计算分数了;
			 * ArrayList<Integer> list = new ArrayList<Integer>(); list.add(-1);
			 * broadcast(ru.getUser(), RoomInterface.MID_OPER_CARD,
			 * eCardOper.MID_BROADCAST_HUCARD, list);
			 */
			/* ru.setYou(eYouState.NONE); */
            this._startPullCard(ru, card);
        } else {
            this._startPullCard(ru, card);
        }

    }

    private void _startPullCard(RoomUser ru, CardInfo card) {
        if (null != card) {

            String fstr = String.format("_startPullCard：%s \n", card.getName());
            LogRecord(ru.getUser(), fstr);
            // todo - 这里需要检查是听牌还是胡牌;
            card.setOwner(ru);
            if (ru.getPreOper() == eCardOper.MID_SENDGANG || ru.getPreOper() == eCardOper.MID_ANGANG) {
                card.setFromGang(true);
                String str = String.format("这是杠后刚摸的一张牌：%s \n", card.getName());
                LogRecord(ru.getUser(), str);

                if (card.getOwner().getRoleId() == ru.getRoleId()) {
                    card.setFromGangAn(true);
                }
            } else {
                card.setFromGang(false);
            }
            boolean hdly = true;
            if (CardManager.ishdly) { // 如果允许海底捞月的话；
                if (this.m_cards.size() <= this.ms_lastcard + 3) {
                    hdly = false;
                }
            }
            boolean ret2 = hdly && ru.checkGangCard(card, true);
            boolean ret3 = ru.getHuCard(card, true, false, false);
            boolean ret1 = false;
            boolean ret4 = hdly && ru.checkGangCard(card, false);

            CardInfo ret5 = null;
            CardInfo ret6 = null;
            if (hdly) {
                ret5 = ru.checkAnGang();
                ret6 = ru.checkMingGang();
            }

            // if(CardManager.canchi){
            // ret1 = ru.checkChiCard(card);
            // }
			/*
			 * if(ru.getYou()!=eYouState.NONE){ ret3=false; }
			 */

            if (ret1 || ret2 || ret3 || ret4 || ret5 != null || ret6 != null) {
                SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_OPERATION);

                if (ret1) {
                    // this.collectCardJudge(ru.getRoleId(), eCardState.CHI);
                    // String fstr2 = String.format("摸到了一张可以吃的牌：%s,停止倒计时",
                    // card.getName());
                    // LogRecord(ru.getUser(), fstr2);
                    // SendMsgBuffer p =
                    // PackBuffer.GetInstance().Clear().AddID(Reg.ROOM,
                    // RoomInterface.MID_TELL_OPERATION);
                    // this.SendUserOper(p, ru.getUser(), card,
                    // eCardOper.MID_SENDEAT);

                }
                // 广播胡牌了;
                if (ret2) {
                    this.collectCardJudge(ru.getRoleId(), eCardState.GANG);
                    String fstr2 = String.format("摸到了一张可以杠的牌：%s,停止倒计时", card.getName());
                    LogRecord(ru.getUser(), fstr2);
                    // SendMsgBuffer p =
                    // PackBuffer.GetInstance().Clear().AddID(Reg.ROOM,
                    // RoomInterface.MID_TELL_OPERATION);
                    // this.SendUserOper(p, ru.getUser(), card,
                    // eCardOper.MID_SENDGANG);

                }
                if (ret3) {
                    this.collectCardJudge(ru.getRoleId(), eCardState.ZIMO);
                    String fstr2 = String.format("摸到了一张可以胡的牌：%s,停止倒计时", card.getName());
                    LogRecord(ru.getUser(), fstr2);
                    // SendMsgBuffer p =
                    // PackBuffer.GetInstance().Clear().AddID(Reg.ROOM,
                    // RoomInterface.MID_TELL_OPERATION);
                    // this.SendUserOper(p, ru.getUser(), card,
                    // eCardOper.MID_ZIMO);

                }
                if (ret2) {
                    this.collectCardJudge(ru.getRoleId(), eCardState.ANGANG);
                    String fstr2 = String.format("摸到了一张可以暗杠的牌：%s,停止倒计时", card.getName());
                    LogRecord(ru.getUser(), fstr2);

                }
                // 手里有暗杠

                if (ret5 != null) {
                    this.collectCardJudge(ru.getRoleId(), eCardState.ANGANG);
                    String fstr2 = String.format("手里有暗杠的牌：%s,停止倒计时", ret5.getName());
                    LogRecord(ru.getUser(), fstr2);
                }
                if (ret6 != null && !ret2) {

                    this.collectCardJudge(ru.getRoleId(), eCardState.GANG);
                    String fstr2 = String.format("手里有明杠的牌：%s,停止倒计时", ret6.getName());
                    LogRecord(ru.getUser(), fstr2);
                }

                if (ret5 != null) {
                    this.SendUserOper(p, ru.getUser(), card, ret5);
                } else if (ret6 != null) {
                    this.SendUserOper(p, ru.getUser(), card, ret6);
                } else {
                    this.SendUserOper(p, ru.getUser(), card, null);
                }

                this.stopRecord();

            } else {

                if (this.m_cards.size() <= ms_lastcard || gangOver()) {
                    // 流局了；
                    gameOver(ru);

                } else {
                    if (CardManager.ishdly) { // 如果允许海底捞月的话；
                        // 如果是葫芦岛的话;
                        if (this.m_cards.size() <= this.ms_lastcard + 3) {
                            hdlBegin = true;
                            // 下家开始摸牌
                            this.changUserState(ru.getNextUser().getUser(), eUserState.MOPAI, false);
                        } else {
                            this.changUserState(ru.getUser(), eUserState.CHUPAI, false);
                        }
                    } else {
                        this.changUserState(ru.getUser(), eUserState.CHUPAI, false);
                    }

                }
            }

            ru.setCurrentCard(card);
            SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_OPER_CARD);
            p.Add(ru.getUser().GetRoleGID());
            p.Add(eCardOper.MID_GET_CARD);
            p.Add((short) 1);
            p.Add(card.getTid());
            p.Send(ru.getUser());
            // 广播剩余的排数；
            this.broadcast(RoomInterface.MID_LAST_CARD, this.m_cards.size());
            if (CardManager.ishuludao) {
                // 如果是葫芦岛的话;
                if (this.ms_lastcard == CardManager.MS_BASE_LASTCARD + 3) {

                    // 如果到了海底捞月的时候：
                    hdlBegin = true;
                    this.broadcast(RoomInterface.MID_BROADCAST_HDLY, 1);
                    // 给下个人发张
                }
            }
        }
    }

    private boolean gangOver() {

        if (!CardManager.isyizhou || !m_rule.hasWanFaYZ(eWanFaType_yz.SIGANGHUANGZHUABG)) {
            return false;
        }
        int k = 0;
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {

            RoomUser next = it.next();
            k += next.getAnGang().size();
            k += next.getMingGang().size();
            k += next.getZhiGang().size();
            LogRecord(null, "杠牌的数量" + k);
            if (k >= 4) {
                return true;
            }
        }
        return false;
    }

    private void gameOver(RoomUser ru) {

        // 流局了；
        this.roomState = eGameState.GAME_OVER;
        String fstr1 = String.format("RoomID:%d 流局了", this.roomId);
        LogRecord(ru.getUser(), fstr1);
        this.checkGameOver();
        if (CardManager.isyizhou) {
            Iterator<RoomUser> it = userInfo.iterator();
            int k = 0;
            while (it.hasNext()) {
                RoomUser next = it.next();
                k += next.getAnGang().size();
                k += next.getMingGang().size();
                k += next.getZhiGang().size();
                LogRecord(null, "杠牌的数量" + k);
                if (k > 0) {
                    break;
                }
            }
            if (k > 0) {
                this.changeZhuang(null);
            }
        } else {
            this.changeZhuang(null);
        }
	/*	if (CardManager.isshanxi) {
			this.broadcast(RoomInterface.MID_BROADCAST_LIUJU, this.getZhuang().getRoleId(), m_ju);
		} else {
			this.broadcast(RoomInterface.MID_BROADCAST_LIUJU, this.getZhuang().getRoleId());
		}*/
        if (CardManager.ishuangz) {
            Iterator<RoomUser> its = this.userInfo.iterator();
            while (its.hasNext()) {
                RoomUser obj = its.next();
                obj.getDeskBalance().reset();
            }
            // 葫芦岛里面，即使流局也要给庄家2分;
            Iterator<RoomUser> it = this.userInfo.iterator();
            if (CardManager.ishuludao) {
                while (it.hasNext()) {
                    RoomUser ru1 = it.next();
                    HLDDeskBalance db = (HLDDeskBalance) ru1.getDeskBalance();
                    db.processGang(null, userInfo, null);

                    if (ru1.getZhuang() == 1) {
                        db.liuju = 6;
                    } else {
                        db.liuju = -2;
                    }
                }
            }

            if (CardManager.isjinzhou) {
                while (it.hasNext()) {
                    RoomUser ru1 = it.next();
                    JZDeskBalance db = (JZDeskBalance) ru1.getDeskBalance();
                    db.processGang(null, userInfo, null);
                    if (ru1.getZhuang() == 1) {
                        db.liuju = 6;
                    } else {
                        db.liuju = -2;
                    }
                }
            }

            Iterator<RoomUser> it2 = this.userInfo.iterator();
            while (it2.hasNext()) {
                RoomUser obj = it2.next();
                obj.updateScore(null);
            }

        }
        if (CardManager.isshanxi) {
            this.broadcast(RoomInterface.MID_BROADCAST_LIUJU, this.getZhuang().getRoleId(), m_ju);
        } else if (CardManager.ishuludao || CardManager.isjinzhou) {
            m_ju++;
            LogRecord(null, "开始广播流局了");
            this.broadcast(RoomInterface.MID_BROADCAST_LIUJU, this.getZhuang().getRoleId(), true);
        } else {
            this.broadcast(RoomInterface.MID_BROADCAST_LIUJU, this.getZhuang().getRoleId());
        }
    }

    /**
     * 出一张牌;
     *
     * @param ru
     * @param card
     */

    public void startPushCard(RoomUser ru, CardInfo card) {
        CardInfo ci = card == null ? ru.getRandomCard() : card;
        System.err.printf("startPushCard： %s \n", ci.getName());
        boolean ting = pushCard(ru.getUser(), ci);
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(ci.getTid());
        broadcast(ru.getUser(), RoomInterface.MID_OPER_CARD, eCardOper.MID_BROADCAST_SENDCARD, list);
    }

    public void tagLeaveRoom(MyUser user) {

        RoomUser ru = this.getRoomUser(user);
        if (null != ru) {

            if (roomState == eGameState.GAME_ALL_OVER) {
                user.setRoomId(-1);
                LogRecord(null, "玩家掉线游戏结束，id设为空");
            }

            ru.tagLeaveRoom(true);
            this.broadcast(RoomInterface.MID_BROADCAST_SATTUS, user.GetRoleGID(), 1);
            String str = "有人离开房间了" + ru.getRoleId() + "名字" + user.GetUserName();
            LogRecord(user, str);
        }
    }

    public void leaveRoom(MyUser user) {
        RoomUser ru = this.getRoomUser(user);

        if (roomState == eGameState.GAME_ALL_OVER) {
            user.setRoomId(-1);
        }

        this.removeUser(user);
        if (null != ru && ru.getZhuang() == 1) {
            // 如果是房主离开房间，直接解散房间;
            String fstr2 = String.format("房主离开了房间:%d - %d", user.GetRoleGID(), this.roomId);
            LogRecord(user, fstr2);

        } else {
            this.broadcast(RoomInterface.MID_BROADCAST_LEAVE, user.GetRoleGID());
            String fstr2 = String.format("有人离开了房间:%d - %d", user.GetRoleGID(), this.roomId);
            LogRecord(user, fstr2);
        }

    }

    // 更新扣牌的轮次;,判断是否已经完成了扣牌;
    public int changeKouRound(MyUser user, boolean ret) {
        this.m_curKou++;
        int result = 1;
        if (!ret) {
            this.max_lastKouRound--;
            String fstr2 = String.format("有人放弃扣牌，剩余总扣牌人数:%d", this.max_lastKouRound);
            LogRecord(user, fstr2);
        } else {
            String fstr = String.format("有人扣牌了，当前已经扣牌的人数:%d, 总人数%d", this.m_curKou, this.max_kouRound);
            LogRecord(user, fstr);
        }

        if (this.m_curKou >= max_kouRound) {
            this.m_kouRound++;
            this.max_kouRound = this.max_lastKouRound;
            this.m_curKou = 0;
            String fstr = String.format("一轮扣牌完成了");
            LogRecord(user, fstr);
            result = 2;
        }
        if (this.m_kouRound >= 3) {
            // 三轮的扣牌完成;
            String fstr2 = String.format("已经经历了4轮扣牌，扣牌结束了:%d", this.max_lastKouRound);
            LogRecord(user, fstr2);
            return 3;
        }

        if (this.max_lastKouRound == 0) {
            return 3;
        }
        return result;
    }

    // 打包目前的房间的数据;
    public void packRoomInfo(SendMsgBuffer buffer, long selfRoleId) {
        buffer.Add(this.roomState.ID());
        if (this.m_userState.size() > 0) {
            ArrayList<Long> list = new ArrayList<Long>();
            Iterator<Entry<Long, Integer>> it = this.m_userState.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Long, Integer> s = it.next();
                if (s.getValue() == 1) {
                    list.add(s.getKey());
                }
            }
            if (list.size() > 0) {
                buffer.Add((short) list.size());
                Iterator<Long> it2 = list.iterator();
                while (it2.hasNext()) {
                    Long i = it2.next();
                    buffer.Add(i);
                }
            } else {
                buffer.Add((short) 1);
                buffer.Add(3L);
            }
        } else {
            buffer.Add((short) 1);
            buffer.Add(3L);
        }

        Iterator<RoomUser> it = userInfo.iterator();
        buffer.Add((short) userInfo.size());
        while (it.hasNext()) { // 只打包用户的数据;
            RoomUser user = it.next();
            user.packUserData(buffer, false);
            if (user.getRoleId() == selfRoleId) {
                user.packCardData(buffer);
            } else {
                user.packOtherCardData(buffer);
            }
        }
        this.m_rule.packData(buffer);
        if (this.m_huilist.isEmpty()) {
            buffer.Add((short) 1);
            buffer.Add(0);
        } else {
            buffer.Add((short) this.m_huilist.size());
            Iterator<Integer> it2 = this.m_huilist.iterator();
            while (it2.hasNext()) {
                buffer.Add(it2.next());
            }
        }
        if (CardManager.isyizhou || CardManager.ishuludao) {
            buffer.Add(m_round);
        }
        if (CardManager.isshanxi) {


            Iterator<RoomUser> iterator = this.userInfo.iterator();
            while (iterator.hasNext()) {
                RoomUser roomUser = (RoomUser) iterator.next();
                if (roomUser.getState() == eUserState.CHUPAI) {
                    buffer.Add(roomUser.getRoleId());
                    System.out.println("sdsdssa" + roomUser.getRoleId());
                    break;
                }
            }
            buffer.Add(m_ju);
            LogRecord(null, "当前局数" + m_ju);
            buffer.Add((short) 4);
            for (RoomUser roomUser : userInfo) {
                roomUser.packJuScore(buffer);
            }
        }
    }

    public void destroy() {
        Root.GetInstance().RemoveTimer(this.m_timeid);
    }

    // 结束了;
    public boolean checkGameOver() {
        this.LogRecord(null, "第" + m_round + "轮结束.共有" + m_rule.getRound() + "轮");
        this.LogRecord(null, "第" + m_ju + "局结束.共有" + m_rule.getRound() + "*4吧");
        // -----------以下是测试代码
        if (CardManager.isTest) {
            roomState = eGameState.GAME_ALL_OVER;
            // 测试总的结算;
            this.LogRecord(null, "游戏结束，准备退出");
            // 整个游戏结束了
            Iterator<RoomUser> it = userInfo.iterator();
            ArrayList<Integer> list = new ArrayList<Integer>();
            while (it.hasNext()) {
                RoomUser ru = it.next();
                list.add(ru.getPos());
                list.add(ru.getUser().getCenterData().changeScore(0));
                ru.getUser().getCenterData().resetScore();// 重置分数;
            }
            this.broadcast(RoomInterface.MID_ROUND_OVER, list);
            return true;
        }

        // ------------以上是测试代码;

        if (checkover(false)) {
            this.LogRecord(null, "游戏结束，准备退出");
            // 整个游戏结束了
            Iterator<RoomUser> it = userInfo.iterator();
            ArrayList<Integer> list = new ArrayList<Integer>();

            while (it.hasNext()) {
                RoomUser ru = it.next();
                list.add(ru.getPos());
                list.add(ru.getUser().getCenterData().changeScore(0));
                ru.getUser().getCenterData().resetScore();// 重置分数;
                LogRecord(null, "最终结算" + list.toString());
            }
            gameOver = true;
            if (CardManager.isshanxi) {
                this.broadcast(RoomInterface.MID_ROUND_OVER, list);
            } else {
                this.broadcast(RoomInterface.MID_ROUND_OVER, 1);
            }

            roomState = eGameState.GAME_ALL_OVER;
            return true;
        }
        return false;
    }

    public void backGuo() {
        if (guo > 0 && m_guoScore.size() > 0) {
            LogRecord(null, "锅内总分" + guo + "\t" + m_guoScore.size());
            ArrayList m_list = new ArrayList(m_guoScore);
            for (int i = 0; i < m_guoScore.size(); i++) {
                long rid = (long) m_guoScore.get(i);
                Iterator<RoomUser> it = userInfo.iterator();
                while (it.hasNext()) {
                    RoomUser next = it.next();
                    if (next.getRoleId() == rid) {
                        XADeskBalance deskBalance = (XADeskBalance) next.getDeskBalance();
                        m_list.remove(m_guoScore.get(i));
                        deskBalance.guo += 1;
                        LogRecord(next.getUser(), "返还锅内一分");
                    }
                }
            }
            m_guoScore = m_list;

        }

    }

    public boolean checkover(boolean s) {
        if (CardManager.isshanxi) {
            int max_ju = m_rule.getRound();
            if (s) {
                if (m_ju >= max_ju) {
                    LogRecord(null, "游戏结算前前检测" + m_ju + "最大局屬" + max_ju);
                    return true;
                }
            } else {
                if (m_ju >= max_ju) {
                    LogRecord(null, "第局数检测" + "最大局屬" + max_ju);
                    return true;
                }
            }
        } else {
            LogRecord(userInfo.get(0).getUser(), "当前圈数" + m_round + "总圈数" + m_rule.getRound());
            if (m_round > m_rule.getRound()) {
                return true;
            }
        }
        return false;

    }

    // 手机每个玩家对当当前牌的判定;
    private void collectCardJudge(long roleId, eCardState state) {
        ArrayList<eCardState> list = this.m_pgh_list.get(roleId);
        if (null != list && list.size() > 0) {
            list.add(state);
        } else {
            ArrayList<eCardState> l = new ArrayList<eCardState>();
            l.add(state);
            this.m_pgh_list.put(roleId, l);
        }
    }

    // 通知自己该干什么了，这个主要用于重连以后的操作;
    private void NoticeSelfOper(RoomUser ru, CardInfo card) {
        // 如果重连以后，桌上的最后一张牌仍然是自己打的那张，就不要判断了;
        if (card.getOwner().getRoleId() == ru.getRoleId()) {
            return;
        }
        MyUser my = ru.getUser();
        int co = 0;
        SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_OPERATION);
        if (null != this.canHu(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDHU;
        } else if (this.canGang(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDGANG;
        } else if (this.canPeng(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDPENG;
        } else if (this.canChi(ru) && ru.getPreUser().getRoleId() == card.getOwner().getRoleId()) { // 我只能吃上一个人的牌;
            my = ru.getUser();
            co = eCardOper.MID_SENDEAT;
        }
        // -------------------------------------------------------------
        // 重连以后需要告诉这个人你需要进行什么样的操作了;
        if (null != my && !this.m_pgh_list.isEmpty()) {
            this.SendUserOper(p, my, card, null);
        }
    }

    // 通知某个用户该干什么了;
    private void NoticeUserOper(RoomUser ru, CardInfo ci) {
        RoomUser nextUser = ru.getNextUser();
        RoomUser nextUser2 = nextUser.getNextUser();
        MyUser my = null;
        int co = 0;
        SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM, RoomInterface.MID_TELL_OPERATION);
        if (null != this.canHu(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDHU;
        } else if (null != this.canHu(nextUser)) {
            my = nextUser.getUser();
            co = eCardOper.MID_SENDHU;
        } else if (null != this.canHu(nextUser2)) {
            my = nextUser2.getUser();
            co = eCardOper.MID_SENDHU;
        } else if (this.canGang(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDGANG;
        } else if (this.canGang(nextUser)) {
            my = nextUser.getUser();
            co = eCardOper.MID_SENDGANG;
        } else if (this.canGang(nextUser2)) {
            my = nextUser2.getUser();
            co = eCardOper.MID_SENDGANG;
        } else if (this.canPeng(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDPENG;
        } else if (this.canPeng(nextUser)) {
            my = nextUser.getUser();
            co = eCardOper.MID_SENDPENG;
        } else if (this.canPeng(nextUser2)) {
            my = nextUser2.getUser();
            co = eCardOper.MID_SENDPENG;
        } else if (this.canChi(ru)) {
            my = ru.getUser();
            co = eCardOper.MID_SENDEAT;
        }

        // -------------------------------------------------------------
        // 发送胡的消息;
        if (null != my) {
            this.SendUserOper(p, my, ci, null);
        }
        // --------------------------------------------------------------

    }

    private void SendUserOper(SendMsgBuffer p, MyUser my, CardInfo ci, CardInfo angang) {
        ArrayList<eCardState> list = this.m_pgh_list.get(my.GetRoleGID());
        if (list != null) {
            p.Add((short) list.size());
        } else {
            p.Add((short) 0);
        }
        String oper = "";
        if (list != null) {
            Iterator<eCardState> it = list.iterator();

            while (it.hasNext()) {
                eCardState op = it.next();
                p.Add(op.ID());
                oper += op.ID() + "-";
            }
        }
        if (null != ci) {
            oper += ci.getName();
        }

        p.Add(ci == null ? 0 : ci.getTid());
        p.Add(angang == null ? 0 : angang.getTid());
        if (null != angang) {
            oper += angang.getName();
        }
        p.Send(my);
        String fstr = String.format("通知%d可以进行的操作:%s", my.GetRoleGID(), oper);
        LogRecord(my, fstr);
    }

    private void _Log(MyUser user, int roomId, long master) {
        String rs = "";
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser ru = it.next();
            long rid = ru.getUser().GetRoleGID();
            rs += rid + "|";
        }
        user.Log(eLogicSQLLogType.LOGIC_SQL_ROOMINFO, master, roomId, rs);
    }

    private void LogRecord(MyUser user, String record) {
        if (null != user) {
            Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
        } else {
            Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
        }
    }

    public void cancleRoom() {
        m_userState.clear();
        this.broadcast(RoomInterface.MID__BROADCAST_CENCELDIS, 0);
        this.changeRoomState(eGameState.GAME_PLAYING);

    }

    public CardInfo getlastCard() {
        // TODO Auto-generated method stub
        CardInfo lastCard = this.m_cardList.get(this.m_cardList.size() - 1);
        return lastCard;
    }

    public int getbiCount(RoomUser winner) {
        int biCount = 0;
        Iterator<RoomUser> it = userInfo.iterator();
        while (it.hasNext()) {
            RoomUser ru = it.next();
            if (ru.getRoleId() != winner.getRoleId() && ru.isBimen()) {
                biCount++;
            }
        }
        return biCount;
    }

    public ArrayList<CardInfo> getM_allCards() {
        return m_allCards;
    }

    public boolean isDihu(CardInfo ci) {
        // TODO Auto-generated method stub
        if (m_cardList.size() < 2 && m_cardList.contains(ci)) {
            return true;
        }
        return false;

    }

    public CardInfo getEndcard() {
        // TODO Auto-generated method stub
        return lastCard;

    }

    public void addHui(SendMsgBuffer p) {
        // TODO Auto-generated method stub
        p.Add(huiCard);

    }
}
