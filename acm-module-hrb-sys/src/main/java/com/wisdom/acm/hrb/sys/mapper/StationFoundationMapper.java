package com.wisdom.acm.hrb.sys.mapper;

import com.wisdom.acm.hrb.sys.po.StationFoundationPo;
import com.wisdom.acm.hrb.sys.vo.StationFoundationVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/10/20/020 10:51
 * Description:<描述>
 */
public interface StationFoundationMapper extends CommMapper<StationFoundationPo> {
    void updateStationFoundationList(@Param("updateList")List<StationFoundationPo> stationFoundationPos);
    void delStationFoundationListByLine(@Param("lines")List<String> lines);
    List<StationFoundationVo> queryStationListByParam(Map<String, Object> mapWhere);
}
