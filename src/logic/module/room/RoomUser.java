package logic.module.room;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.*;
import logic.config.MahJongConfig;
import logic.config.handler.MahJongHandler;
import logic.module.log.eLogicDebugLogType;
import logic.module.log.eLogicSQLLogType;
import logic.userdata.*;
import manager.BalanceFactory;
import manager.CardManager;
import manager.ConfigManager;
import manager.RoomManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoomUser {
    private MyUser m_user;
    private int m_pos; // 座位号;
    private int m_zhuang; // 是否是庄家;
    private eUserState m_state; // 用户的状态;
    private long m_roleId; // 保存一份roleid;

    private ArrayList<CardInfo> handMahList = new ArrayList<CardInfo>(); // 玩家手里的牌
    private ArrayList<CardInfo> mingMahList = new ArrayList<CardInfo>(); // 玩家打出去的牌
    private ArrayList<CardInfo> eatBumpBarsList = new ArrayList<CardInfo>(); // 玩家碰的牌
    private ArrayList<CardInfo> chiList = new ArrayList<CardInfo>(); // 玩家吃的牌
    private ArrayList<CardInfo> gangList = new ArrayList<CardInfo>();
    private ArrayList<CardInfo> kouList = new ArrayList<CardInfo>(); // 扣牌的列表;
    private ArrayList<Integer> eatGanglist = new ArrayList<Integer>(); // 碰后杠牌列表
    private ArrayList<CardInfo> zhiGanglist = new ArrayList<CardInfo>(); // 直杠牌的列表
    public IDeskBalance m_db;
    private RoomUser m_preUser;
    private RoomUser m_nextUser;
    private boolean m_tagLeaveRoom;
    private boolean preOperGang; // 上一次操作的牌;（主要用来记录是不是杠,用来判断杠上开花）
    private boolean preOperCaiGang; // 上一次操作的牌;（主要用来记录是不是杠,用来判断杠上开花）
    private boolean m_kouFinished; // 扣牌是否完成;
    private int m_bei; // 存储倍数;
    private piaoTag m_piao; // 是否是飘：
    private int m_huCardCount; // 胡牌的数量;用来判断是屁胡还是单胡：
    private eYouState m_you;// 会儿悠；
    private boolean m_sendHui; // 是否打出过会儿牌;
    private int m_bao; // 宝牌的tid;
    private String m_ip;
    private CardInfo m_hu;// 落胡牌
    private CardInfo m_hu2;// 落胡牌
    private int sumall;
    private long scoregid;// 分数记录ID
    public CardInfo newMahJong = null; // 摸到的牌;
    public int nu_MahJong = 0; // 摸到的牌;
    public boolean isYouhu = false;
    public boolean isHu = false;
    public boolean giveGuo = true;
    public ArrayList<CardInfo> anGangList = new ArrayList<CardInfo>();
    public ArrayList<Integer> m_roundScore;
    public int m_preScore = 0;
    private int m_preOper; // 我的上一次操作；
    private int m_preCard; //上一次操作的牌;
    public boolean haske; // 葫芦岛胡牌限制
    public boolean tianhu;// 天胡标记
    public boolean dihu;// 地胡胡标记
    public boolean danhu;// 单胡胡标记
    public boolean piaohu;// 票胡标记
    public boolean hasTing;// 是不是听过牌了

    private ArrayList<CardInfo> m_hulist;

    public boolean ting;

    public long getScoregid() {
        return scoregid;
    }

    public void setScoregid(long scoregid) {
        this.scoregid = scoregid;
    }

    public void cleanLuoHu() {
        // TODO Auto-generated method stub
        m_hu = null;

    }

    public void setPreOper(int pre) {
        this.m_preOper = pre;
    }

    //保存上一次操作的牌;
    public void savePreCard(int card) {
        m_preCard = card;
    }


    //获取他上一次操作的牌;
    public int getPreCard() {
        return this.m_preCard;
    }

    public int getPreOper() {
        return this.m_preOper;
    }

    public void packJuScore(SendMsgBuffer buffer) {
        buffer.Add(this.getRoleId());
        buffer.Add((short) m_roundScore.size());
        for (Integer integer : m_roundScore) {
            buffer.Add(integer);

        }
        System.out.println("打包每局分数" + m_roundScore.toString());
    }

    public enum piaoTag {
        PIAO, NOPIAO, NONE;
    }

    public RoomUser(MyUser user, String ip) {
        m_user = user;
        if (null != user) {
            this.m_roleId = user.GetRoleGID();
        }
        this.m_state = eUserState.ENTER;

        this.m_db = BalanceFactory.getInstance().createBalance(this);
        this.m_kouFinished = false;
        this.m_bei = 1;
        this.m_huCardCount = 0;
        this.m_you = eYouState.NONE;
        m_piao = piaoTag.NONE;
        m_bao = 0;
        isYouhu = false;
        this.m_ip = ip;
        this.m_preCard = 0;
        m_roundScore = new ArrayList<>();
    }

    public RoomUser() {
        // TODO Auto-generated constructor stub
    }

    public String getClientIp() {
        return m_ip;
    }

    public int getHuCardCount() {
        return this.m_huCardCount;
    }

    public void setBao(int val) {
        m_bao = val;
    }

    public int getBao() {
        return m_bao;
    }

    public void setBei(int val) {
        this.m_bei = val;
    }

    public int getBei() {
        return this.m_bei;
    }

    /**
     * 悠；
     *
     * @param val
     */
    public void setYou(eYouState val) {
        this.m_you = val;
    }

    public eYouState getYou() {
        return this.m_you;
    }

    /**
     * 是否打出过会儿牌;
     */
    public boolean getSendHui() {
        return this.m_sendHui;
    }

    public void setPiao(piaoTag piao) {
        m_piao = piao;
        LogRecord(m_user, "飘得属性被改变" + piao.toString());
    }

    public piaoTag getPiao() {
        return m_piao;
    }

    public void updateUser(MyUser user) {
        m_user = user;
    }

    public RoomUser getPreUser() {
        return m_preUser;
    }

    public RoomUser getNextUser() {
        return m_nextUser;
    }

    public IDeskBalance getDeskBalance() {
        return m_db;
    }

    public void setPreUser(RoomUser user) {
        if (user == null) {
            assert (false);
        }
        m_preUser = user;
    }

    public void setNextUser(RoomUser user) {
        if (user == null) {
            assert (false);
        }
        m_nextUser = user;
    }

    public void setNext(RoomUser user) {

        this.setNextUser(user);
        user.setPreUser(this);

    }

    public void setDeskBalance(IDeskBalance db) {
        m_db = db;
    }

    public void updateScore(RoomUser winner) {
        int score = m_db.calScore(winner);
        sumall += score;
        LogRecord(m_user, "总分改变" + score);
        this.m_user.getCenterData().changeScore(score);
    }

    // 获得扣 的数量;
    public int getKouCount() {
        int count = 0;
        String str = "";
        ArrayList<CardInfo> list = new ArrayList<CardInfo>();
        list.addAll(handMahList);
        if (gangList != null) {
            list.addAll(gangList);
        }
        if (eatBumpBarsList != null) {
            list.addAll(eatBumpBarsList);
        }
        Iterator<CardInfo> it = list.iterator();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getKou()) {
                count++;
                str += ci.getName();
            }
        }
        LogRecord(m_user, "乘除前扣牌的总数量" + count + str);
        return count / 4;
    }

    // 获得大将 的数量;
    public int getDajiangCount() {
        int count = 0;
        ArrayList<Integer> daJiang = new ArrayList<>();
        Iterator<CardInfo> it = this.handMahList.iterator();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getKou() && ci.getType() > 3) {
                count++;
                if (!daJiang.contains(ci.getType())) {
                    daJiang.add(ci.getType());
                }
            }
        }
        return daJiang.size();
    }

    public void kouCard(ArrayList<Integer> list) {
        if (list.size() > 0) {
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int tid = it.next();
                Iterator<CardInfo> it2 = this.handMahList.iterator();
                while (it2.hasNext()) {
                    CardInfo ci = it2.next();
                    /* LogRecord(m_user, "要扣牌" + tid); */
                    if (ci.getTid() == tid) {
                        ci.setKou(true);
                        LogRecord(m_user, "扣牌" + ci.getName() + ci.getTid());
                        // it2.remove();
                        // this.kouList.add(ci);
                    }
                }
            }
        }
        if (list.size() == 1) {
            String fstr = String.format("%s 放弃扣牌\n", this.m_user.GetNick());
            LogRecord(this.m_user, fstr);
        } else {
            String fstr = String.format("%s 扣牌\n", this.m_user.GetNick());
            LogRecord(this.m_user, fstr);
        }
        this.m_kouFinished = list.size() <= 1;

    }

    // 检查是否能吃碰杠；
    public boolean checkHasCard(ArrayList<Integer> list) {
        Iterator<CardInfo> it = handMahList.iterator();
        Iterator<Integer> it2 = list.iterator();
        int count = 0;
        while (it.hasNext()) {
            CardInfo ci = it.next();
            while (it2.hasNext()) {
                if (ci.getTid() == it2.next()) {
                    count++;
                }
            }
        }
        if (count == list.size() - 1) {
            return true;
        }
        return false;
    }

    /*
     * // 检查是否能胡牌； public boolean checkHuCard(List<CardInfo> list, int huiCount,
     * boolean checkTing) {
     *
     * boolean flag = false; if (list.size() == 0) { flag = true; } else if
     * (list.size() < 3) { // 用两张会儿牌来合成一组牌; if (list.size() == 1 && huiCount >
     * 1) { huiCount -= 2; flag = true; } else if (list.size() == 2 && huiCount
     * > 0) { if (list.get(0).getType() == list.get(1).getType() &&
     * Math.abs(list.get(0).getNumber() - list.get(1).getNumber()) <= 2) { flag
     * = true; System.out.println("胡牌了会的数量" + huiCount); huiCount--; } } //
     * 检查是否是听牌; if (checkTing) { flag = list.size() == 1; if (list.size() == 2)
     * { if (list.get(0).getType() == list.get(1).getType() &&
     * Math.abs(list.get(0).getNumber() - list.get(1).getNumber()) <= 2) {
     *
     * flag = true; } } }
     *
     * } else { String p = ""; for (int i = 0; i < list.size(); ++i) { p +=
     * list.get(i).getName() + ","; } String fstr =
     * String.format("checkHuCard 我们剩余的牌:%s \n", p); LogRecord(this.getUser(),
     * fstr); ArrayList<CardInfo> temp = new ArrayList<CardInfo>();
     * ArrayList<CardInfo> temp2 = new ArrayList<CardInfo>(); for(int i = 0; i <
     * list.size() - 1; ++ i){ int i = 0; if (i < list.size() - 1) { CardInfo
     * card1 = list.get(i); temp.clear(); temp2.clear(); temp.add(card1);
     * temp2.add(card1); for (int j = i + 1; j < list.size(); ++j) { CardInfo
     * card2 = list.get(j); if (card1.getType() == card2.getType()) { int c1 =
     * card2.getNumber() - card1.getNumber(); if (c1 == temp.size()) {
     *
     * temp.add(card2);
     *
     * } else if (c1 == 0) { temp2.add(card2); } else if (huiCount > 0 && c1 ==
     * 2) { // 如果是类似1*3的，而且有会儿的情况下; temp.add(card2); break; } } if (temp.size()
     * == 3) { list.remove(temp.get(0)); list.remove(temp.get(1));
     * list.remove(temp.get(2)); flag = this.checkHuCard(list, huiCount,
     * checkTing); break; } else if (temp2.size() == 3) {
     * list.remove(temp2.get(0)); list.remove(temp2.get(1));
     * list.remove(temp2.get(2)); flag = this.checkHuCard(list, huiCount,
     * checkTing); break; }
     *
     * } // 如果有两个连续的牌，就用会儿牌补充。 //
     *
     * if (temp.size() == 2 && huiCount > 0) { System.out.println("会的数量" +
     * huiCount); huiCount--; fstr =
     * String.format("checkHuCard 1有会儿牌被补充了:%s 剩余:%d\n ", temp.get(0).getName()
     * + temp.get(1).getName(), huiCount); LogRecord(this.getUser(), fstr);
     * list.remove(temp.get(0)); list.remove(temp.get(1)); flag =
     * this.checkHuCard(list, huiCount, checkTing);
     *
     * } else if (temp2.size() == 2 && huiCount > 0) { // 只有葫芦岛的玩法才不能当1.9 if
     * (!temp2.get(0).isOneNine() || !CardManager.ishuludao) { huiCount--; fstr
     * = String.format("checkHuCard 2有会儿牌被补充了:%s  剩余:%d\n",
     * temp2.get(0).getName() + temp2.get(1).getName(), huiCount);
     * LogRecord(this.getUser(), fstr); list.remove(temp2.get(0));
     * list.remove(temp2.get(1)); flag = this.checkHuCard(list, huiCount,
     * checkTing); } } }
     *
     * }
     *
     * String fstr = String.format("-----------------------------------%s \n",
     * flag); LogRecord(this.getUser(), fstr); return flag; }
     */
    // 检查是否能胡牌；
    public boolean checkHuCard(List<CardInfo> list, int huiCount, boolean checkTing) {
        // 落胡
        if (!CardManager.isTestscore) {
            CardInfo lastCard = RoomManager.getInstance().getRoom(this.getRoomId()).lastCard;
        }
        boolean flag = false;
        if (list.size() == 0) {
            flag = true;
        } else if (list.size() < 3) {
            // 用两张会儿牌来合成一组牌;
            if (list.size() == 1 && huiCount > 1) {
                huiCount -= 2;
                flag = true;
                danhu = false;
            } else if (list.size() == 2 && huiCount > 0) {
                if (list.get(0).getType() == list.get(1).getType()
                        && Math.abs(list.get(0).getNumber() - list.get(1).getNumber()) <= 2) {
                    flag = true;
                    checkBKD(list);
                    if (Math.abs(list.get(0).getNumber() - list.get(1).getNumber()) != 0) {
                        piaohu = false;
                    }
                    huiCount--;
                }
            }
            // 检查是否是听牌;
            if (checkTing) {
                LogRecord(m_user, "检查听牌");
                flag = list.size() <= 1;
                if (list.size() == 2) {
                    LogRecord(m_user, "检查听牌" + list.get(0).getName() + list.get(1).getName());
                    if (list.get(0).getType() == list.get(1).getType()
                            && Math.abs(list.get(0).getNumber() - list.get(1).getNumber()) <= 2) {
                        flag = true;
                    }
                }
            }

        } else {
            String p = "";
            for (int i = 0; i < list.size(); ++i) {
                p += list.get(i).getName() + ",";
            }
            String fstr = String.format("checkHuCard 我们剩余的牌:%s \n", p);
            LogRecord(this.getUser(), fstr);
            List<CardInfo> temp = new ArrayList<CardInfo>(); // 牌型 123
            List<CardInfo> temp2 = new ArrayList<CardInfo>();// 12*
            List<CardInfo> temp3 = new ArrayList<CardInfo>();// 1*3
            List<CardInfo> temp4 = new ArrayList<CardInfo>();// 111
            List<CardInfo> temp5 = new ArrayList<CardInfo>();// 11*
            List<CardInfo> temp6 = new ArrayList<CardInfo>();// 1**
			/* for(int i = 0; i < list.size() - 1; ++ i){ */

            CardInfo card1 = list.get(0);

            temp.add(card1);
            temp2.add(card1);
            temp3.add(card1);
            temp4.add(card1);
            temp5.add(card1);
            temp6.add(card1);
            for (int j = 1; j < list.size(); ++j) {
                CardInfo card2 = list.get(j);
                if (card1.getType() == card2.getType()) {
                    int c1 = card2.getNumber() - card1.getNumber();
                    if (c1 == temp.size() && temp.size() < 3) {
                        temp.add(card2);
                    }
                    if (c1 == 1 && huiCount > 0 && temp2.size() < 2) {
                        temp2.add(card2);
                    }
                    if (c1 == 2 && huiCount > 0 && temp3.size() < 2) {
                        temp3.add(card2);
                    }
                    if (c1 == 0 && temp4.size() < 3) {
                        temp4.add(card2);
                    }
                    if (c1 == 0 && temp5.size() < 2) {
                        temp5.add(card2);
                    }
                }

            }

            if (temp.size() == 3) {
                ArrayList<CardInfo> list2 = new ArrayList<>(list);
                list2.remove(temp.get(0));
                list2.remove(temp.get(1));
                list2.remove(temp.get(2));
                piaohu = false;
                flag = this.checkHuCard(list2, huiCount, checkTing);
                if (flag) {
                    logOut(temp);
                    checkBKD(temp);
                    return true;
                }
            }

            if (temp2.size() == 2 && huiCount > 0) {
                ArrayList<CardInfo> list2 = new ArrayList<>(list);
                list2.remove(temp2.get(0));
                list2.remove(temp2.get(1));
                huiCount--;
                piaohu = false;
                flag = this.checkHuCard(list2, huiCount, checkTing);
                if (flag) {
                    logOut(temp2);
                    checkBKD(temp2);
                    return true;
                } else {
                    huiCount++;
                }

            }
            if (temp3.size() == 2 && huiCount > 0) {
                ArrayList<CardInfo> list2 = new ArrayList<>(list);
                list2.remove(temp3.get(0));
                list2.remove(temp3.get(1));
                huiCount--;
                flag = this.checkHuCard(list2, huiCount, checkTing);
                piaohu = false;
                if (flag) {
                    logOut(temp3);
                    checkBKD(temp3);

                    return true;
                } else {
                    huiCount++;
                }
            }
            if (temp4.size() == 3) {
                ArrayList<CardInfo> list2 = new ArrayList<>(list);
                list2.remove(temp4.get(0));
                list2.remove(temp4.get(1));
                list2.remove(temp4.get(2));
                flag = this.checkHuCard(list2, huiCount, checkTing);
                if (flag) {
                    logOut(temp4);
                    checkBKD(temp4);
                    haske = true;
                    return true;
                } else {

                }

            }
            if (temp5.size() == 2 && huiCount > 0) {

                ArrayList<CardInfo> list2 = new ArrayList<>(list);
                list2.remove(temp5.get(0));
                list2.remove(temp5.get(1));
                huiCount--;

                flag = this.checkHuCard(list2, huiCount, checkTing);
                if (flag) {
                    logOut(temp5);
                    haske = true;
                    checkBKD(temp);
                    return true;
                } else {
                    huiCount++;
                }
            }
            if (temp6.size() == 1 && huiCount > 1) {
                ArrayList<CardInfo> list2 = new ArrayList<>(list);
                list2.remove(temp6.get(0));
                huiCount--;
                huiCount--;
                flag = this.checkHuCard(list2, huiCount, checkTing);
                if (flag) {
                    logOut(temp6);
                    haske = true;
                    danhu = false;
                    return true;
                } else {
                    huiCount++;
                    huiCount++;
                }
            }
        }
        String fstr = String.format("-----------------------------------%s \n", flag);
        LogRecord(this.getUser(), fstr);
        return flag;
    }

    private void logOut(List<CardInfo> list) {
        // TODO Auto-generated method stub
        String s = ":";
        for (int i = 0; i < list.size(); i++) {
            s = s + list.get(i).getName() + ",";
        }
        if (list.size() < 3) {
            int j = 3 - list.size();
            s = s + "\t 会的数量:" + j;
        }
        String str = String.format("--------------胡牌%s \n", s);
        LogRecord(this.getUser(), str);

    }

    public int getMaxCount(List<CardInfo> list, int type) {
        Iterator<CardInfo> it = list.iterator();
        int count = 0;
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getType() == type) {
                count++;
            }
        }
        return count;
    }

    // 葫芦岛检查是不是单胡 ，能胡几张
    public boolean isdanhu() {
        Room room = RoomManager.getInstance().getRoom(getRoomId());
        ArrayList<CardInfo> allCards = room.getM_allCards();
        Iterator<RoomUser> it = room.getUserInfo().iterator();
        while (it.hasNext()) {
            RoomUser ru = (RoomUser) it.next();
            if (ru.gangList != null) {
                Iterator<CardInfo> it2 = ru.gangList.iterator();
                while (it2.hasNext()) {
                    CardInfo caInfo = (CardInfo) it2.next();
                    allCards.remove(caInfo);

                }
            }

        }
        int i = 0;
        Iterator<CardInfo> cards = allCards.iterator();
        while (cards.hasNext()) {
            CardInfo cardInfo = (CardInfo) cards.next();
            if (cardInfo.getTid() % 4 == 1) {
                if (getHuCard(cardInfo, false, false, false)) {
                    i++;
                    if (i > 1) {
                        return false;
                    }
                }
                ;
            }

        }
        return true;

    }

    // 把包涵最后一张牌或者会牌的牌形加进来
    private void addHu(ArrayList<CardInfo> list) {
        // TODO Auto-generated method stub
        Room room = RoomManager.getInstance().getRoom(getRoomId());
        CardInfo endcard = room.getEndcard();
        if (endcard != null) {
            if (list.contains(endcard))
                m_hulist.clear();
            m_hulist.addAll(list);
        }
    }

    // 获得胡那些牌;
    // checkyou ： 悠牌的检查;
    public boolean getHuCard(CardInfo ci, boolean zimo, boolean checkTing, boolean checkYou) {
        danhu = true;
      isHu=false;
        if (CardManager.isTestyou) {
            checkYou = true;
        }
        if (CardManager.isyizhou) {
            if (!zimo) {
                if (m_hu != null) {
                    if (m_hu.getType() == ci.getType() && m_hu.getNumber() == ci.getNumber()) {
                        return false;
                    }
                    if (m_hu2 != null) {
                        if (m_hu2.getType() == ci.getType() && m_hu2.getNumber() == ci.getNumber()) {
                            return false;
                        }
                    }
                }
            } else {
                m_hu = null;
                m_hu2 = null;
            }
        }

        this.m_huCardCount = 0;
        ArrayList<CardInfo> hu = new ArrayList<CardInfo>(this.handMahList);
        int huiCount = this.getHuiCount();
        if (null != ci) {
            hu.add(ci);
            if (ci.getHui()) {
                huiCount++;
            }
        }
        // 检查是否能够悠牌，悠牌的前提是必须有一张会儿牌；
        if (checkYou) {
            if (huiCount <= 0) {
                return false;
            }
            huiCount--;
            ArrayList<CardInfo> pai = new ArrayList<CardInfo>(hu);
            if (CardManager.isTestyou) {
                pai.remove(pai.size() - 1);
                logOut(pai);
            }
            // 删除掉里面的会儿牌;
            ArrayList<CardInfo> pai2 = new ArrayList<CardInfo>();
            Iterator<CardInfo> it2 = pai.iterator();
            while (it2.hasNext()) {
                CardInfo ci2 = it2.next();
                if (!ci2.getHui()) {
                    pai2.add(ci2);
                }
            }
            String fstr2 = String.format("开始测试能否悠牌 \n");
            LogRecord(this.getUser(), fstr2);
            boolean result = this.checkHuCard(pai2, huiCount, checkTing);
            if (!result && CardManager.isqidui) {
                result = this.isQiduiYou(null);
            }
            if (result && CardManager.isTestscore && CardManager.getInstance().isyizhou) {
                new YZDeskBalance(this).testprocessHu(ci, this, zimo);
            }
            return result;
        }
        String p = "";
        for (int i = 0; i < hu.size(); ++i) {
            p += hu.get(i).getName() + ",";
        }
        String fstr1 = String.format("getHuCard-我现在手里的牌:%s 会儿牌数量 %d \n", p, huiCount);
        LogRecord(this.getUser(), fstr1);

        // 只有保定，益州，葫芦岛的可以胡七对；
        if (CardManager.isbaoding || CardManager.isyizhou
                || CardManager.ishuludao /* || CardManager.isjinzhou */) {
            CardManager.isqidui = true;
        }
        if (CardManager.isyizhou) {
            if (huiCount > 0 && !zimo) {
                String fstr = String.format("有会儿牌不能被点炮胡 \n");
                LogRecord(this.getUser(), fstr);
                return false;
            }
        }
        if (CardManager.ishuludao || CardManager.isjinzhou || CardManager.ischaoyang) {
            // 门清和七对儿的规则不检测碰杠的牌;
            Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
            RoomRule rr = r.getRoomRule();
            if (null != rr && CardManager.ishuludao) {
                CardManager.isqidui = rr.hasWanFaHLD(eWanFaType_hld.QIDUI);
            }
            if (null != rr && CardManager.isjinzhou) {
                CardManager.isqidui = rr.hasWanFaJZ(eWanFaType_jz.QIXIAODUI);
            }

            ArrayList<CardInfo> alist = new ArrayList<CardInfo>(hu);

            if (!this.eatBumpBarsList.isEmpty()) {
                alist.addAll(this.eatBumpBarsList);
            }
            if (!this.gangList.isEmpty()) {
                alist.addAll(this.gangList);
            }
            if (!this.chiList.isEmpty()) {
                alist.addAll(this.chiList);
            }
            p = "";
            for (int i = 0; i < alist.size(); ++i) {
                p += alist.get(i).getName() + ",";
            }
            fstr1 = String.format("getHuCard-我全部的牌:%s \n", p);
            LogRecord(this.getUser(), fstr1);
            int huase1 = 0;
            int huase2 = 0;
            int huase3 = 0;
            int onnine = 0;
            int hongzhong = 0;
            // 葫芦岛需要判断中发白的数量;
            int hongz = 0;
            int facai = 0;
            int baiban = 0;

            Iterator<CardInfo> myit = alist.iterator();
            while (myit.hasNext()) {
                CardInfo ci1 = myit.next();
                if (ci1.getHui())
                    continue;
                if (ci1.getType() == 1) {
                    huase1++;
                }
                if (ci1.getType() == 2) {
                    huase2++;
                }
                if (ci1.getType() == 3) {
                    huase3++;
                }
                if (ci1.getNumber() == 1 || ci1.getNumber() == 10 || ci1.getNumber() == 19
                        || ci1.getNumber() % 9 == 0) {
                    onnine++;
                }
                if (ci1.getType() == 8 && CardManager.ishuludao) { // 红中可以替换1.9
                    if (rr.hasWanFaHLD(eWanFaType_hld.QUFENGPAI)) {
                        onnine++;
                    }
                }
                if (ci1.getType() == 8 && CardManager.isjinzhou) { // 红中可以替换1.9

                    onnine++;

                }
                if (ci1.getType() > 3 && CardManager.ischaoyang) { // 红中可以替换1.9

                    onnine++;

                }
                if (ci1.getType() == 8) {
                    hongz++;
                }
                if (ci1.getType() == 9) {
                    facai++;
                }
                if (ci1.getType() == 10) {
                    baiban++;
                }
            }

            if (huase1 == 0 || huase2 == 0 || huase3 == 0) {
                String fstr = String.format("三种花色不全 \n" + huase1 + "\t" + huase2 + "\t" + huase3);
                LogRecord(this.getUser(), fstr);
                return false;
            }
            if (onnine == 0) {
                String fstr = String.format("缺少一九 \n");
                LogRecord(this.getUser(), fstr);
                return false;

            }
            if (hongz < 2 && facai < 2 && baiban < 2) {
                // String fstr = String.format("中发白至少得有一对儿 \n");
                // LogRecord(this.getUser(), fstr);
                // return false;
            }

        }

        if (CardManager.isyizhou) {
            Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
            if (null != r) {
                RoomRule rr = r.getRoomRule();
                if (null != rr && rr.hasWanFaYZ(eWanFaType_yz.HUBAOZHANG)) {
                    CardManager.ishubazhang = true;
                } else {
                    CardManager.ishubazhang = false;
                }
            }
        }

        if (CardManager.isbaoding) {
            Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
            if (null != r) {
                RoomRule rr = r.getRoomRule();
                if (null != rr && rr.hasWanFa(eWanFaType.HUBAZHANG)) {
                    CardManager.ishubazhang = true;
                } else {
                    CardManager.ishubazhang = false;
                }
            }
        }

        if (CardManager.ishubazhang) {
            ArrayList<CardInfo> hu2 = new ArrayList<CardInfo>(hu);
            hu2.addAll(this.eatBumpBarsList);
            hu2.addAll(this.gangList);
            hu2.addAll(this.chiList);
            int count1 = this.getMaxCount(hu2, 1);
            int count2 = this.getMaxCount(hu2, 2);
            int count3 = this.getMaxCount(hu2, 3);
            int total = 0;
            for (int i = 4; i < 11; ++i) {
                int count = this.getMaxCount(hu2, i);
                total += count;
            }
            if (count1 < 8 && count2 < 8 && count3 < 8 && total < 8) {
                String fstr = String.format("判定胡八张失败 \n");
                LogRecord(this.getUser(), fstr);
                return false;
            }
        }

        hu = this.sortCard(hu);
        ArrayList<JiangCard> list = this.getJiangCard(hu);
        CardInfo lastCard = ci;
        if (lastCard == null && !CardManager.isTest) {
            lastCard = RoomManager.getInstance().getRoom(this.getRoomId()).getM_mopai();

        }
        for (int i = 0; i < list.size(); ++i) {
            String fstr2 = String.format("getHuCard - 我找到的将牌:%s \n", list.get(i).toString());
            LogRecord(this.getUser(), fstr2);
        }

        //
        boolean result = false;
        boolean useHui = false;// 是否你在将牌里面使用了会儿牌，如果用了就说明可以悠了;
        Iterator<JiangCard> it = list.iterator();
        while (it.hasNext()) {

            haske = false;
            JiangCard jc = it.next();
            ArrayList<CardInfo> pai = new ArrayList<CardInfo>(hu);
            int huiCo = huiCount;

            // 把将牌里面使用的会儿的数量减少掉;
            if (jc.getCard1().getHui()) {
                huiCo--;
            } else if (jc.getCard1().getType() > 7) {
                if (CardManager.ishdly)
                    haske = true;
            } else if (CardManager.isjinzhou && jc.getCard1().getType() == 8) {
                haske = true;
            }

            if (jc.getCard2().getHui()) {
                huiCo--;
            } else if (jc.getCard2().getType() > 7) {
                if (CardManager.ishdly)
                    haske = true;
            } else if (CardManager.isjinzhou && jc.getCard1().getType() == 8) {
                haske = true;
            }

            boolean used = huiCo < huiCount;
            pai.remove(jc.getCard1());
            pai.remove(jc.getCard2());
            // 删除掉里面的会儿牌;
            ArrayList<CardInfo> pai2 = new ArrayList<CardInfo>();
            Iterator<CardInfo> it2 = pai.iterator();
            while (it2.hasNext()) {
                CardInfo ci2 = it2.next();
                if (!ci2.getHui()) {
                    pai2.add(ci2);
                }
            }
            String p2 = "";
            for (int i = 0; i < pai2.size(); ++i) {
                p2 += pai2.get(i).getName() + ",";
            }
            String fstr2 = String.format("getHuCard-删除会儿牌后手里的牌:%s 会儿牌的剩余数量 %d \n", p2, huiCo);
            LogRecord(this.getUser(), fstr2);

            piaohu = true;
            result = this.checkHuCard(pai2, huiCo, checkTing);

            if (result) {
                // todo 这里有问题，需要判断出到底是屁胡还是单胡;
                this.m_huCardCount++;
                useHui = used;
                if (!CardManager.isTestscore) {
                    CardInfo lastc = RoomManager.getInstance().getRoom(this.getRoomId()).lastCard;
                    if (jc.getCard1().getHui() && jc.getCard1() != lastc) {
                        danhu = false;

                    }
                    if (jc.getCard2().getHui() && jc.getCard2() != lastc) {
                        danhu = false;

                    }
                }

            }
            if (result) {
                break;
            }
        }

        int dui = 0;
        if (CardManager.isqidui) {
            String fstr2 = String.format("开始判定七对儿\n");
            LogRecord(this.getUser(), fstr2);

            dui = this.isQidui(ci);
            if (dui > 0) {
                if (CardManager.isyizhou) {
                    useHui = this.isQiduiYou(ci);
                }
                fstr2 = String.format("判定七对胡牌了\n");
                LogRecord(this.getUser(), fstr2);
            }

        }

        boolean ssy = false;
        if (CardManager.isbaoding) {
            ssy = CardManager.getInstance().isShiSanYao(hu);
            // 只有保定玩法才有十三幺;
            if (ssy) {
                String fstr2 = String.format("判定十三幺胡牌了\n");
                LogRecord(this.getUser(), fstr2);
            }
        }

        if (CardManager.ishuludao || CardManager.isjinzhou || CardManager.ischaoyang) {
            if (checkPeng()) {
                Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
                RoomRule rr = r.getRoomRule();
                if (null != rr && CardManager.ishuludao) {
					/*
					 * if (!rr.hasWanFaHLD(eWanFaType_hld.MENQING) &&
					 * !rr.hasWanFaHLD(eWanFaType_hld.QIDUI)) { return false; }
					 */

                    boolean flag = true;
                    if (rr.hasWanFaHLD(eWanFaType_hld.MENQING) || rr.hasWanFaHLD(eWanFaType_hld.QIDUI)) {
                        if (rr.hasWanFaHLD(eWanFaType_hld.MENQING) && chiList.isEmpty()) {
                            flag = false;
                        }
                        if (rr.hasWanFaHLD(eWanFaType_hld.QIDUI) && (this.isQidui(ci) > 0)) {
                            flag = false;
                        }
                        ;
                        if (flag) {
                            return false;
                        }
                    } else {
                        String fstr = String.format("必须有一组碰杠牌() \n");
                        LogRecord(this.getUser(), fstr);
                        return false;
                    }
                } else if (null != rr && CardManager.ishuludao) {

                    boolean flag = true;
                    if (rr.hasWanFaJZ(eWanFaType_jz.QIXIAODUI)) {
                        if (chiList.isEmpty()) {
                            flag = false;
                        }

                        if (flag) {
                            return false;
                        }
                    } else {
                        String fstr = String.format("必须有一组碰杠牌() \n");
                        LogRecord(this.getUser(), fstr);
                        return false;
                    }

                } else if (null != rr && CardManager.ischaoyang) {

                    String fstr = String.format("必须有一组碰杠牌() \n");
                    LogRecord(this.getUser(), fstr);
                    return false;

                }

            }
        }
        boolean ret = this.m_huCardCount > 0 || dui > 0 || ssy;
        if (!ret) {
            // 上一轮的杠在这一轮没有胡牌，就判定不是杠上开花;
            this.preOperGang = false;
            this.preOperCaiGang = false;
        }

        // 是否通知悠;
        if (CardManager.isyizhou) {
            if (this.getHuiCount() > 0 && zimo) {
                if (ret && useHui) {
					/*
					 * String fstr = String.format("设置该用户为悠牌状态\n");
					 * LogRecord(this.m_user, fstr); this.m_you =
					 * eYouState.CANYOU;
					 */
                } else {
                    // 重置了，因为你本轮的自摸没有胡牌;
                    this.m_you = eYouState.NONE;
                }
            }
        }

        if ((result || ret) && CardManager.isTestscore && CardManager.getInstance().isyizhou) {
            new YZDeskBalance(this).testprocessHu(ci, this, zimo);
        }

        if (ret && CardManager.isyizhou && !zimo) {
            if (m_hu == null) {
                m_hu = ci;
            } else {
                m_hu2 = ci;
            }
        }
        if (ret && CardManager.isshanxi ) {
            Room room = RoomManager.getInstance().getRoom(m_user.getRoomId());
            RoomRule rr = room.getRoomRule();
            this.isHu = true;
            Room r = RoomManager.getInstance().getRoom(getRoomId());
            ArrayList<RoomUser> info = r.getUserInfo();

            if (rr.hasWanFaSX(eWanFaType_sx.DIANPAOSHIXIANG)&& !zimo) {
                Iterator<RoomUser> it2 = info.iterator();
                while (it2.hasNext()) {
                    RoomUser user = it2.next();

                    if (user.getRoleId() != this.getRoleId()&&!user.isHu) {
                        boolean huCard = user.getHuCard(ci, false, false, false);
                        if (huCard) {
                            user.isHu = true;
                        }
                    }

                }

            }

        }

        if (result && CardManager.isTestscore && CardManager.getInstance().isyizhou) {
            new YZDeskBalance(this).testprocessHu(ci, this, zimo);
        }

        if (ret) {

        }
        return ret;
    }

    private void isDanhu() {
        // TODO Auto-generated method stub

    }

    // 检查是不是十三幺;
    public boolean checkShiSanYao(ArrayList<CardInfo> list) {
        Iterator<CardInfo> it = list.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();

        }
        return true;
    }

    // 检查是不是七对;
    public boolean checkQiDui(ArrayList<CardInfo> list) {
        return false;
    }

    // 检查是否已经听牌;
    public boolean checkTingCard() {

        boolean huCard = this.getHuCard(null, false, true, false);
        if (huCard) {
            LogRecord(m_user, "检查到听牌了");
            if (ting) {

                return true;
            }
        }
        return false;

    }

    // 找到将牌的数组;
    private ArrayList<JiangCard> getJiangCard(List<CardInfo> list) {
        logOut(list);
        ArrayList<CardInfo> hui = new ArrayList<CardInfo>();
        ArrayList<CardInfo> pai = new ArrayList<CardInfo>();
        ArrayList<JiangCard> jiang = new ArrayList<JiangCard>();
        for (int i = 0; i < list.size() - 1; i++) {
            CardInfo card1 = list.get(i);
            CardInfo card2 = list.get(i + 1);
            // 会儿牌当将来用;
            if (card1.sameTo(card2) && !card1.getHui()) {
                JiangCard jc = new JiangCard(card1, card2);
                jiang.add(jc);
            }
            if (card1.getHui()) {
                hui.add(card1);
            } else {
                pai.add(card1);
            }
        }
        // 判断一下最后一张牌;
        if (list.get(list.size() - 1).getHui()) {
            hui.add(list.get(list.size() - 1));
        } else {
            pai.add(list.get(list.size() - 1));
        }
        if (hui.size() >= 1) {
            // 其他所有的非会儿牌配牌组成会儿牌的将;
            Iterator<CardInfo> it = pai.iterator();
            while (it.hasNext()) {
                CardInfo jci = it.next();
                // 将牌里面也要去掉1.9的将;
				/*
				 * if (jci.isOneNine() && (CardManager.ishuludao ||
				 * CardManager.isjinzhou)) { continue; }
				 */
                JiangCard jc = new JiangCard(hui.get(0), jci);
                jiang.add(jc);
            }
            if (hui.size() > 1) {
                JiangCard jc = new JiangCard(hui.get(0), hui.get(1));
                jiang.add(jc);
            }
        }
        // 去除掉重复的将牌;
        ArrayList<JiangCard> jiang2 = new ArrayList<JiangCard>();
        for (int i = 0; i < jiang.size(); ++i) {
            boolean find = false;
            for (int j = i + 1; j < jiang.size(); ++j) {
                if (jiang.get(i).sameTo(jiang.get(j))) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                jiang2.add(jiang.get(i));
            }
        }
        return jiang2;
    }

    // 会儿牌的使用情况;返回还有多少会儿牌可用;
    public int getHuiCount() {
        int count = 0;
        Iterator<CardInfo> it = this.handMahList.iterator();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getHui()) {
                count++;
            }
        }
        return count;
    }

    public class JiangCard {
        private CardInfo card1;
        private CardInfo card2;

        public JiangCard(CardInfo c1, CardInfo c2) {
            card1 = c1;
            card2 = c2;
        }

        public boolean sameTo(JiangCard jc) {
            return (card1.sameTo(jc.getCard1()) && card2.sameTo(jc.getCard2()))
                    || (card1.sameTo(jc.getCard2()) && card2.sameTo(jc.getCard1()));
        }

        public CardInfo getCard1() {
            return card1;
        }

        public CardInfo getCard2() {
            return card2;
        }

        public String toString() {
            return "将牌 :" + card1.getName() + " - " + card2.getName();
        }

    }

    // 检查是否能彭傲;
    public boolean checkPengCard(CardInfo ci) {
        int num = ci.getNumber();
        int type = ci.getType();
        if (CardManager.ishuludao || CardManager.isjinzhou) { // 葫芦岛玩法;
            if (!CardManager.qufengpai) {// 如果没有去风牌;
                if (type == 8 || type == 9 || type == 10) {
                    // 如果碰的是中发白，但是目前有吃牌，禁止碰牌;
                    if (this.hasChiCard()) {
                        return false;
                    }
                }
            }
        }

        int count = 0;
        Iterator<CardInfo> it = handMahList.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();
            if (card.getNumber() == num && !card.getHui()) {
                count++;
            }
        }
        if (count >= 2) {
            String fstr = String.format("%s 找到了可以碰的牌 :%s", this.getUser().GetNick(), ci.getName());
            LogRecord(this.getUser(), fstr);
        }
        return count >= 2;
    }

    // 葫芦岛玩法监测是不是有碰杠牌
    private boolean checkPeng() {
        // TODO Auto-generated method stub
        if (!this.eatBumpBarsList.isEmpty() || !this.gangList.isEmpty() || haske) {
            return false;
        }
        ;
        return true;
    }

    // 检查手里的牌能不能组成一刻
    public boolean checkke() {
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        int huiCount = getHuiCount();
        for (int i = 0; i < temp.size(); i++) {
            CardInfo cardInfo = temp.get(i);
            if (checkGang(cardInfo, temp, 3 - huiCount, null)) {
                String fstr = String.format("%s 手里有碰的牌:%s", this.getUser().GetNick(), cardInfo.getName());
                LogRecord(this.getUser(), fstr);
                return true;
            }
        }
        return false;

    }

    // 检查边卡掉
    private void checkBKD(List<CardInfo> list) {
        if (CardManager.ishuludao || CardManager.isjinzhou) {
            CardInfo lastc = RoomManager.getInstance().getRoom(this.getRoomId()).lastCard;
            // TODO Auto-generated method stub
            while (list.size() < 3) {
                // 加上hui
                list.add(null);

            }
            if (list.contains(null) || list.contains(lastc)) {
                if (list.size() >= 3) {
                    int c1 = this.getCardNum(list.get(0), lastc);
                    int c2 = this.getCardNum(list.get(1), lastc);
                    int c3 = this.getCardNum(list.get(2), lastc);
                    // 有2张会 或者 一张会 一张是最后一张牌
                    if ((c1 == 0 && (c2 == 0 || c3 == 0)) || (c2 == 0 && c3 == 0)) {
                        danhu = false;
                    } else if (c1 != -1 || c2 != -1 || c3 != -1) {
                        if (isKa(c1, c2) || isKa(c1, c3) || isKa(c2, c3)) {
                            danhu = false;
                        }
                    }

                }

            }
        }
    }

    // 检查是不是卡牌型
    private boolean isKa(int a, int b) {
        if (Math.abs(a - b) == 1 && a != 1 && a != 9 && b != 1 && b != 9 && a != 0 && b != 0) {
            // 不是卡 的牌型 是24类型
            return true;
        }
        return false;

    }

    private int getCardNum(Object ob, CardInfo last) {
        // TODO Auto-generated method stub
        if (ob != null) {
            CardInfo o = (CardInfo) ob;
            if (o.getNumber() == last.getNumber()) {
                return 0;
            }
            if (o.getNumber() < 109) {
                return o.getNumber() % 9 == 0 ? 9 : o.getNumber() % 9;
            } else {
                // 如果是凤牌
                return -1;
            }

        }
        return 0;

    }

    // 检查是否能杠牌；
    public boolean checkGangCard(CardInfo ci, boolean isMo) {
        int type = ci.getNumber();
        int count = 0;
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        if (isMo && !this.eatBumpBarsList.isEmpty()) {
            temp.addAll(this.eatBumpBarsList);
        }
        return checkGang(ci, temp, 3, null);
    }

    // 检查手里是否有暗杠的牌
    public CardInfo checkAnGang() {
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        for (int i = 0; i < temp.size(); i++) {
            CardInfo cardInfo = temp.get(i);
            if (checkGang(cardInfo, temp, 4, null)) {
                String fstr = String.format("%s 手里有暗杠的牌:%s", this.getUser().GetNick(), cardInfo.getName());
                LogRecord(this.getUser(), fstr);
                return cardInfo;
            }
        }
        return null;

    }

    //// 检查手里是否有明杠的牌
    public CardInfo checkMingGang() {
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        ArrayList<CardInfo> temp2 = new ArrayList<CardInfo>(this.eatBumpBarsList);
        for (int i = 0; i < temp.size(); i++) {
            CardInfo cardInfo = temp.get(i);
            if (checkGang(cardInfo, temp2, 3, null)) {
                String fstr = String.format("%s 手里有明杠的牌:%s", this.getUser().GetNick(), cardInfo.getName());
                LogRecord(this.getUser(), fstr);
                return cardInfo;
            }
        }
        return null;

    }

    // 检查该牌在牌集个数
    public boolean checkGang(CardInfo ci, ArrayList<CardInfo> temp, int counts, ArrayList<CardInfo> peng) {
        int type = ci.getNumber();
        int count = 0;
        if (peng != null) {
            temp.addAll(peng);
            System.out.println("所有的牌的个数是" + temp.size());
        }
        Iterator<CardInfo> it = temp.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();
            if (card.getNumber() == type && !card.getHui()) {
                count++;
            }
        }
        if (count >= counts) {
            String fstr = String.format("%s 找到了可以碰杠的牌 :%s", this.getUser().GetNick(), ci.getName());
            LogRecord(this.getUser(), fstr);
        }
        return count >= counts;
    }

    /*
     * 检测手里的牌和碰的牌能否杠牌;
     */
    public boolean checkHandGangCard() {

        Iterator<CardInfo> it2 = this.handMahList.iterator();
        while (it2.hasNext()) {
            CardInfo ci = it2.next();
            int num = ci.getNumber();
            int count = 0;
            Iterator<CardInfo> it = this.eatBumpBarsList.iterator();
            while (it.hasNext()) {
                CardInfo card = it.next();
                if (card.getNumber() == num && !card.getHui() && !ci.getHui()) {
                    count++;
                }
            }
            if (count >= 3) {
                String fstr = String.format("%s checkHandGangCard找到了可以杠的牌 :%s", this.getUser().GetNick(), ci.getName());
                LogRecord(this.getUser(), fstr);
            }
            return count >= 3;
        }
        return false;
    }

    /**
     * @return
     */
    public boolean checkHandAnGangCard() {
        Iterator<CardInfo> it2 = this.handMahList.iterator();
        while (it2.hasNext()) {
            CardInfo ci = it2.next();
            int num = ci.getNumber();
            int count = 0;
            Iterator<CardInfo> it = this.handMahList.iterator();
            while (it.hasNext()) {
                CardInfo card = it.next();
                if (card.getNumber() == num && !card.getHui() && !ci.getHui()) {
                    count++;
                }
            }
            if (count >= 4) {
                String fstr = String.format("%s 从手里的牌找到了暗杠 :%s", this.getUser().GetNick(), ci.getName());
                LogRecord(this.getUser(), fstr);
            }
            return count >= 4;
        }
        return false;
    }

    public boolean checkChiCard(CardInfo ci) {
        if (ci.getHui()) {
            return false;
        }

        int num = ci.getNumber();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        Iterator<CardInfo> it = handMahList.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();
            if (card.getHui()) {
                continue;
            }
            if (card.getNumber() == num - 1 && card.getType() == ci.getType()) {
                count1++;
            }
            if (card.getNumber() == num + 1 && card.getType() == ci.getType()) {
                count2++;
            }
            if (card.getNumber() == num - 2 && card.getType() == ci.getType()) {
                count3++;
            }
            if (card.getNumber() == num + 2 && card.getType() == ci.getType()) {
                count4++;
            }
        }
        return ((count1 >= 1 && count2 >= 1)) || ((count2 >= 1) && (count4 >= 1)) || ((count1 >= 1) && (count3 >= 1));
    }

    public void setState(eUserState state) {
        m_state = state;
        // SendMsgBuffer p = PackBuffer.GetInstance().Clear().AddID(Reg.ROOM,
        // RoomInterface.MID_USER_STATE);
        // p.Add(state.ID());
        // p.Send(m_user); // 告诉自己都有谁在房间里;
    }

    public void tagLeaveRoom(boolean ret) {
        m_tagLeaveRoom = ret;
    }

    public boolean getTagLeaveRoom() {
        return this.m_tagLeaveRoom;
    }

    public eUserState getState() {
        return m_state;
    }

    public void setRoomId(int rid) {
        m_user.setRoomId(rid);
    }

    public int getRoomId() {
        return m_user.getRoomId();
    }

    public void setPos(int pos) {
        m_pos = pos;
    }

    public int getPos() {
        return m_pos;
    }

    public void setZhuang(int z) {
        m_zhuang = z;
    }

    public int getZhuang() {
        return m_zhuang;
    }

    // 获得出牌的次序;
    public int getMingCardPosition(CardInfo card) {
        for (int i = 0; i < this.mingMahList.size(); ++i) {
            if (this.mingMahList.get(i).getTid() == card.getTid()) {
                return i;
            }
        }
        return -1;
    }

    public MyUser getUser() {
        return m_user;
    }

    public long getRoleId() {

        return this.m_roleId;
    }

    // 初始化手里的牌;
    public void initCard(ArrayList<CardInfo> list) {
        handMahList = list;
        this.kouList.clear();
        /// handMahList = this.sortCard(handMahList); //暂时不能排序；
        this.eatBumpBarsList.clear();
        this.newMahJong = null;
        this.nu_MahJong = 0;
        this.mingMahList.clear();
        this.gangList.clear();
        this.chiList.clear();
        this.m_kouFinished = false;
        this.m_bei = 1;
        this.m_huCardCount = 0;
        this.m_you = eYouState.NONE;
        this.m_bao = 0;
        this.m_sendHui = false;
        this.isHu = false;
        giveGuo = true;
        haske = false;
        tianhu = false;
        dihu = false;
        danhu = true;
        ting = true;
        piaohu = true;
        hasTing = false;
        m_hulist = new ArrayList<CardInfo>();

        String p = "";
        for (int i = 0; i < handMahList.size(); ++i) {
            p += handMahList.get(i).getName() + "|";
        }
        String fstr = String.format("初始化的牌:%s \n", p);
        LogRecord(this.getUser(), fstr);
        _log("init", -1, p);
    }

    public void setCurrentCard(CardInfo card) {
        this.newMahJong = card;
        nu_MahJong++;
        handMahList.add(card);
        handMahList = this.sortCard(handMahList);
    }

    // （插入式）排序;
    public ArrayList<CardInfo> sortCard(ArrayList<CardInfo> list) {
        Iterator<CardInfo> it = list.iterator();
        int i;
        CardInfo key;
        for (int j = 1; j < list.size(); ++j) {
            key = list.get(j).clone();
            i = j - 1;
            while (i >= 0 && list.get(i).getTid() > key.getTid()) {
                list.set(i + 1, list.get(i));
                i--;
            }
            list.set(i + 1, key);
        }
        this.checkCardData(list);
        return list;
    }

    public void checkCardData(List<CardInfo> list) {
        for (int i = 0; i < list.size() - 1; ++i) {
            for (int j = i + 1; j < list.size(); ++j) {
                if (list.get(i).getTid() == list.get(j).getTid()) {
                    assert (false);
                    break;
                }
            }
        }
    }

    // 随机删除一张牌;
    public CardInfo getRandomCard() {
        if (null != this.newMahJong) {
            return this.newMahJong;
        }
        CardInfo card = this.handMahList.get(this.handMahList.size() - 1);
        return card;
    }

    // 打出去的牌;
    public CardInfo pushCard(int tid) {
        CardInfo card = this.removeCard(tid);
        if (null != card) {
            mingMahList.add(card);
            String fstr2 = String.format("打出的牌：%s \n", card.getName());
            LogRecord(m_user, fstr2);
            nu_MahJong++; // todo 打出牌以后也不能算天胡；
            // System.err.printf("pushCard:%s \n", card.getName());
        }

        if (card.getHui()) {
            this.m_sendHui = true;
            String fstr2 = String.format("打出了会儿牌：%s \n", card.getName());
            LogRecord(m_user, fstr2);
        }

        return card;
    }

    // 删除牌;
    public CardInfo removeCard(int tid) {
        Iterator<CardInfo> it = handMahList.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();
            if (card.getTid() == tid) {
                it.remove();
                System.out.println("删除了手中牌" + card.getName());
                return card;
            }
        }
        System.err.printf("没有找到要删除的牌:%d \n", tid);
        return null;
    }

    public CardInfo getCard(int tid) {
        Iterator<CardInfo> it = handMahList.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();
            if (card.getTid() == tid) {
                return card;
            }
        }
        return null;
    }

    // 从吃碰的数组里面删除牌;,然后放到杠的数组里面;
    public CardInfo removeCardFromEatPeng(int tid) {
        Iterator<CardInfo> it = this.eatBumpBarsList.iterator();
        while (it.hasNext()) {
            CardInfo card = it.next();
            if (card.getTid() == tid) {
                it.remove();
                return card;
            }
        }
        return null;
    }

    // 是否是暗杠;
    public boolean isAngGang(ArrayList<Integer> list) {
        int size = 0;
        for (int i = 0; i < list.size(); ++i) {
            Iterator<CardInfo> it = this.handMahList.iterator();
            while (it.hasNext()) {
                if (it.next().getTid() == list.get(i)) {
                    size++;
                }
            }
        }
        return size > 3;
    }

    public boolean isMingGang(ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); ++i) {
            Iterator<CardInfo> it = this.eatBumpBarsList.iterator();
            while (it.hasNext()) {
                if (it.next().getTid() == list.get(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 是暗杠吗?
    public eGangType getGangType(ArrayList<Integer> list) {
        boolean find1 = this.isAngGang(list);
        boolean find2 = this.isMingGang(list);

        if (find2) {
            return eGangType.minggang;
        }
       else if (find1) {
            return eGangType.angang;
        }

        else {
            return eGangType.zhigang;
        }
    }

    // 吃碰的牌放这里，在你随机打牌的时候，不能把这些牌打出去;
    public void operCard(ArrayList<Integer> list, int type, CardInfo lastCard) {
        String p = "";
        for (int i = 0; i < list.size(); ++i) {
            MahJongHandler handler = (MahJongHandler) ConfigManager.getInstance()
                    .getHandler(ConfigManager.MahJongConfig);
            MahJongConfig conf = handler.getConfigById(list.get(i));
            p += conf.name + ":" + conf.id + ",";
        }
        String fstr = String.format("operCard你想要操作的牌:%d - %s \n", type, p);
        LogRecord(this.getUser(), fstr);
        Iterator<Integer> it = list.iterator();
        // 这里需要严谨一点儿，需要判断是否能杠.
        // 把上一个杠的操作记录下来；
        preOperGang = (type == eCardOper.MID_ANGANG) || (type == eCardOper.MID_SENDGANG);
        if (list != null && !list.isEmpty() && preOperGang) {
            Integer integer = list.get(0);
            if (integer > 124 && integer < 129) {
                preOperCaiGang = true;
            }
        }
        //
        //
        eGangType t = eGangType.zhigang;
        if (type == eCardOper.MID_SENDGANG || type == eCardOper.MID_ANGANG) {
            t = this.getGangType(list);
        }

        while (it.hasNext()) {
            int card = it.next();
            CardInfo c = this.removeCard(card);
            if (null != c) {
                if (type == eCardOper.MID_SENDGANG || type == eCardOper.MID_ANGANG) {
                    c.setGangType(t);
                    this.gangList.add(c);
                    this.anGangList.add(c);
                } else if (type == eCardOper.MID_SENDEAT) {
                    this.chiList.add(c);
                } else if (type == eCardOper.MID_SENDPENG) {
                    this.eatBumpBarsList.add(c);
                }

            } else {
                // 如果这个牌在你的手牌里不存在;
                if (type == eCardOper.MID_SENDGANG) {
                    // 先看杠的牌是否在碰的队列里面：
                    CardInfo c1 = this.removeCardFromEatPeng(card);
                    if (c1 != null && !eatGanglist.contains(card)) {
                        eatGanglist.addAll(list);
                    }
                    if (null != c1) {
                        // 这是个明杠;你的上一次操作是摸牌，直接开杠;
                        c1.setGangType(t);
                        this.gangList.add(c1);
                    } else {
                        // 直杠，你是通过别人打出的牌进行杠的;
                        lastCard.setGangType(t);
                        this.gangList.add(lastCard);
                    }

                } else if (type == eCardOper.MID_SENDEAT) {
                    this.chiList.add(lastCard);
                } else if (type == eCardOper.MID_SENDPENG) {
                    this.eatBumpBarsList.add(lastCard);
                }
                // 暗杠的牌必须都有;
                // 这里上线前改成日志输出;
                assert (type != eCardOper.MID_ANGANG);
            }
        }

        p = "";
        for (int i = 0; i < handMahList.size(); ++i) {
            p += handMahList.get(i).getName() + ",";
        }
        String fstr1 = String.format("吃碰杠后手里的牌:%s \n", p);
        LogRecord(this.getUser(), fstr1);
    }

    // 把当前用户的手里牌的数量和吃碰的牌的数据打包;
    public void packOtherCardData(SendMsgBuffer buffer) {
        buffer.Add(this.handMahList.size());
        buffer.Add((short) 1);
        buffer.Add(1);
        this.packCards(buffer);
    }

    public void packCardData(SendMsgBuffer buffer) {

        Iterator<CardInfo> its = this.handMahList.iterator();
        int i = 0;
        while (its.hasNext()) {

            CardInfo card = its.next();
            if (!card.getKou()) {
                i++;
            }
        }
        buffer.Add(this.handMahList.size());
        buffer.Add((short) i);
        Iterator<CardInfo> it = this.handMahList.iterator();
        while (it.hasNext()) {

            CardInfo card = it.next();
            if (!card.getKou()) {
                buffer.Add(card.getTid());
            }
        }
        this.packCards(buffer);
    }

    public void packKouCardData(SendMsgBuffer buffer) {
        int c = this.getKouCount() * 4;
        LogRecord(m_user, "扣牌个数" + c);
        buffer.Add((short) Math.max(c, 1));
        if (c == 0) {
            buffer.Add(0);
        } else {

            ArrayList<CardInfo> list = new ArrayList<CardInfo>();
            list.addAll(handMahList);
            if (gangList != null) {
                list.addAll(gangList);
            }
            if (eatBumpBarsList != null) {
                list.addAll(eatBumpBarsList);
            }

            Iterator<CardInfo> it = list.iterator();
            int i = 0;
            while (it.hasNext()) {
                CardInfo card = it.next();
                if (card.getKou()) {
                    buffer.Add(card.getTid());
                    i++;
                }
            }
            LogRecord(m_user, "实际扣牌个数" + c);
        }
    }

    public boolean isLong(CardInfo card) {
        // 我先把会儿牌去掉。

        int count = 0;
        ArrayList<CardInfo> pai = new ArrayList<CardInfo>();
        Iterator<CardInfo> it2 = handMahList.iterator();

        while (it2.hasNext()) {
            CardInfo ci2 = it2.next();
            if (!ci2.getHui()) {
                pai.add(ci2);
            } else {
                count++;
            }
        }
        if (card != null) {
            pai.add(card);
        }
        // 这里存放吃的牌;
        if (!this.chiList.isEmpty()) {
            pai.addAll(this.chiList);
        }
        // 这里存放的是碰的牌;
        // 我只取一张到当前需要测试的龙牌队列里，因为其他的两张是一样的;
		/*
		 * if (!this.eatBumpBarsList.isEmpty()) { if
		 * (this.eatBumpBarsList.size() > 3) { CardInfo curr = null;
		 * Iterator<CardInfo> it = this.eatBumpBarsList.iterator(); while
		 * (it.hasNext()) { CardInfo inf = it.next(); if (curr == null ||
		 * !inf.sameTo(curr)) { pai.add(inf); curr = inf; } } } else {
		 * pai.add(this.eatBumpBarsList.get(0)); }
		 * 
		 * }
		 */

        if (!this.eatBumpBarsList.isEmpty()) {
            if (this.eatBumpBarsList.size() > 0) {
                pai.addAll(eatBumpBarsList);
            }

        }

        // 这里存放的是杠的牌;
		/*
		 * if (!this.gangList.isEmpty()) { if (this.gangList.size() > 4) {
		 * CardInfo curr = null; Iterator<CardInfo> it =
		 * this.gangList.iterator(); while (it.hasNext()) { CardInfo inf =
		 * it.next(); if (curr == null || !inf.sameTo(curr)) { pai.add(inf);
		 * curr = inf; } } } else { pai.add(this.gangList.get(0)); } }
		 */
        pai = this.sortCard(pai);
        return CardManager.getInstance().isLong(pai, count, this);
    }

    public boolean isQingYise() {
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        if (!gangList.isEmpty())
            temp.addAll(this.gangList);
        if (!eatBumpBarsList.isEmpty())
            temp.addAll(this.eatBumpBarsList);
        if (!chiList.isEmpty())
            temp.addAll(this.chiList);
        return CardManager.getInstance().isQingYiSe(temp);
    }

    public boolean isFengYise() {
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        if (!gangList.isEmpty())
            temp.addAll(this.gangList);
        if (!eatBumpBarsList.isEmpty())
            temp.addAll(this.eatBumpBarsList);
        if (!chiList.isEmpty())
            temp.addAll(this.chiList);
        return CardManager.getInstance().isQingYiSe(temp);
    }

    // 是否是七对的悠牌儿;
    public boolean isQiduiYou(CardInfo card) {
        int huiCount = 0;
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        if (null != card) {
            if (card.getHui()) {
                huiCount++;
            } else {
                temp.remove(card);
            }
        }
        ArrayList<CardInfo> temp2 = new ArrayList<CardInfo>();
        Iterator<CardInfo> it2 = temp.iterator();
        while (it2.hasNext()) {
            CardInfo ci2 = it2.next();
            if (!ci2.getHui()) {
                temp2.add(ci2);
            } else {
                huiCount++;
            }
        }
        huiCount--;// 减少一张会儿牌,如果剩余的仍然是六对儿，就说明可以悠牌了;
        String str = temp2.toString();

        return CardManager.getInstance().isQiDui(temp2, huiCount, false) > 0;

    }

    public int isQidui(CardInfo card) {
        // String str = String.format("isQidui - 七对的判断:%s\n", card.getName());
        // LogRecord(this.m_user, str);
        int huiCount = 0;
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        if (null != card) {
            temp.add(card);
        }

        Iterator<CardInfo> it = this.gangList.iterator();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getGangType() == eGangType.angang) {
                temp.add(ci);
            }
        }
        ArrayList<CardInfo> temp2 = new ArrayList<CardInfo>();
        Iterator<CardInfo> it2 = temp.iterator();
        while (it2.hasNext()) {
            CardInfo ci2 = it2.next();
            if (!ci2.getHui()) {
                temp2.add(ci2);
            } else {
                huiCount++;
            }
        }

        if (temp2.size() + huiCount < 14) {
            return 0;
        }
        String p = "";
        for (int i = 0; i < temp2.size(); ++i) {
            p += temp2.get(i).getName() + ",";
        }

        String fstr = String.format("isQidui 我的牌:%s 我的会儿牌:%d\n", p, huiCount);
        LogRecord(this.getUser(), fstr);
        return CardManager.getInstance().isQiDui(temp2, huiCount, true);
    }

    public boolean isShiSanyao(CardInfo card) {
        ArrayList<CardInfo> temp = new ArrayList<CardInfo>(this.handMahList);
        if (null != card) {
            temp.add(card);
        }

        return CardManager.getInstance().isShiSanYao(temp);
    }

    // 是否是杠上开花;
    public boolean isGangShang() {
        Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
        RoomRule rr = r.getRoomRule();
        return this.preOperGang && rr.hasGskh();
    }

    public boolean isCaiGangShang() {
        Room r = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
        RoomRule rr = r.getRoomRule();
        return this.preOperCaiGang && rr.hasGskh();
    }

    // 是否曾经打过这个牌;
    public boolean hasPushCard(int tid) {
        Iterator<CardInfo> it = this.mingMahList.iterator();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getNumber() == tid) {
                return true;
            }
        }
        return false;
    }

    // 是否有直杠；
    public ArrayList<CardInfo> getZhiGang() {

        Iterator<CardInfo> it = this.gangList.iterator();

        ArrayList<CardInfo> list = new ArrayList<CardInfo>();

        while (it.hasNext()) {
            CardInfo ci = it.next();
            // 从杠的组里面找到哪张牌不是自己的.从而找到是谁点杠的;
            if (this.checkPengGang(ci) && ci.getOwner().getRoleId() != this.getRoleId()) {
                list.add(ci);
            }
        }
        return list;
    }

    private boolean checkPengGang(CardInfo ci) {
        Iterator<Integer> it = eatGanglist.iterator();
        while (it.hasNext()) {
            Integer integer = (Integer) it.next();
            if (ci.getTid() == integer) {
                return false;
            }
        }
        return true;
    }

    // 是否是庄家的首轮开杠；(先找到谁是庄家，看看他是否打出过这张牌，然后再检查是在第几轮打出的)
    public boolean isZhuangFirstGang(ArrayList<RoomUser> users, int cid) {
        Iterator<RoomUser> it = users.iterator();
        while (it.hasNext()) {
            RoomUser ru = it.next();
            if (ru.getZhuang() == 1) {
                CardInfo ci = ru.getMingList().get(0);
                if (ci.getTid() == cid) {
                    return true;
                }
            }
        }
        return false;
    }

    // 获取明杠的数量向;
    public ArrayList<CardInfo> getMingGang() {
        if (CardManager.ishuludao || CardManager.isshanxi) {
            return getMingGang_HLD();
        }
        Iterator<CardInfo> it = this.gangList.iterator();
        ArrayList<CardInfo> list = new ArrayList<CardInfo>();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getGangType() == eGangType.minggang) {
                boolean find = false;
                for (int i = 0; i < list.size(); ++i) {
                    if (ci.getNumber() == list.get(i).getNumber()) {
                        find = true;
                    }
                }
                if (!find) {
                    list.add(ci);
                }
            }
        }
        return list;
    }

    public ArrayList<CardInfo> getMingGang_HLD() {
        ArrayList<CardInfo> list = new ArrayList<CardInfo>();
        Iterator<CardInfo> it = this.gangList.iterator();
        while (it.hasNext()) {
            CardInfo cardInfo = (CardInfo) it.next();
            Iterator<Integer> its = eatGanglist.iterator();
            while (its.hasNext()) {
                Integer integer = (Integer) its.next();
                if (cardInfo.getTid() == integer) {
                    boolean find = false;
                    for (int i = 0; i < list.size(); ++i) {
                        if (cardInfo.getNumber() == list.get(i).getNumber()) {
                            find = true;
                        }
                    }
                    if (!find) {
                        list.add(cardInfo);
                    }
                }
            }

        }
        return list;

    }

    // 获取暗杠的数量;
    public ArrayList<CardInfo> getAnGang() {
        Iterator<CardInfo> it = this.gangList.iterator();
        ArrayList<CardInfo> list = new ArrayList<CardInfo>();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getGangType() == eGangType.angang) {
                boolean find = false;
                for (int i = 0; i < list.size(); ++i) {
                    if (ci.getNumber() == list.get(i).getNumber()) {
                        find = true;
                    }
                }
                if (!find) {
                    list.add(ci);
                }
            }
        }
        return list;
    }

    public ArrayList<CardInfo> getGang() {
        Iterator<CardInfo> it = this.gangList.iterator();
        ArrayList<CardInfo> list = new ArrayList<CardInfo>();
        while (it.hasNext()) {
            CardInfo ci = it.next();
            if (ci.getGangType() == eGangType.angang || ci.getGangType() == eGangType.minggang) {
                boolean find = false;
                for (int i = 0; i < list.size(); ++i) {
                    if (ci.getNumber() == list.get(i).getNumber()) {
                        find = true;
                    }
                }
                if (!find) {
                    list.add(ci);
                }
            }
        }
        return list;
    }

    // 是否是抢杠胡;
    public boolean isQiangGangHu(CardInfo ci) {
        if (null == ci) {
            return false;
        }
        RoomUser ru = this.getPreUser();
        return ru.checkGangCard(ci, false);
    }

    // 是否是海底捞月;
    public boolean isHaiDi() {
        // 是否是最后的一张;
        return RoomManager.getInstance().getRoom(getRoomId()).isHdlBegin();

    }

    // 谨慎开放这个接口;
    public ArrayList<CardInfo> getMingList() {
        return this.mingMahList;
    }

    public boolean hasChiCard() {
        return !this.chiList.isEmpty();
    }

    // 获得手牌的数量，用来判断是否是立直;
    public int getHandCardCount() {
        return this.handMahList.size();
    }

    // 我是不是已经拥有某张牌;
    public int isIncludeSomeCard(CardInfo card) {
        int count = 0;
        if (this.chiList.isEmpty()) {
            Iterator<CardInfo> it = this.chiList.iterator();
            while (it.hasNext()) {
                if (card.sameTo(it.next())) {
                    count++;
                }
            }
        }
        if (this.eatBumpBarsList.isEmpty()) {
            Iterator<CardInfo> it = this.eatBumpBarsList.iterator();
            while (it.hasNext()) {
                if (card.sameTo(it.next())) {
                    count++;
                }
            }
        }
        if (this.gangList.isEmpty()) {
            Iterator<CardInfo> it = this.gangList.iterator();
            while (it.hasNext()) {
                if (card.sameTo(it.next())) {
                    count++;
                }
            }
        }
        return count;
    }

    private void packCards(SendMsgBuffer buffer) {

        if (this.eatBumpBarsList.isEmpty()) {
            buffer.Add((short) 1);
            buffer.Add(0);
        } else {
            buffer.Add((short) this.eatBumpBarsList.size());
            Iterator<CardInfo> it2 = this.eatBumpBarsList.iterator();
            while (it2.hasNext()) {
                CardInfo card = it2.next();
                buffer.Add(card.getTid());
            }
        }
        if (this.mingMahList.isEmpty()) {
            buffer.Add((short) 1);
            buffer.Add(0);
        } else {
            buffer.Add((short) this.mingMahList.size());
            Iterator<CardInfo> it3 = this.mingMahList.iterator();
            while (it3.hasNext()) {
                CardInfo card = it3.next();
                buffer.Add(card.getTid());
            }
        }
        if (this.gangList.isEmpty()) {
            buffer.Add((short) 1);
            buffer.Add(0);
        } else {
            buffer.Add((short) this.gangList.size());
            Iterator<CardInfo> it4 = this.gangList.iterator();
            while (it4.hasNext()) {
                CardInfo card = it4.next();
                buffer.Add(card.getTid());
            }
        }

        if (this.chiList.isEmpty()) {
            buffer.Add((short) 1);
            buffer.Add(0);
        } else {
            buffer.Add((short) this.chiList.size());
            Iterator<CardInfo> it4 = this.chiList.iterator();
            while (it4.hasNext()) {
                CardInfo card = it4.next();
                buffer.Add(card.getTid());
            }
        }
        this.packKouCardData(buffer);
    }

    public void packUserData(SendMsgBuffer buffer, boolean ishu) {
        buffer.Add(m_zhuang);
        buffer.Add(m_pos);
        buffer.Add(this.m_state.ID());
        m_user.packUserData(buffer);
        // buffer.Add(this.m_roleId);
        buffer.Add(this.m_ip);
    }

    public void packAllCard(SendMsgBuffer buffer) {
        int calScore = this.getDeskBalance().calScore(null);
        buffer.Add(calScore);
        String fst = String.format("我胡的最中得分:%d \n", calScore);
        LogRecord(this.m_user, fst);
        Iterator<CardInfo> it = handMahList.iterator();
        Iterator<CardInfo> it2 = this.eatBumpBarsList.iterator();
        Iterator<CardInfo> it3 = this.gangList.iterator();
        Iterator<CardInfo> it4 = this.chiList.iterator();
        buffer.Add((short) (handMahList.size() + this.eatBumpBarsList.size() + this.gangList.size()
                + this.chiList.size()));
        String p = "";
        String fstr = "";
        while (it.hasNext()) {
            CardInfo card = it.next();
            buffer.Add(card.getTid());
            p += card.getName() + ",";
        }
        fstr = String.format("我胡的最终牌型(手里的):%s \n", p);
        LogRecord(this.m_user, fstr);
        p = "";
        while (it2.hasNext()) {
            CardInfo card = it2.next();
            buffer.Add(card.getTid());
            p += card.getName() + ",";
        }
        while (it4.hasNext()) {
            CardInfo card = it4.next();
            buffer.Add(card.getTid());
            p += card.getName() + ",";
        }
        fstr = String.format("我胡的最终牌型(吃碰的):%s \n", p);
        LogRecord(this.m_user, fstr);
        p = "";
        while (it3.hasNext()) {
            CardInfo card = it3.next();
            buffer.Add(card.getTid());
            p += card.getName() + ",";
        }
        fstr = String.format("我胡的最终牌型(杠牌):%s \n", p);
        LogRecord(this.m_user, fstr);

        String fstr1 = String.format("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝当前用户[%d]的分数:%d \n", this.getRoleId(), calScore);
        LogRecord(this.m_user, fstr1);
        p = "";
        this.m_db.packData(buffer);

        if (CardManager.isyizhou || CardManager.ishuludao || CardManager.isjinzhou) {
            buffer.Add((short) m_roundScore.size());
            for (int i = 0; i < m_roundScore.size(); i++) {
                buffer.Add(m_roundScore.get(i));
            }
			/*
			 * 测试用buffer.Add((short) 1);
			 * 
			 * buffer.Add(0);
			 */
            buffer.Add(m_user.getCenterData().changeScore(0));
        }

        if (CardManager.isshanxi/* ||CardManager.isyizhou */) {
            Room room = RoomManager.getInstance().getRoom(this.getRoomId());
            Iterator<RoomUser> its = room.getUserInfo().iterator();
            while (its.hasNext()) {
                RoomUser obj = its.next();
                calScore = obj.getDeskBalance().calScore(null);

                String string = ((XADeskBalance) obj.getDeskBalance()).toString() + "总分合计" + calScore;
                LogRecord(obj.getUser(), string);
            }
        }

    }

    private void _log(String oper, int tid, String hand) {
        this.m_user.Log(eLogicSQLLogType.LOGIC_SQL_ROOMCARD, this.m_user.GetRoleGID(), this.m_user.getRoomId(), oper,
                tid, hand);
    }

    private void LogRecord(MyUser user, String record) {
        Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
    }

    public boolean hasnew() {

        if (this.getHandCardCount() == 13 && handMahList.contains(newMahJong)) {

            return false;
        }
        return true;
    }

/*
        public boolean canGuos(CardInfo ci) {
            Room room = RoomManager.getInstance().getRoom(this.getRoomId());
            CardInfo m_mopai = room.getM_mopai();
            if (ci == null && m_mopai != null) {
                ci = m_mopai;
                Iterator<CardInfo> it = handMahList.iterator();
                ArrayList<CardInfo> list = new ArrayList<>();
                while (it.hasNext()) {
                    CardInfo next = it.next();
                    if (next.getType() == ci.getType() && next.getNumber() == ci.getNumber()) {
                        list.add(next);
                    }
                    if (list.size() == 2) {

                        ArrayList<CardInfo> pai = new ArrayList<CardInfo>(handMahList);
                        pai.add(ci);
                        pai.remove(list.get(0));
                        pai.remove(list.get(1));
               *//*         int hui = 0;
                        // 删除掉里面的会儿牌;
                        ArrayList<CardInfo> pai2 = new ArrayList<CardInfo>();
                        Iterator<CardInfo> it2 = pai.iterator();
                        while (it2.hasNext()) {
                            CardInfo ci2 = it2.next();
                            if (!ci2.getHui()) {
                                pai2.add(ci2);
                            } else {
                                hui++;
                            }
                        }
*//*
                        boolean result = this.checkHuCard(pai, 0, false);
                        if (result) {
                            return true;
                        }

                    }
                }
                return false;

            }
            return false;
        }*/
    public boolean canGuo(CardInfo ci) {
        return this.danhu;
    }

    public void addCard(int tid) {
        // TODO Auto-generated method stub
        if (this.handMahList.size() + eatBumpBarsList.size() + chiList.size() == 13) {

            CardInfo card = RoomManager.getInstance().getRoom(m_user.getRoomId()).getlastCard();
            if (card != null) {
                handMahList.add(card);
            }
        }
    }

    public boolean isBimen() {
        if (CardManager.ischaoyang) {
            RoomRule rr = RoomManager.getInstance().getRoom(getRoomId()).getRoomRule();
            if (rr.hasWanFaCY(eWanFaType_cy.mingpiaosuanbimen)) {
                if (getHandCardCount() <= 2 && !hasChiCard()) {
                    return true;
                }
            }
        }
        return this.getHandCardCount() >= 13;
    }

    // 葫芦岛非飘不能剩一张
    public boolean checkOne() {
        if (CardManager.ischaoyang || CardManager.isjinzhou || CardManager.ishuludao) {
            if (handMahList.size() <= 4 && chiList.size() > 0) {
                return false;
            }
        }
        return true;
        // TODO Auto-generated method stub

    }

    public void packLiuju(SendMsgBuffer b) {
        // TODO Auto-generated method stub
        if (CardManager.isjinzhou) {
            JZDeskBalance db = (JZDeskBalance) this.getDeskBalance();
            b.Add(this.getRoleId());
            b.Add(db.calScore(null));
            db.packLiuju(b);

        } else if (CardManager.ishuludao) {
            HLDDeskBalance db = (HLDDeskBalance) this.getDeskBalance();
            b.Add(this.getRoleId());
            b.Add(db.calScore(null));
            db.packLiuju(b);
        }

    }
}
