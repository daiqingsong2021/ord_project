package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_classify")
@Data
public class BaseClassifyPo extends BasePo {

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "BO_CODE")
    private String boCode;

    @Column(name = "CLASSIFY_CODE")
    private String classifyCode;
    // 分类码类型：1分类码，2码值。
    @Column(name = "CLASSIFY_TYPE")
    private Integer classifyType;

    @Column(name = "classify_name")
    private String classifyName;
}
