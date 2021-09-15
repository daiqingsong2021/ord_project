package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class AuthorityMenuTree extends TreeVo<AuthorityMenuTree> implements Serializable{
    @Autowired
    private Mapper mapper;

    String text;
    List<AuthorityMenuTree> nodes = new ArrayList<AuthorityMenuTree>();
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public AuthorityMenuTree(String text, List<AuthorityMenuTree> nodes) {
        this.text = text;
        this.nodes = nodes;
    }

    public AuthorityMenuTree() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<AuthorityMenuTree> getNodes() {
        return nodes;
    }

    public void setNodes(List<AuthorityMenuTree> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void setChildren(List<AuthorityMenuTree> children) {
        super.setChildren(children);
        nodes = new ArrayList<AuthorityMenuTree>();
    }

    @Override
    public void add(AuthorityMenuTree node) {
        super.add(node);
        AuthorityMenuTree n = mapper.map(node, AuthorityMenuTree.class);
        //BeanUtils.copyProperties(node,n);
        nodes.add(n);
    }
}
