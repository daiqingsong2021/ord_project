package com.wisdom.acm.base.vo.dict;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class BaseDictVo  {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 业务对象代码
     */
    private String boCode;

    /**
     * 字典码值代码
     */
    private String typeCode;

    /**
     * 数字字典名称
     */
    private String dictName;

    /**
     * 数字字典代码
     */
    private String dictCode;

}
