package com.wisdom.acm.dc5.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.common.DateUtil;
import com.wisdom.acm.dc5.common.SzxmEnumsUtil;
import com.wisdom.acm.dc5.form.FaultDailyAddForm;
import com.wisdom.acm.dc5.form.FaultDailyUpdateForm;
import com.wisdom.acm.dc5.po.FaultDailyPo;
import com.wisdom.acm.dc5.service.FaultDailyService;
import com.wisdom.acm.dc5.vo.FaultDailyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ExportTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *故障日况
 */
@Controller
@RestController
@RequestMapping("gzrk/faultDaily")
public class FaultDailyController {

    @Autowired
    private FaultDailyService faultDailyService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 故障日况:线路下的id和时间相同不能新增
     * @param faultDailyAddForm
     * @return
     */
    @PostMapping(value = "add")
    @AddLog(title = "新增故障日况", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT ,initContent = true)
    public ApiResult addFaultDaily(@RequestBody @Valid FaultDailyAddForm faultDailyAddForm) {
        FaultDailyVo FaultDailyVo = faultDailyService.addFaultDaily(faultDailyAddForm);
        return ApiResult.success(FaultDailyVo);
    }

    /**
     * 删除故障日况:删除的时候需要将关联数据删除(只有新增和驳回的状态可以删除，前端做了控制)
     * @param ids
     * @return
     */
    @DeleteMapping(value = "delete")
    @AddLog(title = "删除故障日况", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT)
    public ApiResult deleteFaultDaily(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids))
            return ApiResult.result(1001, "id不能为空");
        List<FaultDailyPo> faultDailyPos = faultDailyService.selectByIds(ids);
        List<String> statusList = faultDailyPos.stream().map(FaultDailyPo::getStatus).distinct().collect(Collectors.toList());
        if (statusList.contains(SzxmEnumsUtil.StatusEnum.APPROVAL.getCode()) || statusList.contains(SzxmEnumsUtil.StatusEnum.APPROVED.getCode()))
            return ApiResult.result(1001, "所选的有审批中和已完成的状态，不允许删除");
        faultDailyService.deleteFaultDaily(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "update")
    public ApiResult updateFaultDaily(@RequestBody @Valid FaultDailyUpdateForm FaultDailyUpdateForm) {
        FaultDailyVo FaultDailyVo = faultDailyService.updateFaultDaily(FaultDailyUpdateForm);
        return ApiResult.success(FaultDailyVo);
    }

    /**
     * 查询列车日况
     * @param mapWhere  line   endTime  startTime
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "list/{pageSize}/{currentPageNum}")
    public ApiResult getFaultDailyListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum) {
        if(!ObjectUtils.isEmpty(mapWhere.get("startTime"))) {
            mapWhere.put("startTime", mapWhere.get("startTime") + " 00:00:00");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime",mapWhere.get("endTime")+" 00:00:00");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("lines"))) {
            String[] idsArray = String.valueOf(mapWhere.get("lines")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
        }
        PageInfo<FaultDailyVo> queryFaultDailyList=faultDailyService.selectFaultDailyPageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryFaultDailyList);
    }

    /**
     * 查询列车日况
     * @param mapWhere  line   endTime  startTime
     * @return
     */
    @GetMapping(value = "list")
    public ApiResult getFaultDailyList(@RequestParam Map<String, Object> mapWhere) {
        if(!ObjectUtils.isEmpty(mapWhere.get("startTime"))) {
            mapWhere.put("startTime", mapWhere.get("startTime") + " 00:00:00");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime",mapWhere.get("endTime")+" 00:00:00");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("lines"))) {
            String[] idsArray = String.valueOf(mapWhere.get("lines")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
        }
        if(StringHelper.isNullAndEmpty(String.valueOf(mapWhere.get("status")))){
            mapWhere.put("status",SzxmEnumsUtil.StatusEnum.INIT.getCode());
        }
        List<FaultDailyVo> queryFaultDailyList=faultDailyService.selectFaultDailyList(mapWhere);
        return ApiResult.success(queryFaultDailyList);
    }
    /**
     * 导出
     * */
    @PostMapping(value = "export")
    @AddLog(title = "下载故障日况汇总数据", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT)
    public void downTemplate(HttpServletResponse response, @RequestParam Map<String, Object> mapWhere) {
        if(!ObjectUtils.isEmpty(mapWhere.get("startTime"))) {
            mapWhere.put("startTime", mapWhere.get("startTime") + " 00:00:00");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime",mapWhere.get("endTime")+" 00:00:00");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("lines"))) {
            String[] idsArray = String.valueOf(mapWhere.get("lines")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
        }
        List<FaultDailyVo> queryFaultDailyList=faultDailyService.selectFaultDailyList(mapWhere);
        for(FaultDailyVo vo : queryFaultDailyList){
            vo.setRecordDayStr(DateUtil.getDateFormat(vo.getRecordDay(),"yyyyMMdd"));
        }
        ExportTemplateUtil.export(request, response, "faultDailyReports.xlsx","故障日况汇总.xlsx", queryFaultDailyList);
    }

    /**
     * 获取小铃铛下看到的列表
     * */
    @GetMapping(value = "getFlowFaultDailyList")
    public ApiResult getFlowFaultDailyList(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取ID
        String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
        List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
        mapWhere.put("ids",idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList()));
        return ApiResult.success(faultDailyService.selectFaultDailyList(mapWhere));
    }


    @GetMapping(value = "/approvedFaultDaily")
    public ApiResult approvedFaultDaily(@RequestParam String ids)
    {
        if (StringHelper.isNotNullAndEmpty(ids)) {//如果主键Ids 不为空
            String[] idsArray = ids.split(",");
            List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> idsIntegerList=idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            faultDailyService.approvedFaultDaily(idsIntegerList);
            return ApiResult.success();
        }
        else
        {
            return ApiResult.result(1001, "id不能为空");
        }
    }


}
