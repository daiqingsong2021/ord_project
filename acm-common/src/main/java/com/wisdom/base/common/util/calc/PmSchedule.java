package com.wisdom.base.common.util.calc;

import com.wisdom.base.common.util.calc.calendar.PmCalendar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PmSchedule implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 进行中的任务计算方式（进度跨越）
	 */
	public static final String CALCULMETHOD_STRIDE_ACROSS = "StrideAcross";

	/**
	 * 进行中的任务计算方式(保持逻辑关系)
	 */
	public static final String CALCULMETHOD_LOGIC_RELATION = "LogicRelation";

	/**
	 * 关键路径取值类型（最长路径)
	 */
	public static final String KEY_PATH_LONG_PATH = "LongPath";

	/**
	 * 关键路径取值类型(总浮时)
	 */
	public static final String KEY_PATH_TOTAL_FLOAT = "TotalFloat";

	/**
	 * 数据日期
	 */
	private Date calcDate;

	/**
	 * 日历
	 */
	private List<PmCalendar> calendars;

	/**
	 * 计划
	 */
	private List<PmPlan> plans;

	/**
	 * 进行中的任务计算方式
	 */
	private String calculMethod = CALCULMETHOD_STRIDE_ACROSS;

	/**
	 * 关键路径取值类型
	 */
	private String keyPath = KEY_PATH_TOTAL_FLOAT;

	/**
	 * @return the calcDate
	 */
	public Date getCalcDate() {
		return calcDate;
	}

	/**
	 * @param calcDate the calcDate to set
	 */
	public void setCalcDate(Date calcDate) {
		this.calcDate = calcDate;
	}

	/**
	 * @return the calendars
	 */
	public List<PmCalendar> getCalendars() {
		return this.calendars;
	}

	/**
	 * @param calendars the calendars to set
	 */
	public void setCalendars(final List<PmCalendar> calendars) {
		this.calendars = calendars;
	}

	/**
	 * @return the plans
	 */
	public List<PmPlan> getPlans() {
		return this.plans;
	}

	/**
	 * @param plans the plans to set
	 */
	public void setPlans(final List<PmPlan> plans) {
		this.plans = plans;
	}

	/**
	 * @return the calculMethod
	 */
	public String getCalculMethod() {
		return this.calculMethod;
	}

	/**
	 * @param calculMethod the calculMethod to set
	 */
	public void setCalculMethod(final String calculMethod) {
		this.calculMethod = calculMethod;
	}

	/**
	 * @return the keyPath
	 */
	public String getKeyPath() {
		return this.keyPath;
	}

	/**
	 * @param keyPath the keyPath to set
	 */
	public void setKeyPath(final String keyPath) {
		this.keyPath = keyPath;
	}
}