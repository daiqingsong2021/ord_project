package com.wisdom.acm.dc3.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc3.form.EnergyDetailUpdateForm;
import com.wisdom.acm.dc3.service.EnergyDailyService;
import com.wisdom.acm.dc3.service.EnergyDetailService;
import com.wisdom.acm.dc3.vo.EnergyDailyVo;
import com.wisdom.acm.dc3.vo.EnergyDetailVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *列车日况
 */
@Controller
@RestController
@RequestMapping("nhrk/energyDetail")
public class EnergyDetailController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private EnergyDetailService energyDetailService;


    /**
     * 根据dailyId  查找detail 详情数据
     * @param
     * @return
     */
    @GetMapping(value = "/dailyDetail/{dailyId}/{pageSize}/{currentPageNum}")
    public ApiResult getFlowEnergyDailyPageList(@RequestParam Map<String, Object> mapWhere, @PathVariable("dailyId")Integer dailyId, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if (ObjectUtils.isEmpty(dailyId))
        {
            return ApiResult.result(1001, "dailyId不能为空");
        }
        mapWhere.put("dailyId",dailyId);
        PageInfo<EnergyDetailVo> queryEnergyDailyList= energyDetailService.selectEnergyDatailPageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryEnergyDailyList);
    }
    /**
     * 根据dailyId  查找detail 详情数据
     * @param dailyId
     * @return
     */
    @GetMapping(value = "/dailyDetail/{dailyId}")
    public ApiResult getEnergyDailyByDailyId(@PathVariable("dailyId")Integer dailyId)
    {
        if (ObjectUtils.isEmpty(dailyId))
        {
            return ApiResult.result(1001, "dailyId不能为空");
        }
        Map<String,Object> mapWhere =new HashMap<>();
        mapWhere.put("dailyId",dailyId);
        return ApiResult.success( energyDetailService.selectByParams(mapWhere));
    }

    @GetMapping(value = "/{id}")
    public ApiResult getEnergyDailyById(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success( energyDetailService.selectById(id));
    }

    @PutMapping(value = "/update")
    public ApiResult updateTrainDaily(@RequestBody @Valid List<EnergyDetailUpdateForm> energyDetailUpdateForm)
    {

        if(ObjectUtils.isEmpty(energyDetailUpdateForm))
        {
            return ApiResult.result(1001, "更新对象不能为空");
        }
        List<EnergyDetailVo> energyDailyVo = energyDetailService.updateEnergyDetail(energyDetailUpdateForm);
        return ApiResult.success(energyDailyVo);
    }




}
