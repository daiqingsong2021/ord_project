package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_func")
@Data
public class SysFuncPo extends BasePo {

    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "func_name")
    private String funcName;

    @Column(name = "func_code")
    private String funcCode;

    @Column(name = "remark")
    private String remark;

    @Column(name = "del")
    private Integer del;

    @Column(name = "short_code")
    private String shortCode;

}
