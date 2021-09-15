package com.wisdom.acm.dc5.mapper;

import com.wisdom.acm.dc5.po.FaultDailyProblemPo;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FaultDailyProblemMapper extends CommMapper<FaultDailyProblemPo> {

    List<FaultDailyProblemVo> selectFaultDailyProblemByParams(Map<String, Object> mapWhere);

    int deleteFaultDailyProblemDealByProblemIds(Map<String, Object> mapWhere);

}
