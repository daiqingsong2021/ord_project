package com.wisdom.acm.dc2.form.message;


import lombok.Data;


@Data
public class GroupManageDetailsAddForm
{
	 
    /**
     *odr_message_group表主键Id
     */
    private Integer groupId;

    /**
     *群组人员姓名
     */
    private String personName;
    
    /**
     * 群组人员手机号
     */
    private String personMobile;

    /**
     * 群组人员部门
     */
    private String personDepartment;
    
    /**
     * 群组人员职务
     */
    private String personPosition;


}
