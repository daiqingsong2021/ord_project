package com.wisdom.acm.wf.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.wf.vo.UnfinshTaskVo;
import com.wisdom.base.common.form.WfFinishSearchForm;
import com.wisdom.base.common.form.WfMineSearchForm;
import com.wisdom.base.common.form.WfUnFinishSearchForm;
import com.wisdom.base.common.vo.wf.MyFinishTaskVo;
import com.wisdom.base.common.vo.wf.MyUnFinishTaskVo;
import com.wisdom.base.common.vo.wf.WfMineTaskVo;
import com.wisdom.base.common.vo.wf.WfTaskDetailVo;

import java.text.ParseException;


public interface WfTaskService {

	/**
	 * 我的待只数量
	 *
	 * @param userId
	 * @return
	 */
	Long queryWfUnfinishCount(String userId);

	/**
	 * 我的待办条数
	 *
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	UnfinshTaskVo queryWfUnfinshTaskMessage(String userId) throws ParseException;

	/**
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @param wfUnFinishSearchForm
	 * @return
	 */
	PageInfo<MyUnFinishTaskVo> queryWfUnfinshTaskList(Integer pageSize, Integer pageNum, String userId, WfUnFinishSearchForm wfUnFinishSearchForm);


	/**
	 * 我的已办
	 *
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	PageInfo<MyFinishTaskVo> queryWfFinshTaskList(Integer pageSize, Integer pageNum, String userId, WfFinishSearchForm form);

	/**
	 * 我发起的
	 *
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	PageInfo<WfMineTaskVo> queryWfMineTaskList(Integer pageSize, Integer pageNum, String userId, WfMineSearchForm form);

	/**
	 * 查询流程任务ID
	 * @param taskId　任务ID
	 * @return　任务详情
	 */
	WfTaskDetailVo getTaskByTaskId(String taskId, String userId);

	/**
	 * 查询流程任务ID
	 * @param taskId　任务ID
	 * @return　任务详情
	 */
	WfTaskDetailVo getTaskByProcInstId(String taskId, String userId);
}
