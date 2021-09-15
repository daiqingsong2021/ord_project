package com.wisdom.acm.activiti.service.impl;

import com.wisdom.acm.activiti.mapper.ActivitiMapper;
import com.wisdom.acm.activiti.po.WorkflowPo;
import com.wisdom.acm.activiti.service.ActivitiDefService;
import com.wisdom.base.common.service.BaseService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.*;

@Service
public class ActivitiDefServiceImpl extends BaseService<ActivitiMapper, WorkflowPo> implements ActivitiDefService {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void deleteProcess(String deploymentId){
        processEngine.getRepositoryService().deleteDeployment(deploymentId, true); //删除流程定义及所有子表
    }

    //@Override
    public BpmnModel getBpmnModel(String procDefId){
        try{
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(procDefId);
            InputStream is = repositoryService.getResourceAsStream(processDefinitionEntity.getDeploymentId(),processDefinitionEntity.getResourceName());
            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(new InputStreamSource(is), false, false);
            return bpmnModel;
        }catch (Exception e){

        }
        return null;
    }
}
