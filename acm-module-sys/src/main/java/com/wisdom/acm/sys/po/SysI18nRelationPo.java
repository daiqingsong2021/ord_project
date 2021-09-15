package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_i18n_relation")
@Data
public class SysI18nRelationPo extends BasePo {

    //关联id
    @Column(name = "i18n_id")
    private Integer i18nId;

    //编码
    @Column(name = "i18n_code")
    private String i18nCode;

    //值
    @Column(name = "i18n_value")
    private String i18nValue;
}
