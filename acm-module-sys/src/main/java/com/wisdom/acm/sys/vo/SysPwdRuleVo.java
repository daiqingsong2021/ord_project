package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysPwdRuleVo {

    private Integer id;
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
