package com.wisdom.base.common.enums;

public enum StatusEnum {

    NEWBUILD("NEWBUILD","新建"),
    ACCEPTANCE("ACCEPTANCE", "已验收"),
    NOACCEPTANCE("NOACCEPTANCE", "未验收"),
    RELEASE("RELEASE", "已发布"),
    EDIT("EDIT", "编制中"),
    APPROVAL("APPROVAL","审批中"),
    CONFIRM("CONFIRM","已确认"),
    CONFIRMING("CONFIRMING","确认审批中"),
    APPROVED("APPROVED","已审批"),
    CLOSED("CLOSED","已关闭")
    ;

    private String code;
    private String message;

    StatusEnum() {
    }

    StatusEnum(String code, String message){
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
            for(StatusEnum e : StatusEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }


}
