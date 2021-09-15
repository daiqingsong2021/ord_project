package com.wisdom.acm.wf.vo;

import com.wisdom.base.common.vo.wf.WfFormDataVo;
import lombok.Data;

import java.util.List;

@Data
public class WfInstVo {

    private String id;

    //业务代码
    private String typeCode;

    // 表单ID
    private Integer formId;

    //业务名称
    private String typeName;

    //表单地址
    private String url;

    //流程事件
    private String event;

    //模块代码
    private String moduleCode;

    // 业务数据
    private List<WfFormDataVo> formDatas;

}
