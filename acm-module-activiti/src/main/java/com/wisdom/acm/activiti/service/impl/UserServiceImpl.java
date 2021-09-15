package com.wisdom.acm.activiti.service.impl;

import com.github.pagehelper.PageHelper;
import com.wisdom.acm.activiti.mapper.RoleMapper;
import com.wisdom.acm.activiti.mapper.UserMapper;
import com.wisdom.acm.activiti.po.User;
import com.wisdom.acm.activiti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public int getCount(String findContent) {
		return userMapper.getCount(findContent);
	}

	@Override
	public List<User> getList(int pageNum, int pageSize, String findContent) {
//		pageNum = (pageNum - 1) * pageSize;
		PageHelper.startPage(pageNum, pageSize,false);
		return userMapper.getList(pageNum, pageSize, findContent);
	}

	@Override
	public List<User> getListById(List<String> userIds) {
		return userMapper.getListById(userIds);
	}

}
