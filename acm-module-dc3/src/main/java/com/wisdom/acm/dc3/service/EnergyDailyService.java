package com.wisdom.acm.dc3.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc3.po.EnergyDailyPo;
import com.wisdom.acm.dc3.vo.EnergyDailyVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EnergyDailyService extends CommService<EnergyDailyPo>
{


    PageInfo<EnergyDailyVo> selectEnergyDailyPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    List<EnergyDailyVo> selectEnergyDailyList(Map<String, Object> mapWhere);

    void deleteEnergyDaily(Integer id);

   // EnergyDailyVo updateEnergyDaily(EnergyDailyUpdateForm energyDailyUpdateForm);

    EnergyDailyVo selectById(Integer id);

    List<EnergyDailyVo> selectByParams(Map<String, Object> mapWhere);

    String uploadEnergyDailyTemplate(Map<String, Object> mapWhere,MultipartFile file);

    EnergyDailyVo insertEnergyDaily(Date recordTime, String line,String linePeriod);

    EnergyDailyVo updateEnergyDaily(Integer dailyId);

    Boolean updateEnergyMonthly(Date recordTime, String line);

    /**
     * 发布
     * @param ids 业务IDs
     */
    void approvedEnergyDaily(List<Integer> ids);

}
