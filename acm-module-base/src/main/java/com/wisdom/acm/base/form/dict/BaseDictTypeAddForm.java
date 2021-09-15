package com.wisdom.acm.base.form.dict;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseDictTypeAddForm extends BaseForm {

    @NotNull(message = "业务对象不能为空")
    private String boCode;

    @NotBlank(message = "字典码值代码不能为空")
    private String typeCode;

    @NotBlank(message = "字典码值名称不能为空")
    private String typeName;

    public String getLogContent(){
        return "增加数据字典，数据字典名称："+ this.getTypeName();
    }
}
