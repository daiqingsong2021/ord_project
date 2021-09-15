package com.wisdom.acm.base.vo.coderule;

import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

@Data
public class BaseCoderuleBoVo {

    private Integer id;

    //代码
    private String boCode;

    //名称
    private String boName;

    //表名字
    private DictionaryVo tableName;

    //代码列名
    private DictionaryVo codeColumnName;

    //seqScope
    private DictionaryVo seqScope;

    //代码
    private DictionaryVo assignColumnName;

}
