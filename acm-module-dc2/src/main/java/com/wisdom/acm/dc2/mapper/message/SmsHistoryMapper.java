package com.wisdom.acm.dc2.mapper.message;

import com.wisdom.acm.dc2.po.SmsTaskPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SmsHistoryMapper extends CommMapper<SmsTaskPo>
{
}
