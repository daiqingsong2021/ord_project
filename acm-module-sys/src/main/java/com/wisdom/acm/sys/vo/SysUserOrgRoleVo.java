package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysUserOrgRoleVo {

    private Integer userId;

    private Integer orgId;

    private Integer roleId;

    private String roleCode;

    private String roleName;
}
