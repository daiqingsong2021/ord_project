package com.wisdom.acm.dc3.mapper;

import com.wisdom.acm.dc3.po.EnergyDetailPo;
import com.wisdom.acm.dc3.po.EnergyMonthlyPo;
import com.wisdom.acm.dc3.vo.EnergyDetailVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface EnergyDetailMapper extends CommMapper<EnergyDetailPo>
{
    List<EnergyDetailVo> selectByParams(Map<String, Object> mapWhere);

    EnergyMonthlyPo selectByMonthRecordTime(Map<String, Object> mapWhere);

}
