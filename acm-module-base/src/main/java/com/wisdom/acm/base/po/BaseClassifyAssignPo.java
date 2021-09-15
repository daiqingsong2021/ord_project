package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_classifyassign")
@Data
public class BaseClassifyAssignPo extends BasePo {

    /**
     * 序列号.
     */
    private static final long serialVersionUID = 4L;

    @Column(name = "classify_id")
    private Integer classifyId;

    @Column(name = "bo_code")
    private String boCode;

    @Column(name = "biz_id")
    private Integer bizId;

    @Column(name = "classify_type_id")
    private Integer classifyTypeId;
}
