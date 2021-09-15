package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_view")
@Data
public class SysViewPo extends BasePo {

    //用户
    @Column(name = "user_id")
    private Integer userId;

    //目标类型
    @Column(name = "biz_type")
    private String bizType;

    //视图名称
    @Column(name = "view_name")
    private String viewName;

    //视图内容
    @Column(name = "view_content")
    private String viewContent;

    //默认视图（1默认）
    @Column(name = "default_view")
    private Integer defaultView;

    /**
     * 视图显示的栏位
     */
    @Column(name = "view_fields")
    private String viewFields;

    //视图显示的栏位
    @Column(name = "view_sortcols")
    private String viewSortcols;

    //视图列宽
    @Column(name = "view_widthcols")
    private String viewWidthcols;

}
