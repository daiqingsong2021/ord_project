package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.form.WfDelegateAddForm;
import com.wisdom.acm.wf.form.WfDelegateUpdateForm;
import com.wisdom.acm.wf.po.WfDelegatePo;
import com.wisdom.acm.wf.vo.WfDelegateVo;
import com.wisdom.base.common.service.CommService;

import java.util.Date;
import java.util.List;

public interface WfDelegateService extends CommService<WfDelegatePo> {

    List<WfDelegateVo> queryWfDelegateByUserId(Integer userId);

    WfDelegateVo getWfDelegateInfo(Integer id);

    WfDelegatePo addWfDelegate(WfDelegateAddForm wfDelegateAddForm);

    WfDelegatePo updateWfDelegate(WfDelegateUpdateForm updateForm);

    void deleteWfDelegate(List<Integer> ids);

    /**
     * 查询流程代理信息
     * @param bizTypeCode 业务代码
     * @param assignees 委托人
     * @return
     */
    List<WfDelegateVo> selectWfDelegateVosByBizTypeCode(String bizTypeCode, List<Integer> assignees);
}
