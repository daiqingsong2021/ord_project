package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_wf_formdata")
public class WfFormDataPo extends BasePo {

    //流程标题
    @Column(name = "form_id")
    private Integer formId;

    //业务对象
    @Column(name = "biz_type")
    private String bizType;

    //业务对象ID
    @Column(name = "biz_id")
    private String bizId;

    //通过
    @Column(name = "passed")
    private String passed;

}
