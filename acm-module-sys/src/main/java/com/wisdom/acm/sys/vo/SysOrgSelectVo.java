package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class SysOrgSelectVo extends TreeVo<SysOrgSelectVo> {

    // 名称
    private String name;
    // 类型
    private String type;
}
