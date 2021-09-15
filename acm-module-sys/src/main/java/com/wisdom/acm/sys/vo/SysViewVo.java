package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysViewVo{

    private Integer id;

    //用户
    private Integer userId;

    //目标对象
    private String bizType;

    //视图名称
    private String viewName;

    //视图内容
    private String viewContent;

    //默认视图（0默认）
    private Integer defaultView;

    /**
     * 视图显示的栏位
     */
    private String viewFields;

    //视图显示的栏位
    private String viewSortcols;

    //视图列宽
    private String viewWidthcols;

    //视图类型（全局或个人）
    private String viewType;

}
