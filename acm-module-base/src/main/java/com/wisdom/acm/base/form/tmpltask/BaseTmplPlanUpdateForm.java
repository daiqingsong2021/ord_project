package com.wisdom.acm.base.form.tmpltask;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmplPlanUpdateForm extends BaseForm {

    private Integer id;

   // @NotNull(message = "代码不能为空")
   // private String tmplCode;

    @NotNull(message = "名称不能为空")
    @LogParam(title = "名称")
    private String tmplName;

    @NotNull(message = "是否全局不能为空")
    @LogParam(title = "是否全局")
    private Integer isGlobal;

   // private String status;
}
