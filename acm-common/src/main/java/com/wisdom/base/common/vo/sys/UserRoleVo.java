package com.wisdom.base.common.vo.sys;

import lombok.Data;

import java.util.List;

/**
 * 用户角色
 */
@Data
public class UserRoleVo {

    private UserVo user;

    private List<RoleVo> role;

    public UserRoleVo(){

    }

    public UserRoleVo(UserVo user, List<RoleVo> role){
        this.user = user;
        this.role = role;
    }

}
