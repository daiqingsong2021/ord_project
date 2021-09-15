package com.wisdom.acm.base.form.coderule;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class BaseCoderuleUpdateForm extends BaseForm {

    private Integer id;

    //名称
    @LogParam(title = "名称")
    private String ruleName;

    //默认
    private Integer defaultFlag;

    //能否修改
    private Integer modified;

}
