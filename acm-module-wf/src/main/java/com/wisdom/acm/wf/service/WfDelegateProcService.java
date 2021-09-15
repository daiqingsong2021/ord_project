package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.po.WfDelegateProcPo;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.acm.wf.vo.WfDelegateProcVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface WfDelegateProcService extends CommService<WfDelegateProcPo> {

    void wfDelegateProc(Integer delegateId, List<String> bizTypeCodes);

    List<WfDelegateProcVo> queryDelegateProcList(Integer delegateId);

    void deleteDelegateProc(List<Integer> ids);

    List<WfBizTypeVo> queryDelegateWfTypeList(Integer delegateId);
}
