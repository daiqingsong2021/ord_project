package com.wisdom.acm.base.form.coderule;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BaseCoderuleBoUpdateForm extends BaseForm {

    private Integer id;

    //代码
    @NotBlank(message = "代码不能为空！")
    @LogParam(title = "代码")
    private String boCode;

    //名称
    @NotBlank(message = "名称不能为空")
    @LogParam(title = "名称")
    private String boName;

    //表名
    @NotBlank(message = "表名不能为空！")
    @LogParam(title = "表名")
    private String tableName;

    //代码列名
    @NotBlank(message = "代码字段不能为空！")
    @LogParam(title = "代码字段")
    private String codeColumnName;

    //seqScope
    @NotBlank(message = "流水号范围不能为空！")
    @LogParam(title = "流水号范围")
    private String seqScope;

    //被分配字段
    @NotBlank(message = "被分配字段不能为空！")
    @LogParam(title = "被分配字段")
    private String assignColumnName;
}
