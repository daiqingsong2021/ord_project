package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.mapper.WfFormDataMapper;
import com.wisdom.acm.wf.po.WfFormDataPo;
import com.wisdom.acm.wf.service.WfFormDataService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import com.wisdom.base.common.vo.wf.WfFormProcVo;
import com.wisdom.base.common.vo.wf.WfProcessBizVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WfFormDataServiceImpl extends BaseService<WfFormDataMapper, WfFormDataPo> implements WfFormDataService {

    /**
     * 通过流程实例ID查询bizIds
     * @param procInstId 流程实例ID
     * @return 业务数据
     */
    @Override
    public List<WfProcessBizVo> queryWfFormDataBizIds(Integer procInstId){
        List<WfProcessBizVo> processBizVos = mapper.selectWfDataBizIds(procInstId);
        return processBizVos;
    }

    /**
     * 根据业务id查询所有所有流程活动集合
     * @return 业务数据
     */
    @Override
    public List<WfFormProcVo> queryProcInstIdByBiz(List<Integer> bizIds, String bizType){
        return mapper.selectProcInstIdByBizs(bizIds, bizType);
    }


    /**
     * 根据业务id查询所有所有流程活动集合
     * @return 业务数据
     */
    @Override
    public WfFormProcVo queryProcInstIdByBiz(Integer bizId, String bizType){
        return mapper.selectProcInstIdByBiz(bizId, bizType);
    }

    /**
     * 查询表单数据
     * @param procInstId 流程实例ID
     * @return 业务数据
     */
    @Override
    public List<WfFormDataVo> queryFormDataVosByProcInst(String procInstId){
        return mapper.selectFormDataVosByProcInst(procInstId);
    }

    /**
     * 查询表单数据
     * @param formId 流程表单ID
     * @return 业务数据
     */
    @Override
    public List<WfFormDataVo> queryFormDataVosByFormId(String formId){
        return mapper.selectFormDataVosByFormId(formId);
    }

}
