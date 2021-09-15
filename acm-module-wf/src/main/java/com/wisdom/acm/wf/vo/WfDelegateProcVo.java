package com.wisdom.acm.wf.vo;

import lombok.Data;

@Data
public class WfDelegateProcVo {

    private Integer id;

    //流程业务ID
    private Integer typeId;

    //业务代码
    private String typeCode;

    //业务名称
    private String typeName;

    //流程时间
    private String event;

    //模块代码
    private String moduleCode;

    //表单地址
    private String url;

}
