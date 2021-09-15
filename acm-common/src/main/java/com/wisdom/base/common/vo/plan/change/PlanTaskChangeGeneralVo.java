package com.wisdom.base.common.vo.plan.change;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/3/1 15:09
 * @Version 1.0
 */
@Data
public class PlanTaskChangeGeneralVo {

    //变更id
    private Integer id;
    //任务id
    private Integer taskId;
    //父级类型
    private String parentType;
    //变更前名称
    private String oldName;
    //变更后名称
    private String newName;
    //变更前责任主体
    private OrgVo oldOrg;
    //变更后责任主体
    private OrgVo newOrg;
    //变更前责任人
    private UserVo oldUser;
    //变更后责任人
    private UserVo newUser;
    //变更前日期
    private CalendarVo oldCalendar;
    //变更后日期
    private CalendarVo newCalendar;
    //变更前计划开始时间
    private Date oldPlanStartTime;
    //变更后计划开始时间
    private Date newPlanStartTime;
    //变更前计划完成时间
    private Date oldPlanEndTime;
    //变更后计划完成时间
    private Date newPlanEndTime;
    //变更前工期
    private Double oldPlanDrtn;
    //变更后工期
    private Double newPlanDrtn;
    //变更前计划工时
    private Double oldPlanQty;
    //变更后计划工时
    private Double newPlanQty;
    //变更前计划级别
    private DictionaryVo oldPlanLevel;
    //变更后计划级别
    private DictionaryVo newPlanLevel;
    //变更前计划类型
    private DictionaryVo oldPlanType;
    //变更后计划类型
    private DictionaryVo newPlanType;
    //变更后作业类型
    private Integer oldTaskType;
    //变更前作业类型
    private Integer newTaskType;
    //变更前工期类型
    private DictionaryVo oldTaskDrtnType;
    //变更后工期类型
    private DictionaryVo newTaskDrtnType;
    //变更前wbs反馈
    private Integer oldWbsFeedBack;
    //变更后wbs反馈
    private Integer newWbsFeedBack;
    //变更前控制账户
    private Integer oldControlAccount;
    //变更后控制账户
    private Integer newControlAccount;
    //变更前描述
    private String oldDesc;
    //变更后描述
    private String newDesc;
    //变更类型
    private StatusVo changeType;
    //变更状态
    private StatusVo status;
    //创建人
    private UserVo creator;
   //创建日期
    private Date creatTime;
    //发布人
    private UserVo aprvUser;
    //发布日期
    private Date aprvTime;
}
