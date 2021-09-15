package com.wisdom.acm.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.acm.activiti.service.ActivitiService;
import com.wisdom.acm.activiti.util.Resp;
import com.wisdom.acm.activiti.util.TimeFormat;
import com.wisdom.acm.activiti.po.InitData;
import com.wisdom.acm.activiti.po.LeaveInfo;
import com.wisdom.acm.activiti.po.User;
import com.wisdom.acm.activiti.service.LeaveService;
import com.wisdom.acm.activiti.service.ModelTestService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HtmlController extends GlobalController {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ModelTestService modelTestService;

	@Autowired
	LeaveService leaveService;

	@Autowired
	ActivitiService activitiService;

	/**
	 * 从网关进来
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modeler")
	public String getModeler(Model model, HttpServletResponse response, HttpServletRequest request) {
		String modelId = request.getParameter("modelId");
		return "redirect:/modeler.html?modelId=" + modelId;
	}

	/**
	 * 从网关进来
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modeler/display")
	public String getModelerDisplay(Model model, HttpServletResponse response, HttpServletRequest request) {
		String procInstId = request.getParameter("procInstId");
		return "redirect:/modeler-display.html?procInstId=" + procInstId;
	}

	/**
	 * 首页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/")
	public String index(Model model) {
		return "login";
	}


	/**
	 * 登录接口
	 *
	 * @param loginId  账户名
	 * @param password 密码
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Resp login(String loginId, String password) {
		User user = InitData.getLogin(loginId, password);
		if (user == null) {
			return Resp.fail("账户名密码错误");
		} else {
			return Resp.succ("登陆成功", user);
		}
	}

	/**
	 * 获取通用的模板接口
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/generateModel", method = RequestMethod.GET)
	public Object generateModel(Model model, HttpServletRequest request) {
		/*List<String> companyIdList = InitData.getCompanyType();*/
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<org.activiti.engine.repository.Model> modelList = repositoryService.createModelQuery().modelWithoutTenantId().orderByLastUpdateTime().desc().list();
		/*companyIdList.stream().forEach(companyId -> {
			modelList.addAll(repositoryService.createModelQuery().modelTenantId(companyId).orderByLastUpdateTime().desc().list());
		});*/
		model.addAttribute("modelList", modelList);
		return "model/generateModel";
	}

	/**
	 * 获取公司创建的模板接口
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/companyModel", method = RequestMethod.GET)
	public Object companyModel(Model model, String userId, HttpServletRequest request) {
		User user = InitData.getUser(userId);
		if (user != null) {
			RepositoryService repositoryService = processEngine.getRepositoryService();
			List<org.activiti.engine.repository.Model> modelList = repositoryService.createModelQuery().orderByLastUpdateTime().desc().list();
			modelList = modelList.stream().filter(md -> md.getTenantId() != null && md.getTenantId().trim().length() > 0).collect(Collectors.toList());
			model.addAttribute("modelList", modelList);
		}
		return "model/companyModel";
	}

	/**
	 * 获取流程部署接口
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deployment", method = RequestMethod.GET)
	public Object getDeployment(Model model, String userId, int currentPage, int pageSize) {
		User user = InitData.getUser(userId);
		if (user != null) {
			RepositoryService repositoryService = processEngine.getRepositoryService();
			int firstResult = (currentPage > 0 ? currentPage - 1 : 0) * pageSize;
			List<Deployment> deploymentList = repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().listPage(firstResult, pageSize);
			model.addAttribute("deploymentList", deploymentList);
		}
		model.addAttribute("userId", userId);
		model.addAttribute("upPage", currentPage > 1 ? currentPage - 1 : 1);
		model.addAttribute("nextPage", currentPage + 1);
		model.addAttribute("pageSize", pageSize);
		return "model/procDeploy";
	}

	/**
	 * 获取流程实例接口
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/processInstance", method = RequestMethod.GET)
	public Object getProcessInstance(Model model, String userId, int currentPage, int pageSize, String deployId) {
		User user = InitData.getUser(userId);
		if (user != null) {
			HistoryService service = processEngine.getHistoryService();
			int firstResult = (currentPage > 0 ? currentPage - 1 : 0) * pageSize;
			HistoricProcessInstanceQuery procInstQuery = service.createHistoricProcessInstanceQuery();
			if(deployId != null && deployId.length() > 0){
				procInstQuery.deploymentId(deployId);
			}
			List<HistoricProcessInstance> processInstanceList = procInstQuery.orderByProcessInstanceStartTime().desc().listPage(firstResult, pageSize);
			model.addAttribute("processInstanceList", processInstanceList);
		}
		model.addAttribute("userId", userId);
		model.addAttribute("upPage", currentPage > 1 ? currentPage - 1 : 1);
		model.addAttribute("nextPage", currentPage + 1);
		model.addAttribute("pageSize", pageSize);
		return "model/procInst";
	}

	/**
	 * 发起流程
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/startProcess", method = RequestMethod.POST)
	@ResponseBody
	public Object startProcess(Model model, String deployId, String userId) throws IOException {
		User user = InitData.getUser(userId);
		if (user == null) {
			return Resp.fail("用户为空");
		}
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deployId).singleResult();
		if (processDefinition == null) {
			return Resp.fail("流程定义的获取失败");
		}
		//如果流程有tenantId, 用key发起流程一定要用.startProcessInstanceByKeyAndTenantId(key, tenantId);
		processEngine.getIdentityService().setAuthenticatedUserId(userId); //设置发起流程用户
		ProcessInstance procInst = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId()); //发起流程
		//processEngine.getRuntimeService().addParticipantUser(procInst.getId(), "1");
		processEngine.getRuntimeService().setProcessInstanceName(procInst.getId(),processDefinition.getName() + "_" + procInst.getId()); //修改实例名称
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(procInst.getProcessInstanceId()).list();
		tasks.stream().forEach(task -> {
			processEngine.getTaskService().addCandidateUser(task.getId(), userId);
		});
		return Resp.succ("流程创建成功");
	}

	/**
	 * 我的代办
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myNeedHandle", method = RequestMethod.GET)
	public Object myNeedHandle(Model model, String userId) throws IOException {
		User user = InitData.getUser(userId);
		if (user != null) {
			//个人任务查询
			List<Task> personalLeaveInfo = modelTestService.myNeedHandlePersonalTask(userId);
			model.addAttribute("personalTask", personalLeaveInfo);
			//可认领查询任务查询
			List<String> roleNames = InitData.getRole(userId).stream().map(role -> role.getName()).collect(Collectors.toList());
			List<Task> claimLeaveInfo = modelTestService.myNeedHandleClaimTask(roleNames, user.getCompanyCode());
			model.addAttribute("claimTask", claimLeaveInfo);

			/*WfClaimTaskForm wctf = new WfClaimTaskForm();
			wctf.setProcInstId(personalLeaveInfo.get(0).getProcessInstanceId());
			wctf.setTaskId(personalLeaveInfo.get(0).getId());
			wctf.setUserId(userId);
			this.activitiService.claimTask(wctf);*/
		}
		return "my/myNeedHandle";
	}

	/**
	 * 我的发起
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myInitiate", method = RequestMethod.GET)
	public Object myInitiate(Model model, String userId) throws IOException {
		List<ProcessInstance> processInstanceList = modelTestService.myInitiate(userId);
		List<HistoricProcessInstance> historicProcessInstanceList = modelTestService.findEndBusinessKey(userId);
		List<LeaveInfo> leaveInfoList = new ArrayList<>();
		List<LeaveInfo> finishLeaveInfoList = new ArrayList<>();
		// 我发起的正在审批的流程
		processInstanceList.stream().forEach(processInstance -> {
			LeaveInfo leaveInfo = leaveService.getId(processInstance.getId());
			if (leaveInfo != null) {
				leaveInfoList.add(leaveInfo);
			}
		});
		// 我发起的已审批完的流程
		historicProcessInstanceList.stream().forEach(historicProcessInstance -> {
			LeaveInfo leaveInfo = leaveService.getId(historicProcessInstance.getId());
			if (leaveInfo != null) {
				leaveInfo.setEndTime(TimeFormat.dateToString(historicProcessInstance.getEndTime()));
				leaveInfo.setStartTime(TimeFormat.dateToString(historicProcessInstance.getStartTime()));
				leaveInfo.setStartUserId(historicProcessInstance.getStartUserId());
				leaveInfo.setDurationInMillis(TimeFormat.secToTime(historicProcessInstance.getDurationInMillis()));
				leaveInfo.setName(historicProcessInstance.getName());
				finishLeaveInfoList.add(leaveInfo);
			}
		});
		model.addAttribute("leaveInfo", leaveInfoList);
		model.addAttribute("finishLeaveInfoList", finishLeaveInfoList);
		return "my/myInitiate";
	}

	/**
	 * 任务认领
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/claimTask", method = RequestMethod.POST)
	@ResponseBody
	public Object claimTask(Model model, String userId, String taskId) {
		modelTestService.claimTask(taskId, userId);
		return Resp.succ("认领成功");
	}

	/**
	 * 取消认领
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/unclaimTask", method = RequestMethod.POST)
	@ResponseBody
	public Object unclaimTask(Model model, String taskId) {
		modelTestService.unclaimTask(taskId);
		return Resp.succ("取消成功");
	}

	/**
	 * 任务审批
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/approvalTask", method = RequestMethod.POST)
	@ResponseBody
	public Object approvalTask(Model model, String userId, String taskId, String approval) {
		modelTestService.approvalTask(taskId, userId, approval);
		return Resp.succ("审批成功");
	}

	/**
	 * 统计信息  1.正在审批的流程    2.审批结束的流程
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statisticsInfo", method = RequestMethod.GET)
	public Object statisticsInfo(Model model, String userId) {
		User user = InitData.getUser(userId);
		if (user != null) {
			List<HistoricProcessInstance> historicProcessInstanceList = modelTestService.queryHistoricInstanceAll(user.getCompanyCode());
			List<LeaveInfo> leaveInfoList = historicProcessInstanceList.stream().map(historicProcessInstance -> {
				LeaveInfo leaveInfo = leaveService.getId(historicProcessInstance.getId());
				if (leaveInfo == null) {
					return leaveInfo;
				}
				leaveInfo.setEndTime(TimeFormat.dateToString(historicProcessInstance.getEndTime()));
				leaveInfo.setStartTime(TimeFormat.dateToString(historicProcessInstance.getStartTime()));
				leaveInfo.setStartUserId(historicProcessInstance.getStartUserId());
				leaveInfo.setDurationInMillis(TimeFormat.secToTime(historicProcessInstance.getDurationInMillis()));
				leaveInfo.setName(historicProcessInstance.getName());
				return leaveInfo;
			}).filter(leaveInfo -> leaveInfo != null && StringUtils.isNotEmpty(leaveInfo.getId())).collect(Collectors.toList());
			model.addAttribute("end", leaveInfoList);
			model.addAttribute("endNum", leaveInfoList.size());
			List<ProcessInstance> processInstanceList = modelTestService.queryProcessInstanceByTenantIdAll(user.getCompanyCode());
			List<LeaveInfo> processingLeaveInfoList = processInstanceList.stream().map(processInstance -> {
				LeaveInfo leaveInfo = leaveService.getId(processInstance.getId());
				if (leaveInfo == null) {
					return leaveInfo;
				}
				return leaveInfo;
			}).filter(leaveInfo -> leaveInfo != null && StringUtils.isNotEmpty(leaveInfo.getId())).collect(Collectors.toList());
			model.addAttribute("processing", processingLeaveInfoList);
			model.addAttribute("processingNum", processingLeaveInfoList.size());
		}
		return "data/statisticsInfo";
	}

	/**
	 * 流程明细  1.走了多少步    2.中间经历的审批任务
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public Object statisticsDetail(Model model, String proinId) {
		List<HistoricActivityInstance> historicActivityInstanceList = modelTestService.queryHistoricActivitiInstance(proinId);
		List<HistoricTaskInstance> historicTaskInstanceList = modelTestService.queryHistoricTask(proinId);
		model.addAttribute("activitiInstances", historicActivityInstanceList);
		model.addAttribute("taskInstances", historicTaskInstanceList);
		return "data/detail";
	}

}
