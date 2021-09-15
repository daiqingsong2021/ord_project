package com.wisdom.base.common.util.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PmTaskRsrc implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 人工资源
	 */
	public final static String TASK_TYPE_LABOR = "Labor";

	/**
	 * 设备资源
	 */
	public final static String TASK_TYPE_EQUIP = "Equip";

	/**
	 * 材料资源
	 */
	public final static String TASK_TYPE_MAT = "Mat";

	/**
	 * 主键 .
	 */
	private String id;

	/**
	 * 代码 .
	 */
	private String rsrcCode;

	/**
	 * 名称 .
	 */
	private String rsrcName;

	/**
	 * 类型
	 */
	private String rsrcType;

	/**
	 * 任务主键 .
	 */
	private String taskId;

	/**
	 * 日历 .
	 */
	private String calendarId;

	/**
	 * 驱控作业日期
	 */
	private boolean driveTaskDate;

	/**
	 * 计划开始 .
	 */
	private Date planStartDate;

	/**
	 * 计划完成 .
	 */
	private Date planEndDate;

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
	 * 预计工时
	 */
	private BigDecimal work = new BigDecimal(0);

	/**
	 * 实际工时
	 */
	private BigDecimal actualWork = new BigDecimal(0);

	/**
	 * 尚需工时
	 */
	private BigDecimal remainingWork = new BigDecimal(0);

	/**
	 * 进度 .
	 */
	private BigDecimal completePct = new BigDecimal(0);

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return the rsrcCode
	 */
	public String getRsrcCode() {
		return this.rsrcCode;
	}

	/**
	 * @param rsrcCode the rsrcCode to set
	 */
	public void setRsrcCode(final String rsrcCode) {
		this.rsrcCode = rsrcCode;
	}

	/**
	 * @return the rsrcName
	 */
	public String getRsrcName() {
		return this.rsrcName;
	}

	/**
	 * @param rsrcName the rsrcName to set
	 */
	public void setRsrcName(final String rsrcName) {
		this.rsrcName = rsrcName;
	}

	/**
	 * @return the rsrcType
	 */
	public String getRsrcType() {
		return this.rsrcType;
	}

	/**
	 * @param rsrcType the rsrcType to set
	 */
	public void setRsrcType(final String rsrcType) {
		this.rsrcType = rsrcType;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return this.taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(final String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the calendarId
	 */
	public String getCalendarId() {
		return this.calendarId;
	}

	/**
	 * @param calendarId the calendarId to set
	 */
	public void setCalendarId(final String calendarId) {
		this.calendarId = calendarId;
	}

	/**
	 * @return the driveControlTaskDate
	 */
	public boolean getDriveTaskDate() {
		return this.driveTaskDate;
	}

	/**
	 * @param driveTaskDate the driveTaskDate to set
	 */
	public void setDriveTaskDate(final boolean driveTaskDate) {
		this.driveTaskDate = driveTaskDate;
	}

	/**
	 * @return the planStartDate
	 */
	public Date getPlanStartDate() {
		return this.planStartDate;
	}

	/**
	 * @param planStartDate the planStartDate to set
	 */
	public void setPlanStartDate(final Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	/**
	 * @return the planEndDate
	 */
	public Date getPlanEndDate() {
		return this.planEndDate;
	}

	/**
	 * @param planEndDate the planEndDate to set
	 */
	public void setPlanEndDate(final Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	/**
	 * @return the remEarlyStart
	 */
	public Date getRemEarlyStart() {
		return this.remEarlyStart;
	}

	/**
	 * @param remEarlyStart the remEarlyStart to set
	 */
	public void setRemEarlyStart(final Date remEarlyStart) {
		this.remEarlyStart = remEarlyStart;
	}

	/**
	 * @return the remEarlyEnd
	 */
	public Date getRemEarlyEnd() {
		return this.remEarlyEnd;
	}

	/**
	 * @param remEarlyEnd the remEarlyEnd to set
	 */
	public void setRemEarlyEnd(final Date remEarlyEnd) {
		this.remEarlyEnd = remEarlyEnd;
	}

	/**
	 * @return the remLateStart
	 */
	public Date getRemLateStart() {
		return this.remLateStart;
	}

	/**
	 * @param remLateStart the remLateStart to set
	 */
	public void setRemLateStart(final Date remLateStart) {
		this.remLateStart = remLateStart;
	}

	/**
	 * @return the remLateEnd
	 */
	public Date getRemLateEnd() {
		return this.remLateEnd;
	}

	/**
	 * @param remLateEnd the remLateEnd to set
	 */
	public void setRemLateEnd(final Date remLateEnd) {
		this.remLateEnd = remLateEnd;
	}

	/**
	 * @return the actStartDate
	 */
	public Date getActStartDate() {
		return this.actStartDate;
	}

	/**
	 * @param actStartDate the actStartDate to set
	 */
	public void setActStartDate(final Date actStartDate) {
		this.actStartDate = actStartDate;
	}

	/**
	 * @return the actEndDate
	 */
	public Date getActEndDate() {
		return this.actEndDate;
	}

	/**
	 * @param actEndDate the actEndDate to set
	 */
	public void setActEndDate(final Date actEndDate) {
		this.actEndDate = actEndDate;
	}

	/**
	 * @return the planDrtn
	 */
	public BigDecimal getPlanDrtn() {
		return this.planDrtn;
	}

	/**
	 * @param planDrtn the planDrtn to set
	 */
	public void setPlanDrtn(final BigDecimal planDrtn) {
		this.planDrtn = planDrtn;
	}

	/**
	 * @return the actDrtn
	 */
	public BigDecimal getActDrtn() {
		return this.actDrtn;
	}

	/**
	 * @param actDrtn the actDrtn to set
	 */
	public void setActDrtn(final BigDecimal actDrtn) {
		this.actDrtn = actDrtn;
	}

	/**
	 * @return the remainDrtn
	 */
	public BigDecimal getRemainDrtn() {
		return this.remainDrtn;
	}

	/**
	 * @param remainDrtn the remainDrtn to set
	 */
	public void setRemainDrtn(final BigDecimal remainDrtn) {
		this.remainDrtn = remainDrtn;
	}

	/**
	 * @return the work
	 */
	public BigDecimal getWork() {
		return this.work;
	}

	/**
	 * @param work the work to set
	 */
	public void setWork(final BigDecimal work) {
		this.work = work;
	}

	/**
	 * @return the actualWork
	 */
	public BigDecimal getActualWork() {
		return this.actualWork;
	}

	/**
	 * @param actualWork the actualWork to set
	 */
	public void setActualWork(final BigDecimal actualWork) {
		this.actualWork = actualWork;
	}

	/**
	 * @return the remainingWork
	 */
	public BigDecimal getRemainingWork() {
		return this.remainingWork;
	}

	/**
	 * @param remainingWork the remainingWork to set
	 */
	public void setRemainingWork(final BigDecimal remainingWork) {
		this.remainingWork = remainingWork;
	}

	/**
	 * @param remEarlyStart the remEarlyStart to set
	 */
	public synchronized void setRemEarlyDate(final Date remEarlyStart, final Date remEarlyEnd) {
		this.remEarlyStart = remEarlyStart;
		this.remEarlyEnd = remEarlyEnd;
	}

	/**
	 * @param remLateStart the remLateStart to set
	 */
	public synchronized void setRemLateDate(final Date remLateStart, final Date remLateEnd) {
		this.remLateStart = remLateStart;
		this.remLateEnd = remLateEnd;
	}
}
