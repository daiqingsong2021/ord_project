package com.wisdom.acm.dc5.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FaultDailyProblemUpdateForm extends BaseForm
{

    @NotNull(message = "主键不能为空!")
    private Integer id;

    /**
     *odr_fault_daily表主键
     */
    private Integer faultId;

    /**
     * 问题描述
     */
    @LogParam(title = " 问题描述")
    private String problemDesc;

    /**
     *问题状态
     */
    @LogParam(title = " 问题状态")
    private String problemStatus;

    private String problemReason;

    /**
     * 备注
     */
    private String remark;
    /**
     * 处理详情
     */
    @LogParam(title = " 问题处理描述")
    private String dealDetail;
}
