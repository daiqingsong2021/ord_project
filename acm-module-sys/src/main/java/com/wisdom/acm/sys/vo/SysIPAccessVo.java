package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class SysIPAccessVo {

    private Integer id;

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
    private String accessRule;

    private Integer isEffect;

    /**
     * 备注
     */
    private String remark;
}
