package com.wisdom.base.common.vo.menu;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class SysAuthMenuVo extends TreeVo<SysAuthMenuVo> {

    private String menuName;

    private String menuCode;

    private Integer check;

   // private List<SysAuthFuncVo> funcList;
}
