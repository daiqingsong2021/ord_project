package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class SysMenuLableVo extends TreeVo {

    private String menuCode;

    private String menuName;

    private String shortCode;

    private String i18n;

    private String url;

    private String image;

    private Integer menuType;

    private String parentCode;

}
