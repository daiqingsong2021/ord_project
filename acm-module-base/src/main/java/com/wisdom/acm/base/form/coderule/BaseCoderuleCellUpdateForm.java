package com.wisdom.acm.base.form.coderule;

import lombok.Data;

@Data
public class BaseCoderuleCellUpdateForm {

    private Integer id;

    //wsd_base_coderule_type的id
    private Integer ruleTypeId;

    //名称
    private String ruleCellName;

    //类型
    private String ruleType;

    //值
    private String cellValue;

    //固定长度
    private Integer maxLength;

    //填充字符
    private String fillChar;

    //流水号最小值
    private Integer seqMinValue;

    //流水号步长
    private Integer seqSteep;

    //连接符
    private String connector;
}
