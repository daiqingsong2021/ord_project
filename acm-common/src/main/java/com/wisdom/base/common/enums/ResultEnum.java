package com.wisdom.base.common.enums;

public enum ResultEnum {
    RESULT_SUCCESS(200, "请求成功！"),
    RESULT_ERROR(1000, "请求失败！ "),
    PASS_ERROR(2,"用户名或密码错误！"),
    SECCESS(200,"请求成功！"),
    LOGINSECCESS(201,"登录成功！"),
    USERNAMEEXISTS(202,"账户已存在！"),
    USERNAMENOTEXISTS(1002,"登录名不存在！"),
    REGISTSECCESS(203,"注册成功！"),
    ERROR(1000,"请求失败！"),
    LOGINFAIL(1001,"登录失败！"),
    REGISTFAIL(1003,"注册失败！"),
    VERIFICATION(1004,"验证码失败！"),
    PASSWORDERROR(1005,"密码错误！"),
    UPDATESECCESS(204,"修改成功！"),
    UPDATEERROR(1014,"修改失败！"),
    VERIFYSUCCESS(205,"用户认证成功！"),
    VERIFYERROR(1006,"用户认证失败！"),
    UPLOADSECCESS(206,"上传成功！"),
    UPLOADFAIL(1007,"上传失败！"),
    PUBLICFAIL(1008,"项目发布失败！"),
    PUBLICSUCCESS(207,"项目发布成功！"),
    ISBIDED(1010,"您已报名，不能重复报名！"),
    BIDSUCCESS(208,"报名成功！"),
    BIDERROR(1009,"报名失败！"),
    WINSECCESS(209,"中标成功！"),
    WINERROR(1011,"中标失败！"),
    COLLECTIONSUCCESS(210,"收藏成功！"),
    COLLECTIONERROR(1012,"收藏失败！"),
    OPERATIONSECCESS(211,"操作成功！"),
    OPERATIONERROR(1013,"操作失败！"),
    SAVESUCCESS(212,"保存成功！"),
    SAVEERROR(1015,"保存失败！"),
    DELESUCCESS(213,"删除成功！"),
    DELEERROR(1016,"删除失败！"),
    BUSSI_EERROR(1088,"业务自定义异常错误！"),
    ;

    private Integer code;
    private String message;

    ResultEnum() {
    }

    ResultEnum(Integer code, String message){
        this.code=code;
        this.message=message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
