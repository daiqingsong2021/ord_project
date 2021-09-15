package com.wisdom.base.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MyFinishTaskVo {
	/**
	 * id
	 */
	private String id;

	/**
	 * 流程实例id
	 */
	private String procInstId;

	/**
	 * 名称
	 */
	private String taskName;

	/**
	 * 流程发起人
	 */
	private String creator;

	/**
	 * 送审人
	 */
	private String sender;

	/**
	 * 审批人
	 */
	private String approvalUser;

	/**
	 * 当前节点
	 */
	private String currActivity;

	/**
	 * 开始时间
	 */
	private String startTime;
}
