package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_tmpltask")
@Data
public class BaseTmplTaskPo extends BasePo {

    @Column(name = "tmpl_id")
    private Integer tmplId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "task_code")
    private String taskCode;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "plan_drtn")
    private Double planDrtn;

    @Column(name = "plan_qty")
    private Double planQty;

    @Column(name = "plan_type")
    private String planType;

    @Column(name = "plan_level")
    private String planLevel;

    @Column(name = "is_feedback")
    private Integer isFeedback;

    @Column(name = "control_account")
    private Integer controlAccount;

    @Column(name = "task_type")
    private String taskType;

    @Column(name = "drtn_type")
    private String drtnType;

    @Column(name = "remark")
    private String remark;

    @Column(name = "status")
    private String status;
}
