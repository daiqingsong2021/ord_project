package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.wf.MyUnFinishTaskVo;

import java.util.List;

public interface WfFormService extends CommService<WfFormPo> {

    /**
     * 通过流程实例ID获取Form
     *
     * @param procInstId
     * @return
     */
    WfFormPo getFormInfoByProcInstId(String procInstId);



    int getProcCreatorByProcInstId(String procInstId);

    /**
     * 根据流程实例id获取流程实例对象集合
     *
     * @param procInstIds
     * @return
     */
    List<MyUnFinishTaskVo> queryTaskVoByProcInstIds(List<String> procInstIds);


}
