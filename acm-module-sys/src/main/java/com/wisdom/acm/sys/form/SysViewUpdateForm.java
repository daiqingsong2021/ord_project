package com.wisdom.acm.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SysViewUpdateForm {

    private Integer id;

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
