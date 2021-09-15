package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_wf_delegate_proc")
public class WfDelegateProcPo extends BasePo {

    //流程代理ID
    @Column(name = "delegate_id")
    private Integer delegateId;

    //流程业务代码
    @Column(name = "biz_type_code")
    private String bizTypeCode;
}
