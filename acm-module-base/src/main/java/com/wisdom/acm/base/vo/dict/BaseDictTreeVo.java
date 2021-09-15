package com.wisdom.acm.base.vo.dict;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class BaseDictTreeVo extends TreeVo<BaseDictTreeVo> {


    /**
     * 码值
     */
    private String dictCode;

    /**
     * 说明
     */
    private String dictName;

    /**
     * 字典码值子节点
     */
    private List<BaseDictTreeVo> children;

    /**
     * 是否内置
     */
    private Integer builtIn;
}
