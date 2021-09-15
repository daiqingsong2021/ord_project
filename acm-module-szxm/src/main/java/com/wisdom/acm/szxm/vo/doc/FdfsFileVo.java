package com.wisdom.acm.szxm.vo.doc;

/**
 * fdfs文档对象
 */
public class FdfsFileVo {

	/*
	* 组名
	 */
	private String groupName;
	/**
	 * 文件名
	 */
	private String remoteFileName;

	public FdfsFileVo(String groupName, String remoteFileName) {
		this.groupName = groupName;
		this.remoteFileName = remoteFileName;
	}

	public FdfsFileVo() {
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

}