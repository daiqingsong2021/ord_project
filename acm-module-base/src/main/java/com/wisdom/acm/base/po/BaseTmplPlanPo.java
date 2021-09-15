package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_base_tmplplan")
@Data
public class BaseTmplPlanPo extends BasePo {

    @Column(name = "TMPL_CODE")
    private String tmplCode;

    @Column(name = "TMPL_NAME")
    private String tmplName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "IS_GLOBAL")
    private Integer isGlobal;

}