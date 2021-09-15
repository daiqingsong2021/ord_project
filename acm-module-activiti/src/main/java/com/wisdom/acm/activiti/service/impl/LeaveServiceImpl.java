package com.wisdom.acm.activiti.service.impl;

import com.wisdom.acm.activiti.mapper.LeaveMapper;
import com.wisdom.acm.activiti.po.LeaveInfo;
import com.wisdom.acm.activiti.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private LeaveMapper leaveMapper;



	@Override
	public void addLeaveAInfo(String msg,String id) {
		LeaveInfo leaveInfo = new LeaveInfo();
		leaveInfo.setLeaveMsg(msg);
		leaveInfo.setId(id);
		//新增一条记录至数据库中
		leaveMapper.insert(leaveInfo);
	}

	@Override
	public LeaveInfo getId(String id) {
		return leaveMapper.getById(id);
	}

	@Override
	public void update(LeaveInfo leaveInfo) {
		leaveMapper.update(leaveInfo);
	}


}
