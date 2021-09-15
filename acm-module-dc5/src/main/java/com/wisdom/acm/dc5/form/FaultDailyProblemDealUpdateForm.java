package com.wisdom.acm.dc5.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FaultDailyProblemDealUpdateForm  extends BaseForm
{

    @NotNull(message = "主键不能为空!")
    private Integer id;

    /**
     *odr_fault_daily_problem表主键
     */
    private Integer problemId;

    /**
     * 问题处理描述
     */
    @LogParam(title = " 问题处理描述")
    private String dealDetail;

}
