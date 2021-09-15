package com.wisdom.acm.base.vo.custom;

import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

@Data
public class BaseCustomFieldVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 表名
     */
    private DictionaryVo table;

    /**
     * 显示名称
     */
    private String title;

    /**
     * 国际化
     */
    private String i18nCode;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 数据类型，Text、Numbr、Date
     */
    private DictionaryVo dataType;

    /**
     * 表单类型，Input、InputNumber、TreeSelect、Select、TextArea、Date、DateTime、Checkbox、Radio
     */
    private DictionaryVo formType;

    /**
     * 必填
     */
    private Integer required;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 精度
     */
    private Integer precision;

    /**
     * 格化式字符串
     */
    private String formatter;

    /**
     * 字典
     */
    private DictionaryVo dictType;

    /**
     * 横跨的列数
     */
    private Integer colspan;

    /**
     * 横跨的行数
     */
    private Integer rowspan;

    /**
     * 启用
     */
    private Integer enable;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 排序号
     */
    private Integer sort;
}
