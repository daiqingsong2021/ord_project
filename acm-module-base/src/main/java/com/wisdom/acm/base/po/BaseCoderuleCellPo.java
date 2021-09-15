package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_base_coderule_cell")
public class BaseCoderuleCellPo extends BasePo {

    //wsd_base_coderule的id
    @Column(name = "rule_id")
    private Integer ruleId;

    //wsd_base_coderule_type的id
    @Column(name = "rule_type_id")
    private Integer ruleTypeId;

    //wsd_base_coderule_type的rule_type（字典）
    @Column(name = "rule_type")
    private String ruleType;

    //第X段
    @Column(name = "position")
    private Integer position;

    //名称
    @Column(name = "rule_cell_name")
    private String ruleCellName;

    //SQL
    @Column(name = "cell_sql")
    private String cellSql;

    //值（rule_type=FIXED_VALUE时）（暂时字典vo装载）
    @Column(name = "cell_value")
    private String cellValue;

    //表名
    @Column(name = "table_name")
    private String tableName;

    //列名
    @Column(name = "column_name")
    private String columnName;

    //关联字段
    @Column(name = "foreign_key")
    private String foreignKey;

    //连接符（字典）
    @Column(name = "connector")
    private String connector;

    //固定长度
    @Column(name = "max_length")
    private Integer maxLength;

    //填充字符
    @Column(name = "fill_char")
    private String fillChar;

    //
    @Column(name = "fill_direction")
    private Integer fillDirection;

    //
    @Column(name = "intercept_direction")
    private Integer interceptDirection;

    //流水号最小值
    @Column(name = "seq_min_value")
    private Integer seqMinValue;

    //流水号步长
    @Column(name = "seq_steep")
    private Integer seqSteep;

}
