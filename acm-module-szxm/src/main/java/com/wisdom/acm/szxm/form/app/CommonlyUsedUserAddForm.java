package com.wisdom.acm.szxm.form.app;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommonlyUsedUserAddForm
{
    private  Integer id;

    @NotNull(message = "联系人ID不能为空")
    private  Integer contactsId;

    @NotBlank(message = "联系人姓名不能为空")
    private  String  contactsName;

    @NotBlank(message = "联系人部门/单位不能为空")
    private  String  contactsDept;


    private  String  job;

    private  String  telPhone;

    private  String  email;

    private  String  sex;
    /**
     * 0 组织信息 1 系统管理
     */
    @NotBlank(message = "联系人来源不能为空")
    private String  source;

    private  Integer userId;
}
