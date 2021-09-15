package com.wisdom.acm.sys.vo;

import com.wisdom.acm.sys.po.SysIptPo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SysIptVo extends TreeVo<SysIptVo> {

    /**
     * 代码
     */
    private String iptCode;

    /**
     * 名称
     */
    private String iptName;

    private String level;

    /**
     * 备注
     */
    private String remark;

}
