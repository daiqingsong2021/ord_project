package com.wisdom.acm.base.po;

import com.wisdom.base.common.enums.CodeRuleAttributeTypeEnum;
import com.wisdom.base.common.enums.CodeRuleTypeEnum;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_base_coderule_type")
public class BaseCoderuleTypePo extends BasePo {

    //规则id
    @Column(name = "rule_bo_id")
    private Integer ruleBoId;

    //表名
    @Column(name = "table_name")
    private String tableName;

    //是否国际化
    @Column(name = "global")
    private Integer global;

    //类别
    @Column(name = "rule_type")
    private String ruleType;

    //名称
    @Column(name = "rule_type_name")
    private String ruleTypeName;

    //SQL
    @Column(name = "type_sql")
    private String typeSql;

    //列名
    @Column(name = "column_name")
    private String columnName;

    //关联字段
    @Column(name = "foreign_key")
    private String foreignKey;

    //字典type
    @Column(name = "dict_type")
    private String dictType;

    //字典bo
    @Column(name = "dict_bo")
    private String dictBo;

    //类型
    @Column(name = "attribute_type")
    private String attributeType;

    public void format() {
        if (this.attributeType.equals(CodeRuleAttributeTypeEnum.TABLE_COLUMN.name())) {
            this.typeSql = "select " + this.columnName + " from " + this.tableName + " where id = :" + this.foreignKey;
        } else if (this.attributeType.equals(CodeRuleAttributeTypeEnum.SELF.name())) {
            this.typeSql = "select :" + this.columnName;
        }
    }
}
