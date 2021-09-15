package com.wisdom.acm.base.form.classify;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseClassifyAssignForm {

    @NotNull(message = "码值不能为空")
    private Integer classifyId;

    @NotNull(message = "业务对象不能为空")
    private String boCode;

    @NotNull(message = "业务主键不能为空")
    private Integer bizId;

    @NotNull(message = "分类码不能为空")
    private Integer classifyTypeId;

}
