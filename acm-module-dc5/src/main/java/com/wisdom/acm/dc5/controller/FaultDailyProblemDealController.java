package com.wisdom.acm.dc5.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.form.FaultDailyProblemDealAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemDealUpdateForm;
import com.wisdom.acm.dc5.service.FaultDailyProblemDealService;
import com.wisdom.acm.dc5.vo.FaultDailyProblemDealVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *故障日况问题解决
 */
@Controller
@RestController
@RequestMapping("gzrk/faultDailyProblemDeal")
public class FaultDailyProblemDealController {
    @Autowired
    private FaultDailyProblemDealService faultDailyProblemDealService;

    /**
     * 故障日况处理详情
     * @param faultDailyProblemDealAddForm
     * @return
     */
    @PostMapping(value = "add")
    //@AddLog(title = "新增故障日况处理详情", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT ,initContent = true)
    public ApiResult addFaultDailyProblemDeal(@RequestBody @Valid FaultDailyProblemDealAddForm faultDailyProblemDealAddForm) {
        FaultDailyProblemDealVo FaultDailyProblemDealVo = faultDailyProblemDealService.addFaultDailyProblemDeal(faultDailyProblemDealAddForm);
        return ApiResult.success(FaultDailyProblemDealVo);
    }

    /**
     * 删除故障日况处理详情
     * @param ids
     * @return
     */
    @DeleteMapping(value = "delete")
    @AddLog(title = "删除故障日况处理详情", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT)
            public ApiResult deleteFaultDailyProblemDeal(@RequestBody List<Integer> ids) {
        faultDailyProblemDealService.deleteFaultDailyProblemDeal(ids);
        return ApiResult.success();
    }

    /**
     * 更新故障处理日况:
     * @param faultDailyProblemDealUpdateForm
     * @return
     */
    @PutMapping(value = "update")
    public ApiResult updateFaultDailyProblemDeal(@RequestBody @Valid FaultDailyProblemDealUpdateForm faultDailyProblemDealUpdateForm) {
        FaultDailyProblemDealVo FaultDailyProblemDealVo = faultDailyProblemDealService.updateFaultDailyProblemDeal(faultDailyProblemDealUpdateForm);
        return ApiResult.success(FaultDailyProblemDealVo);
    }

    /**
     * 查询列车日况
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getFaultDailyDealListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum) {

        if(ObjectUtils.isEmpty(mapWhere.get("problemId")))
            throw new BaseException("故障日况的问题id不能为空");
        PageInfo<FaultDailyProblemDealVo> queryFaultDailyDealList=faultDailyProblemDealService.selectFaultDailyProblemDealPageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryFaultDailyDealList);
    }


}
