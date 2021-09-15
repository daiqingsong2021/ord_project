package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.*;

@Table(name = "wsd_sys_userorg_role")
@Data
public class SysUserOrgRolePo extends BasePo {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "org_id")
    private Integer orgId;

}
