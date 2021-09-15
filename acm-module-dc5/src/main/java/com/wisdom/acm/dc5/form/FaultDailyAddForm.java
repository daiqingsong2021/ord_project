package com.wisdom.acm.dc5.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class FaultDailyAddForm  extends BaseForm {

    /**
     * 日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "日期")
    private Date recordDay;

    /**
     *线路
     */
    @LogParam(title = "线路")
    private Integer line;

    /**
     * 车辆专业故障数
     */
    @LogParam(title = "车辆专业故障数")
    private Integer majorVehicle;

    /**
     * 供电专业故障数
     */
    private Integer majorPower;

    /**
     * 信号专业故障数
     */
    private Integer majorSignal;

    /**
     * 通信专业故障数
     */
    private Integer majorCommunication;

    /**
     * 工建专业故障数
     */
    private Integer majorConstruction;

    /**
     *机电专业故障数
     */
    private Integer majorMechatronics;

    /**
     *AFC专业故障数
     */
    private Integer majorAfc;
    /**
     *其他专业故障数
     */
    private Integer majorOther;

}
