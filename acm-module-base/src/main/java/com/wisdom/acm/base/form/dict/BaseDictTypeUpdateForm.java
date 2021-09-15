package com.wisdom.acm.base.form.dict;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseDictTypeUpdateForm extends BaseForm {

    @NotNull(message = "字典码值主键不能为空")
    private Integer id;

    @NotBlank(message = "字典码值代码不能为空")
    @LogParam(title = "代码")
    private String typeCode;

    @NotBlank(message = "字典码值名称不能为空")
    @LogParam(title = "名称")
    private String typeName;

    public String getLogContent(){
        return "";
    }


}
