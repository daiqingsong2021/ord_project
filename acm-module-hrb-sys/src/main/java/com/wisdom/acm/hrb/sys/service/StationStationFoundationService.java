package com.wisdom.acm.hrb.sys.service;

import com.wisdom.acm.hrb.sys.form.StationStationFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.po.StationStationFoundationPo;
import com.wisdom.acm.hrb.sys.vo.StationStationFoundationVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/10/20/020 10:57
 * Description:<站点区间管理表>
 */
public interface StationStationFoundationService extends CommService<StationStationFoundationPo> {
    List<StationStationFoundationVo> queryStationDistanceFoundationList(String line);
    List<StationStationFoundationVo> updateStationDistanceFoundationList(List<StationStationFoundationUpdateForm> stationStationFoundationUpdateForms);
    void delStationStationFoundationServiceByLine(List<String> lines);

    public StationStationFoundationVo queryStationToStationMileage(Map<String, Object> mapWhere);


}
