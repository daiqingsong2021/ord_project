package com.wisdom.base.common.form.plan.change;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/3/6 10:40
 * @Version 1.0
 */
@Data
public class PlanTaskChangeInfoForm extends BaseForm {

    //变更id
    private Integer id;

    //任务id
    private Integer taskId;

    //父级类型
    private String parentType;

    //变更前名称
    private String oldName;

    //变更后名称
    @LogParam(title = "名称")
    private String newName;

    //变更前责任主体
    @LogParam(title = "责任主体", type = ParamEnum.ORG)
    private Integer oldOrgId;

    //变更后责任主体
    @LogParam(title = "责任主体", type = ParamEnum.ORG)
    private Integer newOrgId;

    //变更前责任人
    @LogParam(title = "责任人", type = ParamEnum.USER)
    private Integer oldUserId;

    //变更后责任人
    @LogParam(title = "责任人", type = ParamEnum.USER)
    private Integer newUserId;

    @LogParam(title = "日历",type = ParamEnum.CALENDAR)
    private Integer newCalendarId;

    @LogParam(title = "日历",type = ParamEnum.CALENDAR)
    private Integer oldCalendarId;

    //变更前计划开始时间
    @LogParam(title = "计划开始时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date oldPlanStartTime;

    //变更后计划开始时间
    @LogParam(title = "计划开始时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newPlanStartTime;

    //变更前计划完成时间
    @LogParam(title = "计划结束时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date oldPlanEndTime;

    //变更后计划完成时间
    @LogParam(title = "计划结束时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newPlanEndTime;

    //变更前工期
    @LogParam(title = "工期")
    private Double oldPlanDrtn;

    //变更后工期
    @LogParam(title = "工期")
    private Double newPlanDrtn;

    //变更前工时
    @LogParam(title = "工时")
    private Double oldPlanQty;

    //变更后工时
    @LogParam(title = "工时")
    private Double newPlanQty;

    //变更前计划级别
    @LogParam(title = "计划级别" , type = ParamEnum.DICT, dictType = "plan.task.planlevel")
    private String oldPlanLevel;

    //变更后计划级别
    @LogParam(title = "计划级别" , type = ParamEnum.DICT, dictType = "plan.task.planlevel")
    private String newPlanLevel;

    //变更前计划类型
    @LogParam(title = "计划类型",type = ParamEnum.DICT,dictType = "plan.define.plantype")
    private String oldPlanType;

    //变更后计划类型
    @LogParam(title = "计划类型",type = ParamEnum.DICT,dictType = "plan.define.plantype")
    private String newPlanType;

    //变更前工期类型
    @LogParam(title = "工期类型",type = ParamEnum.DICT,dictType = "plan.project.taskdrtntype")
    private String oldTaskDrtnType;

    //变更后工期类型
    @LogParam(title = "工期类型",type = ParamEnum.DICT,dictType = "plan.project.taskdrtntype")
    private String newTaskDrtnType;

    //变更前作业类型
    private Integer oldTaskType;

    //变更后作业类型
    private Integer newTaskType;

    //变更前描述
    @LogParam(title = "描述")
    private String oldDesc;

    //变更后描述
    @LogParam(title = "描述")
    private String newDesc;

}
