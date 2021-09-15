package com.wisdom.acm.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.acm.activiti.service.ActivitiService;
import com.wisdom.acm.activiti.util.Resp;
import com.wisdom.acm.activiti.po.InitData;
import com.wisdom.acm.activiti.service.LeaveService;
import com.wisdom.acm.activiti.service.ModelTestService;
import com.wisdom.base.common.form.ActModelAddForm;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 处理模板使用的测试类
 */
@RestController
@RequestMapping("models")
public class ModelCreateController extends GlobalController {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ModelTestService modelTestService;

	@Autowired
	ActivitiService activitiService;

	@Autowired
	LeaveService leaveService;

	/**
	 * 新建一个空模型
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("newModel")
	public Resp newModel(HttpServletRequest request) throws IOException {
		ActModelAddForm actModelAddForm = new ActModelAddForm();
		actModelAddForm.setModelTitle("A New Model");
		actModelAddForm.setModelDesc("A New Model");
		String id = activitiService.addModel(actModelAddForm);
		return Resp.succ(null, id);
	}

	/**
	 * 新建一个空模型
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("company/newModel")
	public Resp newCompanyModel(String userId, HttpServletRequest request) throws IOException {
		//response.sendRedirect("/modeler.html?modelId=" + id);
		return Resp.succ(null, this.newModel(request).getData());
}

	/**
	 * 复制一个已存在的模型
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("copyModel")
	public Resp copyModel(String modelId, HttpServletRequest request) throws IOException {
		String id = activitiService.copyModel(modelId);
		//response.sendRedirect("/modeler.html?modelId=" + id);
		return Resp.succ("复制模型成功", id);
	}

	/**
	 * 复制一个已存在的模型
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("company/copyModel")
	public Resp copyCompanyModel(String userId, String modelId, HttpServletRequest request) throws IOException {
		String id = activitiService.copyModel(modelId, InitData.getUser(userId).getCompanyCode());
		//response.sendRedirect("/modeler.html?modelId=" + id);
		return Resp.succ("复制模型成功", id);
	}

	/**
	 * 删除模型
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("{id}")
	public Resp deleteModel(@PathVariable("id") String id, HttpServletRequest request) {
		RepositoryService service = processEngine.getRepositoryService();
		service.deleteModel(id);
		return Resp.succ("删除模型成功");
	}

	/**
	 * 发布流程定义
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deployment", method = RequestMethod.POST)
	@ResponseBody
	public Object deployment(Model model, String modelId, HttpServletRequest request) throws IOException {
		return Resp.succ(activitiService.deployProcess(modelId));
	}

	/**
	 * 删除流程部署
	 *
	 * @param deploymentId
	 * @return
	 */
	@DeleteMapping("deployment/{deploymentId}")
	public Resp deleteDeployment(@PathVariable("deploymentId") String deploymentId, boolean cascade, HttpServletRequest request) {
		RepositoryService service = processEngine.getRepositoryService();
		service.deleteDeployment(deploymentId, cascade); //删除流程定义及所有子表
		return Resp.succ("删除流程定义成功");
	}

	/**
	 * 终止流程实例
	 *
	 * @param procInstId
	 * @return
	 */
	@PutMapping("processInstance/{procInstId}/terminate")
	public Resp terminateProcessInstance(@PathVariable("procInstId") String procInstId, HttpServletRequest request) {
		RuntimeService service = processEngine.getRuntimeService();
		service.deleteProcessInstance(procInstId, "管理员终止了流程");
		return Resp.succ("终止流程成功");
	}

	/**
	 * 删除流程实例
	 *
	 * @param procInstId
	 * @return
	 */
	@DeleteMapping("processInstance/{procInstId}")
	public Resp deleteProcessInstance(@PathVariable("procInstId") String procInstId, HttpServletRequest request) {
		HistoryService service = processEngine.getHistoryService();
		service.deleteHistoricProcessInstance(procInstId);
		return Resp.succ("删除流程实例成功");
	}
}
