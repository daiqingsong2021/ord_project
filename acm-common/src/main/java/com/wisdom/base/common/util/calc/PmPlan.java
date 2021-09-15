package com.wisdom.base.common.util.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PmPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键 .
	 */
	private String id;

	/**
	 * 项目主键 .
	 */
	private String projId;

	/**
	 * 代码.
	 */
	private String planCode;

	/**
	 * 名称 .
	 */
	private String planName;

	/**
	 * 日历主键 .
	 */
	private String calendarId;

	/**
	 * 数据日期 .
	 */
	private Date calcDate;

	/**
	 * 计划开始 .
	 */
	private Date planStartDate;

	/**
	 * 计划完成 .
	 */
	private Date planEndDate;

	/**
	 * 最早开始 .
	 */
	private Date earlyStartDate;

	/**
	 * 最早完成 .
	 */
	private Date earlyEndDate;

	/**
	 * 最晚开始 .
	 */
	private Date lateStartDate;

	/**
	 * 最晚完成 .
	 */
	private Date lateEndDate;

	/**
	 * 尚需最早开始 .
	 */
	private Date remEarlyStart;

	/**
	 * 尚需最早完成 .
	 */
	private Date remEarlyEnd;

	/**
	 * 尚需最晚开始 .
	 */
	private Date remLateStart;

	/**
	 * 尚需最晚完成 .
	 */
	private Date remLateEnd;

	/**
	 * 实际开始 .
	 */
	private Date actStartDate;

	/**
	 * 实际完成 .
	 */
	private Date actEndDate;

	/**
	 * 计划工期 .
	 */
	private BigDecimal planDrtn = new BigDecimal(0);

	/**
	 * 实际工期 .
	 */
	private BigDecimal actDrtn = new BigDecimal(0);

	/**
	 * 尚需工期 .
	 */
	private BigDecimal remainDrtn = new BigDecimal(0);

	/**
	 * 权重 .
	 */
	private BigDecimal estWt;

	/**
	 * 进度 .
	 */
	private BigDecimal completePct = new BigDecimal(0);

	/**
	 * 总浮时 .
	 */
	private BigDecimal totalFloatDrtn = new BigDecimal(0);

	/**
	 * 自由浮时 .
	 */
	private BigDecimal freeFloatDrtn = new BigDecimal(0);

	/**
	 * 尚需浮时 .
	 */
	private BigDecimal remainFloatDrtn = new BigDecimal(0);

	/**
	 * 计划下的所属任务
	 */
	private List<PmTask> tasks;

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
	 * @return projId
	 */
	public String getProjId() {
		return this.projId;
	}

	/**
	 * @param projId 要设置的 projId
	 */
	public void setProjId(final String projId) {
		this.projId = projId;
	}

	/**
	 * @return planCode
	 */
	public String getPlanCode() {
		return planCode;
	}

	/**
	 *
	 * @param planCode 要设置的 planCode
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	/**
	 * @return planName
	 */
	public String getPlanName() {
		return this.planName;
	}

	/**
	 * @param planName 要设置的 planName
	 */
	public void setPlanName(final String planName) {
		this.planName = planName;
	}

	/**
	 * @return calendarId
	 */
	public String getCalendarId() {
		return this.calendarId;
	}

	/**
	 * @param calendarId 要设置的 calendarId
	 */
	public void setCalendarId(final String calendarId) {
		this.calendarId = calendarId;
	}

	/**
	 * @return calcDate
	 */
	public Date getCalcDate() {
		return this.calcDate;
	}

	/**
	 * @param calcDate 要设置的 calcDate
	 */
	public void setCalcDate(final Date calcDate) {
		this.calcDate = calcDate;
	}

	/**
	 * @return planStartDate
	 */
	public Date getPlanStartDate() {
		return this.planStartDate;
	}

	/**
	 * @param planStartDate 要设置的 planStartDate
	 */
	public void setPlanStartDate(final Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	/**
	 * @return planEndDate
	 */
	public Date getPlanEndDate() {
		return this.planEndDate;
	}

	/**
	 * @param planEndDate 要设置的 planEndDate
	 */
	public void setPlanEndDate(final Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	/**
	 * @return earlyStartDate
	 */
	public Date getEarlyStartDate() {
		return this.earlyStartDate;
	}

	/**
	 * @param earlyStartDate 要设置的 earlyStartDate
	 */
	public void setEarlyStartDate(final Date earlyStartDate) {
		this.earlyStartDate = earlyStartDate;
	}

	/**
	 * @return earlyEndDate
	 */
	public Date getEarlyEndDate() {
		return this.earlyEndDate;
	}

	/**
	 * @param earlyEndDate 要设置的 earlyEndDate
	 */
	public void setEarlyEndDate(final Date earlyEndDate) {
		this.earlyEndDate = earlyEndDate;
	}

	/**
	 * @return lateStartDate
	 */
	public Date getLateStartDate() {
		return this.lateStartDate;
	}

	/**
	 * @param lateStartDate 要设置的 lateStartDate
	 */
	public void setLateStartDate(final Date lateStartDate) {
		this.lateStartDate = lateStartDate;
	}

	/**
	 * @return lateEndDate
	 */
	public Date getLateEndDate() {
		return this.lateEndDate;
	}

	/**
	 * @param lateEndDate 要设置的 lateEndDate
	 */
	public void setLateEndDate(final Date lateEndDate) {
		this.lateEndDate = lateEndDate;
	}

	/**
	 * @return remEarlyStart
	 */
	public Date getRemEarlyStart() {
		return this.remEarlyStart;
	}

	/**
	 * @param remEarlyStart 要设置的 remEarlyStart
	 */
	public void setRemEarlyStart(final Date remEarlyStart) {
		this.remEarlyStart = remEarlyStart;
	}

	/**
	 * @return remEarlyEnd
	 */
	public Date getRemEarlyEnd() {
		return this.remEarlyEnd;
	}

	/**
	 * @param remEarlyEnd 要设置的 remEarlyEnd
	 */
	public void setRemEarlyEnd(final Date remEarlyEnd) {
		this.remEarlyEnd = remEarlyEnd;
	}

	/**
	 * @return remLateStart
	 */
	public Date getRemLateStart() {
		return this.remLateStart;
	}

	/**
	 * @param remLateStart 要设置的 remLateStart
	 */
	public void setRemLateStart(final Date remLateStart) {
		this.remLateStart = remLateStart;
	}

	/**
	 * @return remLateEnd
	 */
	public Date getRemLateEnd() {
		return this.remLateEnd;
	}

	/**
	 * @param remLateEnd 要设置的 remLateEnd
	 */
	public void setRemLateEnd(final Date remLateEnd) {
		this.remLateEnd = remLateEnd;
	}

	/**
	 * @return actStartDate
	 */
	public Date getActStartDate() {
		return this.actStartDate;
	}

	/**
	 * @param actStartDate 要设置的 actStartDate
	 */
	public void setActStartDate(final Date actStartDate) {
		this.actStartDate = actStartDate;
	}

	/**
	 * @return actEndDate
	 */
	public Date getActEndDate() {
		return this.actEndDate;
	}

	/**
	 * @param actEndDate 要设置的 actEndDate
	 */
	public void setActEndDate(final Date actEndDate) {
		this.actEndDate = actEndDate;
	}

	/**
	 * @return planDrtn
	 */
	public BigDecimal getPlanDrtn() {
		return this.planDrtn;
	}

	/**
	 * @param planDrtn 要设置的 planDrtn
	 */
	public void setPlanDrtn(final BigDecimal planDrtn) {
		this.planDrtn = planDrtn;
	}

	/**
	 * @return actDrtn
	 */
	public BigDecimal getActDrtn() {
		return this.actDrtn;
	}

	/**
	 * @param actDrtn 要设置的 actDrtn
	 */
	public void setActDrtn(final BigDecimal actDrtn) {
		this.actDrtn = actDrtn;
	}

	/**
	 * @return remainDrtn
	 */
	public BigDecimal getRemainDrtn() {
		return this.remainDrtn;
	}

	/**
	 * @param remainDrtn 要设置的 remainDrtn
	 */
	public void setRemainDrtn(final BigDecimal remainDrtn) {
		this.remainDrtn = remainDrtn;
	}

	/**
	 * @return estWt
	 */
	public BigDecimal getEstWt() {
		return this.estWt;
	}

	/**
	 * @param estWt 要设置的 estWt
	 */
	public void setEstWt(final BigDecimal estWt) {
		this.estWt = estWt;
	}

	/**
	 * @return completePct
	 */
	public BigDecimal getCompletePct() {
		return this.completePct;
	}

	/**
	 * @param completePct 要设置的 completePct
	 */
	public void setCompletePct(final BigDecimal completePct) {
		this.completePct = completePct;
	}

	/**
	 * @return totalFloatDrtn
	 */
	public BigDecimal getTotalFloatDrtn() {
		return this.totalFloatDrtn;
	}

	/**
	 * @param totalFloatDrtn 要设置的 totalFloatDrtn
	 */
	public void setTotalFloatDrtn(final BigDecimal totalFloatDrtn) {
		this.totalFloatDrtn = totalFloatDrtn;
	}

	/**
	 * @return freeFloatDrtn
	 */
	public BigDecimal getFreeFloatDrtn() {
		return this.freeFloatDrtn;
	}

	/**
	 * @param freeFloatDrtn 要设置的 freeFloatDrtn
	 */
	public void setFreeFloatDrtn(final BigDecimal freeFloatDrtn) {
		this.freeFloatDrtn = freeFloatDrtn;
	}

	/**
	 * @return remainFloatDrtn
	 */
	public BigDecimal getRemainFloatDrtn() {
		return this.remainFloatDrtn;
	}

	/**
	 * @param remainFloatDrtn 要设置的 remainFloatDrtn
	 */
	public void setRemainFloatDrtn(final BigDecimal remainFloatDrtn) {
		this.remainFloatDrtn = remainFloatDrtn;
	}

	/**
	 * @return the tasks
	 */
	public List<PmTask> getTasks() {
		return this.tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(final List<PmTask> tasks) {
		this.tasks = tasks;
	}

}
