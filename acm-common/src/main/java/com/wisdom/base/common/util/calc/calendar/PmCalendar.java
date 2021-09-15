package com.wisdom.base.common.util.calc.calendar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PmCalendar implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 日历名称
	 */
	private String clndrName;

	/**
	 * 每天工时
	 */
	private BigDecimal dayHrCnt;

	/**
	 * 每周工时
	 */
	private BigDecimal weekHrCnt;

	/**
	 * 每月工时
	 */
	private BigDecimal monthHrCnt;

	/**
	 * 每年工时
	 */
	private BigDecimal yearHrCnt;

	/**
	 * 例外中获取当前日历的配置
	 * {
	 * 20190401=null,##不上班
	 * 20190401=[{"fromTime": "08:00:00","toTime": "12:00:00"},{"fromTime": "13:00:00","toTime": "17:00:00"}] ##上班时间段
	 * }
	 */
	private Map<Integer, PmDay> exceptMap;

	/**
	 * 默认标准周前日历的配置（周一到周七的每天标准工作配置）
	 * {
	 * 1=null,
	 * 2=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 3=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 4=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 5=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 6=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 7=null
	 * }
	 */
	private Map<Integer, PmDay> workDayOfWeekMap;

	/**
	 * 一周的工作毫秒数
	 */
	private long workHourOfWeek;

	/**
	 * @return id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id 要设置的 id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return clndrName
	 */
	public String getClndrName() {
		return this.clndrName;
	}

	/**
	 * @param clndrName 要设置的 clndrName
	 */
	public void setClndrName(final String clndrName) {
		this.clndrName = clndrName;
	}

	/**
	 * @return dayHrCnt
	 */
	public BigDecimal getDayHrCnt() {
		return this.dayHrCnt;
	}

	/**
	 * @param dayHrCnt 要设置的 dayHrCnt
	 */
	public void setDayHrCnt(final BigDecimal dayHrCnt) {
		this.dayHrCnt = dayHrCnt;
	}

	/**
	 * @return weekHrCnt
	 */
	public BigDecimal getWeekHrCnt() {
		return this.weekHrCnt;
	}

	/**
	 * @param weekHrCnt 要设置的 weekHrCnt
	 */
	public void setWeekHrCnt(final BigDecimal weekHrCnt) {
		this.weekHrCnt = weekHrCnt;
	}

	/**
	 * @return monthHrCnt
	 */
	public BigDecimal getMonthHrCnt() {
		return monthHrCnt;
	}

	/**
	 * @param monthHrCnt 要设置的 monthHrCnt
	 */
	public void setMonthHrCnt(BigDecimal monthHrCnt) {
		this.monthHrCnt = monthHrCnt;
	}

	/**
	 * @return yearHrCnt
	 */
	public BigDecimal getYearHrCnt() {
		return this.yearHrCnt;
	}

	/**
	 * @param yearHrCnt 要设置的 yearHrCnt
	 */
	public void setYearHrCnt(final BigDecimal yearHrCnt) {
		this.yearHrCnt = yearHrCnt;
	}

	/**
	 * @return mapExcept
	 */
	public Map<Integer, PmDay> getExceptMap() {
		return this.exceptMap;
	}

	/**
	 * @param exceptMap 要设置的 exceptMap
	 */
	public void setExceptMap(final Map<Integer, PmDay> exceptMap) {
		this.exceptMap = exceptMap;
	}

	/**
	 * @return mapWorkDay
	 */
	public Map<Integer, PmDay> getWorkDayOfWeekMap() {
		return this.workDayOfWeekMap;
	}

	/**
	 * @param workDayOfWeekMap 要设置的 workDayOfWeekMap
	 */
	public void setWorkDayOfWeekMap(final Map<Integer, PmDay> workDayOfWeekMap) {
		this.workDayOfWeekMap = workDayOfWeekMap;
		this.workHourOfWeek = this.countWorkHourOfWeek();
	}

	/**
	 * @return workHourOfWeek
	 */
	public long getWorkHourOfWeek() {
		return this.workHourOfWeek;
	}

	/**
	 * @param workHourOfWeek 要设置的 workHourOfWeek
	 */
	public void setWorkHourOfWeek(final long workHourOfWeek) {
		this.workHourOfWeek = workHourOfWeek;
	}

	/**
	 * 获取一周的工作毫秒数
	 *
	 * @return the work millis in a week
	 */
	private long countWorkHourOfWeek() {
		long workMillis = 0;
		Map<Integer, PmDay> mapWorkDay = this.workDayOfWeekMap;
		if (!Tools.isEmpty(mapWorkDay)) {
			for (int i = 1; i <= 7; i++) {
				PmDay day = mapWorkDay.get(i);
				if (!Tools.isEmpty(day.getTimes())) {
					List<PmTime> pmHours = day.getTimes();
					if (!Tools.isEmpty(pmHours)) {
						for (PmTime time : pmHours) {
							workMillis += time.getTimeDifference();
						}
					}
				}
			}
		}
		return workMillis;
	}

}
