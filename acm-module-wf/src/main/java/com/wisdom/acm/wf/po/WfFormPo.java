package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_wf_form")
public class WfFormPo extends BasePo {

    //流程标题
    @Column(name = "title")
    private String title;

    //流程实例ID
    @Column(name = "proc_inst_id")
    private String procInstId;

    //流程类型代码
    @Column(name = "type_code")
    private String typeCode;

    //备注
    @Column(name = "remark")
    private String remark;

}
