package com.wisdom.base.common.form;

import com.wisdom.base.common.form.custom.CustomFieldForm;
import lombok.Data;

@Data
public class BaseForm {

    // 日志内容
    private String logContent;

    // 自定义
    private CustomFieldForm custom;
}
