package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class MyTaskVo {

    private Integer id;

    //项目id
    private Integer projectId;

    //计划id
    private Integer defineId;

    //任务名称
    private String taskName;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //完成百分比
    private Double completePct;

    //计划时间
    private String planName;

    //项目名称
    private String name;

    //偏差
    private String deviation;

    //偏差天数
    private Integer deviationDays;

    //反馈状态
    private GeneralVo feedbackStatus;

}
