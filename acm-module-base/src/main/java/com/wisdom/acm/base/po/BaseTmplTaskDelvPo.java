package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_tmpltaskdelv")
@Data
public class BaseTmplTaskDelvPo extends BasePo {

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "delv_id")
    private String delvId;

}
