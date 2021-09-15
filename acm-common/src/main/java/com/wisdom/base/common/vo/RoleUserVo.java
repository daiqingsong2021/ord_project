package com.wisdom.base.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户角色
 */
@Data
public class RoleUserVo {

    private RoleVo role;

    private List<UserVo> user;

    public RoleUserVo(){

    }

    public RoleUserVo(RoleVo role, List<UserVo> user){
        this.role = role;
        this.user = user;
    }

}
