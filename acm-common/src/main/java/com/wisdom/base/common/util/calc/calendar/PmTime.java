package com.wisdom.base.common.util.calc.calendar;

import java.io.Serializable;

public class PmTime implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 开始时间
	 */
	private int[] fromTime;

	/**
	 * 开始时间毫秒
	 */
	private long fromTimeMillis;

	/**
	 * 结束时间
	 */
	private int[] toTime;

	/**
	 * 结束时间毫秒
	 */
	private long toTimeMillis;

	/**
	 * 开始时间至结束时间的毫秒数
	 */
	private long timeDifference;

	/**
	 * @return the fromTime
	 */
	public int[] getFromTime() {
		return this.fromTime;
	}

	/**
	 * @param fromTime the fromTime to set
	 */
	public void setFromTime(final int[] fromTime) {
		this.fromTime = fromTime;
		this.fromTimeMillis = (this.fromTime[0] * 60 * 60 * 1000L) + (this.fromTime[1] * 60 * 1000L);
		this.setTimeDifference();
	}

	/**
	 * @return the fromTimeMillis
	 */
	public long getFromTimeMillis() {
		return this.fromTimeMillis;
	}

	/**
	 * @return the toTime
	 */
	public int[] getToTime() {
		return this.toTime;
	}

	/**
	 * @param toTime the toTime to set
	 */
	public void setToTime(final int[] toTime) {
		this.toTime = toTime;
		this.toTimeMillis = (this.toTime[0] * 60 * 60 * 1000L) + (this.toTime[1] * 60 * 1000L);
		this.setTimeDifference();
	}

	/**
	 * @return the toTimeMillis
	 */
	public long getToTimeMillis() {
		return this.toTimeMillis;
	}

	/**
	 * @return the timeDifference
	 */
	public long getTimeDifference() {
		return this.timeDifference;
	}

	/**
	 * 设置一段上班时间的差值
	 */
	private void setTimeDifference() {
		if (!Tools.isEmpty(this.fromTimeMillis) && !Tools.isEmpty(this.toTimeMillis)) {
			this.timeDifference = this.toTimeMillis - this.fromTimeMillis;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("fromTime: ");
		if (this.fromTimeMillis > 0) {
			if (this.fromTime[0] < 10) {
				sb.append("0");
			}
			sb.append(this.fromTime[0]).append(":");
			if (this.fromTime[1] < 10) {
				sb.append("0");
			}
			sb.append(this.fromTime[1]).append(":");
			if (this.fromTime[2] < 10) {
				sb.append("0");
			}
			sb.append(this.fromTime[2]);
		}
		sb.append("\ttoTime: ");
		if (this.toTimeMillis > 0) {
			if (this.toTime[0] < 10) {
				sb.append("0");
			}
			sb.append(this.toTime[0]).append(":");
			if (this.toTime[1] < 10) {
				sb.append("0");
			}
			sb.append(this.toTime[1]).append(":");
			if (this.toTime[2] < 10) {
				sb.append("0");
			}
			sb.append(this.toTime[2]);
		}
		return sb.toString();
	}
}
