package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_useript_role")
@Data
public class SysUserIptRolePo extends BasePo {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "ipt_id")
    private Integer iptId;

    @Column(name = "role_id")
    private Integer roleId;
}
