package com.wisdom.acm.szxm.po.doc;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_doc_folder_menuid")
@Data
public class DocFolderMenuIdPo extends BasePo {
    /**
     * 文件夹id
     */
    @Column(name = "folder_id")
    private Integer folderId;
    /**
     *菜单id
     */
    @Column(name = "menu_Id")
    private Integer menuId;

}
