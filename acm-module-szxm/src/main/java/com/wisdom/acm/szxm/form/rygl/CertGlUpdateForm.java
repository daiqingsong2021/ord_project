package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CertGlUpdateForm
{
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    @NotBlank(message = "证书名称不能为空")
    private String certName;

    private String certVerifyUrl;

    @NotNull(message = "预警天数不能为空")
    private Integer warnPeriod;
}
