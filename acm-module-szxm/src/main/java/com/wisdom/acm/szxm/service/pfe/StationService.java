package com.wisdom.acm.szxm.service.pfe;


import com.wisdom.acm.szxm.form.pfe.StationAddForm;
import com.wisdom.acm.szxm.form.pfe.StationUpdateForm;
import com.wisdom.acm.szxm.po.pfe.StationPo;
import com.wisdom.acm.szxm.vo.pfe.StationVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

/**
 * 站点业务逻辑处理
 */
public interface StationService extends CommService<StationPo> {

    /**
     * 获取站点信息
     * @param id 站点ID
     * @return StationVo 站点信息
     */
    StationVo getStationInfo(Integer id);

    /**
     * 查询项目站点
     * @param projectId 项目ID
     * @return 站点List<StationVo>
     */
    List<StationVo> queryStationByProjectId(Integer projectId);

    /**
     * 新增站点
     * @param stationAddForm
     */
    StationVo addStationByForm(StationAddForm stationAddForm);

    /**
     * 修改站点信息
     * @param stationUpdateForm
     */
    StationVo updateStationByForm(StationUpdateForm stationUpdateForm);

    /**
     * 删除站点
     * @param ids
     */
    void deleteStation(List<Integer> ids);
}
