package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class SysAuthUpdateForm extends BaseForm {

    private Integer id;

    @LogParam(title = "名称")
    private String funcName;

    @LogParam(title = "代码")
    private String funcCode;

    @LogParam(title = "简码")
    private String shortCode;

    @LogParam(title = "激活")
    private Integer del;
}
