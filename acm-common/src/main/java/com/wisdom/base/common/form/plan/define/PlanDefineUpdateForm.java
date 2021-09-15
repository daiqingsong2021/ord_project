package com.wisdom.base.common.form.plan.define;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class PlanDefineUpdateForm extends BaseForm {

    private Integer id;

    //代码
    @LogParam(title = "代码")
    private String planCode;

    //名称
    @LogParam(title = "名称")
    private String planName;

    //责任主体
    @LogParam(title = "责任主体", type = ParamEnum.ORG)
    private Integer orgId;

    //责任人
    @LogParam(title = "责任主体", type = ParamEnum.USER)
    private Integer userId;

    //计划类型
    @LogParam(title = "计划类型" , type = ParamEnum.DICT, dictType = "plan.define.plantype")
    private String planType;

    //备注
    @LogParam(title = "备注")
    private String remark;

    //计划开始时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @LogParam(title = "计划开始时间")
    private Date planStartTime;

    //计划完成时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @LogParam(title = "计划完成时间")
    private Date planEndTime;

    //计划工期
    @LogParam(title = "计划工期")
    private Double planDrtn;

    //权重
    @LogParam(title = "权重", type = ParamEnum.OTHER)
    private Integer estWt;

    //主控计划
    @LogParam(title = "主控开关", type = ParamEnum.OTHER)
    private Integer isMainPlan;

    //是否激活
    @LogParam(title = "状态", type = ParamEnum.OTHER)
    private String status;

    //日历
    @LogParam(title = "日历",type = ParamEnum.CALENDAR)
    private Integer calendarId;

    //标段id
    private Integer section;
}
