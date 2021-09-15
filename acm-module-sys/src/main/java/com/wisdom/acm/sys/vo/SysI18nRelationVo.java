package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

@Data
public class SysI18nRelationVo {

    private Integer i18nId;
    //语种
    private DictionaryVo i18nCodeVo;

    //值
    private String i18nValue;

}
