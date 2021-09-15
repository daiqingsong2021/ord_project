package com.wisdom.acm.hrb.sys.mapper;

import com.wisdom.acm.hrb.sys.po.StationStationFoundationPo;
import com.wisdom.acm.hrb.sys.vo.StationStationFoundationVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/10/20/020 10:52
 * Description:<描述>
 */
public interface StationStationFoundationMapper extends CommMapper<StationStationFoundationPo> {
    void updateStationDistanceFoundationList(@Param("updateList")List<StationStationFoundationPo> stationStationFoundationPos);
    void delStationStationFoundationServiceByLine(@Param("lines")List<String> lines);

    List<StationStationFoundationVo>  queryStationStationFoundationByParam(Map<String, Object> mapWhere);
    StationStationFoundationVo queryStationToStationMileage(Map<String, Object> mapWhere);
}
