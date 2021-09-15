package com.wisdom.acm.dc5.mapper;

import com.wisdom.acm.dc5.po.FaultDailyProblemDealPo;
import com.wisdom.acm.dc5.po.FaultDailyProblemPo;
import com.wisdom.acm.dc5.vo.FaultDailyProblemDealVo;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FaultDailyProblemDealMapper extends CommMapper<FaultDailyProblemDealPo> {

    List<FaultDailyProblemDealVo> selectFaultDailyProblemDealByParams(Map<String, Object> mapWhere);

}
