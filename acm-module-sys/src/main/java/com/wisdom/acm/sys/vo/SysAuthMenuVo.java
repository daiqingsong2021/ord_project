package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class SysAuthMenuVo extends TreeVo<SysAuthMenuVo> {

    private String menuName;

    private String menuCode;

    private Integer check;

    private List<SysAuthFuncVo> funcList;
}
