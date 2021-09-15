package com.wisdom.acm.dc2.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "odr_sms_template")
public class SmsTemplatePo extends BasePo {

    //模板标题
    @Column(name = "template_title")
    private String templateTitle;

    //模板内容
    @Column(name = "template_content")
    private String templateContent;

}
