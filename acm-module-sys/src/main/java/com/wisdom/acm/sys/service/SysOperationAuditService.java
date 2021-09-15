package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysSearchAuditForm;
import com.wisdom.acm.sys.po.SysOperationAuditPo;
import com.wisdom.acm.sys.vo.SysOperationAuditVo;
import com.wisdom.base.common.service.CommService;

public interface SysOperationAuditService extends CommService<SysOperationAuditPo> {

    /**
     * 获取操作审计列表
     * @param searchMap
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<SysOperationAuditVo> queryOperationAudit(SysSearchAuditForm searchMap, Integer pageSize, Integer currentPageNum);
}
