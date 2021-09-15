package com.wisdom.base.common.enums;

public enum  CodeRuleAttributeTypeEnum {

    SELF("SELF","自身属性"),
    TABLE_COLUMN("TABLE_COLUMN","表-列"),
    COMPLEX_SQL("COMPLEX_SQL","复杂sql");

    private String code;
    private String message;

    CodeRuleAttributeTypeEnum() {

    }

    CodeRuleAttributeTypeEnum(String code, String message){
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
            for(CodeRuleAttributeTypeEnum e : CodeRuleAttributeTypeEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }

}
