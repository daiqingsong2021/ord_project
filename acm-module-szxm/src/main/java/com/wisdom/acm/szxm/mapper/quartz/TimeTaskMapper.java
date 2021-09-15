package com.wisdom.acm.szxm.mapper.quartz;

import com.wisdom.acm.szxm.po.rygl.TimeTaskPo;
import com.wisdom.acm.szxm.vo.quartz.TimeTaskVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface TimeTaskMapper extends CommMapper<TimeTaskPo>
{
    List<TimeTaskVo> selectTimeTask(Map<String, Object> mapWhere);
}
