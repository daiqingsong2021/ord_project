package com.wisdom.base.common.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_sys_role")
@Data
public class SysRolePo extends BasePo {

	@Column(name="role_code")
	private String roleCode;
	
	@Column(name="role_name")
	private String roleName;

	@Column(name="role_desc")
	private String roleDesc;

	
}
