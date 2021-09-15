package com.wisdom.acm.base.form.dict;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseDictUpdateForm extends BaseForm {

    @NotNull(message = "数字类型主键不能为空")
    private Integer id;

    @NotBlank(message = "数字类型代码不能为空")
    @LogParam(title = "字典码值代码")
    private String dictCode;

    @NotBlank(message = "数字类型名称不能为空")
    @LogParam(title = "字典码值名称")
    private String dictName;


}
