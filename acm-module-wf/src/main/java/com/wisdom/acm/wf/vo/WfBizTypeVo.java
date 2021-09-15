package com.wisdom.acm.wf.vo;

import lombok.Data;

@Data
public class WfBizTypeVo {

    private Integer id;

    //业务代码
    private String typeCode;

    //业务名称
    private String typeName;

    //表单地址
    private String url;

    //流程事件
    private String event;

    //模块代码
    private String moduleCode;
}
