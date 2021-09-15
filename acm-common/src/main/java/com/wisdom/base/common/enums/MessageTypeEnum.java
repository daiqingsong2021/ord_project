package com.wisdom.base.common.enums;

public enum MessageTypeEnum {

    AAA("AAA", "111"),
    DDD("DDD","升版，在线预览，下载")
    ;

    private String code;
    private String message;

    MessageTypeEnum() {
    }

    MessageTypeEnum(String code, String message){
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
            for(MessageTypeEnum e : MessageTypeEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }
}
