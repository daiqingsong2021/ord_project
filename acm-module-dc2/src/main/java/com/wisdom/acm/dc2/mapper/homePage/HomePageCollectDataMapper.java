package com.wisdom.acm.dc2.mapper.homePage;

import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.acm.dc2.vo.homePage.HomePageCollectDataVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface HomePageCollectDataMapper extends CommMapper<TrainDailyPo>
{
    //客运
    HomePageCollectDataVo selectTrafficLineByParams(Map<String, Object> mapWhere);
    //行车
    HomePageCollectDataVo selectTrainByParams(Map<String, Object> mapWhere);
    //能耗
    HomePageCollectDataVo selectEnergyByParams(Map<String, Object> mapWhere);
    //施工
    HomePageCollectDataVo selectConstructionByParams(Map<String, Object> mapWhere);

}
