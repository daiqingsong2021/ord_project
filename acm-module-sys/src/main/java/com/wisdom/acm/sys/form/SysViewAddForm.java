package com.wisdom.acm.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SysViewAddForm {

    //用户
    private Integer userId;

    //目标对象
    private String bizType;

    //视图名称
    @NotBlank(message = "视图名称不能为空!")
    private String viewName;

    //视图内容
    private String viewContent;

    /**
     * 视图显示的栏位
     */
    private String viewFields;


    //视图显示的栏位
    private String viewSortcols;

    //视图列宽
    private String viewWidthcols;

}
