package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.po.WfFormDataPo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import com.wisdom.base.common.vo.wf.WfFormProcVo;
import com.wisdom.base.common.vo.wf.WfProcessBizVo;

import java.util.List;

public interface WfFormDataService extends CommService<WfFormDataPo> {

    /**
     * 查询业务数据
     * @param procInstId 流程实例ID
     * @return 业务数据
     */
    List<WfProcessBizVo> queryWfFormDataBizIds(Integer procInstId);

    /**
     * 查询流程信息
     * @param bizIds 业务ID
     * @param bizType 业务类型
     * @return 流程信息
     */
    List<WfFormProcVo> queryProcInstIdByBiz(List<Integer> bizIds, String bizType);

    /**
     * 查询流程信息
     * @param bizId 业务ID
     * @param bizType 业务类型
     * @return 流程信息
     */
    WfFormProcVo queryProcInstIdByBiz(Integer bizId, String bizType);

    /**
     * 通过流程实例ID获取流程业务数据集合
     *
     * @param procInstId
     * @return
     */
    List<WfFormDataVo> queryFormDataVosByProcInst(String procInstId);

    /**
     * 通过流程表单ID获取流程业务数据集合
     * @param formId
     * @return
     */
    List<WfFormDataVo> queryFormDataVosByFormId(String formId);
}
