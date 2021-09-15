package com.wisdom.acm.sys.po;


import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.*;

@Table(name = "wsd_sys_userorg")
@Data
public class SysUserOrgPo extends BasePo {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "org_id")
    private Integer orgId;

    @Column(name = "main_org")
    private Integer mainOrg;

    @Column(name = "position")
    private String position;

    @Column(name = "professional")
    private String professional;

}
