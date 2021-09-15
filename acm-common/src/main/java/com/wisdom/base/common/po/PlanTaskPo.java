package com.wisdom.base.common.po;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import com.wisdom.base.common.po.BasePo;


/**
 * task
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Table(name = "wsd_plan_task")
@Data
public class PlanTaskPo extends BasePo {

    /**
     * 代码
     */
    @Column(name = "task_code")
    private String taskCode;

    /**
     * 名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 任务类型(0WBS，1=普通任务，2开始里程碑，3完成里程碑，4=资源任务)
     */
    @Column(name = "task_type")
    private Integer taskType;

    /**
     * 所属项目
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 所属计划
     */
    @Column(name = "define_id")
    private Integer defineId;

    /**
     * 父节点
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 责任主体
     */
    @Column(name = "org_id")
    private Integer orgId;

    /**
     * 责任人
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 日历
     */
    @Column(name = "calendar_id")
    private Integer calendarId;

    /**
     * 期望开始时间
     */
    @Column(name = "target_start_time")
    private Date targetStartTime;

    /**
     * 期望计划完成时间
     */
    @Column(name = "target_end_time")
    private Date targetEndTime;

    /**
     * 计划开始时间
     */
    @Column(name = "plan_start_time")
    private Date planStartTime;

    /**
     * 计划完成时间
     */
    @Column(name = "plan_end_time")
    private Date planEndTime;

    /**
     * 最早开始
     */
    @Column(name = "early_start")
    private Date earlyStart;

    /**
     * 最早完成
     */
    @Column(name = "early_end")
    private Date earlyEnd;

    /**
     * 最晚开始
     */
    @Column(name = "late_start")
    private Date lateStart;

    /**
     * 最晚完成
     */
    @Column(name = "late_end")
    private Date lateEnd;

    /**
     * 尚需最早开始
     */
    @Column(name = "remain_early_start")
    private Date remainEarlyStart;

    /**
     * 尚需最早完成
     */
    @Column(name = "remain_early_end")
    private Date remainEarlyEnd;

    /**
     * 尚需最晚开始
     */
    @Column(name = "remain_late_start")
    private Date remainLateStart;

    /**
     * 尚需最晚完成
     */
    @Column(name = "remain_late_end")
    private Date remainLateEnd;

    /**
     * 实际开始时间
     */
    @Column(name = "act_start_time")
    private Date actStartTime;

    /**
     * 实际完成时间
     */
    @Column(name = "act_end_time")
    private Date actEndTime;

    /**
     * 反馈截止日期 .
     */
    @Column(name = "feedback_time")
    private Date feedbackTime;

    /**
     * 预计完成时间
     */
    @Column(name = "maybe_end_time")
    private Date maybeEndTime;

    /**
     * 计划工期
     */
    @Column(name = "plan_drtn")
    private Double planDrtn;

    /**
     * 实际工期
     */
    @Column(name = "act_drtn")
    private Double actDrtn;

    /**
     * 尚需工期
     */
    @Column(name = "remain_drtn")
    private Double remainDrtn;

    /**
     * 完成百分比
     */
    @Column(name = "complete_pct")
    private Double completePct;

    /**
     * 总浮时(汇总)
     */
    @Column(name = "total_float")
    private Double totalFloat;

    /**
     * 自由浮时(汇总)
     */
    @Column(name = "free_float")
    private Double freeFloat;

    /**
     * 尚需浮时(汇总)
     */
    @Column(name = "remain_float")
    private Double remainFloat;

    /**
     * 权重
     */
    @Column(name = "est_wt")
    private Double estWt;

    /**
     * 总工时(计划数量)
     */
    @Column(name = "plan_qty")
    private Double planQty;

    /**
     * 关键路径
     */
    @Column(name = "critical")
    private Integer critical;

    /**
     * 编制状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 限制类型
     */
    @Column(name = "constraint_type")
    private String constraintType;

    /**
     * 限制时间
     */
    @Column(name = "constraint_time")
    private Date constraintTime;

    /**
     * 计划类型
     */
    @Column(name = "plan_type")
    private String planType;

    /**
     * 计划级别
     */
    @Column(name = "plan_level")
    private String planLevel;

    /**
     * 反馈状态
     */
    @Column(name = "feedback_status")
    private Integer feedbackStatus;

    /**
     * 工期类型
     */
    @Column(name = "task_drtn_type")
    private String taskDrtnType;

    /**
     * 可以反馈(0/1)
     */
    @Column(name = "is_feedback")
    private Integer isFeedback;

    /**
     * 控制账户
     */
    @Column(name = "control_account")
    private Integer controlAccount;

    /**
     * 确认人
     */
    @Column(name = "confirm_user")
    private Integer confirmUser;

    /**
     * 确认时间
     */
    @Column(name = "confirm_time")
    private Date confirmTime;

    /**
     * 发布人
     */
    @Column(name = "release_user")
    private Integer releaseUser;

    /**
     * 发布时间
     */
    @Column(name = "release_time")
    private Date releaseTime;

    /**
     * id路径
     */
    @Column(name = "id_paths")
    private String idPaths;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建OBS
     */
    @Column(name = "creat_org")
    private Integer creatOrg;

    /**
     * 基线计划开始时间
     */
    @Column(name = "bl_plan_start_time")
    private Date blPlanStartTime;

    /**
     * 基线计划完成时间
     */
    @Column(name = "bl_plan_end_time")
    private Date blPlanEndTime;

    /**
     * 基线计划工期
     */
    @Column(name = "bl_plan_drtn")
    private Double blPlanDrtn;

    /**
     * 标志是否删除
     */
    @Column(name = "del")
    private Integer del;
    /**
     * 删除日期
     */
    @Column(name = "del_time")
    private Date delTime;

    /**
     * 密级
     */
    @Column(name = "secuty_level")
    private String secutyLevel;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_01")
    private String custom01;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_02")
    private String custom02;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_03")
    private String custom03;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_04")
    private String custom04;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_05")
    private String custom05;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_06")
    private String custom06;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_07")
    private String custom07;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_08")
    private String custom08;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_09")
    private String custom09;

    /**
     * 扩展字段
     */
    @Column(name = "CUSTOM_10")
    private String custom10;
}
