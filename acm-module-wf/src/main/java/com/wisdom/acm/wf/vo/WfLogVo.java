package com.wisdom.acm.wf.vo;

import lombok.Data;

@Data
public class WfLogVo {

    //id
    private Integer id;

    //内容
    private String content;

    //操作类型
    private String optType;

    //待处理人
    private String handleUser;

    //节点
    private String activity;

    //流程实例ID
    private String procInstId;

    //时间
    private String startTime;

    // 处理人名称
    private String user;

    // 时间段间隔
    private String stayTime;


}
