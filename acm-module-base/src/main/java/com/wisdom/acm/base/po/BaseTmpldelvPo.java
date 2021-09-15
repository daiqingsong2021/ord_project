package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_tmpldelv")
@Data
public class BaseTmpldelvPo extends BasePo {

    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "DELV_TITLE")
    private String delvTitle;

    @Column(name = "DELV_NUM")
    private String delvNum;

    @Column(name = "DELV_VERSION")
    private String delvVersion;

    @Column(name = "DELV_TYPE")
    private String delvType;

    @Column(name = "DELV_DESC")
    private String delvDesc;

    @Column(name = "TYPE")
    private String type;

}