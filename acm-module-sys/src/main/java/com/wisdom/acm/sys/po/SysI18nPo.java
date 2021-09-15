package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_i18n")
@Data
public class SysI18nPo extends BasePo {

    //所属模块
    @Column(name = "menu_id")
    private Integer menuId;

    //简码
    @Column(name = "short_code")
    private String shortCode;

    //代码
    @Column(name = "code")
    private String code;
}
