package com.wisdom.acm.sys.form;

import lombok.Data;

@Data
public class SysPwdRuleAddForm {

    /**
     * 修改周期
     */
    private Integer cycle;

    /**
     * 密码长度
     */
    private Integer length;

    /**
     * 错误次数
     */
    private Integer errorNumber;

    /**
     * 锁定时长（分钟）
     */
    private Integer lockTime;
}
