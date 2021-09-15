package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysUserIptRoleVo {

    private Integer userId;

    private Integer iptId;

    private Integer roleId;

    private String roleName;
}
