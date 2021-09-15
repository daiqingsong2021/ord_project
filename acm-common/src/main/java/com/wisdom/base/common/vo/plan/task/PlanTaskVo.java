package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

/**
 * task
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Data
public class PlanTaskVo {


    private Integer id;

    //代码
    private String taskCode;

    //名称
    private String taskName;

    //所属项目
    private Integer projectId;

    //所属计划
    private Integer defineId;

    //父节点
    private Integer parentId;

    //责任主体
    private OrgVo org;

    //责任人
    private UserVo user;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //计划类型
    private DictionaryVo planType;

    //计划级别
    private DictionaryVo planLevel;

    //尚需工期
    private Double remainDrtn;

    //实际工期
    private Double actDrtn;

    //计划工期
    private Double planDrtn;

    //实际开始时间
    private Date actStartTime;

    //实际完成时间
    private Date actEndTime;

    //开始时间
    private Date startTime;

    //完成时间
    private Date endTime;

    //编制状态
    private StatusVo status;

    //反馈状态
    private GeneralVo feedbackStatus;

    //完成百分比
    private Double completePct;

    //预计完成时间
    private Date maybeEndTime;

    //备注
    private String remark;

    //任务类型(0WBS,1作业任务，2开始里程碑，3完成里程碑，4资源任务)
    private Integer taskType;

    //总工时
    private Double planQty;

    //工期类型
    private DictionaryVo taskDrtnType;

    //可以反馈(0/1)
    private Integer isFeedback;

    //控制账户
    private Integer controlAccount;

    //权重
    private Integer estWt;

    //最早开始
    private Date earlyStart;

    //最早完成
    private Date earlyEnd;

    //最晚开始
    private Date lateStart;

    //最晚完成
    private Date lateEnd;

    //尚需最早开始
    private Date remainEarlyStart;

    //尚需最早完成
    private Date remainEarlyEnd;

    //尚需最晚开始
    private Date remainLateStart;

    //尚需最晚完成
    private Date remainLateEnd;

    // 标志是否删除
    private Integer del;

    /**
     * 排序编号
     */
    private Integer sort;

    /**
     * 最后更新时间
     */
    private Date lastUpdTime;

    /**
     * 最后更新人
     */
    private UserVo lastUpdUser;

    /**
     * 创建日期
     */
    private Date creatTime;

    /**
     * 创建人
     */
    private UserVo creator;

    // 发布人
    private UserVo releaseUser;

    // 发布时间
    private Date releaseTime;

    // 日历
    private CalendarVo calendar;

    //密级
    private DictionaryVo secutyLevel;

    //限制类型
    private DictionaryVo constraintType;

    //限制时间
    private Date constraintTime;

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
