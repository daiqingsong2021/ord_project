package com.wisdom.base.common.vo.doc;

import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class DocCorpFolderTreeVo extends TreeVo<DocCorpFolderTreeVo> {

    // 文件夹名称
    private String name;

    // 文档数量
    private Integer docNum;

    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 菜单编码
     */
    private SelectVo menu;
}
