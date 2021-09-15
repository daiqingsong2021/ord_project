package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_dict")
@Data
public class BaseDictPo extends BasePo {

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "DICT_CODE")
    private String dictCode;

    @Column(name = "DICT_NAME")
    private String dictName;

    @Column(name = "TYPE_CODE")
    private String typeCode;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "BUILT_IN")
    private Integer builtIn;
}
