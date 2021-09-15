package com.wisdom.base.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TreeVo<T extends TreeVo> {

    // 主键ID
    protected Integer id;
    // 父节点ID
    protected Integer parentId;
    // 子节点集合
    protected List<T> children;

    protected String extendedColumn1;

    protected String extendedColumn2;

    protected String extendedColumn3;

    protected String extendedColumn4;

    protected String extendedColumn5;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getExtendedColumn1() {
        return extendedColumn1;
    }

    public void setExtendedColumn1(String extendedColumn1) {
        this.extendedColumn1 = extendedColumn1;
    }

    public String getExtendedColumn2() {
        return extendedColumn2;
    }

    public void setExtendedColumn2(String extendedColumn2) {
        this.extendedColumn2 = extendedColumn2;
    }

    public String getExtendedColumn3() {
        return extendedColumn3;
    }

    public void setExtendedColumn3(String extendedColumn3) {
        this.extendedColumn3 = extendedColumn3;
    }

    public String getExtendedColumn4() {
        return extendedColumn4;
    }

    public void setExtendedColumn4(String extendedColumn4) {
        this.extendedColumn4 = extendedColumn4;
    }

    public String getExtendedColumn5() {
        return extendedColumn5;
    }

    public void setExtendedColumn5(String extendedColumn5) {
        this.extendedColumn5 = extendedColumn5;
    }

    public void add(T node){
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        children.add(node);
    }


}
