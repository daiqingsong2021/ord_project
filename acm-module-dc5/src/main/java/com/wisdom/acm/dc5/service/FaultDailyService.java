package com.wisdom.acm.dc5.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.form.FaultDailyAddForm;
import com.wisdom.acm.dc5.form.FaultDailyUpdateForm;
import com.wisdom.acm.dc5.po.FaultDailyPo;
import com.wisdom.acm.dc5.po.FaultMonthlyPo;
import com.wisdom.acm.dc5.vo.FaultDailyVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FaultDailyService extends CommService<FaultDailyPo> {


    PageInfo<FaultDailyVo> selectFaultDailyPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    List<FaultDailyVo> selectFaultDailyList(Map<String, Object> mapWhere);

    void deleteFaultDaily(List<Integer> ids);

    /**
     * 审批结束流程回调
     * @param ids 业务IDs
     */
    void approveFaultDailyWorkFlow(String bizType, List<Integer> ids);

    FaultDailyVo addFaultDaily(FaultDailyAddForm faultDailyAddForm);

    FaultDailyVo updateFaultDaily(FaultDailyUpdateForm faultDailyUpdateForm);

    Map<Integer,FaultDailyVo> getFaultTotalNum(Integer faultId,List<Integer> faultIdList);

    FaultMonthlyPo updateFaultMonthly(Date recordTime, Integer line);
    void approvedFaultDaily(List<Integer> ids);

}
