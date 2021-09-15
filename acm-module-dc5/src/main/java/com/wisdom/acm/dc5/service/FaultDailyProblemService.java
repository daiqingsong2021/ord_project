package com.wisdom.acm.dc5.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.form.FaultDailyAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemUpdateForm;
import com.wisdom.acm.dc5.form.FaultDailyUpdateForm;
import com.wisdom.acm.dc5.po.FaultDailyPo;
import com.wisdom.acm.dc5.po.FaultDailyProblemPo;
import com.wisdom.acm.dc5.po.FaultMonthlyPo;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
import com.wisdom.acm.dc5.vo.FaultDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FaultDailyProblemService extends CommService<FaultDailyProblemPo> {


    PageInfo<FaultDailyProblemVo> selectFaultDailyProblemPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    List<FaultDailyProblemVo> selectFaultDailyProblemList(Map<String, Object> mapWhere);

    FaultDailyProblemVo addFaultDailyProblem(FaultDailyProblemAddForm faultDailyProblemAddForm);

    void deleteFaultDailyProblem(List<Integer> idList);

    FaultDailyProblemVo updateFaultDailyProblem(FaultDailyProblemUpdateForm faultDailyProblemUpdateForm);

}
