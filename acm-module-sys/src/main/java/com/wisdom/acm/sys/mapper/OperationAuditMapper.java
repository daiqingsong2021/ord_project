package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysSearchAuditForm;
import com.wisdom.acm.sys.po.SysOperationAuditPo;
import com.wisdom.acm.sys.vo.SysOperationAuditVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperationAuditMapper extends CommMapper<SysOperationAuditPo> {

    /**
     * 获取操作审计列表
     * @param searchMap
     * @return
     */
    List<SysOperationAuditVo> selectOperationAudit(@Param("searchMap") SysSearchAuditForm searchMap);
}
