package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_dict_type")
@Data
public class BaseDictTypePo extends BasePo {

    /**
     * 序列号.
     */
    private static final long serialVersionUID = 4L;

    @Column(name = "BO_CODE")
    private String boCode;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Column(name = "TYPE_CODE")
    private String typeCode;

    @Column(name = "BUILT_IN")
    private Integer builtIn;

}
