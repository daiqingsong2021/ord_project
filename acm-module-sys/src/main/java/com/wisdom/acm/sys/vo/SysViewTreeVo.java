package com.wisdom.acm.sys.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysViewTreeVo{

    private Integer id;

    //全局或个人
    private String classifyName;

    // 子节点集合
    protected List<SysViewVo> children;

}
