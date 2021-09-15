package com.wisdom.base.common.form.plan.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
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
public class PlanWbsUpdateForm extends BaseForm {

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

    //计划级别
    @LogParam(title = "计划级别" , type = ParamEnum.DICT, dictType = "plan.task.planlevel")
    private String planLevel;

    //计划工期
    @LogParam(title = "计划工期")
    private Double planDrtn;

    //备注
    @LogParam(title = "备注")
    private String remark;

    //总工时
    @LogParam(title = "计划工时")
    private Double planQty;

    //可以反馈(0/1)
    @LogParam(title = "WBS反馈")
    private Integer isFeedback;

    //控制账户
    @LogParam(title = "控制账户")
    private Integer controlAccount;

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
