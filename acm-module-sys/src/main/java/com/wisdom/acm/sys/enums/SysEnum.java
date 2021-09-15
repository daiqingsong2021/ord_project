package com.wisdom.acm.sys.enums;

/**
 * 系统管理枚举
 */
public enum SysEnum {

    DEVELOPMENT_ORGANIZATION("1","建设单位"),//建设单位
    //团队类型
    ORG("org","单位"),
    SECTION("section","标段"),
    //单位类型 base.org.type
    CONSTRUCTION_UNIT("2", "施工单位"),//单位类型
    CONSTRUCTION_CONTROL_UNIT("3", "监理单位"),//单位类型
    //标段类型  proj.section.type
    CONSTRUCTION("construction", "施工类"),//标段类型
    SUPERVISOR("supervisor", "监理类"),//标段类型

    CONSTRUCTION_UNIT_TYPE("4", "施工单位"),//单位分类
    CONSTRUCTION_CONTROL_UNIT_TYPE("7", "监理单位");//单位分类

    private String code;
    private String message;

    SysEnum(){}

    SysEnum(String code, String message){
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取去value
     * @param code
     * @return
     */
    public static String getMessageByCode(String code){
        if(code != null ){
            for(MessageTypeEnums e : MessageTypeEnums.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }
}
