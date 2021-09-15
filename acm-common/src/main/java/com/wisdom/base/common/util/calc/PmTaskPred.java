package com.wisdom.base.common.util.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PmTaskPred implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 开始-完成(FS)
	 */
	public final static String RELATION_TYPE_FS = "FS";

	/**
	 * 完成-开始(SF)
	 */
	public final static String RELATION_TYPE_SF = "SF";

	/**
	 * 开始-开始(SS)
	 */
	public final static String RELATION_TYPE_SS = "SS";

	/**
	 * 完成-完成(SS)
	 */
	public final static String RELATION_TYPE_FF = "FF";

	/**
	 * 主键 .
	 */
	private String id;

	/**
	 * 任务主键 .
	 */
	private String taskId;

	/**
	 * 计划主健 .
	 */
	private String planId;

	/**
	 * 项目主键 .
	 */
	private String projId;

	/**
	 * 关系类型（FS,SF,SS,FF) .
	 */
	private String relationType;

	/**
	 * 延时 .
	 */
	private BigDecimal lagQty = new BigDecimal(0);

	/**
	 * 紧前任务 .
	 */
	private String predTaskId;

	/**
	 * 紧前计划 .
	 */
	private String predPlanId;

	/**
	 * 紧前项目 .
	 */
	private String predProjId;

	/**
	 * 主要驱动关系（求最长路径）
	 */
	private boolean mainDrive;

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
	 * @return taskId
	 */
	public String getTaskId() {
		return this.taskId;
	}

	/**
	 * @param taskId 要设置的 taskId
	 */
	public void setTaskId(final String taskId) {
		this.taskId = taskId;
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
	 * @return relationType
	 */
	public String getRelationType() {
		return this.relationType;
	}

	/**
	 * @param relationType 要设置的 relationType
	 */
	public void setRelationType(final String relationType) {
		if (!PmTaskPred.RELATION_TYPE_FS.equals(relationType) && !PmTaskPred.RELATION_TYPE_SF.equals(relationType) //
				&& (!PmTaskPred.RELATION_TYPE_SS.equals(relationType) & !PmTaskPred.RELATION_TYPE_FF .equals(relationType))) {
			throw new RuntimeException("relationType = FS, SF, SS, FF");
		}
		this.relationType = relationType;
	}

	/**
	 * @return lagQty
	 */
	public BigDecimal getLagQty() {
		return this.lagQty;
	}

	/**
	 * @param lagQty 要设置的 lagQty
	 */
	public void setLagQty(final BigDecimal lagQty) {
		this.lagQty = lagQty;
	}

	/**
	 * @return predTaskId
	 */
	public String getPredTaskId() {
		return this.predTaskId;
	}

	/**
	 * @param predTaskId 要设置的 predTaskId
	 */
	public void setPredTaskId(final String predTaskId) {
		this.predTaskId = predTaskId;
	}

	/**
	 * @return predPlanId
	 */
	public String getPredPlanId() {
		return this.predPlanId;
	}

	/**
	 * @param predPlanId 要设置的 predPlanId
	 */
	public void setPredPlanId(final String predPlanId) {
		this.predPlanId = predPlanId;
	}

	/**
	 * @return predProjId
	 */
	public String getPredProjId() {
		return this.predProjId;
	}

	/**
	 * @param predProjId 要设置的 predProjId
	 */
	public void setPredProjId(final String predProjId) {
		this.predProjId = predProjId;
	}

	/**
	 * @return the mainDrive
	 */
	public boolean getMainDrive() {
		return this.mainDrive;
	}

	/**
	 * @param mainDrive the mainDrive to set
	 */
	public void setMainDrive(final boolean mainDrive) {
		this.mainDrive = mainDrive;
	}

	/**
	 * @return the earlyStartDate
	 */
	public Date getEarlyStartDate() {
		return this.earlyStartDate;
	}

	/**
	 * @param earlyStartDate the earlyStartDate to set
	 */
	public void setEarlyStartDate(final Date earlyStartDate) {
		this.earlyStartDate = earlyStartDate;
	}

	/**
	 * @return the earlyEndDate
	 */
	public Date getEarlyEndDate() {
		return this.earlyEndDate;
	}

	/**
	 * @param earlyEndDate the earlyEndDate to set
	 */
	public void setEarlyEndDate(final Date earlyEndDate) {
		this.earlyEndDate = earlyEndDate;
	}

	/**
	 * @return the lateStartDate
	 */
	public Date getLateStartDate() {
		return this.lateStartDate;
	}

	/**
	 * @param lateStartDate the lateStartDate to set
	 */
	public void setLateStartDate(final Date lateStartDate) {
		this.lateStartDate = lateStartDate;
	}

	/**
	 * @return the lateEndDate
	 */
	public Date getLateEndDate() {
		return this.lateEndDate;
	}

	/**
	 * @param lateEndDate the lateEndDate to set
	 */
	public void setLateEndDate(final Date lateEndDate) {
		this.lateEndDate = lateEndDate;
	}
}
