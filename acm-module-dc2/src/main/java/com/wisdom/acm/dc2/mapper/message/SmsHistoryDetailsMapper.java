package com.wisdom.acm.dc2.mapper.message;

import com.wisdom.acm.dc2.po.message.SmsHistoryDetailsPo;
import com.wisdom.acm.dc2.vo.message.SmsHistoryDetailsVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface SmsHistoryDetailsMapper extends CommMapper<SmsHistoryDetailsPo>
{
    List<SmsHistoryDetailsVo> selectByParams(Map<String, Object> mapWhere);
    
    void updateSmsHistoryDetails(Map<String, Object> mapWhere);
    
    void deleteSmsHistoryDetails(List<Integer> taskIds);
    
    int targetNumberNum(Map<String, Object> mapWhere);
}
