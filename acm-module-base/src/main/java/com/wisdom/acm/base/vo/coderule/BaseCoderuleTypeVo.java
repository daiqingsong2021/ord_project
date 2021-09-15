package com.wisdom.acm.base.vo.coderule;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class BaseCoderuleTypeVo {

    private Integer id;

    //status
    private String status;

    //名称
    private String ruleTypeName;

    //SQL
    private String typeSql;

    //列名
    private DictionaryVo columnName;

    //表名
    private DictionaryVo tableName;

    //关联字段
    private String foreignKey;

    //字典type
    private GeneralVo dictTypeVo;

    //字典bo
    private GeneralVo dictBoVo;

    //类型
    private DictionaryVo attributeTypeVo;

}
