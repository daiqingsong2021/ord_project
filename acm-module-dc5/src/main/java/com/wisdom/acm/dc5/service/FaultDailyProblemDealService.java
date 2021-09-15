package com.wisdom.acm.dc5.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.form.FaultDailyProblemAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemDealAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemDealUpdateForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemUpdateForm;
import com.wisdom.acm.dc5.po.FaultDailyProblemDealPo;
import com.wisdom.acm.dc5.po.FaultDailyProblemPo;
import com.wisdom.acm.dc5.vo.FaultDailyProblemDealVo;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface FaultDailyProblemDealService extends CommService<FaultDailyProblemDealPo> {


    PageInfo<FaultDailyProblemDealVo> selectFaultDailyProblemDealPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    List<FaultDailyProblemDealVo> selectFaultDailyProblemDealList(Map<String, Object> mapWhere);

    FaultDailyProblemDealVo addFaultDailyProblemDeal(FaultDailyProblemDealAddForm faultDailyProblemDealAddForm);

    void deleteFaultDailyProblemDeal(List<Integer> idList);

    FaultDailyProblemDealVo updateFaultDailyProblemDeal(FaultDailyProblemDealUpdateForm faultDailyProblemDealUpdateForm);

}
