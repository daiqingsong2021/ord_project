package com.wisdom.acm.base.form.classify;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseUpdateClassifyAssignForm extends BaseForm {

    private Integer id;

    @NotNull(message = "码值不能为空")
    @LogParam(title = "码值", type = ParamEnum.OTHER)
    private Integer classifyId;
}
