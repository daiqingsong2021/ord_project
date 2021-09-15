package com.wisdom.acm.dc5.vo.app;

import lombok.Data;

/**
 * 联系人VO
 */
@Data
public class ContactsUserVo
{
    /**
     * 联系人ID
     */
    private  Integer contactsId;

    /**
     * 联系人姓名
     */
    private  String  contactsName;

    /**
     * 名称拼音
     */
    private  String  namePinYin;

    /**
     * 联系人 部门/单位
     */
    private  String  contactsDept;

    /**
     * 来源 0 人员管理 1 项目团队
     */
    private String  source;

    /**
     * 手机号码
     */
    private String  telPhone;

    /**
     * 性别 0女 1男
     */
    private String  sex;

    /**
     * 职务
     */
    private String job;
}
