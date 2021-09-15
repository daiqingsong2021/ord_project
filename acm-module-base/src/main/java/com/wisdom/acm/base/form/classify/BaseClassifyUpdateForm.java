package com.wisdom.acm.base.form.classify;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseClassifyUpdateForm extends BaseForm {

    @NotNull(message = "分类码主键不能为空")
    private Integer id;

    @NotNull(message = "分类码不能为空")
    @LogParam(title = "码值")
    private String classifyCode;

    @NotNull(message = "分类码说明不能为空")
    @LogParam(title = "说明")
    private String classifyName;
}
