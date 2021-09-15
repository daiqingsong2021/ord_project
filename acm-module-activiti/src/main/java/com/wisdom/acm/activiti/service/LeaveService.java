package com.wisdom.acm.activiti.service;


import com.wisdom.acm.activiti.po.LeaveInfo;

public interface LeaveService {

	/**
	 * 新增一条请假单记录
	 * @param msg
	 * @param id
	 */
	void addLeaveAInfo(String msg, String id);

	LeaveInfo getId(String id);

	void update(LeaveInfo leaveInfo);

}
