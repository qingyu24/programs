package logic.userdata;

import core.detail.impl.log.Log;
import core.detail.impl.socket.SendMsgBuffer;
import logic.MyUser;
import logic.eWanFaType_sx;
import logic.module.log.eLogicDebugLogType;
import logic.module.room.CardInfo;
import logic.module.room.Room;
import logic.module.room.RoomRule;
import logic.module.room.RoomUser;
import manager.RoomManager;

import java.util.ArrayList;
import java.util.Iterator;

public class XADeskBalance extends IDeskBalance<XADeskBalance> {

    public int zimo; // 自摸;底分1分；
    public int dianpao; // 点炮;();底分2分；
    public int pihu; // 屁胡;
    public int dihu;// 单胡一张; //地胡
    public int tianhu; // 七对; //天胡

    public int lihu; // 立胡;
    public int bimen; // 闭门;
    public int bimen3; // 三家闭门;
    public int bimen4; // 四家闭门;
    public int gskh; // 杠上开花;
    public int gspao; // 杠上炮；
    public int qys; // 海底捞月;
    public int huangzhuang; // 晃庄；

    public int mg; // 明杠;
    public int ag; // 暗杠；
    public int cg; // 彩杠;
    public int acg; // 暗彩杠；

    public int guo; // guo;
    private int sumAll;//多响之前分数

    // 葫芦岛玩法的算分规则;
    public XADeskBalance(RoomUser ru) {
        m_user = ru;
        ru.setDeskBalance(this);
    }

    @Override
    public void packData(SendMsgBuffer buffer) {
        // TODO Auto-generated method stub
        int bei = 0;
        if (zimo > 0 || dianpao > 0) bei = 1;
        else if (zimo < 0 || dianpao < 0) {
            bei = -1;
        }
        int qys1=0;
        int tianhu1=0;

        if (zimo != 0) {
            qys1 = qys * zimo / 2;
            tianhu1 = tianhu * zimo / 2;
        } else if (dianpao != 0) {
            qys1 = qys * dianpao;
            tianhu1 = tianhu * dianpao;
        }

        if (dianpao < 0) {
            int i = 0;
            Room room = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
            if (room != null) {
                Iterator<RoomUser> iterator = room.getUserInfo().iterator();
                while (iterator.hasNext()) {
                    RoomUser next = iterator.next();
                    if (next.getRoleId() != this.m_user.getRoleId()) {
                        XADeskBalance deskBalance = (XADeskBalance) next.getDeskBalance();
                        i += deskBalance.dianpao;
                    }
                }
            }
            dianpao -= i;
        }
        buffer.Add(zimo); // 自摸;底分1分；
        buffer.Add(dianpao); // 点炮;();底分1分；
        buffer.Add(0); // 屁胡;
        buffer.Add(dihu * bei);// 地胡;
        buffer.Add(tianhu1 * bei); // 七对;
        buffer.Add(guo); // 飘胡; //改成了锅
        buffer.Add(0); // 立胡;
        buffer.Add(0); // 闭门;
        buffer.Add(0); // 三家闭门;
        buffer.Add(0); // 四家闭门;
        buffer.Add(gskh * bei); // 杠上开花;
        buffer.Add(0); // 杠上炮；
        buffer.Add(qys1 * bei); // 清一色);
        buffer.Add(0); // 晃庄；
        buffer.Add(mg); // ；
        buffer.Add(ag); // ；
        buffer.Add(cg); // ；
        buffer.Add(acg); // ；

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        zimo = 0; // 自摸;底分1分；
        dianpao = 0; // 点炮;();底分2分；
        pihu = 0; // 屁胡;
        dihu = 0; // 单胡一张
        tianhu = 0; // 七对;
        guo = 0; // 飘胡;
        lihu = 0; // 立胡;
        bimen = 0; // 闭门;
        bimen3 = 0; // 三家闭门;
        bimen4 = 0; // 四家闭门;
        gskh = 0; // 杠上开花;
        gspao = 0; // 杠上炮；
        qys = 0; // 海底捞月;
        huangzhuang = 0; // 晃庄；
        ag = 0;
        mg = 0;
        cg = 0;
        acg = 0;
        sumAll = 0;
    }


    @Override
    public int calScore(RoomUser winner) {
        int gang = mg + ag + cg + acg;

        int allScore = 0;
        if (dianpao < 0) {
            Room room = RoomManager.getInstance().getRoom(this.m_user.getRoomId());
            if (room != null) {
                Iterator<RoomUser> iterator = room.getUserInfo().iterator();
                while (iterator.hasNext()) {
                    RoomUser next = iterator.next();
                    if (next.getRoleId() != this.m_user.getRoleId()) {
                        XADeskBalance deskBalance = (XADeskBalance) next.getDeskBalance();
                        allScore -= deskBalance.calScoreNoGang(null);

                    }
                }
                return allScore + gang;
            }
        }
        int zimo1 = zimo == 0 ? 1 : zimo;
        int dianpao1 = dianpao == 0 ? 1 : dianpao;

        int tianhu1 = tianhu == 0 ? 1 : tianhu;// +
        int dihu1 = dihu == 0 ? 1 : dihu;

        int qys1 = qys == 0 ? 1 : qys;
        int gskh1 = gskh == 0 ? 1 : gskh;

        int sum = gskh + qys + tianhu + dihu;
        int sum1 = sum == 0 ? 1 : sum;
        int bei = zimo1 * dianpao1 * sum1;

        if (zimo == 0 && dianpao == 0) {
            bei = 0;
        }


        return bei + gang + guo;

    }


    public int calScoreNoGang(RoomUser winner) {


        int gang = mg + ag + cg + acg;

        return this.calScore(null) - gang;

    }

    @Override
    public void processHu(CardInfo card, int size, ArrayList<RoomUser> users, RoomUser winner) {
/*        sumAll=this.calScore(null);*/
        // TODO Auto-generated method stub
        RoomRule rr = RoomManager.getInstance().getRoom(users.get(0).getRoomId()).getRoomRule();
        Room room2 = RoomManager.getInstance().getRoom(users.get(0).getRoomId());
        boolean isDianpao = null != card;
        if (isDianpao) {
            XADeskBalance db = (XADeskBalance) card.getOwner().getDeskBalance();
            Iterator<RoomUser> it = users.iterator();
            db.dianpao = -1;
            this.dianpao = 1;
            // 锅
            if (rr.hasWanFaSX(eWanFaType_sx.SHIGUO) && card.getOwner().giveGuo) {
                card.getOwner().giveGuo = false;
                XADeskBalance m_db = (XADeskBalance) card.getOwner().m_db;
                m_db.guo = -1;
                int roomId = card.getOwner().getRoomId();
                Room room = RoomManager.getInstance().getRoom(roomId);

                room.guo += 1;
                room.m_guoScore.add(card.getOwner().getRoleId());
            }

            if (m_user.isQingYise()) {
                db.qys += 4;
                this.qys = 4;
            }
        } else {
            // 是自摸;
            Iterator<RoomUser> it = users.iterator();
            while (it.hasNext()) {
                RoomUser next = it.next();
                XADeskBalance m_db = (XADeskBalance) next.getDeskBalance();
                m_db.zimo = -2;
            }
            this.zimo = 6;

            if (rr.hasWanFaSX(eWanFaType_sx.SHIGUO)) {
                if (m_user.canGuo(card)) {
                    this.guo += room2.guo;
                    room2.guo = 0;
                    room2.m_guoScore.clear();
                }
            }
            // this.m_user.isGangShang(); //是否是杠上开花;
            if (this.m_user.isGangShang()) {

                it = users.iterator();
                while (it.hasNext()) {
                    RoomUser ru = it.next();
                    {
                        XADeskBalance db = (XADeskBalance) ru.getDeskBalance();
                        db.gskh = 4;
                    }
                }
            }

            if (m_user.isQingYise()) {
                Iterator<RoomUser> its = users.iterator();
                while (its.hasNext()) {
                    RoomUser ru = its.next();
                    {
                        XADeskBalance db = (XADeskBalance) ru.getDeskBalance();
                        db.qys = 4;
                    }

                }
            }

        }
        if (this.m_user.nu_MahJong == 0 && this.m_user.getZhuang() == 1) {
            Iterator<RoomUser> its = users.iterator();
            while (its.hasNext()) {
                RoomUser ru = its.next();
                {
                    XADeskBalance db = (XADeskBalance) ru.getDeskBalance();
                    db.tianhu = 16;
                }
            }

        } else if (this.m_user.nu_MahJong == 1 && this.m_user.getZhuang() != 1) {
            Iterator<RoomUser> its = users.iterator();
            while (its.hasNext()) {
                RoomUser ru = its.next();
                {
                    XADeskBalance db = (XADeskBalance) ru.getDeskBalance();
                    db.dihu = 12;
                }
            }

        }

        String str = "";

        str += String.format("\nzimo:%d\n", zimo); // 自摸;底分1分；
        str += String.format("dianpao:%d\n", dianpao); // 点炮;();底分1分；
        str += String.format("pihu:%d\n", pihu); // 屁胡;
        str += String.format("danhu:%d\n", dihu);// 单胡一张;
        str += String.format("tianhu:%d\n", tianhu);
        str += String.format("lihu:%d\n", lihu); // 立胡;
        str += String.format("bimen:%d\n", bimen); // 闭门;
        str += String.format("bimen3:%d\n", bimen3); // 三家闭门;
        str += String.format("bimen4:%d\n", bimen4); // 四家闭门;
        str += String.format("gskh:%d\n", gskh); // 杠上开花;
        str += String.format("gspao:%d\n", gspao); // 杠上炮；
        str += String.format("hdly:%d\n", qys); // 海底捞月);

        LogRecord(this.m_user.getUser(), str);
    }

    @Override
    public void processGang(CardInfo card, ArrayList<RoomUser> users, RoomUser winner) {
        // TODO Auto-generated method stub
        if (!this.m_user.isHu) {
            return; // 如果你不是赢家，是不能结算杠的分的；
        }
        int sanren = 1;
        if (card == null) {
            sanren = 3;
        } else {
            sanren = 1;
        }
        int fb1 = 1;
        int fb2 = 1;
        int fb3 = 1;
        ArrayList<CardInfo> _mg = m_user.getMingGang();

        ArrayList<CardInfo> _ag = m_user.getAnGang();

        // 把直杠和明杠合并了;
        mg += (_mg.size() + m_user.getZhiGang().size()) * 1 * sanren * fb1;

        ag += _ag.size() * 2 * sanren * fb2;
        // 处理直杠;
/*		if (!_zg.isEmpty()) {

		}*/
        // 其他的玩家都要-1 * count;
        Iterator<RoomUser> it = users.iterator();
        while (it.hasNext()) {
            RoomUser ru = it.next();
            if (ru.getRoleId() != this.m_user.getRoleId() && card == null) {
                XADeskBalance db = (XADeskBalance) ru.getDeskBalance();

                db.mg -= m_user.getMingGang().size() * 1;

                db.ag -= m_user.getAnGang().size() * 2;

            } else if (card != null) {
                if (card.getOwner().getRoleId() == ru.getRoleId()) {
                    XADeskBalance db = (XADeskBalance) ru.getDeskBalance();
                    db.mg -= (m_user.getMingGang().size() + m_user.getZhiGang().size()) * 1;

                    db.ag -= m_user.getAnGang().size() * 2;
                }
            }

        }

        String str = "\n";
        str += String.format("mg:%d\n", mg);
        str += String.format("ag:%d\n", ag);
        str += String.format("cg:%d\n", cg);
        str += String.format("acg:%d\n", acg);
        LogRecord(this.m_user.getUser(), str);
    }

    private void LogRecord(MyUser user, String record) {
        if (null != user) {
            Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, user.GetRoleGID(), record);
        } else {
            Log.out.Log(eLogicDebugLogType.LOGIC_SQL_RECORD, 0l, record);
        }
    }

    /*    @Override*/
    public String toString() {
        return "XADeskBalance [zimo=" + zimo + ", dianpao=" + dianpao + ", pihu=" + pihu + ", danhu=" + dihu
                + ", tianhu=" + tianhu + ", lihu=" + lihu + ", bimen=" + bimen + ", bimen3=" + bimen3 + ", bimen4="
                + bimen4 + ", gskh=" + gskh + ", gspao=" + gspao + ", qys=" + qys + ", huangzhuang=" + huangzhuang
                + ", mg=" + mg + ", ag=" + ag + ", cg=" + cg + ", acg=" + acg + ", guo=" + guo + "]";
    }

}
