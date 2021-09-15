package com.wisdom.acm.base.form.tmpltask;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmplTaskPredUpdateForm extends BaseForm {

    private Integer id;

    @NotNull(message = "逻辑关系不能为空")
    @LogParam(title = "关系类型")
    private String relationType;

    @NotNull(message = "延迟不能为空")
    @LogParam(title = "延迟")
    private Double lagQty;
}
