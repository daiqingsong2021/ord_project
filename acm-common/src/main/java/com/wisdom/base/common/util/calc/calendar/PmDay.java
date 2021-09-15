package com.wisdom.base.common.util.calc.calendar;

import java.io.Serializable;
import java.util.List;

public class PmDay implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日期
	 */
	private int date;

	/**
	 * 开始时间
	 */
	private List<PmTime> times;

	/**
	 * 一天的毫秒数
	 */
	private long onDayMillis;

	/**
	 * @return the date
	 */
	public int getDate() {
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(final int date) {
		this.date = date;
	}

	/**
	 * @return the times
	 */
	public List<PmTime> getTimes() {
		return this.times;
	}

	/**
	 * @param times the times to set
	 */
	public void setTimes(final List<PmTime> times) {
		this.times = times;
		this.onDayMillis = 0;
		if (!Tools.isEmpty(this.times)) {
			for (PmTime time : times) {
				this.onDayMillis += time.getTimeDifference();
			}
		}
	}

	/**
	 * @return the onDayMillis
	 */
	public long getOnDayMillis() {
		return this.onDayMillis;
	}

	@Override
	public String toString() {
		return Tools.toString(this.times);
	}
}
