package com.wisdom.acm.base.form.classify;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseClassifyAddForm extends BaseForm {

    private Integer parentId;

    @NotNull(message = "业务对象不能为空")
    private String boCode;

    @NotNull(message = "分类码不能为空")
    private String classifyCode;

    // 说明
    private String classifyName;

    // 1 分类码，2码值
    private Integer classifyType;

}
