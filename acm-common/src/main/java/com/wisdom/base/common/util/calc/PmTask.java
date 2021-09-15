package com.wisdom.base.common.util.calc;

import com.wisdom.base.common.util.calc.calendar.Tools;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PmTask implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * WBS
	 */
	public final static String TASK_TYPE_WBS = "WBS";

	/**
	 * 普通任务，资源使用作业日期计算
	 */
	public final static String TASK_TYPE_TASK = "Task";

	/**
	 * 任务状态(编制中)
	 */
	public final static String TASK_STATUS_EDIT = "Edit";

	/**
	 * 任务状态(确认)
	 */
	public final static String TASK_STATUS_CONFIRM = "Confirm";

	/**
	 * 任务状态(发布）
	 */
	public final static String TASK_STATUS_RELEASE = "Release";

	/**
	 * 资源任务， 资源使用自已的日历计算
	 */
	public final static String TASK_TYPE_TASKRSRC = "TaskRsrc";

	/**
	 * 开始里程碑
	 */
	public final static String TASK_TYPE_SARTMILE = "SartMile";

	/**
	 * 完成里程碑
	 */
	public final static String TASK_TYPE_ENDMILE = "EndMile";

	/**
	 * 必须开始于(强制开始)
	 */
	public final static String CONSTRAINT_TYPE_MUST_START = "MustStart"; //1;

	/**
	 * 必须完成于(强制完成)
	 */
	public final static String CONSTRAINT_TYPE_MUST_FINISH = "MustFinish"; //2;

	/**
	 * 不得早于...开始(开始不早于)
	 */
	public final static String CONSTRAINT_TYPE_START_NO_EARLIER = "StartNoEarlier"; //3;

	/**
	 * 不得晚于...开始(开始不晚于)
	 */
	public final static String CONSTRAINT_TYPE_START_NO_LATER = "StartNoLater"; //4;

	/**
	 * 不得早于...完成(完成不早于)
	 */
	public final static String CONSTRAINT_TYPE_FINISH_NO_EARLIER = "FinishNoEarlier"; //5;

	/**
	 * 不得晚于...完成(完成不晚于)
	 */
	public final static String CONSTRAINT_TYPE_FINISH_NO_LATER = "FinishNoLater"; //6;

	/**
	 * 主键 .
	 */
	private String id;

	/**
	 * 代码 .
	 */
	private String taskCode;

	/**
	 * 名称 .
	 */
	private String taskName;

	/**
	 * 类型 . WBS、Task、SartMile、 EndMile
	 */
	private String taskType;

	/**
	 * 主键 .
	 */
	private String parentId;

	/**
	 * 计划主键 .
	 */
	private String planId;

	/**
	 * 项目主键 .
	 */
	private String projId;

	/**
	 * 日历 .
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
	 * 完成时工期 .
	 */
	private BigDecimal completeDrtn = new BigDecimal(0);

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
	 * 权重 .
	 */
	private BigDecimal estWt = new BigDecimal(0);

	/**
	 * 进度 .
	 */
	private BigDecimal completePct = new BigDecimal(0);

	/**
	 * 停工日期 .
	 */
	private Date suspendDate;

	/**
	 * 复工日期 .
	 */
	private Date resumeDate;

	/**
	 * 估计完成 .
	 */
	private Date maybeEndDate;

	/**
	 * 限制类型
	 */
	private String constraintType;

	/**
	 * 限制日期 .
	 */
	private Date constraintDate;

	/**
	 * 截止日期 .
	 */
	private Date feedbackDate;

	/**
	 * 关键任务 值:Y .
	 */
	private String criticalPath;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 任务的紧前任务
	 */
	private List<PmTaskPred> taskPreds = new ArrayList<>();

	/**
	 * 表示任务紧前任务的个数，用于正推生产者进程的计算
	 */
	private int taskPredCount;

	/**
	 * 任务的紧后任务
	 */
	private List<PmTaskPred> taskFllows = new ArrayList<>();

	/**
	 * 表示任务紧后任务的个数，用于逆推生产者进程的计算
	 */
	private int taskFllowCount;

	/**
	 * 任务的资源
	 */
	private List<PmTaskRsrc> taskRsrcs = new ArrayList<>();

	/**
	 * 驱控任务的资源
	 */
	private List<PmTaskRsrc> driveTaskRsrcs = new ArrayList<>();

	/**
	 * 子任务
	 */
	private List<PmTask> taskChilds = new ArrayList<>();

	/**
	 * 表示子任务的个数
	 */
	private int taskChildCount;

	/*
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
	 * @return taskCode
	 */
	public String getTaskCode() {
		return this.taskCode;
	}

	/**
	 * @param taskCode 要设置的 taskCode
	 */
	public void setTaskCode(final String taskCode) {
		this.taskCode = taskCode;
	}

	/**
	 * @return taskName
	 */
	public String getTaskName() {
		return this.taskName;
	}

	/**
	 * @param taskName 要设置的 taskName
	 */
	public void setTaskName(final String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return taskType
	 */
	public String getTaskType() {
		return this.taskType;
	}

	/**
	 * @param taskType 要设置的 taskType
	 */
	public void setTaskType(final String taskType) {
		if (!",WBS,Task,TaskRsrc,SartMile,EndMile,".contains("," + taskType + ",")) {
			throw new RuntimeException("taskType = WBS, Task, TaskRsrc, SartMile, EndMile");
		}
		this.taskType = taskType;
	}

	/**
	 * @return parentId
	 */
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * @param parentId 要设置的 parentId
	 */
	public void setParentId(final String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return planId
	 */
	public String getPlanId() {
		return this.planId;
	}

	/**
	 * @param planId 要设置的 planId
	 */
	public void setPlanId(final String planId) {
		this.planId = planId;
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
	 * @return suspendDate
	 */
	public Date getSuspendDate() {
		return this.suspendDate;
	}

	/**
	 * @param suspendDate 要设置的 suspendDate
	 */
	public void setSuspendDate(final Date suspendDate) {
		this.suspendDate = suspendDate;
	}

	/**
	 * @return resumeDate
	 */
	public Date getResumeDate() {
		return this.resumeDate;
	}

	/**
	 * @param resumeDate 要设置的 resumeDate
	 */
	public void setResumeDate(final Date resumeDate) {
		this.resumeDate = resumeDate;
	}

	/**
	 * @return maybeEndDate
	 */
	public Date getMaybeEndDate() {
		return this.maybeEndDate;
	}

	/**
	 * @param maybeEndDate 要设置的 maybeEndDate
	 */
	public void setMaybeEndDate(final Date maybeEndDate) {
		this.maybeEndDate = maybeEndDate;
	}

	/**
	 * @return feedbackDate
	 */
	public Date getFeedbackDate() {
		return this.feedbackDate;
	}

	/**
	 * @param feedbackDate 要设置的 feedbackDate
	 */
	public void setFeedbackDate(final Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	/**
	 * @return the constraintType
	 */
	public String getConstraintType() {
		return this.constraintType;
	}

	/**
	 * @param constraintType the constraintType to set
	 */
	public void setConstraintType(final String constraintType) {
		if (!Tools.isEmpty(constraintType) && !",MustStart,MustFinish,StartNoEarlier,StartNoLater,FinishNoEarlier,FinishNoLater,".contains("," + constraintType + ",")) {
			throw new RuntimeException("taskType = MustStart, MustFinish, StartNoEarlier, StartNoLater, FinishNoEarlier, FinishNoLater");
		}
		this.constraintType = constraintType;
	}

	/**
	 * @return the constraintDate
	 */
	public Date getConstraintDate() {
		return this.constraintDate;
	}

	/**
	 * @param constraintDate the constraintDate to set
	 */
	public void setConstraintDate(final Date constraintDate) {
		this.constraintDate = constraintDate;
	}

	/**
	 * @return criticalPath
	 */
	public String getCriticalPath() {
		return this.criticalPath;
	}

	/**
	 * @param criticalPath 要设置的 criticalPath
	 */
	public void setCriticalPath(final String criticalPath) {
		this.criticalPath = criticalPath;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @param status 要设置的 status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return the taskPreds
	 */
	public List<PmTaskPred> getTaskPreds() {
		return this.taskPreds;
	}

	/**
	 * @param taskPreds the taskPreds to set
	 */
	public void setTaskPreds(final List<PmTaskPred> taskPreds) {
		this.taskPreds = taskPreds;
	}

	/**
	 * @return the taskPredCount
	 */
	public int getTaskPredCount() {
		return this.taskPredCount;
	}

	/**
	 * @param taskPredCount the taskPredCount to set
	 */
	public void setTaskPredCount(final int taskPredCount) {
		this.taskPredCount = taskPredCount;
	}

	/**
	 * 紧前任务总数计数器减一
	 */
	public synchronized void taskPredCountDown() {
		this.taskPredCount = this.taskPredCount - 1;
	}

	/**
	 * @return the taskFllows
	 */
	public List<PmTaskPred> getTaskFllows() {
		return this.taskFllows;
	}

	/**
	 * @param taskFllows the taskFllows to set
	 */
	public void setTaskFllows(final List<PmTaskPred> taskFllows) {
		this.taskFllows = taskFllows;
	}

	/**
	 * @return the taskFllowCount
	 */
	public int getTaskFllowCount() {
		return this.taskFllowCount;
	}

	/**
	 * @param taskFllowCount the taskFllowCount to set
	 */
	public void setTaskFllowCount(final int taskFllowCount) {
		this.taskFllowCount = taskFllowCount;
	}

	/**
	 * 后续任务计数器减一
	 */
	public synchronized void taskFllowCountDown() {
		this.taskFllowCount = this.taskFllowCount - 1;
	}

	/**
	 * @return the taskRsrcs
	 */
	public List<PmTaskRsrc> getTaskRsrcs() {
		return this.taskRsrcs;
	}

	/**
	 * @param taskRsrcs the taskRsrcs to set
	 */
	public void setTaskRsrcs(final List<PmTaskRsrc> taskRsrcs) {
		this.taskRsrcs = taskRsrcs;
	}

	/**
	 * @return the driveTaskRsrcs
	 */
	public List<PmTaskRsrc> getDriveTaskRsrcs() {
		return this.driveTaskRsrcs;
	}

	/**
	 * @param driveTaskRsrcs the driveTaskRsrcs to set
	 */
	public void setDriveTaskRsrcs(final List<PmTaskRsrc> driveTaskRsrcs) {
		this.driveTaskRsrcs = driveTaskRsrcs;
	}

	/**
	 * @return the taskChilds
	 */
	public List<PmTask> getTaskChilds() {
		return this.taskChilds;
	}

	/**
	 * @param taskChilds the taskChilds to set
	 */
	public void setTaskChilds(final List<PmTask> taskChilds) {
		this.taskChilds = taskChilds;
	}

	/**
	 * @return the taskChildCount
	 */
	public int getTaskChildCount() {
		return this.taskChildCount;
	}

	/**
	 * @param taskChildCount the taskChildCount to set
	 */
	public void setTaskChildCount(final int taskChildCount) {
		this.taskChildCount = taskChildCount;
	}

	/**
	 * 任务总数计数器减一
	 */
	public synchronized void taskChildCountDown() {
		this.taskChildCount = this.taskChildCount - 1;
	}

	/**
	 * toString
	 */
	@Override
	public String toString() {
		String s = "id: " + this.getId() //
				+ "\tCode: " + this.getTaskCode() + "(" + this.getTaskType() + ")" //
				+ "\tRemainDrtn: " + this.getRemainDrtn()//
				+ "\nEarlyStart: " + Tools.toDateTimeString(this.getEarlyStartDate()) //
				+ "\tEarlyEnd: " + Tools.toDateTimeString(this.getEarlyEndDate()) //
				+ "\tLateStart: " + Tools.toDateTimeString(this.getLateStartDate()) //
				+ "\tLateEnd: " + Tools.toDateTimeString(this.getLateEndDate()) //
				+ "\tTotalFloat: " + this.getTotalFloatDrtn() //
				+ "\tFreeFloat: " + this.getFreeFloatDrtn() //
				+ "\tCriticalPath: " + this.getCriticalPath();
		return s;
	}

}
