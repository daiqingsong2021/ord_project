package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.authority.PermissionInfo;

import java.util.List;

/**
 * Created by ace on 2017/8/22.
 */
public class FrontUser {
    public String id;
    public String userName;
    public String name;
    private String description;
    private String image;
    private List<PermissionInfo> menus;
    private List<PermissionInfo> elements;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public List<PermissionInfo> getMenus() {
        return menus;
    }

    public void setMenus(List<PermissionInfo> menus) {
        this.menus = menus;
    }

    public List<PermissionInfo> getElements() {
        return elements;
    }

    public void setElements(List<PermissionInfo> elements) {
        this.elements = elements;
    }
}
