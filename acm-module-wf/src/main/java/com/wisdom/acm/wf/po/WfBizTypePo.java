package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_wf_biztype")
public class WfBizTypePo extends BasePo{

    //业务代码
    @Column(name = "type_code")
    private String typeCode;

    //业务名称
    @Column(name = "type_name")
    private String typeName;

    //表单地址
    @Column(name = "url")
    private String url;

    //流程事件
    @Column(name = "event")
    private String event;

    //模块代码
    @Column(name = "module_code")
    private String moduleCode;


}
