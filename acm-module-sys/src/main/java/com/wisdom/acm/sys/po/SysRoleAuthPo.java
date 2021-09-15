package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;

@Table (name = "wsd_sys_roleauth")
@Data
public class SysRoleAuthPo extends BasePo {

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "res_code")
    private String resCode;

    @Column(name = "res_type")
    private String resType;

}
