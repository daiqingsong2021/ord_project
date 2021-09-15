package com.wisdom.acm.base.vo.dict;

import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDictTypeInfoVo {

    private Integer id;

    /**
     * 字典码值代码
     */
    private String typeCode;
    /**
     * 字典码值名称
     */
    private String typeName;

    private UserVo creator;

    private Date creatTime;
}
