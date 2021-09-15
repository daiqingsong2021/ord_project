package com.wisdom.acm.wf.enums;

public enum WfLogOptTypeEnum {

    CANCEL("CANCEL", "撤销"),
    REJECT("REJECT", "驳回"),
    START("START", "发起"),
    SUBMIT("SUBMIT","提交"),
    APPROVE("APPROVE","批准"),
    TERMINATE("TERMINATE","终止"),
    ;

    private String code;
    private String message;

    WfLogOptTypeEnum() {

    }

    WfLogOptTypeEnum(String code, String message){
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
            for(WfLogOptTypeEnum e : WfLogOptTypeEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }
}
