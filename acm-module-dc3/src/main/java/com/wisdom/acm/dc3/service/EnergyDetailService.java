package com.wisdom.acm.dc3.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc3.form.EnergyDetailAddForm;
import com.wisdom.acm.dc3.form.EnergyDetailUpdateForm;
import com.wisdom.acm.dc3.po.EnergyDailyPo;
import com.wisdom.acm.dc3.po.EnergyDetailPo;
import com.wisdom.acm.dc3.vo.EnergyDailyVo;
import com.wisdom.acm.dc3.vo.EnergyDetailVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface EnergyDetailService extends CommService<EnergyDetailPo>
{

    PageInfo<EnergyDetailVo> selectEnergyDatailPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);


    List<EnergyDetailVo> updateEnergyDetail(List<EnergyDetailUpdateForm> energyDailyUpdateForm);

    EnergyDetailVo selectById(Integer id);

    List<EnergyDetailVo> selectByParams(Map<String, Object> mapWhere);

}
