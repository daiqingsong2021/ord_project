package com.wisdom.base.common.enums;

public enum ParamEnum {

    TASK("task","任务"),
    WBS("wbs","WBS"),
    DEFINE("define","计划定义"),
    PROJECT("project","项目"),
    NONE("NONE","空"),
    ORG("ORG","组织"),
    DATE("DATE","空"),
    DICT("DICT","数据字典"),
    CALENDAR("CALENDAR","日历"),
    OTHER("OTHER","其它"),
    USER("USER", "用户");

    private String code;
    private String message;

    ParamEnum() {

    }

    ParamEnum(String code, String message){
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

}
