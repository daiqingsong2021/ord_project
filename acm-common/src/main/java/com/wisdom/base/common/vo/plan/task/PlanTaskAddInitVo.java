package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.vo.CalendarVo;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * 新增任务/WBS初始数据
 *
 */
@Data
public class PlanTaskAddInitVo {

    // 默认组织
    private OrgVo org;
    // 默认用户
    private UserVo user;
    // 默认开始时间
    private Date planStartTime;
    // 默认完成时间
    private Date planEndTime;
    // 计划开始最小值
    private Date minPlanStartTime;
    // 计划开始最大值
    private Date maxPlanStartTime;
    // 计划完成最小值
    private Date minPlanEndTime;
    // 计划完成最大值
    private Date maxPlanEndTime;
    //工期类型
    private DictionaryVo taskDrtnType;
    //权重
    private Integer estWt;
    //计划工期
    private Double planDrtn;
    // 日历
    private CalendarVo calendar;
}
