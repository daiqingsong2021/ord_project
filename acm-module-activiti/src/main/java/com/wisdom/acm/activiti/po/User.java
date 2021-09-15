package com.wisdom.acm.activiti.po;

public class User {

    private String id;

    private String loginId;

    private String userName;

    private String password;

    private String orgId;

    private String orgName;

    private String companyCode;

    public User(){

    }

    public User(String id, String loginId, String userName, String orgName) {
        this.id = id;
        this.loginId = loginId;
        this.userName = userName;
        this.orgName = orgName;
    }

    public User(String id, String loginId, String userName, String password, String companyCode, String orgId, String orgName) {
        this.id = id;
        this.loginId = loginId;
        this.userName = userName;
        this.password = password;
        this.companyCode = companyCode;
        this.orgName = orgName;
        this.orgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
