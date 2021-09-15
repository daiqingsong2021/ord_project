package com.wisdom.acm.processing.po.report;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zll
 * 2020/8/20/020 11:02
 * Description:<描述>
 */
@Data
@Table(name = "wsd_doc")
public class DocPo extends BasePo {
    //'文件id'
    @Column(name = "id")
    private Integer id;

    //'审批状态'
    @Column(name = "status")
    private String status;
}
