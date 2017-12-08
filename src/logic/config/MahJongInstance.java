package logic.config;
public class MahJongInstance extends InstanceBase {

    //#region 属性

   // #endregion

    //#region 功能实现
    // 初始数据
    public boolean init(MahJongConfig mahJongConfig) {
        boolean ret = false;
        m_iInstanceId = mahJongConfig.id;
        m_iTemplateId = mahJongConfig.id;
        m_iSorting = mahJongConfig.sortType;
        m_TemplateDef = mahJongConfig;
        return ret;
    }
    // 获取模板数据
    public MahJongConfig getMahJongConfig() {
        return (MahJongConfig) m_TemplateDef;
    }
}
