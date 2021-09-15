package com.wisdom.acm.hrb.sys.service;

import com.wisdom.acm.hrb.sys.form.StationFoundationAddForm;
import com.wisdom.acm.hrb.sys.form.StationFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.po.StationFoundationPo;
import com.wisdom.acm.hrb.sys.vo.StationFoundationVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/10/20/020 10:56
 * Description:<站点管理信息>
 */
public interface StationFoundationService extends CommService<StationFoundationPo> {
    List<StationFoundationVo> addStationFoundationList(List<StationFoundationAddForm> stationFoundationAddForms);
    List<StationFoundationVo> queryStationFoundationList(String line, String stationType);
    Map<String, String> getStationListMap(String line,String status);
    List<StationFoundationVo> updateStationFoundationList(List<StationFoundationUpdateForm> stationFoundationUpdateForms);
    Map<String,List<String>>  getAuxiliaryStationList(String line);
    void delStationFoundationListByLine(List<String> lines);
    void delStationFoundationList(List<StationFoundationVo> stationFoundationVos);
    List<StationFoundationVo> getStationList(String line, String status);
    public List<StationFoundationVo> queryStationListByParam(Map<String, Object> mapWhere);
    String checkStationFoundationIsHave(String line,String stationCode,String stationType);
}
