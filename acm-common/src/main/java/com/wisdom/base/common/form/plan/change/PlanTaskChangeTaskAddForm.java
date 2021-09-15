package com.wisdom.base.common.form.plan.change;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/4/10 9:30
 * @Version 1.0
 */
@Data
public class PlanTaskChangeTaskAddForm extends BaseForm {

    //所属项目
    private Integer projectId;

    //所属计划
    private Integer defineId;

    //父节点
    private Integer parentId;

    //父级类型
    private String parentType;

    //任务类型
    private String taskType;

    //变更后代码
    @LogParam(title = "代码")
    private String newCode;

    //变更后名称
    @LogParam(title = "名称")
    private String newName;

    //变更后责任主体
    private Integer newOrgId;

    //变更后责任人
    private Integer newUserId;

    //变更后日历
    private Integer newCalendarId;

    //变更后计划开始时间
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newPlanStartTime;

    //变更后计划完成时间
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newPlanEndTime;

    //变更后工期
    private Double newPlanDrtn;

    //变更后计划工时
    private Double newPlanQty;

    //变更后计划类型
    private String newPlanType;

    //变更后描述
    private String newDesc;

    //变更后计划级别
    private String newPlanLevel;

    //变更说明
    private String changeDesc;

    //变更后工期类型
    private String newTaskDrtnType;

    //作业类型
    private Integer newTaskType;
}
