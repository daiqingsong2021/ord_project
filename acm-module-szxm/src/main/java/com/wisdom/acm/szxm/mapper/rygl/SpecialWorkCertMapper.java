package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.CertGlPo;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkCertPo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkCertVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface SpecialWorkCertMapper extends CommMapper<SpecialWorkCertPo>
{
    List<SpecialWorkCertVo> selectSpecialWorkCert(Map<String, Object> mapWhere);

    List<SpecialWorkCertVo> selectSwWorkWithWorker();
    
    List<SpecialWorkCertVo> selectWorkCertByUserId(Map<String, Object> mapWhere);
}
