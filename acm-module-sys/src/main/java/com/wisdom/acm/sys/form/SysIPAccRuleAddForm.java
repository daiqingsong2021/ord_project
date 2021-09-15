package com.wisdom.acm.sys.form;

import lombok.Data;


@Data
public class SysIPAccRuleAddForm {

    /**
     * 开始ip
     */
    private String startIP;

    /**
     * 结束ip
     */
    private String endIP;

    /**
     * 规则设置
     */
    private Integer accessRule;

    private Integer isEffect;

    /**
     * 备注
     */
    private String remark;

    public String getLogContent() {
        return "增加访问规则设置，开始IP:" + this.getStartIP() + ",结束IP:" + this.getEndIP();
    }
}
