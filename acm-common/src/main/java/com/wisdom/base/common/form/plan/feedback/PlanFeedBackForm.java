package com.wisdom.base.common.form.plan.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class PlanFeedBackForm extends BaseForm {

    //任务id
    private Integer taskId;

    //实际开始时间
    @LogParam(title = "实际开始时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date actStartTime;

    //实际结束时间
    @LogParam(title = "实际结束时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date actEndTime;

    //报告时间
    @LogParam(title = "报告时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date reportingTime;

    //截止时间
    @LogParam(title = "截止时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    //估计完成
    @LogParam(title = "估计完成")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date estimatedTime;

    //申请完成%
    @LogParam(title = "申请完成%")
    private Double completePct;

    //进展说明
    @LogParam(title = "进展说明")
    private String remark;

}
