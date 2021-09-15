package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Table(name = "wsd_sys_menu")
@Data
public class SysMenuPo extends BasePo {

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "menu_code")
    @NotBlank(message = "菜单代码不能为空")
    private String menuCode;

    @Column(name = "menu_name")
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @Column(name = "short_code")
    @NotBlank(message = "菜单简码不能为空")
    private String shortCode;

    @Column(name = "url")
    @NotBlank(message = "菜单链接不能为空")
    private String url;

    /**
     * 图标
     */
    @Column(name = "image")
    private String image;

    /**
     * 菜单位置（null：菜单栏 1：九宫格）
     */
    @Column(name = "menu_local")
    private Integer menuLocal;

    /**
     * 组
     */
    @Column(name = "menu_group")
    private Integer group;

    /**
     * 国际化
     */
    @Column(name = "i18n")
    private String i18n;

    /**
     * 隐藏
     */
    @Column(name = "hidden")
    private Integer hidden;

    /**
     * 共享
     */
    @Column(name = "share_")
    private Integer share;

    /**
     * 菜单
     */
    @Column(name = "is_menu")
    private Integer isMenu;

    /**
     * 激活
     */
    @Column(name = "active")
    private Integer active;

    /**
     * 描述
     */
    @Column(name = "menu_desc")
    private String menuDesc;

    /**
     * 菜单类型
     */
    @Column(name = "menu_type")
    private Integer menuType;

    /**
     * 組名
     */
    @Column(name = "group_name")
    private String groupName;

}