package com.wisdom.auth.server.bean;


import com.wisdom.auth.server.common.util.jwt.IJWTInfo;

/**
 *
 */
public class ClientInfo implements IJWTInfo {
    String clientId;
    String name;
    String actuName;
    String userType;

    public ClientInfo(String clientId, String name, String id) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActuName(String actuName) {
        this.actuName = actuName;
    }

    @Override
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String getUniqueName() {
        return clientId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getActuName() {
        return this.actuName;
    }
}
