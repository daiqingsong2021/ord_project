package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.SmsTaskPo;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface SmsTaskMapper extends CommMapper<SmsTaskPo> {

    /**
     * 查询列表
     * @param mapWhere
     * @return
     */
    List<SmsTaskVo> querySmsTaskList(Map<String, Object> mapWhere);

    //扫描定时任务
    List<SmsTaskVo> scanSmsTaskList();
}
