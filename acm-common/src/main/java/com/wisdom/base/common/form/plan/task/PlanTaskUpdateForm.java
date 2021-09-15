package com.wisdom.base.common.form.plan.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * 修改任务表单
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Data
public class PlanTaskUpdateForm extends BaseForm {

    //主键
    private Integer id;

    //代码
    @LogParam(title = "代码")
    private String taskCode;

    //名称
    @LogParam(title = "名称")
    private String taskName;

    //责任主体
    @LogParam(title = "责任主体", type = ParamEnum.ORG)
    private Integer orgId;

    //责任人
    @LogParam(title = "责任人", type = ParamEnum.USER)
    private Integer userId;

    //计划开始时间
    @LogParam(title = "计划开始时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    //计划完成时间
    @LogParam(title = "计划完成时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    //计划类型
    @LogParam(title = "计划类型" , type = ParamEnum.DICT, dictType = "plan.define.plantype")
    private String planType;

    // 任务类型0wbs,1任务作业，2开始里程碑，3完成里程碑，4资源作业
    private Integer taskType;

    //计划级别
    @LogParam(title = "计划级别" , type = ParamEnum.DICT, dictType = "plan.task.planlevel")
    private String planLevel;

    //计划工期
    @LogParam(title = "计划工期")
    private Double planDrtn;

    //备注
    @LogParam(title = "备注")
    private String remark;

    // 总工时
    @LogParam(title = "计划工时")
    private Double planQty;

    //工期类型
    @LogParam(title = "工期类型" , type = ParamEnum.DICT, dictType = "plan.project.taskdrtntype")
    private String taskDrtnType;

    //限制类型
    @LogParam(title = "限制类型" , type = ParamEnum.DICT, dictType = "plan.constraint.type")
    private String constraintType;

    //限制时间
    @LogParam(title = "限制时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date constraintTime;

    //密级
    @LogParam(title = "密级" , type = ParamEnum.DICT, dictType = "comm.secutylevel")
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
