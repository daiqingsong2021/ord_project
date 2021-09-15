package com.wisdom.base.common.vo.plan.feedback;

import com.wisdom.base.common.vo.StatusVo;
import lombok.Data;

import java.util.Date;

@Data
public class PlanFeedBackVo {
    //反馈id
    private Integer id;

    //任务
    private Integer taskId;

    //计划开始时间
    private Date planStartTime;

    //计划结束时间
    private Date planEndTime;

    //实际开始时间
    private Date actStartTime;

    //实际结束时间
    private Date actEndTime;

    //报告时间
    private Date reportingTime;

    //截止时间
    private Date deadline;

    //估计完成
    private Date estimatedTime;

    //申请完成%
    private Double completePct;

    //进展说明
    private String remark;

    //状态
    private StatusVo status;

    //创建时间
    private  Date creatTime;

}
