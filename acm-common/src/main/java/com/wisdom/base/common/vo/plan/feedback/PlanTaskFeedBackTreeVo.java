package com.wisdom.base.common.vo.plan.feedback;

import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.base.CalendarVo;
import com.wisdom.base.common.vo.sys.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/2/26 10:45
 * @Version 1.0
 */
@Data
public class PlanTaskFeedBackTreeVo extends TreeVo<PlanTaskFeedBackTreeVo> {

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

    //批准完成
    private Double approvePct;

    //申请完成
    private Double applyPct;

    //任务类型
    private Integer taskType;

    //计划类型
    private com.wisdom.base.common.vo.base.DictionaryVo planType;

    //wbs路径
    private String wbsRoot;

    //责任主体
    private com.wisdom.base.common.vo.sys.OrgVo org;

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

    //反馈状态
    private StatusVo progressStatus;

    //节点类型
    private String type;

    //报告时间
    private Date reportingTime;

    //进展说明
    private String remark;

    private Integer auth;
    /**
     * 是否需要反馈 true:是/false:否
     */
    private Boolean isFeedback = false;

    /**
     * 扩展字段
     */
    private String custom01;

    /**
     * 扩展字段
     */
    private String custom02;

    /**
     * 扩展字段
     */
    private String custom03;

    /**
     * 扩展字段
     */
    private String custom04;

    /**
     * 扩展字段
     */
    private String custom05;

    /**
     * 扩展字段
     */
    private String custom06;

    /**
     * 扩展字段
     */
    private String custom07;

    /**
     * 扩展字段
     */
    private String custom08;

    /**
     * 扩展字段
     */
    private String custom09;

    /**
     * 扩展字段
     */
    private String custom10;
}
