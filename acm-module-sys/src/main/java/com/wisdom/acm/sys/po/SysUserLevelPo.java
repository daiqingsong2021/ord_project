package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_user")
@Data
public class SysUserLevelPo extends BasePo {

    /**
     * 姓名
     */
    @Column(name = "actu_name")
    private String name;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密级
     */
    @Column(name = "level_")
    private String level;

}
