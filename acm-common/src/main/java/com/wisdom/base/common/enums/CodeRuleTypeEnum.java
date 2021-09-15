package com.wisdom.base.common.enums;

public enum CodeRuleTypeEnum {

    ATTRIBUTE("ATTRIBUTE", "属性"),
    FIXED_VALUE("FIXED_VALUE", "固定值"),
    DATE("DATE","时间"),
    SEQUENCE("SEQUENCE","序列"),
    USER_DEFINE("USER_DEFINE","自定义");

    private String code;
    private String message;

    CodeRuleTypeEnum() {

    }

    CodeRuleTypeEnum(String code, String message){
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
            for(CodeRuleTypeEnum e : CodeRuleTypeEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }

}
