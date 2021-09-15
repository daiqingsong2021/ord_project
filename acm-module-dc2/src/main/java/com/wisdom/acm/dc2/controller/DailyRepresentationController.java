package com.wisdom.acm.dc2.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.form.DailyRepresentationAddForm;
import com.wisdom.acm.dc2.form.DailyRepresentationUpdateForm;
import com.wisdom.acm.dc2.service.DailyRepresentationService;
import com.wisdom.acm.dc2.vo.DailyRepresentationVo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 *补充情况说明
 */
@Controller
@RestController
@RequestMapping("bcsm/DailyRepresentation")
public class DailyRepresentationController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DailyRepresentationService dailyRepresentationService;


    /**
     * 新增补充情况说明
     * @param dailyRepresentationAddForm
     * @return
     */
    @PostMapping(value = "/add")
    public ApiResult addDailyRepresentation(@RequestBody @Valid DailyRepresentationAddForm dailyRepresentationAddForm)
    {
        if(ObjectUtils.isEmpty(dailyRepresentationAddForm.getLine()) || ObjectUtils.isEmpty(dailyRepresentationAddForm.getRecordTime()))
        {
            return ApiResult.result(1001, "时间线路不能为空");
        }
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("line",dailyRepresentationAddForm.getLine());
        mapWhere.put("recordTime", DateUtil.getDateFormat(dailyRepresentationAddForm.getRecordTime()));
        List<DailyRepresentationVo> queryDailyRepresentationList=dailyRepresentationService.selectByParams(mapWhere);
        if (!ObjectUtils.isEmpty(queryDailyRepresentationList))
        {
            return ApiResult.result(1001, "一个线路一天只能提交一次补充说明");
        }

        DailyRepresentationVo DailyRepresentationVo = dailyRepresentationService.addDailyRepresentation(dailyRepresentationAddForm);
        return ApiResult.success(DailyRepresentationVo);
    }


   
    @DeleteMapping(value = "/delete")
    public ApiResult deleteDailyRepresentation(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        //为了添加多条删除日志
        for(Integer id:ids)
        {
            dailyRepresentationService.deleteDailyRepresentation(id);
        }
        return ApiResult.success();
    }

    @PutMapping(value = "/update")
    public ApiResult updateDailyRepresentation(@RequestBody @Valid DailyRepresentationUpdateForm DailyRepresentationUpdateForm)
    {
        DailyRepresentationVo DailyRepresentationVo = dailyRepresentationService.updateDailyRepresentation(DailyRepresentationUpdateForm);
        return ApiResult.success(DailyRepresentationVo);
    }

    @GetMapping(value = "/{id}")
    public ApiResult getDailyRepresentationById(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(dailyRepresentationService.selectById(id));
    }

    /**
     * 查询补充情况说明
     * @param mapWhere  line   endTime  startTime
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getDailyRepresentationListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("startTime")))
        {
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("line"))) {
            String[] idsArray = String.valueOf(mapWhere.get("line")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        PageInfo<DailyRepresentationVo> queryDailyRepresentationList=dailyRepresentationService.selectDailyRepresentationList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryDailyRepresentationList);
    }

    /**
     * 查询补充情况说明
     * @param mapWhere  line   endTime  startTime
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getDailyRepresentationList(@RequestParam Map<String, Object> mapWhere)
    {
        if(ObjectUtils.isEmpty(mapWhere.get("startTime")))
        {
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        }
        if(ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("line"))) {
            String[] idsArray = String.valueOf(mapWhere.get("line")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        List<DailyRepresentationVo> queryDailyRepresentationList=dailyRepresentationService.selectByParams(mapWhere);
        return ApiResult.success(queryDailyRepresentationList);
    }




}
