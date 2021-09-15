package com.wisdom.acm.dc3.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class EnergyDetailUpdateForm   extends BaseForm
{

    /**
     * 主键ID
     */
    @NotEmpty(message = "详情主键ID")
    private String id;

    /**
     * 耗电量
     */
    @LogParam(title = "耗电量")
    private String powerConsumption;

    /**
     *描述
     */
    @LogParam(title = "描述")
    private String description;

    /**
     *日况id
     */
    @NotEmpty(message = "日况id")
    private String dailyId;
}
