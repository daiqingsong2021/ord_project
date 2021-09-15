package com.wisdom.acm.dc5.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FaultDailyUpdateForm extends BaseForm
{

    @NotNull(message = "主键不能为空!")
    private Integer id;

    /**
     * 车辆专业故障数
     */
    @LogParam(title = "车辆专业故障数")
    private String majorVehicle;

    /**
     * 供电专业故障数
     */
    @LogParam(title = "供电专业故障数")
    private String majorPower;

    /**
     * 信号专业故障数
     */
    @LogParam(title = "信号专业故障数")
    private String majorSignal;

    /**
     * 通信专业故障数
     */
    @LogParam(title = "通信专业故障数")
    private String majorCommunication;

    /**
     * 工建专业故障数
     */
    @LogParam(title = "工建专业故障数")
    private String majorConstruction;

    /**
     *机电专业故障数
     */
    @LogParam(title = "机电专业故障数")
    private String majorMechatronics;

    /**
     *AFC专业故障数
     */
    @LogParam(title = "AFC专业故障数")
    private String majorAfc;
    /**
     *其他专业故障数
     */
    @LogParam(title = "其他专业故障数")
    private String majorOther;

}
