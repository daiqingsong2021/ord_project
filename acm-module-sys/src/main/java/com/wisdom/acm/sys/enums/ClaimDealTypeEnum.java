package com.wisdom.acm.sys.enums;

public enum ClaimDealTypeEnum {

    INXXBEFOREHF("INXXBEFOREHF", "在...之前回复"),
    INXXBEFOREPZ("INXXBEFOREPZ", "在...之前批准"),
    INXXBEFORETYJ("INXXBEFORETYJ", "在...之前提意见"),
    INXXBEFOREXGTJ("INXXBEFOREXGTJ","在...之前修改并提交"),
    INXXBEFORETJBJ("INXXBEFORETJBJ","在...之前提交并报价"),
    INXXBEFOREJF("INXXBEFOREJF","于...之前根据图纸制造并交付"),
    INXXBEFOREZBJZ("INXXBEFOREZBJZ","于...之前招标截至"),
    INXXBEFOREGZSD("INXXBEFOREGZSD","于...之前告知收到")
    ;

    private String code;
    private String message;

    ClaimDealTypeEnum() {
    }

    ClaimDealTypeEnum(String code, String message){
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
            for(ClaimDealTypeEnum e : ClaimDealTypeEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getMessage();
                }
            }
        }
        return  null;
    }

}
