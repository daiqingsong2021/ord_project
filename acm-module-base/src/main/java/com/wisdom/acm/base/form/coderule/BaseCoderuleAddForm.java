package com.wisdom.acm.base.form.coderule;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class BaseCoderuleAddForm extends BaseForm {

    //boId
    private Integer ruleBoId;

    //名称
    private String ruleName;

    //默认
    private Integer defaultFlag;

    //能否修改
    private Integer modified;

}
