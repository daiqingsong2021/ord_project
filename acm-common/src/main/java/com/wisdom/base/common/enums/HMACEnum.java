package com.wisdom.base.common.enums;

public enum HMACEnum {

    /**
     * hmac加密key
     */
    HMAC_KEY("Wisdom"),

    /**
     * hmac加密常量
     */
    HMAC_SHA1_ALGORITHM("HmacSHA1");


    private String type;

    HMACEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
