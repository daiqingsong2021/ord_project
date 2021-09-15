package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProjInfoUpdateForm
{

    @NotNull(message = "主键ID不能为空")
    private Integer id;

    @NotBlank(message = "单位名称不能为空")
    private String orgName;

    private String orgCategory;

    @NotBlank(message = "单位类型不能为空")
    private String orgType;

    private String projUnitName;

    private String corporationer;

    private String leader;

    private String telPhone;

    private String artisan;

    private String projUnitAddress;

    private String remark;
}
