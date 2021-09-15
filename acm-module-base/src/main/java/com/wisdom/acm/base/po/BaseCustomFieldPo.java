package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 自定义字段
 */
@Table(name = "wsd_base_custom_field")
@Data
public class BaseCustomFieldPo extends BasePo {

    /**
     * 表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 显示名称
     */
    @Column(name = "title")
    private String title;

    /**
     * 国际化
     */
    @Column(name = "i18n_code")
    private String i18nCode;

    /**
     * 字段名
     */
    @Column(name = "field_name")
    private String fieldName;

    /**
     * 数据类型，Text、Numbr、Date
     */
    @Column(name = "data_type")
    private String dataType;

    /**
     * 表单类型，Input、InputNumber、TreeSelect、Select、TextArea、Date、DateTime、Checkbox、Radio
     */
    @Column(name = "form_type")
    private String formType;

    /**
     * 必填
     */
    @Column(name = "required")
    private Integer required;

    /**
     * 最大长度
     */
    @Column(name = "max_length")
    private Integer maxLength;

    /**
     * 精度
     */
    @Column(name = "precision_")
    private Integer precision;

    /**
     * 格化式字符串
     */
    @Column(name = "formatter")
    private String formatter;

    /**
     * 字典
     */
    @Column(name = "dict_type")
    private String dictType;

    /**
     * 横跨的列数
     */
    @Column(name = "colspan")
    private Integer colspan;

    /**
     * 横跨的行数
     */
    @Column(name = "rowspan")
    private Integer rowspan;

    /**
     * 启用
     */
    @Column(name = "enable")
    private Integer enable;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

}
