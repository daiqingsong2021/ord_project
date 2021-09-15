package com.wisdom.acm.activiti.mapper;

import com.wisdom.acm.activiti.po.LeaveInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LeaveMapper {

	/*int getCount();*/
	
	void insert(LeaveInfo entity);
	
	/*void delete(String id);*/
	
	LeaveInfo getById(String id);
	
	/*List<LeaveInfo> getList();*/
	
	void update(LeaveInfo entity);
}
