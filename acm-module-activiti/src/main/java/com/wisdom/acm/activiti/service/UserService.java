package com.wisdom.acm.activiti.service;

import com.wisdom.acm.activiti.po.User;

import java.util.List;

public interface UserService {

	/**
	 * 查询总数
	 * @return
	 */
	int getCount(String findContent);

	/**
	 * 查询角色集合
	 * @return
	 */
	List<User> getList(int pageNum, int pageSize, String findContent);

	/**
	 * 查询角色集合
	 * @return
	 */
	List<User> getListById(List<String> userIds);

}
