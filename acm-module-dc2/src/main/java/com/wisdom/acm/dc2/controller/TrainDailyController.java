package com.wisdom.acm.dc2.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyAddForm;
import com.wisdom.acm.dc2.form.TrainDailyUpdateForm;
import com.wisdom.acm.dc2.service.TrainDailyService;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.base.common.dc.util.StringHelper;
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
 *列车日况
 */
@Controller
@RestController
@RequestMapping("xcrk/trainDaily")
public class TrainDailyController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TrainDailyService trainDailyService;


    /**
     * 新增列车日况
     * @param TrainDailyAddForm
     * @return
     */
    @PostMapping(value = "/add")
    public ApiResult addTrainDaily(@RequestBody @Valid TrainDailyAddForm TrainDailyAddForm)
    {
        TrainDailyVo TrainDailyVo = trainDailyService.addTrainDaily(TrainDailyAddForm);
        return ApiResult.success(TrainDailyVo);
    }


    /**
     *  //是否存在同一时间的记录    checkCreatTime  true 存在 ，false 不存在
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/checkCreatTime")
    //@AddLog(title = "新增行车日况校验日期，线路 是否存在", module = LoggerModuleEnum.DRIVING_DRIVINGINFO)
    public ApiResult checkCreatTime(@RequestParam Map<String, Object> mapWhere)
    {
        //String scheduleName=mapWhere.get("scheduleName").toString();
        if(ObjectUtils.isEmpty(mapWhere.get("checkCreatTime")))
        {
            return ApiResult.success("true");
        }
        if(ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            return ApiResult.success("true");
        }
        mapWhere.put("checkCreatTime", StringHelper.formattString(mapWhere.get("checkCreatTime")));
        mapWhere.put("line",mapWhere.get("line"));
        //mapWhere.put("checkReviewStatus","REJECT");
        List<TrainDailyVo> trainScheduleVoList = trainDailyService.selectByParams(mapWhere);
        //checkCreatTime  true 存在 ，false 不存在
        if(!ObjectUtils.isEmpty(trainScheduleVoList))
        {
            return ApiResult.success("true");
        }
        else
        {
            return ApiResult.success("false");
        }
    }

    @DeleteMapping(value = "/delete")
    public ApiResult deleteTrainDaily(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        //为了添加多条删除日志
        for(Integer id:ids)
        {
            trainDailyService.deleteTrainDaily(id);
        }
        return ApiResult.success();
    }

    @PutMapping(value = "/update")
    public ApiResult updateTrainDaily(@RequestBody @Valid TrainDailyUpdateForm TrainDailyUpdateForm)
    {
        TrainDailyVo TrainDailyVo = trainDailyService.updateTrainDaily(TrainDailyUpdateForm);
        return ApiResult.success(TrainDailyVo);
    }

    @GetMapping(value = "/{id}")
    public ApiResult getTrainDailyById(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(trainDailyService.selectById(id));
    }

    /**
     * 查询列车日况
     * @param mapWhere  line   endTime  startTime
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getTrainDailyListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
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
        PageInfo<TrainDailyVo> queryTrainDailyList=trainDailyService.selectTrainDailyList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrainDailyList);
    }

    @GetMapping(value = "/get/{id}")
    public ApiResult getTrainDailyList(@PathVariable("id")Integer id)
    {
        return ApiResult.success(trainDailyService.getTrainDailyList(id));
    }

    @GetMapping(value = "/approvedTrainDaily")
    public ApiResult approvedTrainDaily(@RequestParam String ids){
        if (StringHelper.isNotNullAndEmpty(ids)) {//如果主键Ids 不为空
            String[] idsArray = ids.split(",");
            List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> idsIntegerList = idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            trainDailyService.approvedTrainDaily(idsIntegerList);
            return ApiResult.success();
        }else{
            return ApiResult.result(1001, "id不能为空");
        }
    }


    /**
     * 查询列车日况
     * @param mapWhere  line   endTime  startTime
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getTrainDailyList(@RequestParam Map<String, Object> mapWhere)
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
        List<TrainDailyVo> queryTrainDailyList=trainDailyService.selectByParams(mapWhere);
        return ApiResult.success(queryTrainDailyList);
    }
}
