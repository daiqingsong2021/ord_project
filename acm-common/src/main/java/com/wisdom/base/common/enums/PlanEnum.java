package com.wisdom.base.common.enums;

public enum PlanEnum {

    IMPLMENT_TASK("3","实施计划"),
    ST_IMPLMENT_TASK("ST-IMPLMENT-TASK", "总体计划"),
    ST_IMPLMENT_Y_TASK("ST-IMPLMENT-Y-TASK", "年度计划"),
    ST_IMPLMENT_M_TASK("ST-IMPLMENT-M-TASK", "月度计划");

    private String code;
    private String message;

    PlanEnum() {

    }

    PlanEnum(String code, String message){
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
