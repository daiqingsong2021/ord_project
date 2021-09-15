package com.wisdom.acm.dc5.mapper;

import com.wisdom.acm.dc5.po.FaultMonthlyPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FaultMonthlyMapper extends CommMapper<FaultMonthlyPo> {
    List<FaultMonthlyPo> selectFaultMonthByParams(Map<String, Object> mapWhere);

}
