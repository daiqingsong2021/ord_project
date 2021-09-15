package com.wisdom.acm.base.vo.dict;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BaseDictTypeVo  {

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
     * 字典码值名称
     */
    private String typeName;
    /**
     * 字典码值备注
     */
    private String remark;
    /**
     * 是否内置
     */
    private Integer builtIn;

}
