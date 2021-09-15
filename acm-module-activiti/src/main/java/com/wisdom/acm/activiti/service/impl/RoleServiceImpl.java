package com.wisdom.acm.activiti.service.impl;

import com.github.pagehelper.PageHelper;
import com.wisdom.acm.activiti.mapper.RoleMapper;
import com.wisdom.acm.activiti.po.Role;
import com.wisdom.acm.activiti.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public int getCount(String findContent) {
		return roleMapper.getCount(findContent);
	}

	@Override
	public List<Role> getList(int pageNum, int pageSize, String findContent) {
//		pageNum = (pageNum - 1) * pageSize;
		PageHelper.startPage(pageNum, pageSize,false);
		return roleMapper.getList(pageNum, pageSize, findContent);
	}

	@Override
	public List<Role> getListById(List<String> roleIds) {
		return roleMapper.getListById(roleIds);
	}
}
