package com.wisdom.acm.sys.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "SZXM_SYS_SECTION_RELATION")
public class SectionRelationPo
{
    @Id
    private int id;

    @Column(name = "SGB_ID")
    private Integer sgbId;

    @Column(name = "JLB_ID")
    private Integer jlbId;
}
