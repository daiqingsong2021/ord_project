package com.wisdom.acm.dc5.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.form.FaultDailyProblemAddForm;
import com.wisdom.acm.dc5.form.FaultDailyProblemUpdateForm;
import com.wisdom.acm.dc5.service.FaultDailyProblemService;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
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
 *故障日况问题
 */
@Controller
@RestController
@RequestMapping("gzrk/faultDailyProblem")
public class FaultDailyProblemController {
    @Autowired
    private FaultDailyProblemService faultDailyProblemService;

    /**
     * 故障日况详情
     * @param faultDailyProblemAddForm
     * @return
     */
    @PostMapping(value = "add")
    //@AddLog(title = "新增故障日况问题", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT ,initContent = true)
    public ApiResult addFaultDailyProblem(@RequestBody @Valid FaultDailyProblemAddForm faultDailyProblemAddForm) {
        FaultDailyProblemVo FaultDailyProblemVo = faultDailyProblemService.addFaultDailyProblem(faultDailyProblemAddForm);
        return ApiResult.success(FaultDailyProblemVo);
    }

    /**
     * 删除故障日况:只有新建状态可以删除需要判断底层数据
     * @param ids
     * @return
     */
    @DeleteMapping(value = "delete")
    @AddLog(title = "删除故障日况问题", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT )
    public ApiResult deleteFaultDailyProblem(@RequestBody List<Integer> ids) {
        //删除odr_fault_daily_problem数据,删除odr_fault_daily_problem_deal数据
        faultDailyProblemService.deleteFaultDailyProblem(ids);
        return ApiResult.success();
    }

    /**
     * 更新故障日况:
     * @param faultDailyProblemUpdateForm
     * @return
     */
    @PutMapping(value = "update")
    public ApiResult updateFaultDailyProblem(@RequestBody @Valid FaultDailyProblemUpdateForm faultDailyProblemUpdateForm) {
        FaultDailyProblemVo FaultDailyProblemVo = faultDailyProblemService.updateFaultDailyProblem(faultDailyProblemUpdateForm);
        return ApiResult.success(FaultDailyProblemVo);
    }


    /**
     * 查询列车日况
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "list/{pageSize}/{currentPageNum}")
    public ApiResult getFaultDailyListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum) {

        if(ObjectUtils.isEmpty(mapWhere.get("faultId")))
            throw new BaseException("故障日况的id不能为空");
        PageInfo<FaultDailyProblemVo> queryFaultDailyList=faultDailyProblemService.selectFaultDailyProblemPageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryFaultDailyList);
    }


}
