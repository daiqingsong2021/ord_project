package com.wisdom.base.common.enums;

public enum LoggerModuleEnum {
    ODR_REPORT("ODR_REPORT","日报管理"),
    NONE("",""),
    SM_MENU("SM_MENU","菜单管理"),
    SM_ROLE("SM_ROLE","角色管理"),
    SM_ORG("SM_ORG","组织管理"),
    SM_IPT("SM_IPT","IPT管理"),
    SM_USER("SM_USER","用户管理"),
    SM_TMM("SM_TMM","三员管理"),
    BM_DICT("BM_DICT","数据字典"),
    BM_CLASSIFY("BM_CLASSIFY","分类码"),
    BM_RULETYPE("BM_RULETYPE","规则类型"),
    BM_CALENDAR("BM_CALENDAR","日历设置"),
    BM_CURRENY("BM_CURRENY","货币设置"),
    BM_GLOBALD("BM_GLOBALD","全局设置"),
    BM_TMPL_PLAN("BM_TMPL_PLAN","计划模板"),
    BM_TMPL_DELV("BM_TMPL_DELV","交付物模板"),
    BM_TMPL_DOC("BM_TMPL_DOC","文档模板"),
    BM_CODERULE("BM_CODERULE","编码规则"),
    IM("IM","策划管理"),
    IM_EPS("IM_EPS","项目群"),
    IM_PREPA("IM_PREPA","项目立项"),
    IM_PROJECT("IM_PROJECT","项目信息"),
    IM_DELV("IM_DELV","项目交付物"),
    IM_PROJTEAM("IM_PROJTEAM","项目团队"),
    PM_DEFINE("PM_DEFINE","计划定义"),
    PM_TASK("PM_TASK","计划编制"),
    PM_CHANGE("PM_CHANGE","计划变更"),
    PM_FEEDBACK("PM_FEEDBACK","计划反馈"),
    RM_ROLE("RM_ROLE","资源角色"),
    RM_LIST("RM_LIST","资源清单"),
    CM_QUESTION("CM_QUESTION","问题管理"),
    CM_MEETING("CM_MEETING","会议管理"),
    DM_TEMPORARY("DM_TEMPORARY","临时文档"),
    DM_CORP("DM_CORP","企业文档"),
    DM_PROJECT("DM_PROJECT","项目文档"),
    DM_FAVORITE("DM_FAVORITE","收藏夹"),
    DM_RECYCLEBIN("DM_RECYCLEBIN","回收站"),
    WM_DEFINE("WM_DEFINE","流程定义"),
    WM_BUSSI("WM_BUSSI","流程业务定义"),
    CLASSIFICATION("CLASSIFICATION","物料分类"),
    DRIVING_DRIVINGINFO("DRIVING_DRIVINGINFO","行车日况"),
    DRIVING_TIMETABLE("DRIVING_TIMETABLE","时刻表管理"),
    CONSTRUCTION_DAILYREPORT("CONSTRUCTION_DAILYREPORT","施工日况"),
    MALFUNCTION_DAILYREPORT("MALFUNCTION_DAILYREPORT","故障日况"),
    ENERGY_DETAILEDSEARCH("ENERGY_DETAILEDSEARCH","电耗查询"),
    TRAFFIC_DAILYREPORT("TRAFFIC_DAILYREPORT","客运日况数据"),

    DAILY_REPRESENTATION("DAILY_REPRESENTATION"," 补充情况说明"),
    DAILY_CHANGEVERSION("DAILY_CHANGEVERSION"," 日况版本管理"),
    DRIVING_TIMETABLE_STATION("DRIVING_TIMETABLE_STATION","时刻表管理站点"),
    DAILY_ABNORMAL_SCHEDULE("DAILY_ABNORMAL_SCHEDULE","日况非正常行驶数据"),
    SYS("SYS",""),   //系统登录登出
    ;

    private String code;
    private String name;

    LoggerModuleEnum() {
    }

    LoggerModuleEnum(String code, String name){
        this.code=code;
        this.name=name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code获取去value
     * @param code
     * @return
     */
    public static String getMessageByCode(String code){

        if(code != null ){
            for(LoggerModuleEnum e : LoggerModuleEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getName();
                }
            }
        }
        return  null;
    }


}
