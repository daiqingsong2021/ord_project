package com.wisdom.base.common.vo.plan.task;

import lombok.Data;

import java.util.Date;

@Data
public class MyWarningVo {

    private Integer id;

    //项目id
    private Integer projectId;

    //计划定义id
    private Integer defineId;

    //任务名称
    private String taskName;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //计划名称
    private String planName;

    //项目名称
    private String name;

}
