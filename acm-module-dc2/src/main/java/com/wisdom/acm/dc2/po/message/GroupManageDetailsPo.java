package com.wisdom.acm.dc2.po.message;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wisdom.base.common.po.BasePo;

import lombok.Data;

@Table(name = "odr_message_group_details")
@Data
public class GroupManageDetailsPo extends BasePo
{

    
    /**
     *odr_group_manage表主键Id
     */
	@Column(name = "group_id")
    private Integer groupId;

    /**
     *群组人员姓名
     */
	@Column(name = "person_name")
    private String personName;
    
    /**
     * 群组人员手机号
     */
	@Column(name = "person_mobile")
    private String personMobile;

    /**
     * 群组人员部门
     */
	@Column(name = "person_department")
    private String personDepartment;
    
    /**
     * 群组人员职务
     */
	@Column(name = "person_position")
    private String personPosition;


}
