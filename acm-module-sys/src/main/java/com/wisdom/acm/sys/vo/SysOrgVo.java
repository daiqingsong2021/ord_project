package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.Date;

@Data
public class SysOrgVo extends TreeVo<SysOrgVo> {


    /**
     * 名称
     */
    private String orgName;

    private String orgCode;

    private Integer orgType;

    private Date updateTime;

}
