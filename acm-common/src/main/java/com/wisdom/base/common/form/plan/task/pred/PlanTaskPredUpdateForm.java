package com.wisdom.base.common.form.plan.task.pred;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

/**
 * 逻辑关系修改
 */
@Data
public class PlanTaskPredUpdateForm extends BaseForm {

    private Integer id;

    // 关系
    @LogParam(title = "关系类型")
    private String relationType;

    // 延时
    @LogParam(title = "延迟")
    private Double lagNum;

    //实际开始时间
    private Date actStartTime;

    //实际结束时间
    private Date actEndTime;

    //进展说明
    private String remark;

    //申请完成百分比
    private double completePct;
}
