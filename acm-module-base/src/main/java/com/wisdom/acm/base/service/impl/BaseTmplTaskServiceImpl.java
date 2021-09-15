package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.tmpltask.BaseTmplPlanAddForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskAddForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskUpdateForm;
import com.wisdom.acm.base.form.tmpltask.TaskTmplAddForm;
import com.wisdom.acm.base.mapper.BaseTmplPlanMapper;
import com.wisdom.acm.base.mapper.BaseTmplTaskMapper;
import com.wisdom.acm.base.po.BaseTmplPlanPo;
import com.wisdom.acm.base.po.BaseTmplTaskDelvPo;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.po.BaseTmplTaskPredPo;
import com.wisdom.acm.base.service.BaseTmplPlanService;
import com.wisdom.acm.base.service.BaseTmplTaskDelvService;
import com.wisdom.acm.base.service.BaseTmplTaskPredService;
import com.wisdom.acm.base.service.BaseTmplTaskService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplPlanVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskTreeVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.plan.task.PlanTaskTreeVo;
import com.wisdom.base.common.vo.plan.task.PlanTmplTaskDelvForm;
import com.wisdom.base.common.vo.plan.task.pred.PlanTmplTaskPredForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class BaseTmplTaskServiceImpl extends BaseService<BaseTmplTaskMapper, BaseTmplTaskPo> implements BaseTmplTaskService {

	@Autowired
	private BaseTmplPlanMapper baseTmplPlanMapper;

	@Autowired
	private BaseTmplPlanService baseTmplPlanService;

	@Autowired
	private BaseTmplTaskDelvService baseTmplTaskDelvService;

	@Autowired
	private BaseTmplTaskPredService baseTmplTaskPredService;

	@Autowired
	private LeafService leafService;

	@Override
	public List<BaseTmplTaskTreeVo> queryTmpltaskTreeList() {
		List<BaseTmplTaskTreeVo> treeList = new ArrayList<BaseTmplTaskTreeVo>();
		//查询计划模板列表
		List<BaseTmplPlanVo> allTmplPlanList = baseTmplPlanMapper.selectTmplPlanList();
		//查询WBS任务列表
		List<BaseTmplTaskVo> allTmplTaskList = this.mapper.selectTmplTaskList();
		return this.getTmpltaskTreeList(allTmplPlanList, allTmplTaskList);
	}

	@Override
	public List<BaseTmplTaskTreeVo> queryTmpltaskTreeListByTmplId(Integer id) {
		//查询计划模板列表
		List<BaseTmplPlanVo> allTmplPlanList = new ArrayList<>();
		BaseTmplPlanVo baseTmplPlanVoNew = baseTmplPlanMapper.selectTmplPlanById(id);
		allTmplPlanList.add(baseTmplPlanVoNew);
		//查询WBS任务列表
		List<BaseTmplTaskVo> allTmplTaskList = this.mapper.selectTmplTaskByTmplId(id);
		return this.getTmpltaskTreeList(allTmplPlanList, allTmplTaskList);
	}

	private List<BaseTmplTaskTreeVo> getTmpltaskTreeList(List<BaseTmplPlanVo> allTmplPlanList, List<BaseTmplTaskVo> allTmplTaskList) {
		List<BaseTmplTaskTreeVo> treeList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(allTmplPlanList)) {
			Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap = this.listTmplTaskToMap(allTmplTaskList);
			List<BaseTmplTaskTreeVo> subList = this.getChildrenTmplTask(0, tmplTaskMap);
			BaseTmplTaskTreeVo baseTmplTaskTreeVo = null;
			for (BaseTmplPlanVo baseTmplPlanVo : allTmplPlanList) {
				baseTmplTaskTreeVo = new BaseTmplTaskTreeVo();
				baseTmplTaskTreeVo.setId(baseTmplPlanVo.getId());
				baseTmplTaskTreeVo.setTaskName(baseTmplPlanVo.getTmplName());
				baseTmplTaskTreeVo.setType("tmpl");
				List<BaseTmplTaskTreeVo> subListSub = new ArrayList<>();
				if (!ObjectUtils.isEmpty(subList)) {
					for (BaseTmplTaskTreeVo tmplTaskTree : subList) {
						Integer planId = tmplTaskTree.getTmplId();
						if (planId.equals(baseTmplPlanVo.getId())) {
							subListSub.add(tmplTaskTree);
						}
					}
				}
				if (!ObjectUtils.isEmpty(subListSub) && subListSub.size() > 0) {
					baseTmplTaskTreeVo.setChildren(subListSub);
				}
				treeList.add(baseTmplTaskTreeVo);
			}
		}
		return treeList;
	}

	/**
	 * BaseTmplTaskVoList转Map
	 *
	 * @param treeNodes
	 * @return
	 */
	private Map<Integer, List<BaseTmplTaskVo>> listTmplTaskToMap(List<BaseTmplTaskVo> treeNodes) {
		Map<Integer, List<BaseTmplTaskVo>> childrenMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(treeNodes)) {
			for (BaseTmplTaskVo t : treeNodes) {
				if (childrenMap.get(t.getParentId()) == null) {
					List<BaseTmplTaskVo> l = new ArrayList<BaseTmplTaskVo>();
					l.add(t);
					childrenMap.put(t.getParentId(), l);
				} else {
					childrenMap.get(t.getParentId()).add(t);
				}
			}
		}
		return childrenMap;
	}


	@Override
	public BaseTmplTaskPo addTmplWbs(BaseTmplTaskAddForm baseTmpltaskAddForm) {
		baseTmpltaskAddForm.setTaskType("0");
		List<BaseTmplTaskPo> baseTmplTaskPos = this.queryBaseTmplTaskPoByCodeAndTaskType(baseTmpltaskAddForm.getTaskCode(), baseTmpltaskAddForm.getTmplId());
		if (!ObjectUtils.isEmpty(baseTmplTaskPos)) {
			throw new BaseException("代码不能重复!");
		}
		BaseTmplTaskPo baseTmpltaskPo = this.dozerMapper.map(baseTmpltaskAddForm, BaseTmplTaskPo.class);
		super.insert(baseTmpltaskPo);
		return baseTmpltaskPo;
	}


	@Override
	public BaseTmplTaskPo addTmplTask(BaseTmplTaskAddForm baseTmpltaskAddForm) {
//		if (ObjectUtils.isEmpty(baseTmpltaskAddForm.getTaskType())) {
//			throw new BaseException("作业类型不能为空!");
//		}
		List<BaseTmplTaskPo> baseTmplTaskPos = this.queryBaseTmplTaskPoByCodeAndTaskType(baseTmpltaskAddForm.getTaskCode(), baseTmpltaskAddForm.getTmplId());
		if (!ObjectUtils.isEmpty(baseTmplTaskPos)) {
			throw new BaseException("代码不能重复!");
		}
		BaseTmplTaskPo baseTmpltaskPo = this.dozerMapper.map(baseTmpltaskAddForm, BaseTmplTaskPo.class);
		super.insert(baseTmpltaskPo);
		return baseTmpltaskPo;
	}

	@Override
	public BaseTmplTaskVo getTmplTaskById(Integer TmpltaskId) {
       /* BaseTmplTaskPo baseTmplTaskPo = this.mapper.selectByPrimaryKey(TmpltaskId);
        if(!ObjectUtils.isEmpty(baseTmplTaskPo)){
            return this.dozerMapper.map(baseTmplTaskPo, BaseTmplTaskVo.class);
        }else {
            return null;
        }*/
		BaseTmplTaskVo baseTmplTaskVo = this.mapper.selectTmplTaskById(TmpltaskId);
		return baseTmplTaskVo;
	}

	@Override
	@AddLog(title = "修改计划模板任务", module = LoggerModuleEnum.BM_TMPL_PLAN)
	public BaseTmplTaskPo updateTmplTask(BaseTmplTaskUpdateForm baseTmpltaskUpdateForm) {
		BaseTmplTaskPo baseTmplTaskPo = this.selectById(baseTmpltaskUpdateForm.getId());
		if (baseTmplTaskPo == null) {
			throw new BaseException("修改的任务不存在!");
		}
		// 添加修改日志
		this.addChangeLogger(baseTmpltaskUpdateForm, baseTmplTaskPo);
		List<BaseTmplTaskPo> baseTmplTaskPos = this.queryBaseTmplTaskPoByCodeAndTaskType(baseTmpltaskUpdateForm.getTaskCode(), baseTmplTaskPo.getTmplId());
		if (!ObjectUtils.isEmpty(baseTmplTaskPos) && !baseTmplTaskPos.get(0).getId().equals(baseTmpltaskUpdateForm.getId())) {
			throw new BaseException("代码不能重复!");
		}
		dozerMapper.map(baseTmpltaskUpdateForm, baseTmplTaskPo);
		super.updateById(baseTmplTaskPo);
		return baseTmplTaskPo;
	}

	@Override
	@AddLog(title = "修改计划模板", module = LoggerModuleEnum.BM_TMPL_PLAN)
	public BaseTmplTaskPo updateTmplWbs(BaseTmplTaskUpdateForm baseTmpltaskUpdateForm) {
		BaseTmplTaskPo baseTmplTaskPo = this.selectById(baseTmpltaskUpdateForm.getId());
		if (baseTmplTaskPo == null) {
			throw new BaseException("修改的WBS不存在!");
		}
		// 添加修改日志
		this.addChangeLogger("修改计划模板WBS", baseTmpltaskUpdateForm, baseTmplTaskPo);
		List<BaseTmplTaskPo> baseTmplTaskPos = this.queryBaseTmplTaskPoByCodeAndTaskType(baseTmpltaskUpdateForm.getTaskCode(), baseTmplTaskPo.getTmplId());
		if (!ObjectUtils.isEmpty(baseTmplTaskPos) && !baseTmplTaskPos.get(0).getId().equals(baseTmpltaskUpdateForm.getId())) {
			throw new BaseException("代码不能重复!");
		}
		dozerMapper.map(baseTmpltaskUpdateForm, baseTmplTaskPo);
		baseTmplTaskPo.setTaskType("0");
		super.updateById(baseTmplTaskPo);
		return baseTmplTaskPo;
	}

	@Override
	public void deleteBaseTmplTask(List<Integer> ids) {
		//查询WBS任务列表
		List<BaseTmplTaskVo> allTmplTaskList = this.mapper.selectTmplTaskList();
		Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap = this.listTmplTaskToMap(allTmplTaskList);
		if (!ObjectUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				//删除选中数据
				this.mapper.deleteByPrimaryKey(id);
				//删除子节点
				this.deleteChildrenNode(id, tmplTaskMap);
			}
		}
	}

	@Override
	public void deleteTmplWbsByWbsId(Integer id) {
		//查询WBS任务列表
		List<BaseTmplTaskVo> allTmplTaskList = this.mapper.selectTmplTaskList();
		Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap = this.listTmplTaskToMap(allTmplTaskList);
		if (!ObjectUtils.isEmpty(id)) {
			//删除选中数据
			this.mapper.deleteByPrimaryKey(id);
			//删除子节点
			this.deleteChildrenNode(id, tmplTaskMap);
		} else {
			throw new BaseException("请选择一条删除!");
		}
	}


	@Override
	public void deleteByBaseTmplTaskPoId(Integer id) {
		//查询WBS任务列表
		List<BaseTmplTaskVo> allTmplTaskList = this.mapper.selectTmplTaskList();
		Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap = this.listTmplTaskToMap(allTmplTaskList);
		List<BaseTmplTaskVo> baseTmplTaskVoList = this.queryTmplTaskByTmplId(id);
		for (BaseTmplTaskVo baseTmplTaskVo : baseTmplTaskVoList) {
			this.mapper.deleteByPrimaryKey(baseTmplTaskVo.getId());
			//删除子节点
			this.deleteChildrenNode(baseTmplTaskVo.getId(), tmplTaskMap);
		}
	}

	@Override
	public List<BaseTmplTaskVo> queryTmplTaskByTmplId(Integer tmplId) {
		List<BaseTmplTaskVo> baseTmplTaskVoList = this.mapper.selectTmplTaskByTmplId(tmplId);
		return baseTmplTaskVoList;
	}

	@Override
	public void deleteBaseTmplPlan(List<Integer> ids) {
		//查询WBS任务列表
		List<BaseTmplTaskVo> allTmplTaskList = this.mapper.selectTmplTaskList();
		Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap = this.listTmplTaskToMap(allTmplTaskList);
		if (!ObjectUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				//删除计划模板
				baseTmplPlanService.deleteTmplPlanById(id);
				//删除wbs
				this.deleteByBaseTmplTaskPoId(id);
			}
		}
	}

	@Override
	public void deleteTmplPlanById(Integer id) {
		if (!ObjectUtils.isEmpty(id)) {
			//删除计划模板
			baseTmplPlanService.deleteTmplPlanById(id);
			//删除wbs
			this.deleteByBaseTmplTaskPoId(id);
		} else {
			throw new BaseException("请选择一条删除!");
		}
	}


	/**
	 * 递归删除子节点
	 *
	 * @param id
	 * @param tmplTaskMap
	 */
	private void deleteChildrenNode(Integer id, Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap) {
		List<BaseTmplTaskVo> subList = tmplTaskMap.get(id);
		if (!ObjectUtils.isEmpty(subList)) {
			for (BaseTmplTaskVo baseTmplTask : subList) {
				deleteChildrenNode(baseTmplTask.getId(), tmplTaskMap);
				this.deleteById(baseTmplTask.getId());
			}
		}

	}

	private List<BaseTmplTaskTreeVo> getChildrenTmplTask(Integer id, Map<Integer, List<BaseTmplTaskVo>> tmplTaskMap) {
		List<BaseTmplTaskVo> subList = tmplTaskMap.get(id);
		List<BaseTmplTaskTreeVo> treeList = new ArrayList<BaseTmplTaskTreeVo>();
		if (!ObjectUtils.isEmpty(subList)) {
			BaseTmplTaskTreeVo tmplTask = null;
			for (BaseTmplTaskVo tmplTaskVo : subList) {
				tmplTask = new BaseTmplTaskTreeVo();
				tmplTask.setId(tmplTaskVo.getId());
				//tmplTask.setParentId(tmplTaskVo.getParentId()==0?tmplTaskVo.getTmplId():tmplTaskVo.getParentId());
				tmplTask.setParentId(tmplTaskVo.getParentId());
				tmplTask.setTmplId(tmplTaskVo.getTmplId());
				tmplTask.setTaskName(tmplTaskVo.getTaskName());
				tmplTask.setTaskCode(tmplTaskVo.getTaskCode());
				tmplTask.setPlanDrtn(tmplTaskVo.getPlanDrtn());
				tmplTask.setPlanQty(tmplTaskVo.getPlanQty());
				tmplTask.setPlanLevel(tmplTaskVo.getPlanLevel());
				tmplTask.setPlanType(tmplTaskVo.getPlanType());
				DictionaryVo dictionaryVo = new DictionaryVo();
				if ((!ObjectUtils.isEmpty(tmplTaskVo.getTaskType())) && (!ObjectUtils.isEmpty(tmplTaskVo.getTaskType().getId())) && ("0".equals(tmplTaskVo.getTaskType().getId()))) {
					dictionaryVo.setId("0");
					dictionaryVo.setName("wbs");
					tmplTask.setType("wbs");
					tmplTask.setTaskType(dictionaryVo);
				} else {
					tmplTask.setTaskType(tmplTaskVo.getTaskType());
					tmplTask.setType("task");
				}
				tmplTask.setDrtnType(tmplTaskVo.getDrtnType());
				tmplTask.setIsFeedback(tmplTaskVo.getIsFeedback());
				tmplTask.setControlAccount(tmplTaskVo.getControlAccount());
				tmplTask.setRemark(tmplTaskVo.getRemark());
				//查询子节点
				List<BaseTmplTaskTreeVo> children = this.getChildrenTmplTask(tmplTaskVo.getId(), tmplTaskMap);
				if (!ObjectUtils.isEmpty(children) && children.size() > 0) {
					tmplTask.setChildren(children);
				}
				treeList.add(tmplTask);
			}
		}
		return treeList;
	}

	public List<BaseTmplTaskPo> queryBaseTmplTaskPoByCodeAndTaskType(String taskCode, Integer tmplId) {
		Example example = new Example(BaseTmplTaskPo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("taskCode", taskCode);
		criteria.andEqualTo("tmplId", tmplId);
		List<BaseTmplTaskPo> baseTmplTaskPos = this.selectByExample(example);
		return !ObjectUtils.isEmpty(baseTmplTaskPos) ? baseTmplTaskPos : null;
	}

	public List<BaseTmplTaskPo> queryBaseTmplTaskPoByCodeAndTaskTypes(List<String> taskCodes,Integer tmplId){
		Example example = new Example(BaseTmplTaskPo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("taskCode", taskCodes);
		criteria.andEqualTo("tmplId", tmplId);
		List<BaseTmplTaskPo> baseTmplTaskPos = this.selectByExample(example);
		return !ObjectUtils.isEmpty(baseTmplTaskPos) ? baseTmplTaskPos : null;
	}
	/**
	 * 保存为计划模版
	 * @param taskTmplAddForm
	 */
	@Override
	public void savePlanTaskTmplByDefineId(TaskTmplAddForm taskTmplAddForm, UserInfo userInfo){

		BaseTmplPlanAddForm baseTmplPlanAddForm = new BaseTmplPlanAddForm();
		baseTmplPlanAddForm.setTmplName(taskTmplAddForm.getTmplName());
		baseTmplPlanAddForm.setIsGlobal(taskTmplAddForm.getIsGlobal());
		BaseTmplPlanPo baseTmplPlanPo = baseTmplPlanService.addTmplPlan(baseTmplPlanAddForm);
		List<PlanTaskTreeVo> taskLists = taskTmplAddForm.getTaskLists();

		int inStart = leafService.getId(taskLists.size());
		Map<Integer,Integer> newIdMap = new HashMap<>();
		if(!ObjectUtils.isEmpty(taskLists)){
			//新老id映射
			for(PlanTaskTreeVo planTaskTreeVo : taskLists){
				newIdMap.put(planTaskTreeVo.getId(),inStart);
				inStart++;
			}
			List<BaseTmplTaskPo> baseTmplTaskPoList = new ArrayList<>();
			//插入任务
			for(PlanTaskTreeVo planTaskTreeVo : taskLists){
				BaseTmplTaskPo baseTmpltaskPo = new BaseTmplTaskPo();
				int newId = newIdMap.get(planTaskTreeVo.getId());
				baseTmpltaskPo.setId(newId);
				baseTmpltaskPo.setTmplId(baseTmplPlanPo.getId());
				int newParentId = !ObjectUtils.isEmpty(newIdMap.get(planTaskTreeVo.getParentId())) ? newIdMap.get(planTaskTreeVo.getParentId()) : 0;
				baseTmpltaskPo.setParentId(newParentId);
				baseTmpltaskPo.setTaskCode(planTaskTreeVo.getCode());
				baseTmpltaskPo.setTaskName(planTaskTreeVo.getName());
				baseTmpltaskPo.setPlanDrtn(planTaskTreeVo.getPlanDrtn());
				baseTmpltaskPo.setPlanQty(planTaskTreeVo.getPlanQty());
				baseTmpltaskPo.setPlanType(!ObjectUtils.isEmpty(planTaskTreeVo.getPlanType()) ? planTaskTreeVo.getPlanType().getId() : null);
				baseTmpltaskPo.setPlanLevel(!ObjectUtils.isEmpty(planTaskTreeVo.getPlanLevel()) ? planTaskTreeVo.getPlanLevel().getId() : null);
				baseTmpltaskPo.setIsFeedback(planTaskTreeVo.getIsFeedback());
				baseTmpltaskPo.setControlAccount(planTaskTreeVo.getControlAccount());
				baseTmpltaskPo.setTaskType(FormatUtil.toString(planTaskTreeVo.getTaskType()));
				baseTmpltaskPo.setDrtnType(!ObjectUtils.isEmpty(planTaskTreeVo.getTaskDrtnType()) ? planTaskTreeVo.getTaskDrtnType().getId() : null);
				baseTmpltaskPo.setRemark(planTaskTreeVo.getRemark());
				baseTmpltaskPo.setStatus(planTaskTreeVo.getStatus().getId());
				baseTmpltaskPo.setSort(planTaskTreeVo.getSortNum());
				baseTmplTaskPoList.add(baseTmpltaskPo);
			}
			 this.insert(baseTmplTaskPoList);

			//插入逻辑关系
			List<PlanTmplTaskPredForm> tmplTaskPredVoList = taskTmplAddForm.getTaskPredLists();
			if(!ObjectUtils.isEmpty(tmplTaskPredVoList)){
				List<BaseTmplTaskPredPo> baseTmplTaskPredPoList = new ArrayList<>();
				for(PlanTmplTaskPredForm tmplTaskPredVo : tmplTaskPredVoList){
					BaseTmplTaskPredPo baseTmplTaskPredPo = new BaseTmplTaskPredPo();
					baseTmplTaskPredPo.setTaskId(newIdMap.get(tmplTaskPredVo.getTaskId()));
					baseTmplTaskPredPo.setPredTaskId(newIdMap.get(tmplTaskPredVo.getPredTaskId()));
					baseTmplTaskPredPo.setRelationType(tmplTaskPredVo.getRelationType());
					baseTmplTaskPredPo.setLagQty(tmplTaskPredVo.getLagQty());
					baseTmplTaskPredPoList.add(baseTmplTaskPredPo);
				}
				baseTmplTaskPredService.addPlanTmplTaskPred(baseTmplTaskPredPoList);
			}
			//插入交付物
			List<PlanTmplTaskDelvForm> tmplTaskDelvVoList = taskTmplAddForm.getTaskDelvLists();
			if(!ObjectUtils.isEmpty(tmplTaskDelvVoList)){
				List<BaseTmplTaskDelvPo> baseTmplTaskDelvPoList = new ArrayList<>();
				for(PlanTmplTaskDelvForm tmplTaskDelvVo : tmplTaskDelvVoList){
					tmplTaskDelvVo.setTaskId(newIdMap.get(tmplTaskDelvVo.getTaskId()));
					BaseTmplTaskDelvPo baseTmplTaskDelvPo = this.dozerMapper.map(tmplTaskDelvVo, BaseTmplTaskDelvPo.class);
					baseTmplTaskDelvPoList.add(baseTmplTaskDelvPo);
				}
				baseTmplTaskDelvService.addPlanTmplTaskDelv(baseTmplTaskDelvPoList);
			}
		}
	}
}
