package com.wisdom.acm.processing.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.processing.form.FineDailyReportAddForm;
import com.wisdom.acm.processing.service.report.FineDailyReportService;
import com.wisdom.acm.processing.vo.report.FineDailyReportVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2020/12/24/024 9:32
 * Description:<描述>
 */
@Controller
@RestController
@RequestMapping("fine/report")
public class FineDailyReportController extends BaseController {
    @Autowired
    private FineDailyReportService fineDailyReportService;
    @Autowired
    private HttpServletRequest request;
    /**
     * 新增日报
     * @param fineDailyReportAddForm
     * @return
     */
    @PostMapping(value = "/addFineDailyReport")
    public ApiResult addFineDailyReport(@RequestBody @Valid FineDailyReportAddForm fineDailyReportAddForm)
    {
        FineDailyReportVo fineDailyReportVo=fineDailyReportService.addFineDailyReport(fineDailyReportAddForm);
        return ApiResult.success(fineDailyReportVo);
    }

    /**
     * 根据条件查询日报数据
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryDailyReportList/{pageSize}/{currentPageNum}")
    public ApiResult getFineDailyReportList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<FineDailyReportVo> getFineDailyReportList=fineDailyReportService.getFineDailyReportList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(getFineDailyReportList);
    }

    /**
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteFineReport")
    public ApiResult deleteReport(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "请检查主键id是否为空");
        }
        fineDailyReportService.deleteFineReport(ids);
        return ApiResult.success();
    }

    /**
     * 流程数据
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getFlowFineDailyReportList")
    public ApiResult getFlowFineDailyReportList(@RequestParam Map<String, Object> mapWhere)
    {
        List<FineDailyReportVo> getFlowFineDailyReportList= fineDailyReportService.getFlowFineDailyReportList(mapWhere);
        return ApiResult.success(getFlowFineDailyReportList);
    }

    /**
     * 获取消息下看到的列表相关流程
     * */
    @GetMapping(value = "/getFlowFineDailyReports")
    public ApiResult getFlowFineDailyReports(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取ID
        String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
        List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
        mapWhere.put("ids",idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList()));
        return ApiResult.success(fineDailyReportService.getFlowFineDailyReports(mapWhere));
    }
}
