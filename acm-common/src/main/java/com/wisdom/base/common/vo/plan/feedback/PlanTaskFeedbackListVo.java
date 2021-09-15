package com.wisdom.base.common.vo.plan.feedback;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/2/25 15:05
 * @Version 1.0
 */
@Data
public class PlanTaskFeedbackListVo {

    //任务id
    private Integer id;

    //反馈id
    private Integer feedbackId;

    //计划定义id
    private Integer defineId;

    //项目id
    private Integer projectId;

    //任务名称
    private String name;

    //任务代码
    private String taskCode;

    //任务类型
    private Integer taskType;

    //计划类型
    private DictionaryVo planType;

    //wbs路径
    private String wbsRoot;

    //批准完成
    private Double approvePct;

    //申请完成
    private Double applyPct;

    //责任主体
    private OrgVo org;

    //责任人
    private UserVo user;

    //日历
    private CalendarVo calendar;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //实际开始时间
    private Date actStartTime;

    //实际完成时间
    private Date actEndTime;

    //进度状态
    private StatusVo progressStatus;

    //节点类型
    private String type;

}
