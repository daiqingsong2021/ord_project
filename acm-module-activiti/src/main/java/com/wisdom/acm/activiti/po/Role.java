package com.wisdom.acm.activiti.po;

public class Role {

    private String id;

    private String code;

    private String name;

    private String companyCode;

    public Role(){

    }

    public Role(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Role(String id, String code, String name, String companyCode) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.companyCode = companyCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
