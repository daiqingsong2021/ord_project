package com.wisdom.base.common.vo.wf;

import lombok.Data;

import java.util.Date;

@Data
public class WfProcLogVo {


    private Integer id;

    //名称
    private String name;

    //创建时间
    private String creatTime;

    //开始时间
    private String startTime;

    //完成时间
    private String endTime;

    //当前所属节点
    private String activity;

    //代办人
    private String newUser;

    //停留时间
    private String stayTime;

    //状态
    private String status;

    //流程实例id
    private String procInstId;

}
