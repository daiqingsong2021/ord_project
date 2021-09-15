package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_set")
@Data
public class BaseSetPo extends BasePo {

    @Column(name = "bo_code")
    private String boCode;

    @Column(name = "bs_key")
    private String bsKey;

    @Column(name = "bs_value")
    private String bsValue;
}
