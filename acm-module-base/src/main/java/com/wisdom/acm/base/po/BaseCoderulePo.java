package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_base_coderule")
public class BaseCoderulePo extends BasePo {

    //规则id
    @Column(name = "rule_bo_id")
    private Integer ruleBoId;

    //名称
    @Column(name = "rule_name")
    private String ruleName;

    //默认
    @Column(name = "default_flag")
    private Integer defaultFlag;

    //能否修改
    @Column(name = "modified")
    private Integer modified;

    //状态
    @Column(name = "status")
    private String status;

}
