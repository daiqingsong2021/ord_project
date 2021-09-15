package com.wisdom.base.common.form.plan.define;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class PlanDefineAddForm extends BaseForm {
    //代码
    @LogParam(title = "代码")
    private String planCode;

    //名称
    @LogParam(title = "名称")
    private String planName;

    //所属项目
    private Integer projectId;

    //责任主体
    private Integer orgId;

    //责任人
    private Integer userId;

    //计划类型
    private String planType;

    //计划模板
    private Integer planTempletType;

    //备注
    private String remark;

    private Integer calendarId;

    //计划开始时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    //计划完成时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    //计算工期
    private Double planDrtn;

    //权重
    private Integer estWt;

    //主控计划
    private Integer isMainPlan;

    //来源
    private String source;

    //标段id
    private Integer section;

}
