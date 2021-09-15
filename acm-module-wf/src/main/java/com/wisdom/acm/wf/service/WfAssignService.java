package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.form.WfAssignAddForm;
import com.wisdom.acm.wf.po.WfAssignPo;
import com.wisdom.acm.wf.vo.WfAssignVo;
import com.wisdom.base.common.form.ActModelAddForm;
import com.wisdom.base.common.vo.base.ActModelVo;
import com.wisdom.base.common.vo.wf.WfProcessDefVo;

import java.util.List;

public interface WfAssignService {
    List<WfAssignVo> queryWfAssignList();

    WfAssignVo addActivityNewModel(ActModelAddForm actModelAddForm);

    WfAssignPo assignBussiNewModel(WfAssignAddForm wfAssignAddForm);

    WfAssignVo releaseBussiNewModel(String modelId);

    String deleteBussiNewModel(String modelId);

    /**
     * 得到业务的流程定义
     * @param typeCode
     * @return
     */
    List<WfProcessDefVo> getAllModelByTypeId(String typeCode);
}
