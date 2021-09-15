package com.wisdom.acm.base.form.dict;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseDictAddForm extends BaseForm {

    @NotBlank(message = "字典代码不能为空")
    @LogParam(title = "码值")
    private String dictCode;

    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @NotBlank(message = "字典类型代码不能为空")
    private String typeCode;

    // 父节点
    private Integer parentId;

}
