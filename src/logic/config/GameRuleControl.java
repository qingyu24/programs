package logic.config;

import java.util.ArrayList;
import java.util.List;

public class GameRuleControl {

    //#region 单列
    private static GameRuleControl _instance;
    public static GameRuleControl getInstance() {
        if (_instance == null) {
            _instance = new GameRuleControl();
        }
        return _instance;
    }
    public GameRuleControl() {
      
    }
    //#endregion
  
    //#region 功能实现
    // 检查是否可以碰牌
    public List<MahJongInstance> checkTouchCards(MahJongInstance _mahJong, List<MahJongInstance> _list) {
        List<MahJongInstance> ret = new ArrayList<MahJongInstance>();
        int count = _list.size();
        for (int i = 0; i < count; i++) {
            MahJongInstance mahJong = _list.get(i);
            if (mahJong.m_iSorting == _mahJong.m_iSorting && mahJong.getMahJongConfig().mahJongType == _mahJong.getMahJongConfig().mahJongType) {
                ret.add(mahJong);
            }
        }
        //List<MahJongInstance> ret = (from c in _list where (c.m_iSorting == mahJongType) select c).ToList<MahJongInstance>();
        if (ret.size() < 2)
            ret = null;
        return ret;
    }
    // --------------------吃牌--------------------------
    /// <summary>
    /// 检查是否可以吃牌
    /// </summary> 
    int crad1 = 0;
    int crad2 = 1;
    int newMahJongType = 0;
    List<MahJongInstance> _allList = null;
    public List<MahJongInstance> checkEatCards(MahJongInstance _mahJong, List<MahJongInstance> _list)
    {
        List<MahJongInstance> ret = null;
        int mahSortType = _mahJong.m_iSorting;
        if (mahSortType > 27)
            return null;
        // 根据类型吃牌
        _allList = _list;
        if (mahSortType >= 1 && mahSortType <= 9) {
            ret = checkEatMahJong(mahSortType, 0, _mahJong.getMahJongConfig().mahJongType);
        }
        else if (mahSortType >= 10 && mahSortType <= 18)
        {
            ret = checkEatMahJong(mahSortType, 9, _mahJong.getMahJongConfig().mahJongType);
        }
        else if (mahSortType >= 18 && mahSortType <= 27)
        {
            ret = checkEatMahJong(mahSortType, 17, _mahJong.getMahJongConfig().mahJongType);
        }
        return ret;
    }
    // 检查可以吃的麻将范围
    List<MahJongInstance> checkEatMahJong(int mahSortType, int _value,int _mahJongType) {
        List<MahJongInstance> ret = null;
        int baseNumber = mahSortType - _value;
        if (baseNumber == 1)
        {
            // 检查单边只检查比当前牌大的
            settingSelecteMahJongId(1, mahSortType, _mahJongType);
            List<MahJongInstance> getList = getEatMahJong();
            if (getList.size() == 2)
                ret = getList;

        }
        else if (baseNumber == 2)
        {
            // 检查中间可吃
            settingSelecteMahJongId(0, mahSortType, _mahJongType);
            List<MahJongInstance> getList = getEatMahJong();
            if (getList.size() == 2)
                ret = getList;

            // 检查单边只检查比当前牌大的
            settingSelecteMahJongId(1, mahSortType, _mahJongType);
            List<MahJongInstance> getList1 = getEatMahJong();
            if (getList1.size() == 2)
                ret = getList1;
        }
        else if (baseNumber == 8)
        {
            // 检查中间可吃
            settingSelecteMahJongId(0, mahSortType, _mahJongType);
            List<MahJongInstance> getList = getEatMahJong();
            if (getList.size() == 2)
                ret = getList;

            // 检查单边只检查比当前牌小的
            settingSelecteMahJongId(2, mahSortType, _mahJongType);
            List<MahJongInstance> getList1 = getEatMahJong();
            if (getList1.size() == 2)
                ret = getList1;

        }
        else if (baseNumber == 9)
        {
            settingSelecteMahJongId(2, mahSortType, _mahJongType);
            List<MahJongInstance> getList = getEatMahJong();
            if (getList.size() == 2)
                ret = getList;

        }
        else {
            // 检查中间可吃
            settingSelecteMahJongId(0, mahSortType, _mahJongType);
            List<MahJongInstance> getList = getEatMahJong();
            if (getList.size() == 2)
                ret = getList;

            // 检查单边只检查比当前牌大的
            settingSelecteMahJongId(1, mahSortType, _mahJongType);
            List<MahJongInstance> getList1 = getEatMahJong();
            if (getList1.size() == 2)
                ret = getList1;

            // 检查单边只检查比当前牌小的
            settingSelecteMahJongId(2, mahSortType, _mahJongType);
            List<MahJongInstance> getList2 = getEatMahJong();
            if (getList2.size() == 2)
                ret = getList2;
        }
        return ret;
    }
    // 设置要查找牌的id
    boolean settingSelecteMahJongId(int type,int mahJongType,int _mahJongType) {
        boolean ret = false;
        if (type == 0) {
            // 检查中间可吃
            crad1 = mahJongType - 1;
            crad2 = mahJongType + 1;
        }
        else if (type == 1)
        {
            // 检查单边只检查比当前牌大的
            crad1 = mahJongType + 1;
            crad2 = mahJongType + 2;
        }
        else if (type == 2)
        {
            // 检查单边只检查比当前牌小的
            crad1 = mahJongType - 1;
            crad2 = mahJongType - 2;
        }
        newMahJongType = _mahJongType;
        //Debug.Log("crad1  :"+ crad1+ "  crad2  :"+ crad2);
        return ret;
    }
    // 接收判断出的id
    List<MahJongInstance> getEatMahJong() {
        List<MahJongInstance> mahJongList = new ArrayList<MahJongInstance>();
        MahJongInstance mji1 = checkHandMahJong(crad1, newMahJongType);
        if (mji1 != null)
            mahJongList.add(mji1);
        MahJongInstance mji2 = checkHandMahJong(crad2, newMahJongType);
        if (mji2 != null)
            mahJongList.add(mji2);
        //Debug.Log("mahJongList ====> "+ mahJongList.size());
        return mahJongList;
    }
    // 根据匹配的类型ID查找玩家手里是否有了类型的牌
    public MahJongInstance checkHandMahJong(int _mahJongSorting,int _mahJongType) {
        //List<MahJongInstance> all = (from c in _allList where (c.m_iSorting == _mahJongSorting) select c).ToList<MahJongInstance>();
        List<MahJongInstance> all = new ArrayList<MahJongInstance>();
        int count = _allList.size();
        for (int i = 0; i < count; i++)
        {
            MahJongInstance mahJong = _allList.get(i);
            if (mahJong.m_iSorting == _mahJongSorting && _mahJongType == mahJong.getMahJongConfig().mahJongType) {
                all.add(mahJong);
            }
        }
        if (all.size() > 0)
            return all.get(0);
        return null;
    }

    // --------------------end--------------------------
    // 检查是否可以杠牌
    public List<MahJongInstance> checkBarsCards(int mahJongSorting, List<MahJongInstance> _list)
    {
        //List<MahJongInstance> ret = (from c in _list where (c.m_iSorting == mahJongType) select c).ToList<MahJongInstance>();
        List<MahJongInstance> ret = new ArrayList<MahJongInstance>();
        int length = _list.size();
        for (int i = 0; i < length; i++)
        {
            MahJongInstance mahJong = _list.get(i);
            if (mahJong.m_iSorting == mahJongSorting) {
                ret.add(mahJong);
            }
        }
        if (ret.size() < 3)
            ret = null;
        return ret;
    }

    // 提取单张牌
    public List<MahJongInstance> getLeaflet(List<MahJongInstance> _handMah) {
        List<MahJongInstance> ret = new ArrayList<MahJongInstance>();
        int count = _handMah.size();
        for (int i = 0; i < count; i++) {
            MahJongInstance mahJong = _handMah.get(i);
            List<MahJongInstance> getCheck = checkHandMahJong(mahJong.m_iSorting, mahJong.getMahJongConfig().mahJongType, _handMah);
            if (getCheck.size() >= 2)
            {
                continue;// 多张牌，从新查找
            }
            else if(getCheck.size() > 0)
            {
                // 检查是否有和当前牌组成顺子的牌
                List<MahJongInstance> getCheck1 = checkHandMahJong(mahJong.m_iSorting+1, mahJong.getMahJongConfig().mahJongType, _handMah);
                List<MahJongInstance> getCheck2 = checkHandMahJong(mahJong.m_iSorting+2, mahJong.getMahJongConfig().mahJongType, _handMah);
                if (getCheck2.size() > 0)
                {
                    i = i + 2;
                    continue;// 多张牌，从新查找
                }
                else if (getCheck1.size() > 0)
                {
                    i = i + 1;
                    continue;// 多张牌，从新查找
                }
                else {
                    ret.add(mahJong);
                }
            }
        }
        //Debug.Log("单张抽取结果----------------》 "+ ret.size());
        return ret;
    }
    // 提取却中间张的牌
    public List<MahJongInstance> getLackOfMiddle(List<MahJongInstance> _handMah) {
        List<MahJongInstance> ret = new ArrayList<MahJongInstance>();
        int count = _handMah.size();
        for (int i = 0; i < count; i++)
        {
            MahJongInstance mahJong = _handMah.get(i);
            // 检查是否有和当前牌组成顺子的牌
            List<MahJongInstance> getCheck1 = checkHandMahJong(mahJong.m_iSorting + 1, mahJong.getMahJongConfig().mahJongType, _handMah);
            List<MahJongInstance> getCheck2 = checkHandMahJong(mahJong.m_iSorting + 2, mahJong.getMahJongConfig().mahJongType, _handMah);
            if (getCheck2.size() > 0 && getCheck1.size() <= 0)
            {
                i = i + 2;
                ret.add(mahJong);
                ret.add(getCheck2.get(0));
            }
        }
        //Debug.Log("缺中间张抽取结果----------------》 " + ret.size());
        return ret;
    }
    // 提取却临边张的牌
    public List<MahJongInstance> getLackOfEdge(List<MahJongInstance> _handMah)
    {
        List<MahJongInstance> ret = new ArrayList<MahJongInstance>();
        int count = _handMah.size();
        for (int i = 0; i < count; i++)
        {
            MahJongInstance mahJong = _handMah.get(i);
            // 检查是否有和当前牌组成顺子的牌
            List<MahJongInstance> getCheck1 = checkHandMahJong(mahJong.m_iSorting + 1, mahJong.getMahJongConfig().mahJongType, _handMah);
            List<MahJongInstance> getCheck2 = checkHandMahJong(mahJong.m_iSorting + 2, mahJong.getMahJongConfig().mahJongType, _handMah);
            if (getCheck2.size() <= 0 && getCheck1.size() > 0)
            {
                ret.add(mahJong);
                ret.add(getCheck1.get(0));
                i = i + 2;
            }
        }
        //Debug.Log("缺领班张抽取结果----------------》 " + ret.size());
        return ret;
    }
    // 根据匹配的类型ID查找玩家手牌相同张数，多条件查找
    public List<MahJongInstance> checkHandMahJong(int stortType, int _mahJongTypeC, List<MahJongInstance> _listC)
    {
        //List<MahJongInstance> all = (from c in _listC where (c.m_iSorting == stortType && c.getMahJongConfig().MahJongType == _mahJongTypeC) select c).ToList<MahJongInstance>();
        List<MahJongInstance> all = new ArrayList<MahJongInstance>();
        int length = _listC.size();
        for (int i = 0; i < length; i++)
        {
            MahJongInstance mahJong = _listC.get(i);
            if (mahJong.m_iSorting == stortType && mahJong.getMahJongConfig().mahJongType == _mahJongTypeC) {
                all.add(mahJong);
            }
        }
        return all;
    }
    // 出牌规则，取单张牌或空闲的牌
    public MahJongInstance playerOutMahJong(List<MahJongInstance> _handMah) {
        MahJongInstance ret = null;
        List<MahJongInstance> getList = getLeaflet(_handMah);
        if (getList.size() > 0)
        {
            ret = getList.get(0);
            return ret;
        }
        else 
        {
            getList = getLackOfMiddle(_handMah);
            if (getList.size() > 0)
            {
                ret = getList.get(0);
            }
            else {
                getList = getLackOfEdge(_handMah);
                if (getList.size() > 0)
                {
                    ret = getList.get(0);
                }
            }
                
        }
       
        return ret;
    }
   
  
    //#endregion
}
