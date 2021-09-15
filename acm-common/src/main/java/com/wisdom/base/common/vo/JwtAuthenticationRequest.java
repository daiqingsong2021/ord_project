package com.wisdom.base.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 */
@Data
public class JwtAuthenticationRequest implements Serializable {

    private String userName;
    private String password;
    private String userHost;

    public JwtAuthenticationRequest(){

    }

    public JwtAuthenticationRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
