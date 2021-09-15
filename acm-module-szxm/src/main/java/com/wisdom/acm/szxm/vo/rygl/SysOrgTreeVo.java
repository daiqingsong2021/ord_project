package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.Date;
@Data
public class SysOrgTreeVo extends TreeVo<SysOrgTreeVo> {
    /**
     * 名称
     */
    private String orgName;

    private String orgCode;

    private Integer orgType;

    private Date updateTime;
}
