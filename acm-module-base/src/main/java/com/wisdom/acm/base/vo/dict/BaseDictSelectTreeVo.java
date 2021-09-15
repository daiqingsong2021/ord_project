package com.wisdom.acm.base.vo.dict;

import lombok.Data;

import java.util.List;

@Data
public class BaseDictSelectTreeVo {

    /**
     * 主键
     */
    private String id;

    /**
     * 码值
     */
    private String name;


    /**
     * 字典码值子节点
     */
    private List<BaseDictSelectTreeVo> children;
}
