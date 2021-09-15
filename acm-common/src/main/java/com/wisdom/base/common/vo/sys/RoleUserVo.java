package com.wisdom.base.common.vo.sys;

import com.wisdom.base.common.vo.sys.RoleVo;
import com.wisdom.base.common.vo.sys.UserVo;
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
