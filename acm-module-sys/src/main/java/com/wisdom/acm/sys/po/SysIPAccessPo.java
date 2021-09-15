package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_ipaccess")
@Data
public class SysIPAccessPo extends BasePo {

    /**
     * 开始ip
     */
    @Column(name = "start_ip")
    private String startIP;

    /**
     * 结束ip
     */
    @Column(name = "end_ip")
    private String endIP;

    /**
     * 规则设置
     */
    @Column(name = "access_rule")
    private String accessRule;

    /**
     * 是否生效
     */
    @Column(name = "is_effect")
    private Integer isEffect;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
