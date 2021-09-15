package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.form.WfLogAddForm;
import com.wisdom.acm.wf.po.WfLogPo;
import com.wisdom.acm.wf.vo.WfLogVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.wf.WfLogDetailVo;

public interface WfLogService extends CommService<WfLogPo> {
    /**
     * 查询日志
     * @param procInstId 流程实例ID
     * @return WfLogDetailVo
     */
    WfLogDetailVo getLogVoListByProcInstId(String procInstId);

    /**
     * 增加日志
     * @param form 表单
     * @return WfLogVo
     */
    WfLogVo addWfLog(WfLogAddForm form);

}
