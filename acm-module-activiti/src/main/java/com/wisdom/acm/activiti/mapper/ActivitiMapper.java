package com.wisdom.acm.activiti.mapper;

import com.wisdom.acm.activiti.po.WorkflowPo;
import com.wisdom.acm.activiti.vo.IdentityInfoVo;
import com.wisdom.acm.activiti.vo.ProcWaitWorkVo;
import com.wisdom.acm.activiti.vo.TaskStartTimeVo;
import com.wisdom.acm.activiti.vo.WorkFlowLogVo;
import com.wisdom.base.common.form.WfMineSearchForm;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.base.ActModelVo;
import com.wisdom.base.common.vo.wf.WfMineTaskVo;
import com.wisdom.base.common.vo.wf.WfProcLogVo;
import com.wisdom.base.common.vo.wf.WfProcessDefVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivitiMapper extends CommMapper<WorkflowPo> {

	List<WfMineTaskVo> selectWfMineTaskList(@Param("userId") String userId, @Param("search") WfMineSearchForm search);

	List<IdentityInfoVo> selectNextUserIdByTaskIds(@Param("taskIds") List<String> taskIds);

	List<ProcWaitWorkVo> selectProcessWaitWork(@Param("procInstIds") List<String> procInstIds);

	List<TaskStartTimeVo> selectTaskStartTimeByProcInsId(@Param("procInstId") String procInstId);

	List<WorkFlowLogVo> selectWorkFlowLogByProcInsId(@Param("procInstId") String procInstId);

	List<WfProcLogVo> selectWorkFlowInfoByProcInstIds(@Param("procInstIds") List<String> procInstIds);

	List<WfProcLogVo> selectWorkFlowInfo2ByProcInstIds(@Param("procInstIds") List<String> procInstIds);

	List<String> selectTaskIdByCurrentUserIdAndProcInstrId(@Param("procInstId") String procInstId);

	List<ActModelVo> selectActAssignList(@Param("modelIds") List<String> modelIds);

	/**
	 * 根据模型查询流程业务
	 *
	 * @param modelIds 模型ID
	 * @return 流程定义
	 */
	List<WfProcessDefVo> selectProcDefByModulIdList(@Param("modelIds") List<String> modelIds);

	/**
	 * 查询流程监听配置
	 * @return
	 */
	String selectWfListenerConfigureUrl();
}
