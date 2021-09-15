package com.wisdom.acm.base.form.custom;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseCustomFieldSaveForm extends BaseForm {

    @NotNull(message = "表名不能为空")
    @LogParam(title = "表名")
    private String tableName;

    @NotNull(message = "显示名称不能为空")
    @LogParam(title = "显示名称")
    private String title;

    @LogParam(title = "国际化")
    private String i18nCode;

    @NotBlank(message = "字段名称不能为空")
    @LogParam(title = "字段名称")
    private String fieldName;

    @NotBlank(message = "数据类型不能为空")
    @LogParam(title = "数据类型")
    private String dataType;

    @NotBlank(message = "表单类型不能为空")
    @LogParam(title = "表单类型")
    private String formType;

    @LogParam(title = "必填")
    private Integer required;

    @LogParam(title = "最大长度")
    private Integer maxLength;

    @LogParam(title = "精度")
    private Integer precision;

    @LogParam(title = "格化式字符串")
    private String formatter;

    @LogParam(title = "字典")
    private String dictType;

    @LogParam(title = "横跨的列数")
    private Integer colspan;

    @LogParam(title = "横跨的行数")
    private Integer rowspan;

    @LogParam(title = "启用")
    private Integer enable;

    @LogParam(title = "排序号")
    private Integer sort;

    public String getLogContent(){
        return "";
    }

}
