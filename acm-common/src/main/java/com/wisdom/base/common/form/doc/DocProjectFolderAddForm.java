package com.wisdom.base.common.form.doc;

import lombok.Data;

@Data
public class DocProjectFolderAddForm {
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 父节点
     */
    private Integer parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单id
     */
    private Integer menuId;
}
