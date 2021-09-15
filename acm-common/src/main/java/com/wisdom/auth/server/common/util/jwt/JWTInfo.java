package com.wisdom.auth.server.common.util.jwt;

import java.io.Serializable;

/**
 *
 */
public class JWTInfo implements Serializable,IJWTInfo {
    private String username;
    private String userId;
    private String name;
    private String actuName;
    private String userType;

    public JWTInfo(String username, String userId, String name) {
        this.username = username;
        this.userId = userId;
        this.name = name;
    }


    public JWTInfo(String username, String userId, String name,String actuName) {
        this.username = username;
        this.userId = userId;
        this.name = name;
        this.actuName = actuName;
    }

    public JWTInfo(String username, String userId, String name,String actuName,String userType) {
        this.username = username;
        this.userId = userId;
        this.name = name;
        this.actuName = actuName;
        this.userType = userType;
    }

    @Override
    public String getUniqueName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
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
    public String getActuName() {
        return this.actuName;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JWTInfo jwtInfo = (JWTInfo) o;

        if (username != null ? !username.equals(jwtInfo.username) : jwtInfo.username != null) {
            return false;
        }
        return userId != null ? userId.equals(jwtInfo.userId) : jwtInfo.userId == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
