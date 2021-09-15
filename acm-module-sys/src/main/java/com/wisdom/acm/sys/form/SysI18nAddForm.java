package com.wisdom.acm.sys.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SysI18nAddForm extends BaseForm {
    //模块id
    private Integer menuId;

    //简码
    @NotNull(message = "简码不能为空！")
    private String shortCode;

    //代码
    private String code;

    //语种
    private List<I18nRelationForm> i18nRelationForms;

    public String getLogContent(){return "新增国际化，国际化代码："+ this.getCode();}
}
