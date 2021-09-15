package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_tmpldelv_type")
@Data
public class BaseTmpldelvTypePo extends BasePo {

    @Column(name = "TYPE_TITLE")
    private String typeTitle;

    @Column(name = "TYPE_NUM")
    private String typeNum;

    @Column(name = "TYPE_VERSION")
    private String typeVersion;

    @Column(name = "TYPE_TYPE")
    private String typeType;

    @Column(name = "TYPE_DESC")
    private String typeDesc;

}