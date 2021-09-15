package com.wisdom.acm.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.activiti.mapper.ActivitiMapper;
import com.wisdom.acm.activiti.po.WorkflowPo;
import com.wisdom.acm.activiti.service.ActivitiService;
import com.wisdom.acm.activiti.util.ExprUtil;
import com.wisdom.acm.activiti.vo.WorkFlowLogVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.DateUtil;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.JsonUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.base.ActModelVo;
import com.wisdom.base.common.vo.sys.UserVo;
import com.wisdom.base.common.vo.wf.*;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ActivitiServiceImpl extends BaseService<ActivitiMapper, WorkflowPo> implements ActivitiService {

    /**
     * 任务服务
     */
    @Autowired
    private TaskService taskService;

    /**
     * 运行时服务
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 资源服务
     */
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 历史服务
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 用户服务
     */
    @Autowired
    private IdentityService identityService;

    /**
     * 流程
     */
    @Autowired
    private ProcessEngine processEngine;

    /**
     * 序列化
     */
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 得到流程定义
     *
     * @param procDefId 流程定义ID
     * @return 流程定义
     */
    private ProcessDefinitionEntity getProcessDefinitionEntity(String procDefId) {
        return (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService).getDeployedProcessDefinition(procDefId);
    }

    /**
     * 获取下一个任务信息
     *
     * @param task 当前任务
     * @return 下一个人工活动
     */
    @Override
    public List<PvmActivity> getNextUserActivity(TaskInfo task) {
        List<PvmActivity> nextActivitys = new ArrayList<>();
        if (!ObjectUtils.isEmpty(task)) {
            Execution execution = this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            Map<String, Object> vars = execution != null ? this.runtimeService.getVariables(execution.getId()) : null;
            ProcessDefinitionEntity procDefEnt = this.getProcessDefinitionEntity(task.getProcessDefinitionId());
            ActivityImpl actImpl = procDefEnt.findActivity(task.getTaskDefinitionKey()); //找到当前节点信息
            nextActivitys = this.getNextUserActivity(actImpl, vars);   //获取下一个人工节点信息
        }
        return nextActivitys;
    }

    /**
     * 获取下一个任务信息
     *
     * @param actImpl 活动ID
     * @param vars    流程执行变量
     * @return 下一人工任务活动
     */
    private List<PvmActivity> getNextUserActivity(ActivityImpl actImpl, Map<String, Object> vars) {
        List<PvmActivity> nextActs = new ArrayList<>();
        if (!ObjectUtils.isEmpty(actImpl)) {
            getNextUserActivity(nextActs, actImpl.getOutgoingTransitions(), vars);   //获取下一个节点信息
        }
        return nextActs;
    }

    /**
     * 下一个任务节点信息,
     * 如果下一个节点为用户任务则直接返回,
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务信息
     *
     * @param nextActs       后续人工活动
     * @param outTransitions 节点所有流向线路信息, 排他网关顺序流线段判断条件, 例如排他网关顺序留线段判断条件为${money>1000}, 若满足流程启动时设置variables中的money>1000, 则流程流向该顺序流信息
     * @param vars           流程执行变量
     */
    private void getNextUserActivity(List<PvmActivity> nextActs, List<PvmTransition> outTransitions, Map<String, Object> vars) {
        for (PvmTransition pt : outTransitions) {
            String srcType = Tools.toString(pt.getSource().getProperty("type")); //源节点的类型,单一网关=exclusiveGateway;并行网关=ParallelGateway;包含网关=InclusiveGateway
            PvmActivity ac = pt.getDestination(); //获取线路的终点节点
            String outType = Tools.toString(ac.getProperty("type"));
            String cond = FormatUtil.toString(pt.getProperty("conditionText")).trim();  //获取排他网关线路判断条件信息
            if (!Tools.isEmpty(vars) && !Tools.isEmpty(cond) && !cond.replace(" ", "").contains("expr.existActivity(nextActPart,") //不包含指派参与者
                && ("exclusiveGateway".equals(srcType) || "exclusiveGateway".equals(outType)
                || "inclusiveGateway".equals(srcType) || "inclusiveGateway".equals(outType)
                || "userTask".equals(srcType) || "userTask".equals(outType))) { //如果流向线路为排他和包含网关
                if (!this.parseExpression(cond, vars)) {
                    continue;
                }
            }
            if ("userTask".equals(outType)) {
                nextActs.add(ac);
            } else {
                this.getNextUserActivity(nextActs, ac.getOutgoingTransitions(), vars);
            }
        }
    }

    /**
     * 查询流程表达式是否通过信息
     *
     * @param expression el表达式key信息
     * @param execution  流程实例Id信息
     * @return VariableValue
     */
    private Boolean parseExpression(String expression, Execution execution) {
        if (!Tools.isEmpty(expression) && !Tools.isEmpty(execution)) {
            Map<String, Object> vars = this.runtimeService.getVariables(execution.getId());
            if (!ObjectUtils.isEmpty(vars)) {
                if (this.parseExpression(expression, vars)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据key和value判断el表达式是否通过信息
     *
     * @param expression el表达式
     * @param vars       el表达式传入值信息
     * @return booelan
     */
    private Boolean parseExpression(String expression, Map<String, Object> vars) {
        if (!ObjectUtils.isEmpty(expression) && !ObjectUtils.isEmpty(vars)) {
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            try {
                context.setFunction("expr", "equals", ExprUtil.class.getMethod("equals", Object.class, Object.class));
                context.setFunction("expr", "equalsIgnoreCase", ExprUtil.class.getMethod("equalsIgnoreCase", Object.class, Object.class));
                context.setFunction("expr", "contains", ExprUtil.class.getMethod("contains", Object.class, Object.class));
                context.setFunction("expr", "startsWith", ExprUtil.class.getMethod("startsWith", Object.class, Object.class));
                context.setFunction("expr", "endsWith", ExprUtil.class.getMethod("endsWith", Object.class, Object.class));
                context.setFunction("expr", "matches", ExprUtil.class.getMethod("matches", Object.class, Object.class));
                context.setFunction("expr", "split", ExprUtil.class.getMethod("split", Object.class, Object.class, Object.class));
                context.setFunction("expr", "indexOf", ExprUtil.class.getMethod("indexOf", Object.class, Object.class));
                context.setFunction("expr", "lastIndexOf", ExprUtil.class.getMethod("lastIndexOf", Object.class, Object.class));
                context.setFunction("expr", "existActivity", ExprUtil.class.getMethod("existActivity", Object.class, Object.class));
                for (Map.Entry<String, Object> d : vars.entrySet()) {
                    context.setVariable(d.getKey(), factory.createValueExpression(d.getValue(), d.getValue().getClass()));
                }
            } catch (NoSuchMethodException e) {
                throw new BaseException(e);
            }
            //SimpleContext中，类和方法名中是以:分隔，而流程底层解析时类和方法之间又是以.分隔
            expression = expression.contains("expr.") ? expression.replace("expr.", "expr:") : expression;
            ValueExpression e = factory.createValueExpression(context, expression, Boolean.class);
            return Tools.parseBoolean(e.getValue(context));
        } else {
            return false;
        }
    }

    /**
     * 得到活动节点的参与者
     *
     * @param procDefEnt    流程定义
     * @param activity      当前活动节点
     * @param nextActivitys 后续活动节点
     * @return 参与者
     */
    private WfCandidateVo getTaskDefCandidate(ProcessDefinitionEntity procDefEnt, ActivityImpl activity, List<PvmActivity> nextActivitys) {
        WfCandidateVo vo = new WfCandidateVo();
        if (!ObjectUtils.isEmpty(nextActivitys)) {
            List<WfActivityVo> activities = new ArrayList<>();
            vo.setActivityOnly(this.isActivityOnly(activity));
            for (PvmActivity pa : nextActivitys) {
                TaskDefinition taskDef = procDefEnt.getTaskDefinitions().get(pa.getId());
                WfActivityVo activityVo = new WfActivityVo(taskDef.getKey(), taskDef.getNameExpression().getExpressionText());
                activityVo.setCandidateGroups(this.getCandidateGroup(taskDef));
                activityVo.setCandidateUsers(this.getCandidateUser(taskDef));
                activities.add(activityVo);
            }
            vo.setActivities(activities);
        }
        return vo;
    }

    /**
     * 判断当前节点是否只能选一
     *
     * @param activity 当前活动
     * @return 是/否
     */
    private boolean isActivityOnly(ActivityImpl activity) {
        boolean only = true;
        List<PvmTransition> pts = activity.getOutgoingTransitions();
        if (!Tools.isEmpty(pts)) {
            for (PvmTransition pt : pts) {
                String type = Tools.toString(pt.getDestination().getProperty("type"));
                if (type.equalsIgnoreCase("parallelGateway") || type.equalsIgnoreCase("inclusiveGateway")) {
                    only = false;
                }
            }
        }
        return only;
    }

    /**
     * 得到后选组
     *
     * @param taskDef 活动定义
     */
    private List<WfCandidateGroupVo> getCandidateGroup(TaskDefinition taskDef) {
        List<WfCandidateGroupVo> candidates = new ArrayList<>();
        Set<Expression> candidateExp = taskDef.getCandidateGroupIdExpressions();
        if (!ObjectUtils.isEmpty(candidateExp)) {
            for (Expression exp : candidateExp) {
                candidates.add(new WfCandidateGroupVo(exp.getExpressionText()));
            }
        }
        return candidates;
    }

    /**
     * 得到后选人
     *
     * @param taskDef 活动定义
     */
    private List<WfCandidateUserVo> getCandidateUser(TaskDefinition taskDef) {
        List<WfCandidateUserVo> candidates = new ArrayList<>();
        Set<Expression> candidateExp = taskDef.getCandidateUserIdExpressions();
        if (!ObjectUtils.isEmpty(candidateExp)) {
            for (Expression exp : candidateExp) {
                candidates.add(new WfCandidateUserVo(exp.getExpressionText()));
            }
        }
        return candidates;
    }

    /**
     * 得到发起人节点(第一个人工节点)
     *
     * @param procDefEnt 流程定义
     * @return 第一个人工活动
     */
    private ActivityImpl getFirstActivity(ProcessDefinitionEntity procDefEnt) {
        List<ActivityImpl> tasks = procDefEnt.getActivities();
        if (!ObjectUtils.isEmpty(tasks)) {
            for (ActivityImpl act : tasks) {
                if ("startEvent".equals(FormatUtil.toString(act.getProperty("type")))) {
                    if (!ObjectUtils.isEmpty(act.getOutgoingTransitions())) {
                        return (ActivityImpl) act.getOutgoingTransitions().get(0).getDestination();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 得到批准人节点(第一个人工节点)
     *
     * @param procDefEnt 流程定义
     * @return 第最后人工活动
     */
    private List<ActivityImpl> getLastActivity(ProcessDefinitionEntity procDefEnt) {
        List<ActivityImpl> lastTasks = new ArrayList<>();
        List<ActivityImpl> tasks = procDefEnt.getActivities();
        if (!ObjectUtils.isEmpty(tasks)) {
            for (ActivityImpl act : tasks) {
                if ("endEvent".equals(FormatUtil.toString(act.getProperty("type")))) {
                    if (!ObjectUtils.isEmpty(act.getIncomingTransitions())) {
                        for (PvmTransition pt : act.getIncomingTransitions()) {
                            lastTasks.add((ActivityImpl) pt.getSource());
                        }
                    }
                }
            }
        }
        return lastTasks;
    }

    /**
     * 得到开始人工活动的下一个人工活动候选人
     *
     * @param procDefKey 流程定义
     * @return 下一个人工活动候选人
     */
    @Override
    public WfCandidateVo getStartNextCandidate(String procDefKey, Map<String, Object> vars) {
        WfCandidateVo candidate = new WfCandidateVo();
        ProcessDefinition procDef = this.repositoryService.createProcessDefinitionQuery().processDefinitionKey(procDefKey).latestVersion().singleResult();
        if (!ObjectUtils.isEmpty(procDef)) {
            ProcessDefinitionEntity procDefEnt = this.getProcessDefinitionEntity(procDef.getId());
            ActivityImpl actImpl = this.getFirstActivity(procDefEnt);
            if (!ObjectUtils.isEmpty(actImpl)) {
                List<PvmActivity> nextUserActivities = this.getNextUserActivity(actImpl, vars); //得到后续活动定义信息
                candidate = this.getTaskDefCandidate(procDefEnt, actImpl, nextUserActivities);
            }
        }
        return candidate;
    }

    /**
     * 得到后续任务参与者
     *
     * @param taskId 任务ID
     * @return 参与者
     */
    @Override
    public WfCandidateVo getNextCandidate(String taskId) {
        WfCandidateVo candidate = new WfCandidateVo();
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        if (!ObjectUtils.isEmpty(task)) {
            Execution execution = this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            Map<String, Object> vars = execution != null ? this.runtimeService.getVariables(execution.getId()) : null;
            ProcessDefinitionEntity procDefEnt = this.getProcessDefinitionEntity(task.getProcessDefinitionId());
            ActivityImpl actImpl = procDefEnt.findActivity(task.getTaskDefinitionKey()); //找到当前节点信息
            List<PvmActivity> nextActivitys = this.getNextUserActivity(actImpl, vars); //得到后续活动定义信息
            candidate = this.getTaskDefCandidate(procDefEnt, actImpl, nextActivitys);
        }
        return candidate;
    }

    /**
     * 发起流程
     *
     * @param startForm form
     * @return 流程运行VO
     */
    @Override
    public WfRunProcessVo startProcess(WfStartProcessForm startForm) {
        String procDefKey = startForm.getProcDefKey();
        String userId = startForm.getUserId();

        this.identityService.setAuthenticatedUserId(userId); //设置发起流程用户
        Map<String, Object> vars = startForm.getVars(); //流程变量，全局可用
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(procDefKey, startForm.getTitle(), vars); //启动流程实例

        this.runtimeService.setProcessInstanceName(processInstance.getId(), startForm.getTitle()); //修改实例名称
        Task task = this.taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        this.completeTask(userId, task, startForm.getNextActPart(), startForm.getComment(), vars); //完成第一个任务(默认为发起人活动）
        return this.getWfRuningProcessVo(task, userId);
    }

    /**
     * 判断是否存在条件配置
     *
     * @param outTransitions 线路
     * @return 存在条件
     */
    private boolean isExistCondition(List<PvmTransition> outTransitions) {
        for (PvmTransition pt : outTransitions) {
            String condition = FormatUtil.toString(pt.getProperty("conditionText")).trim();  //获取排他网关线路判断条件信息
            if (!ObjectUtils.isEmpty(condition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到流程运行VO
     *
     * @param task 任务
     * @return 流程运行VO
     */
    private WfRunProcessVo getWfRuningProcessVo(Task task, String userId) {
        WfRunProcessVo procVo = new WfRunProcessVo();
        procVo.setProcDefId(task.getProcessDefinitionId());
        procVo.setProcInstId(task.getProcessInstanceId());
        procVo.setActivityId(task.getTaskDefinitionKey());
        procVo.setActivityName(task.getName());
        procVo.setTaskId(task.getId());
        procVo.setUserId(userId);
        return procVo;
    }

    /**
     * 认领
     *
     * @param claimTaskForm 表单
     * @return 认领VO
     */
    @Override
    public WfClaimVo claimTask(WfClaimTaskForm claimTaskForm) {
        WfClaimVo vo = this.dozerMapper.map(claimTaskForm, WfClaimVo.class);
        vo.setProcInstId(claimTaskForm.getProcInstId());
        vo.setUserId(claimTaskForm.getUserId());
        Task task = this.taskService.createTaskQuery().taskId(claimTaskForm.getTaskId()).taskCandidateOrAssigned(claimTaskForm.getUserId()).singleResult();
        if (!ObjectUtils.isEmpty(task)) {
            ProcessDefinitionEntity procDefEnt = this.getProcessDefinitionEntity(task.getProcessDefinitionId()); //得到流程定义实体
            ActivityImpl firstActivity = this.getFirstActivity(procDefEnt); //得到发起人节点
            vo.setActivityId(task.getTaskDefinitionKey());
            vo.setActivityName(task.getName());
            vo.setAgree(!task.isSuspended()); //同意
            vo.setReject(!task.isSuspended() && !task.getTaskDefinitionKey().equals(firstActivity.getId())); //驳回，流程无挂起并不是发起人节点
            vo.setTerminate(task.getTaskDefinitionKey().equals(firstActivity.getId())); //发启人节点默认可以终止
            vo.setStart(firstActivity.getId().equals(vo.getActivityId()));
            this.setEnd(vo, procDefEnt); //流程是否完成
        } else {
            vo.setCancel(this.getCancel(vo.getProcInstId(), vo.getTaskId(), vo.getUserId()));
        }
        return vo;
    }

    /**
     * 判断是否能撤销
     * @param procInstId 流程实例
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return
     */
    private boolean getCancel(String procInstId, String taskId, String userId){
        boolean cancel = false;
        if(!ObjectUtils.isEmpty(procInstId) && !ObjectUtils.isEmpty(taskId) && !ObjectUtils.isEmpty(userId)){
            ProcessInstance procInst = this.runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            if(!ObjectUtils.isEmpty(procInst)) { //流程未结束
                ProcessDefinitionEntity procDefEnt = this.getProcessDefinitionEntity(procInst.getProcessDefinitionId());
                List<HistoricTaskInstance> tasks = this.historyService.createHistoricTaskInstanceQuery() //
                    .processInstanceId(procInstId).finished().orderByHistoricTaskInstanceEndTime().desc().list();
                Map<String, Object> vars = procInst.getProcessVariables();
                HistoricTaskInstance task = this.getCancelTask(tasks, taskId, userId);
                if(!ObjectUtils.isEmpty(task)){
                    ActivityImpl actImpl = procDefEnt.findActivity(task.getTaskDefinitionKey()); //找到当前节点信息
                    if(this.isActivityCancel(actImpl)) {
                        cancel = false;
                    } else {
                        List<PvmActivity> nextActs = this.getNextUserActivity(actImpl, vars);
                        if (!ObjectUtils.isEmpty(nextActs)) {
                            cancel = true;
                            for (PvmActivity act : nextActs) {
                                for (HistoricTaskInstance hiTask : tasks) {
                                    if (act.getId().equals(hiTask.getTaskDefinitionKey()) && !Tools.isEmpty(hiTask.getEndTime()) && hiTask.getEndTime().after(task.getEndTime())) {
                                        cancel = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return cancel;
    }

    /**
     * 得到当前任务
     * @param tasks
     * @param taskId
     * @param userId
     * @return
     */
    private HistoricTaskInstance getCancelTask(List<HistoricTaskInstance> tasks, String taskId, String userId){
        HistoricTaskInstance hiTask = null;
        if(!ObjectUtils.isEmpty(tasks) && !ObjectUtils.isEmpty(taskId) && !ObjectUtils.isEmpty(userId)){
            for(HistoricTaskInstance task : tasks){
                if(taskId.equals(task.getId()) && !Tools.isEmpty(task.getEndTime()) && userId.equals(task.getAssignee())){
                    hiTask = task;
                    break;
                }
            }
        }
        return hiTask;
    }

    /**
     * 判断当前节点的前驱是否有合并网关或包含网关
     *
     * @param activity 当前活动
     * @return 是/否
     */
    private boolean isActivityCancel(PvmActivity activity) {
        boolean cancel = false;
        List<PvmTransition> pts = activity.getOutgoingTransitions();
        if (!Tools.isEmpty(pts)) {
            for (PvmTransition pt : pts) {
                String type = Tools.toString(pt.getDestination().getProperty("type"));
                if (type.equalsIgnoreCase("parallelGateway") || type.equalsIgnoreCase("inclusiveGateway")) {
                    if(pt.getDestination().getIncomingTransitions().size() > 1) {
                        cancel = true;
                    }
                }
            }
        }
        return cancel;
    }

    /**
     * 设置是否是结束活动
     *
     * @param vo         认领
     * @param procDefEnt 流程定义
     */
    public void setEnd(WfClaimVo vo, ProcessDefinitionEntity procDefEnt) {
        List<ActivityImpl> last = this.getLastActivity(procDefEnt);
        for (ActivityImpl ai : last) {
            if (vo.getActivityId().equals(ai.getId())) {
                vo.setEnd(true); //是否为结束审批节点
            }
        }
    }

    /**
     * 完成工作项
     *
     * @param completeForm 完成工作项
     * @return 流程运行VO
     */
    @Override
    public WfRunProcessVo completeTask(WfCompleteTaskForm completeForm) {
        WfRunProcessVo procVo = null;
        String userId = completeForm.getUserId();
        String taskId = completeForm.getTaskId();
        Task task = this.taskService.createTaskQuery().taskId(taskId).taskCandidateOrAssigned(userId).singleResult();
        if (!ObjectUtils.isEmpty(task)) {
            this.completeTask(userId, task, completeForm.getNextActPart(), completeForm.getComment(), completeForm.getVars());
            procVo = this.getWfRuningProcessVo(task, userId);
            ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(completeForm.getProcInstId()).singleResult();
            procVo.setProcComplete(ObjectUtils.isEmpty(pi)); //为空，流程结束
        } else {
            throw new BaseException("任务未找到，可能已被撤回或流程已终止!");
        }
        return procVo;
    }

    /**
     * 完成任务
     * 注意：条件变量找不到会报错
     *
     * @param userId      用户
     * @param task        任务
     * @param nextActPart 后续参与者
     * @param comment     意见
     */
    private void completeTask(String userId, Task task, List<WfActivityVo> nextActPart, String comment, Map<String, Object> vars) {
        vars = ObjectUtils.isEmpty(vars) ? new HashMap<>() : vars; //流程变量,全局可用
        if (!ObjectUtils.isEmpty(task) && !task.isSuspended()) {
            if (!ObjectUtils.isEmpty(nextActPart)) {
                vars.put("nextActPart", JsonUtil.toJson(nextActPart)); //在全局监听中设置后续活动的用户
            }
            this.taskService.claim(task.getId(), userId);
            //this.taskService.addComment(task.getId(), task.getProcessInstanceId(), FormatUtil.toString(comment)); //增加意见
            this.taskService.complete(task.getId(), vars); //完成任务
        }
    }

    /**
     * 查询可驳回的节点
     *
     * @param taskId 活动ID
     * @return 前驱人工活动
     */
    @Override
    public List<WfActivityVo> getRejectActivity(String taskId) {
        List<ActivityImpl> rtnList = new ArrayList<>();
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        if (!ObjectUtils.isEmpty(task)) {
            ProcessDefinitionEntity procDef = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
            Execution execution = this.runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            ActivityImpl currActivity = procDef.findActivity(execution.getActivityId());// 当前节点
            rtnList = this.getUpBackActivity(procDef, task.getProcessInstanceId(), taskId, currActivity, new ArrayList<>(), new ArrayList<>());
        }
        return reverList(rtnList);
    }

    /**
     * 迭代循环流程树结构，查询当前节点可驳回的任务节点
     *
     * @param procDefEnt   流程定义
     * @param proceInstId  流程实例ID
     * @param taskId       当前任务ID
     * @param currActivity 当前活动节点
     * @param rtnList      存储回退节点集合
     * @param tempList     临时存储节点集合（存储一次迭代过程中的同级userTask节点）
     * @return 回退节点集合
     */
    private List<ActivityImpl> getUpBackActivity(ProcessDefinitionEntity procDefEnt, String proceInstId, String taskId, ActivityImpl currActivity, List<ActivityImpl> rtnList, List<ActivityImpl> tempList) {
        List<PvmTransition> incomingTransitions = currActivity.getIncomingTransitions();  // 当前节点的流入来源
        List<ActivityImpl> exclusiveGateways = new ArrayList<>(); // 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
        List<ActivityImpl> parallelGateways = new ArrayList<>();  // 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
        for (PvmTransition pvmTransition : incomingTransitions) { // 遍历当前节点所有流入路径
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            ActivityImpl activityImpl = transitionImpl.getSource();
            String type = (String) activityImpl.getProperty("type");
            /**
             * 并行节点配置要求：<br>
             * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
             */
            if ("parallelGateway".equals(type)) {// 并行路线
                String gatewayId = activityImpl.getId();
                String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
                if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归
                    return rtnList;
                } else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
                    parallelGateways.add(activityImpl);
                }
            } else if ("startEvent".equals(type)) {// 开始节点，停止递归
                return rtnList;
            } else if ("userTask".equals(type)) {// 用户任务
                tempList.add(activityImpl);
            } else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
                currActivity = transitionImpl.getSource();
                exclusiveGateways.add(currActivity);
            }
        }
        for (ActivityImpl activityImpl : exclusiveGateways) { //迭代条件分支集合，查询对应的userTask节点
            getUpBackActivity(procDefEnt, proceInstId, taskId, activityImpl, rtnList, tempList);
        }
        for (ActivityImpl activityImpl : parallelGateways) { //迭代并行集合，查询对应的userTask节点
            getUpBackActivity(procDefEnt, proceInstId, taskId, activityImpl, rtnList, tempList);
        }
        currActivity = filterNewestActivity(proceInstId, tempList); //根据同级userTask集合，过滤最近发生的节点
        if (currActivity != null) {
            String id = findParallelGatewayId(currActivity);  // 查询当前节点的流向是否为并行终点，并获取并行起点ID
            if (ObjectUtils.isEmpty(id)) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点
                rtnList.add(currActivity);
            } else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点
                currActivity = procDefEnt.findActivity(id);
            }
            tempList.clear(); // 清空本次迭代临时集合
            getUpBackActivity(procDefEnt, proceInstId, taskId, currActivity, rtnList, tempList); // 执行下次迭代
        }
        return rtnList;
    }

    /**
     * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
     *
     * @param activityImpl 当前节点
     * @return String
     */
    private String findParallelGatewayId(ActivityImpl activityImpl) {
        List<PvmTransition> incomingTransitions = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvmTransition : incomingTransitions) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            activityImpl = transitionImpl.getDestination();
            String type = (String) activityImpl.getProperty("type");
            if ("parallelGateway".equals(type)) {// 并行路线
                String gatewayId = activityImpl.getId();
                String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
                if ("END".equals(gatewayType.toUpperCase())) {
                    return gatewayId.substring(0, gatewayId.lastIndexOf("_")) + "_start";
                }
            }
        }
        return null;
    }

    /**
     * 根据流入任务集合，查询最近一次的流入任务节点
     *
     * @param proceInstId 流程实例
     * @param tempList    流入任务集合
     * @return 任务节点
     */
    private ActivityImpl filterNewestActivity(String proceInstId, List<ActivityImpl> tempList) {
        while (tempList.size() > 0) {
            ActivityImpl activity_1 = tempList.get(0);
            HistoricActivityInstance activityInstance_1 = findHistoricUserTask(proceInstId, activity_1.getId());
            if (activityInstance_1 == null) {
                tempList.remove(activity_1);
                continue;
            }
            if (tempList.size() > 1) {
                ActivityImpl activity_2 = tempList.get(1);
                HistoricActivityInstance activityInstance_2 = findHistoricUserTask(proceInstId, activity_2.getId());
                if (activityInstance_2 == null) {
                    tempList.remove(activity_2);
                    continue;
                }
                if (activityInstance_1.getEndTime().before(activityInstance_2.getEndTime())) {
                    tempList.remove(activity_1);
                } else {
                    tempList.remove(activity_2);
                }
            } else {
                break;
            }
        }
        if (tempList.size() > 0) {
            return tempList.get(0);
        }
        return null;
    }

    /**
     * 查询指定任务节点的最新记录
     *
     * @param proceInstId 流程实例ID
     * @param activityId  任务节点ID
     * @return 历史任务节点
     */
    private HistoricActivityInstance findHistoricUserTask(String proceInstId, String activityId) {
        HistoricActivityInstance rtnVal = null;
        // 查询当前流程实例审批结束的历史节点
        List<HistoricActivityInstance> historicActivityInstances = this.historyService.createHistoricActivityInstanceQuery()
            .activityType("userTask").processInstanceId(proceInstId).activityId(activityId).finished() //
            .orderByHistoricActivityInstanceEndTime().desc().list();
        if (historicActivityInstances.size() > 0) {
            rtnVal = historicActivityInstances.get(0);
        }
        return rtnVal;
    }

    /**
     * 反向排序list集合，便于驳回节点按顺序显示
     *
     * @param list 活动
     * @return 活动
     */
    private List<WfActivityVo> reverList(List<ActivityImpl> list) {
        List<ActivityImpl> rtnList = new ArrayList<>();
        List<WfActivityVo> rtnAiList = new ArrayList<>();
        // 由于迭代出现重复数据，排除重复
        for (int i = list.size(); i > 0; i--) {
            if (!rtnList.contains(list.get(i - 1)))
                rtnList.add(list.get(i - 1));
        }
        for (ActivityImpl ai : list) {
            rtnAiList.add(new WfActivityVo(ai.getId(), FormatUtil.toString(ai.getProperty("name"))));
        }
        return rtnAiList;
    }

    /**
     * 撤销任务
     *
     * @param form 表单
     */
    @Override
    public WfRunProcessVo cancelTask(WfRejectTaskForm form) {
        WfRunProcessVo procVo = null;
        List<Task> nextTasks = new ArrayList<>();
        boolean cancel = this.getCancel(form.getProcInstId(), form.getTaskId(), form.getUserId());
        if (cancel) {
            nextTasks = this.getCancelTask(form);
            if(!ObjectUtils.isEmpty(nextTasks)){
                for(Task task : nextTasks){
                    if(task.getId().equals(nextTasks.get(nextTasks.size() - 1).getId())){
                        this.rejectTask(task, form.getActivityId(), form.getUserId());
                        List<Task> tasks_ = this.taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(form.getActivityId()).list();
                        for(Task task_ : tasks_){
                            procVo = this.getWfRuningProcessVo(task_, form.getUserId());
                        }
                    }else {
                        this.cancelTask(task, form.getActivityId(), form.getUserId());
                    }
                }
            }
        }
        if(ObjectUtils.isEmpty(nextTasks)) {
            throw new BaseException("任务撤回失败！");
        }
        return procVo;
    }

    /**
     *  得到撤销的任务
     * @param form
     * @return
     */
    public List<Task> getCancelTask(WfRejectTaskForm form) {
        List<Task> nextTasks = new ArrayList<>();
        HistoricTaskInstance hiTask = historyService.createHistoricTaskInstanceQuery().taskId(form.getTaskId()).singleResult();
        if(ObjectUtils.isEmpty(form.getActivityId())) {
            form.setActivityId(hiTask.getTaskDefinitionKey());
        }
        List<PvmActivity> nextActs = this.getNextUserActivity(hiTask); //得到下一审批节点
        List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(form.getProcInstId()).list(); //得到运行中的任务
        for (PvmActivity act : nextActs) {
            for (int i = 0; i <  tasks.size(); i++) {
                if (act.getId().equals(tasks.get(i).getTaskDefinitionKey())) {
                    nextTasks.add(tasks.get(i));
                }
            }
        }
        return nextTasks;
    }

    /**
     * 驳回任务
     *
     * @param form 表单
     */
    @Override
    public WfRunProcessVo rejectTask(WfRejectTaskForm form) {
        WfRunProcessVo procVo = null;
        Task task = this.taskService.createTaskQuery().taskId(form.getTaskId()).taskCandidateOrAssigned(form.getUserId()).singleResult();
        if (!ObjectUtils.isEmpty(task)) {
            String assignee = this.rejectTask(task, form.getActivityId(), form.getUserId());
            procVo = this.getWfRuningProcessVo(task, assignee);
        }
        return procVo;
    }

    /**
     * 回退任务
     * @param task
     * @param activityId
     * @param userId
     * @return
     */
    private String rejectTask(Task task, String activityId, String userId) {
        if (!ObjectUtils.isEmpty(task)) {
            HistoricActivityInstance hai = this.findHistoricUserTask(task.getProcessInstanceId(), activityId);
            ProcessDefinitionEntity procDef = this.getProcessDefinitionEntity(task.getProcessDefinitionId());
            ActivityImpl currActivity = procDef.findActivity(task.getTaskDefinitionKey());// 当前节点
            List<PvmTransition> oriPvmTransitionList = this.clearTransition(currActivity); // 清空当前流向
            TransitionImpl newTransition = currActivity.createOutgoingTransition(); // 创建新流向
            ActivityImpl pointActivity = procDef.findActivity(activityId); // 目标节点
            newTransition.setDestination(pointActivity); // 设置新流向的目标节点

            List<WfActivityVo> nextActPart = this.getRejectActPart(hai);
            this.completeTask(userId, task, nextActPart, null, null); //完成任务

            currActivity.getOutgoingTransitions().remove(newTransition); // 删除目标节点新流入
            pointActivity.getIncomingTransitions().remove(newTransition); // 删除目标节点新流入
            this.restoreTransition(currActivity, oriPvmTransitionList); // 还原以前流向
            return hai.getAssignee();
        }
        return null;
    }

    /**
     * 得到回退的节点参与者
     * @param hai
     * @return
     */
    private List<WfActivityVo> getRejectActPart(HistoricActivityInstance hai) {
        List<WfActivityVo> nextActPart = new ArrayList<>();
        if (!ObjectUtils.isEmpty(hai)) {
            WfActivityVo activityVo = new WfActivityVo();
            activityVo.setId(hai.getActivityId());
            activityVo.setName(hai.getActivityName());
            List<WfCandidateUserVo> candidateUsers = new ArrayList<>();
            candidateUsers.add(new WfCandidateUserVo(hai.getAssignee()));
            activityVo.setCandidateUsers(candidateUsers);
            nextActPart.add(activityVo);
        }
        return nextActPart;
    }

    /**
     * 撤销任务
     * @param task
     * @param activityId
     * @param userId
     * @return
     */
    private void cancelTask(Task task, String activityId, String userId) {
        if (!ObjectUtils.isEmpty(task)) {
            ProcessDefinitionEntity procDef = this.getProcessDefinitionEntity(task.getProcessDefinitionId());
            ActivityImpl currActivity = procDef.findActivity(activityId);// 当前节点
            List<PvmTransition> oriPvmTransitionList = this.clearTransition(currActivity); // 清空当前流向
            this.completeTask(userId, task, null, null, null); //完成任务
            this.restoreTransition(currActivity, oriPvmTransitionList); // 还原以前流向
        }
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        List<PvmTransition> oriPvmTransitionList = new ArrayList<>();  // 存储当前节点所有流向临时变量
        if (!ObjectUtils.isEmpty(activityImpl)) {
            List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();  // 获取当前节点所有流向，存储到临时变量，然后清空
            oriPvmTransitionList.addAll(pvmTransitionList);
            pvmTransitionList.clear();
        }
        return oriPvmTransitionList;
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions(); // 清空现有流向
        pvmTransitionList.clear();
        pvmTransitionList.addAll(oriPvmTransitionList); // 还原以前流向
    }

    @Override
    public void terminateProcess(WfTerminateTaskForm terminateTaskForm) {
        this.runtimeService.deleteProcessInstance(terminateTaskForm.getProcInstId(), FormatUtil.toString(terminateTaskForm.getComment()));
    }

    @Override
    public void deleteProcess(String procInstId) {
        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        if (!ObjectUtils.isEmpty(pi)) { //防止流程已经终止，终止后不能在次终止
            this.runtimeService.deleteProcessInstance(procInstId, "删除原因");
        }
        this.historyService.deleteHistoricProcessInstance(procInstId);
    }

    @Override
    public void activeProcess(String procInstId) {
        this.runtimeService.activateProcessInstanceById(procInstId);
    }

    @Override
    public void suspendProcess(String procInstId) {
        this.runtimeService.suspendProcessInstanceById(procInstId);
    }

    @Override
    public List<WfTaskVo> getTaskByActDefId(String procInstId, String actDefId) {
        List<WfTaskVo> wfTasks = new ArrayList<>();
        WfTaskVo wfTask;
        List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(procInstId).taskDefinitionKey(actDefId).orderByTaskCreateTime().desc().list();
        if (!ObjectUtils.isEmpty(tasks)) {
            for (Task task : tasks) {
                wfTask = new WfTaskVo(task.getId(), task.getName());
                wfTask.setProcDefId(task.getProcessDefinitionId());
                wfTask.setProcInstId(task.getProcessInstanceId());
                wfTask.setAssignee(task.getAssignee());
                wfTask.setOwner(task.getOwner());
                wfTask.setDelegation(task.getDelegationState().name());
                wfTask.setCreateTime(task.getCreateTime());
            }
        }
        return wfTasks;
    }

    /**
     * 查询我的待办数量
     *
     * @param userId
     * @return
     */
    @Override
    public Long queryWfUnfinishCount(String userId) {
        return this.taskService.createTaskQuery().taskCandidateOrAssigned(userId).active().count();
    }

    /**
     * 查询我的待办
     *
     * @param userId 用户
     * @param form   Form
     * @return 待办
     */
    @Override
    public PageInfo<MyUnFinishTaskVo> queryMyUnfinishTaskList(String userId, int pageSize, int pageNum, WfUnFinishSearchForm form) {
        PageInfo page = new PageInfo();
        List<MyUnFinishTaskVo> taskVos = new ArrayList<>();
        TaskQuery taskQuery = this.taskService.createTaskQuery().taskCandidateOrAssigned(userId).active();
        if (!Tools.isEmpty(form.getStartTime())) {
            taskQuery.taskCreatedAfter(form.getStartTime());
        }
        if (!Tools.isEmpty(form.getEndTime())) {
            taskQuery.taskCreatedBefore(Tools.toDate(Tools.toDateString(form.getEndTime()) + " 23:59:59"));
        }
        if (!Tools.isEmpty(form.getBizType())) {
            taskQuery.processCategoryIn(Lists.newArrayList(form.getBizType()));
        }
        if (!Tools.isEmpty(form.getKeyword())) {
            taskQuery.processInstanceBusinessKeyLikeIgnoreCase("%" + form.getKeyword() + "%");
        }
        if (pageNum == 1) {
            page.setTotal(taskQuery.count());
        }
        List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage((pageNum - 1) * pageSize, pageSize);
        if (!ObjectUtils.isEmpty(tasks)) {
            for (Task task : tasks) {
                taskVos.add(this.getMyUnFinishTask(task));
            }
        }
        page.setList(taskVos);
        return page;
    }

    /**
     * 得到我未完成的代办
     *
     * @param task 任务
     * @return 代办
     */
    private MyUnFinishTaskVo getMyUnFinishTask(Task task) {
        MyUnFinishTaskVo taskVo = new MyUnFinishTaskVo();
        taskVo.setId(task.getId());
        taskVo.setProcInstId(task.getProcessInstanceId()); //流程实例id
        taskVo.setTaskName(task.getName());
        taskVo.setCreateTime(task.getCreateTime());
        if (!Tools.isEmpty(task.getDescription())) {
            Map<String, Object> desc = JsonUtil.readValue(task.getDescription(), Map.class);
            if (!Tools.isEmpty(desc) && !Tools.isEmpty(desc.get("sender"))) {
                taskVo.setSender(JsonUtil.readValue(JsonUtil.toJson(desc.get("sender")), UserVo.class));
            }
        }
        return taskVo;
    }

    /**
     * 查询我的已办
     *
     * @param userId 用户
     * @return 已办
     */
    @Override
    public PageInfo<MyFinishTaskVo> queryMyFinishTaskList(String userId, int pageSize, int pageNum, WfFinishSearchForm form) {
        PageInfo page = new PageInfo();
        List<MyFinishTaskVo> taskVos = new ArrayList<>();
        HistoricTaskInstanceQuery taskQuery = this.historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished();
        if (!Tools.isEmpty(form.getStartTime())) {
            taskQuery.taskCreatedAfter(form.getStartTime());
        }
        if (!Tools.isEmpty(form.getEndTime())) {
            taskQuery.taskCreatedBefore(Tools.toDate(Tools.toDateString(form.getEndTime()) + " 23:59:59"));
        }
        if (!Tools.isEmpty(form.getBizType())) {
            taskQuery.processCategoryIn(Lists.newArrayList(form.getBizType()));
        }
        if (!Tools.isEmpty(form.getKeyword())) {
            taskQuery.processInstanceBusinessKeyLikeIgnoreCase("%" + form.getKeyword() + "%");
        }
        if (pageNum == 1) {
            page.setTotal(taskQuery.count());
        }
        List<HistoricTaskInstance> tasks = taskQuery.orderByTaskCreateTime().desc().listPage((pageNum - 1) * pageSize, pageSize);
        if (!ObjectUtils.isEmpty(tasks)) {
            Map<String, ProcessInstance> procInstMap = this.getProcessInstance(tasks);
            for (HistoricTaskInstance task : tasks) {
                taskVos.add(this.getMyFinishTask(task, procInstMap));
            }
        }
        page.setList(taskVos);
        return page;
    }

    /**
     * 得到已办
     *
     * @param tasks
     * @return
     */
    private Map<String, ProcessInstance> getProcessInstance(List<HistoricTaskInstance> tasks) {
        Map<String, ProcessInstance> procInstMap = new HashMap<>();
        if (!Tools.isEmpty(tasks)) {
            Set<String> procInstIds = new HashSet<>();
            for (HistoricTaskInstance task : tasks) {
                procInstIds.add(task.getProcessInstanceId());
            }
            List<ProcessInstance> procInsts = this.runtimeService.createProcessInstanceQuery().processInstanceIds(procInstIds).list();
            for (ProcessInstance procInst : procInsts) {
                procInstMap.put(procInst.getProcessInstanceId(), procInst);
            }
        }
        return procInstMap;
    }

    /**
     * 得到已办
     *
     * @param task
     * @return
     */
    private MyFinishTaskVo getMyFinishTask(HistoricTaskInstance task, Map<String, ProcessInstance> procInstMap) {
        MyFinishTaskVo taskVo = new MyFinishTaskVo();
        taskVo.setId(task.getId());
        taskVo.setProcInstId(task.getProcessInstanceId());
        taskVo.setTaskName(task.getName());
        taskVo.setCreateTime(task.getCreateTime());
        taskVo.setEndTime(task.getEndTime());
        if (!Tools.isEmpty(task.getDescription())) {
            Map<String, Object> desc = JsonUtil.readValue(task.getDescription(), Map.class);
            if (!Tools.isEmpty(desc) && !Tools.isEmpty(desc.get("sender"))) {
                taskVo.setSender(JsonUtil.readValue(JsonUtil.toJson(desc.get("sender")), UserVo.class));
            }
        }
        taskVo.setStatus(procInstMap.containsKey(task.getProcessInstanceId()) ? "未完成" : "已完成");
        return taskVo;
    }

    /**
     * 查询我发起的流程
     *
     * @param userId   用户
     * @param pageSize 每页大小
     * @param pageNum  当前页
     * @return 发起的流程
     */
    @Override
    public PageInfo<WfMineTaskVo> queryWfMineTaskList(String userId, int pageSize, int pageNum, WfMineSearchForm search) {
        PageHelper.startPage(pageNum, pageSize, pageNum == 1);
        search.setKeyword(Tools.isEmpty(search.getKeyword()) ? null : ("%" + search.getKeyword() + "%"));
        List<WfMineTaskVo> taskVos = this.mapper.selectWfMineTaskList(userId, search);
        return new PageInfo(taskVos);
    }

    @Override
    public List<WfProcLogVo> queryProcessHandleLogList(List<String> procInstIds) throws ParseException {
        List<WfProcLogVo> wfProcLogVos = new ArrayList<>();
        //名稱，開始時間，完成時間
        List<WfProcLogVo> wfProcLogVos1 = this.mapper.selectWorkFlowInfoByProcInstIds(procInstIds);
        //當前所屬節點，創建時間
        List<WfProcLogVo> wfProcLogVos2 = this.mapper.selectWorkFlowInfo2ByProcInstIds(procInstIds);
        Map<String, List<WfProcLogVo>> wfProcLogVosMap = ListUtil.bulidTreeListMap(wfProcLogVos2, "procInstId", String.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!ObjectUtils.isEmpty(wfProcLogVos1)) {
            for (WfProcLogVo procLogVo : wfProcLogVos1) {
                String procInstId = procLogVo.getProcInstId();
                List<WfProcLogVo> childWfProcLogVos = wfProcLogVosMap.get(procInstId);
                if (!ObjectUtils.isEmpty(childWfProcLogVos)) {
                    WfProcLogVo wfProcLogChildVos2 = childWfProcLogVos.get(0);
                    procLogVo.setActivity(wfProcLogChildVos2.getActivity());
                    Date creatTime = sdf.parse(wfProcLogChildVos2.getCreatTime());
                    procLogVo.setStayTime(DateUtil.getDatePoor(creatTime, new Date()));
                    if (!Tools.isEmpty(wfProcLogChildVos2.getNewUser())) {
                        procLogVo.setNewUser(wfProcLogChildVos2.getNewUser());
                    } else if (!Tools.isEmpty(wfProcLogChildVos2.getId())) {
                        List<IdentityLink> ils = this.processEngine.getTaskService().getIdentityLinksForTask(FormatUtil.toString(wfProcLogChildVos2.getId()));
                        if (!Tools.isEmpty(ils)) {
                            for (IdentityLink il : ils) {
                                if (!Tools.isEmpty(il.getUserId())) {
                                    procLogVo.setNewUser(il.getUserId());
                                }
                            }
                        }
                    }
                }
                if (!ObjectUtils.isEmpty(procLogVo.getEndTime())) {
                    procLogVo.setStatus("完成");
                } else {
                    procLogVo.setStatus("未完成");
                }
                wfProcLogVos.add(procLogVo);
            }
        }
        return wfProcLogVos;
    }

    /**
     * 流程进展日志
     *
     * @param procInstId 流程实例ID
     * @return 日志
     */
    @Override
    public WfLogDetailVo queryProcessHandleLogDetail(String procInstId) {
        List<Comment> comments = this.taskService.getProcessInstanceComments(procInstId);
        Map<String, Comment> taskId_comment = new HashMap<>();
        if (comments != null) {
            for (Comment comment : comments) {
                taskId_comment.put(comment.getTaskId(), comment);
            }
        }
        HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        List<HistoricTaskInstance> historicTaskInstances = this.historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId).orderByHistoricTaskInstanceEndTime().desc().list();
        List<WorkFlowLogVo> logList = this.mapper.selectWorkFlowLogByProcInsId(procInstId);
        Map<String, String> logMap = new HashMap<>();
        Map<String, String> dateMap = new HashMap<>();
        Map<String, String> nextUserMap = new HashMap<>();
        Map<String, Date> actEndMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(logList)) {
            for (WorkFlowLogVo log : logList) {
                logMap.put("WORKITEMID", log.getOperate());
                dateMap.put("WORKITEMID", log.getWsdCreator());
                nextUserMap.put("WORKITEMID", log.getNextUserId());
                actEndMap.put("WORKITEMID", log.getActEndTime());

            }
        }
        //用户id集合
        List<String> userIdList = ListUtil.toValueList(logList, "nextUserId", String.class);
        List<String> userIdsList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(userIdList)) {
            for (String userId : userIdList) {
                if (userId.contains(",")) {
                    List<String> userIds = ListUtil.spliceArrayListByStr(userId, ",");
                    userIdsList.addAll(userIds);
                } else {
                    userIdsList.add(userId);
                }
            }
        }
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
        BpmnModel bpmnModel = this.getBpmnModel(processDefinitionEntity);
        List<WfProcLogDetailVo> wfProcLogDetailVos = new ArrayList<>();
        if (!ObjectUtils.isEmpty(historicTaskInstances)) {
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                WfProcLogDetailVo wfProcLogDetailVo = new WfProcLogDetailVo();
                wfProcLogDetailVo.setOpinion(ObjectUtils.isEmpty(taskId_comment.get(historicTaskInstance.getId())) ? "" : taskId_comment.get(historicTaskInstance.getId()).getFullMessage());
                wfProcLogDetailVo.setOperateUser(!ObjectUtils.isEmpty(dateMap) ? dateMap.get(historicTaskInstance.getId()) : "");
                wfProcLogDetailVo.setCreateTime(ObjectUtils.isEmpty(taskId_comment.get(historicTaskInstance.getId())) ? null : taskId_comment.get(historicTaskInstance.getId()).getTime());
                String operate = logMap.get(historicTaskInstance.getId());
                wfProcLogDetailVo.setOperateType(ObjectUtils.isEmpty(operate) ? "" : (operate.equals("EXECUTE") ? "同意" : operate.equals("BACKPLAN") ? "驳回计划" : (operate.equals("BACK") ? "回退" : (operate.equals("TERMINATE") ? "终止" : (operate.equals("HELP") ? "协办" : (operate.equals("DELEG") ? "代办" : (operate.equals("REDO") ? "重做" : (operate.equals("CALLBACK") ? "收回" : (operate.equals("REJECT") ? "拒绝" : (operate.equals("CONFIRM") ? "确认" : ""))))))))));
                wfProcLogDetailVo.setWorkItemName(ObjectUtils.isEmpty(bpmnModel.getMainProcess().getFlowElement(historicTaskInstance.getTaskDefinitionKey()).getName()) ? "" : bpmnModel.getMainProcess().getFlowElement(historicTaskInstance.getTaskDefinitionKey()).getName());
                wfProcLogDetailVo.setNextUserName(nextUserMap.get(historicTaskInstance.getId()));
                wfProcLogDetailVo.setStayTime(DateUtil.getDatePoor(FormatUtil.toDate(historicTaskInstance.getStartTime() == null ? "" : FormatUtil.stringFromTime(historicTaskInstance.getStartTime())), (FormatUtil.toDate(historicTaskInstance.getStartTime() == null ? "" : FormatUtil.stringFromTime(historicTaskInstance.getEndTime())))));
                wfProcLogDetailVos.add(wfProcLogDetailVo);
            }
        }
        WfLogDetailVo wfLogDetailVo = new WfLogDetailVo();
        if (!ObjectUtils.isEmpty(historicProcessInstance)) {
            wfLogDetailVo.setId(0);
            wfLogDetailVo.setProcessInstName(historicProcessInstance.getName());
            wfLogDetailVo.setWfLog(wfProcLogDetailVos);
        }
        return wfLogDetailVo;

    }

    /**
     * 得到设计模型
     *
     * @param processDefinitionEntity Entity
     * @return BpmnModel
     */
    private BpmnModel getBpmnModel(ProcessDefinitionEntity processDefinitionEntity) {
        BpmnModel bpmnModel = null;
        try {
            InputStream is = this.repositoryService.getResourceAsStream(processDefinitionEntity.getDeploymentId(), processDefinitionEntity.getResourceName());
            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            bpmnModel = bpmnXMLConverter.convertToBpmnModel(new InputStreamSource(is), false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bpmnModel;
    }

    /**
     * queryTaskIdByCurrentUserIdAndProcInstrId
     *
     * @param procInstId procInstId
     * @param userId     userId
     * @return String
     */
    @Override
    public String queryTaskIdByUserIdAndProcInstrId(String procInstId, String userId) {
        //获取待办
        List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(procInstId).taskCandidateOrAssigned(userId).list();
        if (!Tools.isEmpty(tasks)) {
            return tasks.get(tasks.size() - 1).getId();
        } else { //获取已办
            List<HistoricTaskInstance> hiTasks = this.historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId).taskAssignee(userId).finished().list();
            if (!Tools.isEmpty(hiTasks)) {
                return hiTasks.get(hiTasks.size() - 1).getId();
            }
        }
        return null;
    }

    /**
     * 获取分配流程信息
     *
     * @param modelIds ID
     * @return vo
     */
    @Override
    public List<ActModelVo> queryWfAssignList(List<String> modelIds) {
        List<ActModelVo> actModelVos = new ArrayList<>();
        if (!ObjectUtils.isEmpty(modelIds)) {
            actModelVos = this.mapper.selectActAssignList(modelIds);
            if (!ObjectUtils.isEmpty(actModelVos)) {
                for (ActModelVo actModelVo : actModelVos) {
                    actModelVo.setStatus(!ObjectUtils.isEmpty(actModelVo.getStatus()) ? "已发布" : "未发布");
                }
            }
        }
        return actModelVos;
    }

    /**
     * 获取分配流程定义信息
     *
     * @param modelIds ID
     * @return vo
     */
    @Override
    public List<WfProcessDefVo> queryProcDefByModulIdList(List<String> modelIds) {
        return this.mapper.selectProcDefByModulIdList(modelIds);
    }

    /**
     * 创建模版
     *
     * @param form
     * @return modelId
     */
    @Override
    public String addModel(ActModelAddForm form) {
        int revision = 1; //版本
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        Model model = repositoryService.newModel(); //初始化一个空模型
        model.setName(form.getModelTitle()); //名称
        model.setCategory(form.getCategory());
        //model.setTenantId(tenantId);
        repositoryService.saveModel(model);
        String modelId = model.getId();
        //更新模型
        model = repositoryService.getModel(modelId); //不重新查询对象在事务中会出错
        String key = getNewModelKey(modelId);
        model.setKey(key);
        ObjectNode metaInfoJson = this.objectMapper.createObjectNode();
        metaInfoJson.put(ModelDataJsonConstants.MODEL_NAME, key);
        metaInfoJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, form.getModelDesc()); //描述
        metaInfoJson.put(ModelDataJsonConstants.MODEL_REVISION, revision);
        model.setMetaInfo(metaInfoJson.toString());
        repositoryService.saveModel(model);
        //完善ModelEditorSource
        ObjectNode editorNode = this.objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");

        ObjectNode properties = this.objectMapper.createObjectNode();
        properties.put("process_id", key);
        properties.put("name", form.getModelTitle());
        editorNode.putPOJO("properties", properties);

        ObjectNode stencilSetNode = this.objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.putPOJO("stencilset", stencilSetNode);

        repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return modelId;
    }

    /**
     * 得到一个新生成的KEY
     *
     * @param id Id
     * @return key
     */
    private String getNewModelKey(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //yyyyMMdd_HHmmss_S
        return "model_" + sdf.format(new Date()) + "_" + id;
    }

    /**
     * 发布模版
     *
     * @param modelId ID
     * @return success
     */
    @Override
    public String deployProcess(String modelId) throws IOException {
        RepositoryService repositoryService = this.processEngine.getRepositoryService(); //获取模型
        Model modelData = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
        if (bytes == null) {
            return ("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }
        JsonNode modelNode = new ObjectMapper().readTree(bytes);
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if (model.getProcesses().size() == 0) {
            return ("数据模型不符要求，请至少设计一条主线流程。");
        }
        String checkModel = this.validationProcess(model); //验证模型
        if (!ObjectUtils.isEmpty(checkModel)) {
            return checkModel;
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
        String processName = modelData.getName() + ".bpmn20.xml"; //发布流程名称
        Deployment deployment = repositoryService.createDeployment().name(modelData.getName()) //
            .category(modelData.getCategory()).tenantId(modelData.getTenantId()) //
            .addString(processName, new String(bpmnBytes, StandardCharsets.UTF_8)).deploy(); //
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
        ProcessDefinition procDef = this.processEngine.getRepositoryService().createProcessDefinitionQuery()//
            .deploymentId(deployment.getId()).singleResult();
        if (!Tools.isEmpty(procDef)) {
            this.processEngine.getRepositoryService().setProcessDefinitionCategory(procDef.getId(), modelData.getCategory());
        }
        return "success";
    }

    /**
     * 验证流程
     *
     * @param bpmnModel 模型
     */
    private String validationProcess(BpmnModel bpmnModel) {
        String retMsg = "";
        if (!Tools.isEmpty(bpmnModel) && !Tools.isEmpty(bpmnModel.getProcesses())) { //设置活动定义参与者名称
            Collection<FlowElement> flowElementList = bpmnModel.getProcesses().get(0).getFlowElements(); //得到所有节点
            for (FlowElement flow : flowElementList) {
                if (flow instanceof UserTask || flow instanceof ExclusiveGateway) { //验证人工活动和单一网关后有无条件
                    FlowNode flowNode = (FlowNode) flow;
                    List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
                    if (!ObjectUtils.isEmpty(sequenceFlows) && sequenceFlows.size() > 1) {
                        for (SequenceFlow sequenceFlow : sequenceFlows) {
                            if (ObjectUtils.isEmpty(sequenceFlow.getConditionExpression())) {
                                retMsg = "【" + flow.getName() + "】的后续活动单一分支【" + sequenceFlow.getName() + "】未设置条件！";
                                return retMsg;
                            }
                        }
                    }
                }
                if (flow instanceof UserTask) { //验证人工活动无名称
                    FlowNode flowNode = (FlowNode) flow;
                    if (ObjectUtils.isEmpty(flowNode.getName())) {
                        retMsg = "存在人工活动未设置名称！";
                        return retMsg;
                    }
                }
            }
        }
        return retMsg;
    }

    /**
     * 复制流程
     *
     * @param modelId 模型id
     * @return id
     */
    @Override
    public String copyModel(String modelId) {
        return this.copyModel(modelId, null);
    }

    /**
     * 复制流程
     *
     * @param modelId  模型id
     * @param tenantId 租户ID
     * @return id
     */
    @Override
    public String copyModel(String modelId, String tenantId) {
        try {
            RepositoryService repositoryService = this.processEngine.getRepositoryService();
            //初始化一个空模型  用来拷贝数据用的
            Model modelCopy = repositoryService.getModel(modelId);
            if (modelCopy != null) {
                Model model = repositoryService.newModel();
                model.setName(modelCopy.getName());
                model.setCategory(modelCopy.getCategory());
                model.setTenantId(tenantId);
                model.setVersion(1);
                //model.setCategory(category);
                repositoryService.saveModel(model);
                //完善信息
                String id = model.getId();
                model = repositoryService.getModel(id); //不重新查询对象在事务中会出错
                String key = getNewModelKey(id);
                ObjectNode metaInfoJson = (ObjectNode) objectMapper.readTree(modelCopy.getMetaInfo());
                metaInfoJson.put(ModelDataJsonConstants.MODEL_NAME, key);
                model.setKey(key);
                model.setMetaInfo(metaInfoJson.toString());
                repositoryService.saveModel(model);
                //完善ModelEditorSource
                byte[] source = repositoryService.getModelEditorSource(modelId);
                ObjectNode modelJson = (ObjectNode) objectMapper.readTree(source);
                ObjectNode properties = (ObjectNode) modelJson.get("properties");
                properties.put("process_id", key);
                repositoryService.addModelEditorSource(id, modelJson.toString().getBytes(StandardCharsets.UTF_8));
                repositoryService.addModelEditorSourceExtra(id, repositoryService.getModelEditorSourceExtra(modelId));
                return id;
            }
        } catch (Exception e) {
            throw new ActivitiException("Error saving model", e);
        }
        return null;
    }

    /**
     * 删除
     *
     * @param modelId ID
     * @return success
     */
    @Override
    public String deleteActivityNewModel(String modelId) {
        RepositoryService service = this.processEngine.getRepositoryService();
        service.deleteModel(modelId);
        return "success";
    }

    /**
     * 根据流程实例Id获取流程名称
     *
     * @param processInstanceId 实例ID
     * @return 流程实例名称
     */
    @Override
    public WfProcessInstVo getProcessInstanceById(String processInstanceId) {
        WfProcessInstVo instVo = null;
        HistoricProcessInstanceEntity hpi = (HistoricProcessInstanceEntity) this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (!Tools.isEmpty(hpi)) {
            instVo = this.getProcessInstanceByHistoricProcessInstance(hpi);
        }
        return instVo;
    }

    /**
     * 得到流程实例
     *
     * @param hpi 流程实例
     * @return 流程实例
     */
    public WfProcessInstVo getProcessInstanceByHistoricProcessInstance(HistoricProcessInstance hpi) {
        WfProcessInstVo instVo = new WfProcessInstVo();
        instVo.setProcDefKey(hpi.getProcessDefinitionKey());
        instVo.setProcDefId(hpi.getProcessDefinitionId());
        instVo.setProcDefName(hpi.getProcessDefinitionName());
        instVo.setProcInstId(hpi.getId());
        instVo.setProcInstName(hpi.getName());
        instVo.setStartUserId(hpi.getStartUserId());
        instVo.setStartTime(hpi.getStartTime());
        instVo.setEndTime(hpi.getEndTime());
        return instVo;
    }

    /**
     * 根据流程实例Id获取流程名称
     *
     * @param processInstanceId 实例ID
     * @return 流程实例名称
     */
    @Override
    public List<WfActivityInstanceVo> getActivityInstanceByProcessInstanceId(String processInstanceId) {
        List<WfActivityInstanceVo> instVos = new ArrayList<>();
        List<HistoricActivityInstance> hais = this.historyService.createHistoricActivityInstanceQuery() //
            .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        if (!Tools.isEmpty(hais)) {
            for (HistoricActivityInstance hai : hais) {
                instVos.add(this.getActivityInstanceByHistoricActivityInstance(hai));
            }
        }
        return instVos;
    }

    /**
     * 得到活动
     *
     * @param hai
     * @return
     */
    public WfActivityInstanceVo getActivityInstanceByHistoricActivityInstance(HistoricActivityInstance hai) {
        WfActivityInstanceVo instVo = new WfActivityInstanceVo();
        instVo.setId(hai.getId());
        instVo.setActivityId(hai.getActivityId());
        instVo.setActivityName(hai.getActivityName());
        instVo.setActivityType(hai.getActivityType());
        instVo.setProcessDefinitionId(hai.getProcessDefinitionId());
        instVo.setProcessInstanceId(hai.getProcessInstanceId());
        instVo.setAssignee(hai.getAssignee());
        instVo.setStartTime(hai.getStartTime());
        instVo.setEndTime(hai.getEndTime());
        instVo.setGetDurationInMillis(hai.getDurationInMillis());
        return instVo;
    }

    /**
     * 根据流程实例Id获取流程名称
     *
     * @param activityInstanceId 实例ID
     * @return 流程实例名称
     */
    @Override
    public WfActivityInstanceVo getActivityInstanceByActivityInstanceId(String activityInstanceId) {
        WfActivityInstanceVo instVo = null;
        HistoricActivityInstance hai = this.historyService.createHistoricActivityInstanceQuery() //
            .activityInstanceId(activityInstanceId).singleResult();
        if (!Tools.isEmpty(hai)) {
            instVo = this.getActivityInstanceByHistoricActivityInstance(hai);
        }
        return instVo;
    }

    /**
     * 根据工作项id获得工作项
     *
     * @param taskId 任务ID
     * @return WfTaskVo
     */
    @Override
    public WfTaskVo getTaskByTaskId(String taskId) {
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        if (!Tools.isEmpty(task)) {
            return this.getTaskByTask(task);
        } else {
            return this.getHistTaskById(taskId);
        }
    }

    /**
     * 转换对象
     *
     * @param task 任务ID
     * @return 任务
     */
    private WfTaskVo getTaskByTask(Task task) {
        WfTaskVo taskVo = new WfTaskVo();
        taskVo.setId(task.getId());
        taskVo.setActivityId(task.getTaskDefinitionKey());
        taskVo.setName(task.getName());
        taskVo.setProcDefId(task.getProcessDefinitionId());
        taskVo.setProcInstId(task.getProcessInstanceId());
        taskVo.setDelegation(task.getDelegationState() != null ? task.getDelegationState().name() : "");
        taskVo.setAssignee(task.getAssignee());
        taskVo.setOwner(task.getOwner());
        taskVo.setCreateTime(task.getCreateTime());
        return taskVo;
    }

    /**
     * 得到任务的历史记录
     *
     * @param taskId 任务ID
     * @return 任务
     */
    private WfTaskVo getHistTaskById(String taskId) {
        WfTaskVo taskVo = null;
        HistoricTaskInstance task = this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (!Tools.isEmpty(task)) {
            taskVo = this.getHistTaskByTask(task);
        }
        return taskVo;
    }

    /**
     * 转换对象
     *
     * @param task 任务
     * @return 任务
     */
    private WfTaskVo getHistTaskByTask(HistoricTaskInstance task) {
        WfTaskVo taskVo = new WfTaskVo();
        taskVo.setId(task.getId());
        taskVo.setActivityId(task.getTaskDefinitionKey());
        taskVo.setName(task.getName());
        taskVo.setProcDefId(task.getProcessDefinitionId());
        taskVo.setProcInstId(task.getProcessInstanceId());
        taskVo.setAssignee(task.getAssignee());
        taskVo.setOwner(task.getOwner());
        taskVo.setCreateTime(task.getCreateTime());
        taskVo.setEndTime(task.getEndTime());
        return taskVo;
    }

    /**
     * 根据流程实例查询任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    @Override
    public List<WfTaskVo> getRunTaskByProcessInstanceId(String processInstanceId) {
        List<WfTaskVo> taskVos = new ArrayList<>();
        List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (!Tools.isEmpty(tasks)) {
            for (Task task : tasks) {
                WfTaskVo taskVo = this.getTaskByTask(task);
                taskVos.add(taskVo);
            }
        }
        return taskVos;
    }

    /**
     * 根据流程实例查询任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    @Override
    public List<WfTaskVo> getTaskByProcessInstanceId(String processInstanceId) {
        List<WfTaskVo> taskVos = new ArrayList<>();
        List<HistoricTaskInstance> tasks = this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
        if (!Tools.isEmpty(tasks)) {
            for (HistoricTaskInstance histTask : tasks) {
                WfTaskVo taskVo = this.getHistTaskByTask(histTask);
                taskVos.add(taskVo);
            }
        }
        return taskVos;
    }

    /**
     * 根据流程实例查询任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    @Override
    public List<WfRunTaskVo> getRunTaskByProcessInstanceId(String processInstanceId, String userId) {
        List<WfRunTaskVo> taskVos = new ArrayList<>();
        List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).active().taskCandidateOrAssigned(userId).list();
        if (!Tools.isEmpty(tasks)) {
            ProcessDefinitionEntity procDef = this.getProcessDefinitionEntity(tasks.get(0).getProcessDefinitionId());
            ActivityImpl first = this.getFirstActivity(procDef);
            List<ActivityImpl> last = this.getLastActivity(procDef);
            for (Task task : tasks) {
                WfTaskVo taskVo = this.getTaskByTask(task);
                WfRunTaskVo vo = this.dozerMapper.map(taskVo, WfRunTaskVo.class);
                vo.setStart(vo.getActivityId().equals(first.getId())); //是否为发起节点
                for (ActivityImpl ai : last) {
                    if (vo.getActivityId().equals(ai.getId())) {
                        vo.setEnd(true); //是否为结束审批节点
                    }
                }
                taskVos.add(vo);
            }
        }
        return taskVos;
    }

    /**
     * 根据流程实例查询任务
     *
     * @param taskId 流程实例ID
     * @return 任务
     */
    @Override
    public List<String> getTaskCandidates(String taskId) {
        List<String> ids = new ArrayList<>();
        List<IdentityLink> ils = this.processEngine.getTaskService().getIdentityLinksForTask(taskId);
        if (!Tools.isEmpty(ils)) {
            for (IdentityLink il : ils) {
                ids.add(il.getUserId());
            }
        }
        return ids;
    }

    /**
     * 判断是否开始
     *
     * @param task
     * @return
     */
    public static boolean isStart(TaskEntity task) {
        if (!Tools.isEmpty(task.getExecution())) {
            ActivityImpl activity = task.getExecution().getActivity();
            if (!Tools.isEmpty(activity) && !Tools.isEmpty(activity.getIncomingTransitions())) {
                for (PvmTransition pt : activity.getIncomingTransitions()) {
                    if (!Tools.isEmpty(pt.getSource()) && "startEvent".equals(pt.getSource().getProperty("type"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否结束
     *
     * @param task
     * @return
     */
    public static boolean isEnd(TaskEntity task) {
        if (!Tools.isEmpty(task.getExecution())) {
            ActivityImpl activity = task.getExecution().getActivity();
            if (!Tools.isEmpty(activity) && !Tools.isEmpty(activity.getOutgoingTransitions())) {
                for (PvmTransition pt : activity.getOutgoingTransitions()) {
                    if (!Tools.isEmpty(pt.getDestination()) && "endEvent".equals(pt.getDestination().getProperty("type"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 查询流程配置URL
     *
     * @return
     */
    @Override
    public String getWfListenerConfigureUrl() {
        return this.mapper.selectWfListenerConfigureUrl();
    }

    public static void main(String[] args) {
        Map<String, Object> param = new HashMap<>();
		//param.put("sid_D16274D9_2F36_4F72_8CB5_0C6B2F9FB53A", true);
		//param.put("sid_D16274D9_2F36_4F72_8CB5_0C6B2F9FB53B", "2");
		//String expression = "${ sid_D16274D9_2F36_4F72_8CB5_0C6B2F9FB53A == true }";
        List<Object> b = new ArrayList<>();
        b.add("a");
        b.add("b");
        b.add("c");

        List<Map<?, ?>> c = new ArrayList<>();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("a", "a");
        m.put("b", "b");
        m.put("c", "c");

        param.put("a", "1,2,3");
        param.put("b", b);
        param.put("c", m);
        String expression = "${ expr . existActivity( nextActPart , '111111111111111111')}";
        System.out.println(expression.replace(" ", "").contains("expr.existActivity(nextActPart,"));
        System.out.println(new ActivitiServiceImpl().parseExpression(expression, param));
    }
}
