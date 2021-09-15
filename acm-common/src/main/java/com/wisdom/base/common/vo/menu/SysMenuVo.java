package com.wisdom.base.common.vo.menu;

import com.wisdom.base.common.po.SysFuncPo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysMenuVo extends TreeVo<SysMenuVo> {

    private String menuCode;

    private String menuName;

    private String shortCode;

    private String i18n;

    private String url;

    private String image;

    private Integer hidden;

    private Integer share;

    private Integer isMenu;

    private Integer group;

    private Integer active;

    private String menuDesc;

    private GeneralVo menuType;

    private Date creatTime;

    private Integer menuLocal;

    private Integer sort;

    private String creator;

    private Date lastUpdTime;

//    @Transient
    private List<SysFuncPo> funcList;

    private Integer isFavorite;

    //是否是系统菜单(0-是/1-不是)
    private Integer system;

    //是否内置(0-是/1-不是)
    private Integer built_in;

}
