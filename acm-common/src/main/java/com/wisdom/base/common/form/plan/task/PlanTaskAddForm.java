package com.wisdom.base.common.form.plan.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * 增加任务表单
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Data
public class PlanTaskAddForm extends BaseForm {

    //代码
    private String taskCode;

    //名称
    @LogParam(title = "名称")
    private String taskName;

    //所属项目
    private Integer projectId;

    //所属计划
    private Integer defineId;

    //父节点
    private Integer parentId;

    //责任主体
    private Integer orgId;

    //责任人
    private Integer userId;

    private Integer calendarId;

    //计划开始时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    //计划完成时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    //计划类型
    private String planType;

    //计划级别
    private String planLevel;

    //计划工期
    private Double planDrtn;

    //备注
    private String remark;

    //任务类型(0任务，1开始里程碑，2完成里程碑，3WBS)
    private Integer taskType;

    //总工时
    private Double planQty;

    //工期类型
    private String taskDrtnType;

    //编制状态
    private String status;

    //密级
    private String secutyLevel;

    //权重
    private Double estWt;

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
