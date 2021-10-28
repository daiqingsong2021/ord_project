package com.wisdom.auth.server.util.user;

import java.io.Serializable;

public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String userName;
    private String password;
    private String userHost;
    private String uid;
    private String phone_number;
    private String name;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JwtAuthenticationRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public JwtAuthenticationRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserHost(String userHost) {
        this.userHost = userHost;
    }

    public String getUserHost() {
        return userHost;
    }
}
