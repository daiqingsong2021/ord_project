package com.wisdom.acm.activiti.service;



import com.wisdom.acm.activiti.po.Role;

import java.util.List;

public interface RoleService {

	/**
	 * 查询总数
	 * @return
	 */
	int getCount(String findContent);

	/**
	 * 查询角色集合
	 * @return
	 */
	List<Role> getList(int pageNum, int pageSize, String findContent);

	/**
	 * 查询角色集合
	 * @return
	 */
	List<Role> getListById(List<String> roleIds);

}
