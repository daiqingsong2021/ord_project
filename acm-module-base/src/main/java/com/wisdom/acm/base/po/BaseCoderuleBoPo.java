package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_base_coderule_bo")
public class BaseCoderuleBoPo extends BasePo {

    //代码
    @Column(name = "bo_code")
    private String boCode;

    //名称
    @Column(name = "bo_name")
    private String boName;

    //表名字
    @Column(name = "table_name")
    private String tableName;

    //代码列名
    @Column(name = "code_column_name")
    private String codeColumnName;

    //seqScope
    @Column(name = "seq_scope")
    private String seqScope;

    //代码
    @Column(name = "assign_column_name")
    private String assignColumnName;
}
