package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.List;

@Data
public class SysIptInfoVo {
    private Integer id;
    /**
     * 代码
     */
    private String iptCode;

    /**
     * 名称
     */
    private String iptName;

    /**
     * 父ID
     */
    private GeneralVo parent;


    private GeneralVo level;

    /**
     * 备注
     */
    private String remark;

    private Integer sort;

}
