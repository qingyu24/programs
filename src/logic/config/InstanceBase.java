package logic.config;
public class InstanceBase {
    //#region 实例属性
    public int m_iInstanceId;                          // 实例ID
    public int m_iTemplateId;                          // 模板id
    public int m_iSorting = 0;                         // 要排序的类型（通过模板数据进行赋值，没有排序需求的可直接忽略）
    public IConfig m_TemplateDef;                 // 模板对象的引用
    protected boolean m_bIsSelecte = false;               // 当前实例是否被选中
    
    public boolean OnSelected(boolean _value)
    {
        boolean res = false;
        m_bIsSelecte = _value;
        return res;
    }
   // #endregion
}
