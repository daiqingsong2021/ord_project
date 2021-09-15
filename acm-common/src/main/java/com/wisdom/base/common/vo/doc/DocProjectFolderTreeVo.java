package com.wisdom.base.common.vo.doc;

import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class DocProjectFolderTreeVo extends TreeVo<DocProjectFolderTreeVo> {

    private Integer id;
    /**
     *姓名
     */
    private String name;

    /**
     * 文档数量
     */
    private Integer docNum;

    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 类型
     */
    private String type;

    /**
     * 菜单编码
     */
    private SelectVo menu;
}