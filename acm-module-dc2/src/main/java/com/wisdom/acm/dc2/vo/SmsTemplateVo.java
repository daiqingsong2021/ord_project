package com.wisdom.acm.dc2.vo;

import lombok.Data;

@Data
public class SmsTemplateVo {

    //主键
    private Integer id;

    //模板标题
    private String templateTitle;

    //模板内容
    private String templateContent;

}
