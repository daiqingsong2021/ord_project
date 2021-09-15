package com.wisdom.acm.dc4.controller;


import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc4.form.ConstructionDailyAddForm;
import com.wisdom.acm.dc4.form.ConstructionDailyUpdateForm;
import com.wisdom.acm.dc4.service.ConstructionDailyService;
import com.wisdom.acm.dc4.vo.ConstructionDailyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ConstructionDetailsAddController class
 * @author  chenhai
 * @data 2020/08/11
 * 施工日况
 */
@RestController
@RequestMapping("sgrk/constructionDaily")
public class ConstructionDailyController {
    @Resource
    private ConstructionDailyService constructionDailyService;

    /**
     * 新增施工日况
     * @param constructionDailyAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "新增施工日况", module = LoggerModuleEnum.CONSTRUCTION_DAILYREPORT,initContent = true)
    public ApiResult addConstructionDaily(@RequestBody @Valid ConstructionDailyAddForm constructionDailyAddForm)
    {
        ConstructionDailyVo constructionDailyVo = constructionDailyService.addConstructionDaily(constructionDailyAddForm);
        return ApiResult.success(constructionDailyVo);
    }

    /**
     * 同步数据
     */
    @PostMapping(value = "/syn/list")
    public ApiResult addConstructionDailyList()
    {
        constructionDailyService.addRcConstructionDaily();
        return ApiResult.success();
    }

    /**
     *  //是否存在同名的   true 存在同名 ，false 不存在同名
     * @param mapWhere    recordTime   line
     * @return
     */
    @GetMapping(value = "/checkAddConstructionDaily")
   // @AddLog(title = "校验新增施工日况时间 线路 是否存在", module = LoggerModuleEnum.CONSTRUCTION_DAILYREPORT)
    public ApiResult checkAddConstructionDaily(@RequestParam Map<String, Object> mapWhere)
    {

        if(ObjectUtils.isEmpty(mapWhere.get("recordTime")))
        {
            return ApiResult.result(1001, "recordTime");
        }
        if(ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            return ApiResult.result(1001, "line");
        }

        Map<String, Object> checkMapWhere =new HashMap<String, Object>();
        checkMapWhere.put("recordTime", String.valueOf(mapWhere.get("recordTime")));
        checkMapWhere.put("line",mapWhere.get("line"));
       // checkMapWhere.put("checkReviewStatus","REJECT");
        //String scheduleName=mapWhere.get("scheduleName").toString();
        List<ConstructionDailyVo> constructionDailyVoList = constructionDailyService.selectByParams(checkMapWhere);
        //是否存在同名的   true 同名 ，false 不存在同名
        if(!ObjectUtils.isEmpty(constructionDailyVoList))
        {
            return ApiResult.success("true");
        }
        else
        {
            return ApiResult.success("false");
        }
    }


    @DeleteMapping(value = "/delete")
    public ApiResult deleteConstructionDaily(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        //为了添加多条删除日志
        for(Integer id:ids)
        {
            constructionDailyService.deleteConstructionDaily(id);
        }
        return ApiResult.success();
    }

    @PostMapping(value = "/update")
    public ApiResult updateConstructionDaily(@RequestBody @Valid ConstructionDailyUpdateForm constructionDailyUpdateForm)
    {
        ConstructionDailyVo constructionDailyVo = constructionDailyService.updateConstructionDaily(constructionDailyUpdateForm);
        return ApiResult.success(constructionDailyVo);
    }

    @GetMapping(value = "/{id}")
    public ApiResult getConstructionDailyById(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(constructionDailyService.selectById(id));
    }

    /**
     * 查询施工日况
     * @param mapWhere    endTime   startTime  line
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getConstructionDailyListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            String[] sourcesArray = mapWhere.get("line").toString().split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(sourcesArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        PageInfo<ConstructionDailyVo> queryConstructionDailyList=constructionDailyService.selectConstructionDailyList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryConstructionDailyList);
    }

    /**
     * 查询列车时刻表
     * @param mapWhere    endTime   startTime  line
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getConstructionDailyList(@RequestParam Map<String, Object> mapWhere)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            String[] sourcesArray = mapWhere.get("line").toString().split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(sourcesArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        List<ConstructionDailyVo> queryConstructionDailyList=constructionDailyService.selectByParams(mapWhere);
        return ApiResult.success(queryConstructionDailyList);
    }


    @GetMapping(value = "/approvedConstructionDaily")
    public ApiResult approvedConstructionDaily(@RequestParam String ids)
    {
        if (StringHelper.isNotNullAndEmpty(ids)) {//如果主键Ids 不为空
            String[] idsArray = ids.split(",");
            List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> idsIntegerList=idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            constructionDailyService.approvedConstructionDaily(idsIntegerList);
            return ApiResult.success();
        }
        else
        {
            return ApiResult.result(1001, "id不能为空");
        }
    }


}
