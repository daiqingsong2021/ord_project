package com.wisdom.acm.activiti.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProcWaitWorkVo {

    private Integer id;

    //名称
    private String title;

    //开始时间
    private Date startTime;

    //完成时间
    private Date endTime;

    //当前所属节点
    private String activity;

    //代办人
    private String nextUser;

    //停留时间
    private String stayTime;

    //状态
    private String status;

    //流程实例id
    private String procInstId;

    /**
     * 活动节点id
     */
    private String actId;

}
