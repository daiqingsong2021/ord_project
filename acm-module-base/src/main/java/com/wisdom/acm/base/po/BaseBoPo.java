package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
@Table(name = "wsd_base_bo")
@Data
public class BaseBoPo extends BasePo {
    /**
     * 序列号.
     */
    private static final long serialVersionUID = 4L;

    @Column(name = "BO_CODE")
    private String boCode;

    @Column(name = "BO_NAME")
    private String boName;

    @Column(name = "BO_TYPE")
    private String boType;
}
