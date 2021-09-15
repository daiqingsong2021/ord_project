package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SectionAddForm;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.vo.SectionVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

/**
 * Author：wqd
 * Date：2019-09-25 13:50
 * Description：<描述>
 */
public interface GCContractService extends CommService<SysOrgPo> {

    /**
     * 获取品高接口中 所有标段信息
     * @return
     */
    List<SectionVo> queryGCSections();

    /**
     * 批量导入品高接口 中标段信息
     * @param sectionAddForms
     */
    void tbSection(List<SectionAddForm> sectionAddForms);
}
