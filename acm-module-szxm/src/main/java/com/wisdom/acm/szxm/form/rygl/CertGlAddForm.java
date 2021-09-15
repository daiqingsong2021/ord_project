package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CertGlAddForm
{
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    private Integer sectionId;

    @NotBlank(message = "证书名称不能为空")
    private String certName;

    private String certVerifyUrl;

    @NotNull(message = "预警天数不能为空")
    private Integer warnPeriod;
}
