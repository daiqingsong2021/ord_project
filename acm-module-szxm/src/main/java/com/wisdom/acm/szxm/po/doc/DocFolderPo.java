package com.wisdom.acm.szxm.po.doc;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_doc_folder")
@Data
public class DocFolderPo extends BasePo {
    /**
     * 父节点
     */
    @Column(name = "parent_id")
    private Integer parentId;
    /**
     *姓名
     */
    @Column(name = "name")
    private String name;
    /**
     *类型
     */
    @Column(name = "project_id")
    private String projectId;
}
