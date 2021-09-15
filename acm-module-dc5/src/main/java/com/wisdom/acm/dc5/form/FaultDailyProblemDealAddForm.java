package com.wisdom.acm.dc5.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class FaultDailyProblemDealAddForm  extends BaseForm
{

    /**
     *odr_fault_daily_problem表主键
     */
    private Integer problemId;

    /**
     * 问题处理描述
     */
    @LogParam(title = "问题处理描述")
    private String dealDetail;

}
