package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_pwdrule")
@Data
public class SysPwdRulePo extends BasePo {

    /**
     * 修改周期
     */
    @Column(name = "cycle")
    private Integer cycle;

    /**
     * 密码长度
     */
    @Column(name = "length")
    private Integer length;

    /**
     * 错误次数
     */
    @Column(name = "errornumber")
    private Integer errorNumber;

    /**
     * 锁定时长（分钟）
     */
    @Column(name = "locktime")
    private Integer lockTime;
}
