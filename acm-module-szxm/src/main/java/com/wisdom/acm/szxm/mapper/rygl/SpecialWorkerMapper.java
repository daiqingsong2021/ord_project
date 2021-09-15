package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.SpecialWorkerPo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkerVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpecialWorkerMapper extends CommMapper<SpecialWorkerPo>
{
    List<SpecialWorkerVo> selectSpecialWorker(Map<String, Object> queryMap);

    /**
     * 检查特殊工种进场审批通过日期与证书数据创建日期超过7天数量
     * @param sectionId
     * @return
     */
    Integer querySpecialWorkerCount(@Param("sectionId")String sectionId);
}
