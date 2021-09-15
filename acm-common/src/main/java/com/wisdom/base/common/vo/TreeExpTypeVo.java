package com.wisdom.base.common.vo;

import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class TreeExpTypeVo<T extends TreeExpTypeVo> {

    // 主键ID
    protected Integer id;

    // 父节点ID
    protected Integer parentId;

    // 类型
    protected String type;

    // 子节点集合
    protected List<T> children;

    public void add(T node){
        children.add(node);
    }

}
