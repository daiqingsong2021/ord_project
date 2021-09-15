package com.wisdom.acm.base.vo.coderule;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class BaseCoderuleCellVo {

    private Integer id;

    private GeneralVo ruleTypeIdVo;

    //wsd_base_coderule_type的rule_type（字典）
    private DictionaryVo ruleTypeVo;

    //值（rule_type=FIXED_VALUE时）
    private String cellValue;

    //连接符（字典）
    private DictionaryVo connectorVo;

    //固定长度
    private Integer maxLength;

    //填充字符
    private String fillChar;

    //流水号最小值
    private Integer seqMinValue;

    //流水号步长
    private Integer seqSteep;

    //名称
    private String ruleCellName;

}
