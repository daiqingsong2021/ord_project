package com.wisdom.acm.sys.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysMenuGroupVo {

    private String groupCode;

    private List<SysMenuLableVo> lables;
}
