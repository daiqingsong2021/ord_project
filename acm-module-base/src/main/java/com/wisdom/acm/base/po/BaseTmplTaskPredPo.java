package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_tmpltaskpred")
@Data
public class BaseTmplTaskPredPo extends BasePo {

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "pred_task_id")
    private Integer predTaskId;

    @Column(name = "relation_type")
    private String relationType;

    @Column(name = "lag_qty")
    private Double lagQty;
}
