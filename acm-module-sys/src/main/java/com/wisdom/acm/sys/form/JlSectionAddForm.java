package com.wisdom.acm.sys.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JlSectionAddForm
{
    @NotNull(message = "施工标ID不能为空")
    private Integer sgbId;

    @NotNull(message = "监理标ID不能为空")
    private Integer jlbId;
}
