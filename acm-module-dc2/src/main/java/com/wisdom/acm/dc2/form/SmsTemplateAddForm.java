package com.wisdom.acm.dc2.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SmsTemplateAddForm {

    //模板标题
    @NotBlank(message = "模板标题为空")
    private String templateTitle;

    //模板内容
    @NotBlank(message = "模板内容为空")
    private String templateContent;
}
