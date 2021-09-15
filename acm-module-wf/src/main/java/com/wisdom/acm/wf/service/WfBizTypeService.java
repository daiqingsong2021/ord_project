package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.form.WfTypeAddForm;
import com.wisdom.acm.wf.form.WfTypeSearchForm;
import com.wisdom.acm.wf.form.WfTypeUpdateForm;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.po.WfFormDataPo;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface WfBizTypeService extends CommService<WfBizTypePo> {
    /**
     * 查询流程定义列表
     * @return
     */
    List<WfBizTypeVo> queryWfTypeList();

    WfBizTypePo addWfType(WfTypeAddForm wfTypeAddForm);

    WfBizTypeVo selectWfTypeVo(Integer id);

    WfBizTypePo updateWfType(WfTypeUpdateForm wfTypeUpdateForm);

    void deleteWfType(Integer id);

    WfBizTypePo queryWfTypeByTypeCode(String typeCode);

    List<WfBizTypeVo> queryWfTypeBySearch(WfTypeSearchForm wfTypeSearchForm);

    /**
     * 通过流程实例ID读取流程类型VO
     *
     * @param procInstId
     * @return
     */
    WfBizTypeVo getWfTypeInfoByProcInstId(String procInstId);
}
