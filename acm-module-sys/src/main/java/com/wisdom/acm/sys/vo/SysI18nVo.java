package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

import java.util.List;

@Data
public class SysI18nVo {
    //id
    private Integer id;

    //所属模块
    private Integer menuId;

    //代码
    private String code;

    //简码
    private String shortCode;

    //语种和值
    private List<SysI18nRelationVo> i18nRelationVos;
}
