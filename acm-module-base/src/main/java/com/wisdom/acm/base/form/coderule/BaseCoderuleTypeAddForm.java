package com.wisdom.acm.base.form.coderule;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BaseCoderuleTypeAddForm extends BaseForm {

    //规则id
    private Integer ruleBoId;

    //名称
    @NotBlank(message = "名称不能为空！")
    private String ruleTypeName;

    //SQL
    private String typeSql;

    //列名
    private String columnName;

    //表名
    private String tableName;

    //关联字段
    private String foreignKey;

    //字典type
    private String dictType;

    //字典bo
    private String dictBo;

    //类型
    @NotBlank(message = "类型不能为空！")
    private String attributeType;
}
