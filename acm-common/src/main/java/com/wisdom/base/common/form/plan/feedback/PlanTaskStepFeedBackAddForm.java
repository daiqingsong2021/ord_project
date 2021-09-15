package com.wisdom.base.common.form.plan.feedback;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/19 15:02
 * @Version 1.0
 */
@Data
public class PlanTaskStepFeedBackAddForm extends BaseForm {

    private Integer id;

    private Integer feedBackId;

    private Integer projectId;

    private Integer defineId;

    private Integer taskId;

    //计划完成量
    private double planComplete;

    //实际完成量
    private double actComplete;

    //实际开始时间
    private Date actStartTime;

    //实际完成时间
    private Date actEndTime;

    //完成比例
    private String completePct;

}
