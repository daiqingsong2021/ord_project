package com.wisdom.acm.sys.enums;

public enum MessageTypeEnums {

    COMMUNICATE("COMMUNICATE", "沟通"),
    DOC_OUTGIVING("DOC_OUTGIVING", "文档传递"),
    DOC_CONCERN("DOC_CONCERN", "文档关注通知"),
    SUBPACKAGEINVITE("SUBPACKAGEINVITE","分包邀请"),
    SERVICEINVITE("SERVICEINVITE","劳务邀请"),
    SYSTEMNOTICE("SYSTEMNOTICE","系统通知")
    ;

    private String code;
    private String message;

    MessageTypeEnums() {
    }

    MessageTypeEnums(String code, String message){
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
