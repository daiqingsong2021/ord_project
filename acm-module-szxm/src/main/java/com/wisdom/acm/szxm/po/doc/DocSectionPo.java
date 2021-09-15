package com.wisdom.acm.szxm.po.doc;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_doc_section")
@Data
public class DocSectionPo extends BasePo {

    /**
     *文档id
     */
    @Column(name = "doc_id")
    private Integer docId;
    /**
     *标段id
     */
    @Column(name = "section_id")
    private Integer sectionId;


}
