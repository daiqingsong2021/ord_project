package com.wisdom.acm.dc3.mapper;

import com.wisdom.acm.dc3.po.EnergyDailyPo;
import com.wisdom.acm.dc3.po.EnergyDetailPo;
import com.wisdom.acm.dc3.po.EnergyMonthlyPo;
import com.wisdom.acm.dc3.vo.EnergyDailyVo;
import com.wisdom.acm.dc3.vo.EnergyDetailVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface EnergyDailyMapper extends CommMapper<EnergyDailyPo>
{
    List<EnergyDailyVo> selectByParams(Map<String, Object> mapWhere);

    List<EnergyDailyVo> select2DailyByParams(Map<String, Object> mapWhere);

    EnergyDailyVo selectDaily2MonthlyByParams(Map<String, Object> mapWhere);

}
