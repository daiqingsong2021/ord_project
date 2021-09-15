package com.wisdom.acm.activiti.po;


public class LeaveInfo {

	private String id;
	private String status;
	private String leaveMsg;

	private String taskId;

	private String startTime;
	private String endTime;
	private String durationInMillis;

	private String startUserId;
	private String name;

	public LeaveInfo(String id, String status, String leaveMsg, String taskId) {
		this.id = id;
		this.status = status;
		this.leaveMsg = leaveMsg;
		this.taskId = taskId;
	}

	public LeaveInfo(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLeaveMsg() {
		return leaveMsg;
	}

	public void setLeaveMsg(String leaveMsg) {
		this.leaveMsg = leaveMsg;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(String durationInMillis) {
		this.durationInMillis = durationInMillis;
	}
}
