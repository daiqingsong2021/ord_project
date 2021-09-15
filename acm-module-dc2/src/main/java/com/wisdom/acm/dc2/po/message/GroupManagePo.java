package com.wisdom.acm.dc2.po.message;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wisdom.base.common.po.BasePo;

import lombok.Data;

@Table(name = "odr_message_group")
@Data
public class GroupManagePo extends BasePo
{

    
    /**
     *群组编号
     */
	@Column(name = "group_code")
    private Integer groupCode;

    /**
     *群组名称
     */
	@Column(name = "group_name")
    private String groupName;
    
    /**
     * 群组状态
     */
	@Column(name = "status")
    private String status;

}
