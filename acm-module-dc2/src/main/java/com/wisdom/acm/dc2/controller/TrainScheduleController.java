package com.wisdom.acm.dc2.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainScheduleAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleUpdateForm;
import com.wisdom.acm.dc2.service.TrainDailyService;
import com.wisdom.acm.dc2.service.TrainScheduleService;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.acm.dc2.vo.TrainScheduleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
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

/**
 *列车时刻表
 */
@Controller
@RestController
@RequestMapping("xcrk/trainSchedule")
public class TrainScheduleController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TrainScheduleService trainScheduleService;

    @Autowired
    private TrainDailyService trainDailyService;

    /**
     * 新增列车时刻表
     * @param trainScheduleAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "新增列车时刻表", module = LoggerModuleEnum.DRIVING_TIMETABLE,initContent = true)
    public ApiResult addTrainSchedule(@RequestBody @Valid TrainScheduleAddForm trainScheduleAddForm)
    {
        TrainScheduleVo trainScheduleVo = trainScheduleService.addTrainSchedule(trainScheduleAddForm);
        return ApiResult.success(trainScheduleVo);
    }

    /**
     *  //是否存在同名的   true 同名 ，false 不存在同名
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/checkAddScheduleCode")
    public ApiResult checkAddScheduleCode(@RequestParam Map<String, Object> mapWhere)
    {
        //String scheduleName=mapWhere.get("scheduleName").toString();
        if(ObjectUtils.isEmpty(mapWhere.get("scheduleCode")))
        {
            return ApiResult.result(1001, "scheduleCode");
        }
        List<TrainScheduleVo> trainScheduleVoList = trainScheduleService.selectByParams(mapWhere);
        //是否存在同名的   true 同名 ，false 不存在同名
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
    public ApiResult deleteTrainSchedule(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        //为了添加多条删除日志
        for(Integer id:ids)
        {
            trainScheduleService.deleteTrainSchedule(id);
        }
        return ApiResult.success();
    }

    @PutMapping(value = "/update")
    public ApiResult updateTrainSchedule(@RequestBody @Valid TrainScheduleUpdateForm trainScheduleUpdateForm)
    {
        TrainScheduleVo trainScheduleVo = trainScheduleService.updateTrainSchedule(trainScheduleUpdateForm);
        return ApiResult.success(trainScheduleVo);
    }

    @GetMapping(value = "/{id}")
    public ApiResult getTrainScheduleById(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(trainScheduleService.selectById(id));
    }

    /**
     * 查询列车时刻表
     * @param mapWhere  scheduleName    checkScheduleName  line
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getTrainScheduleListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            String[] sourcesArray = mapWhere.get("line").toString().split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(sourcesArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        PageInfo<TrainScheduleVo> queryTrainScheduleList=trainScheduleService.selectTrainScheduleList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrainScheduleList);
    }

    /**
     * 查询列车时刻表
     * @param mapWhere  scheduleName    checkScheduleName  line
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getTrainScheduleList(@RequestParam Map<String, Object> mapWhere)
    {
        List<TrainScheduleVo> queryTrainScheduleList=trainScheduleService.selectByParams(mapWhere);
        return ApiResult.success(queryTrainScheduleList);
    }


    /**
     * 获取小铃铛下看到的列表
     * */
    @GetMapping(value = "getFlowContractChangeList")
    public ApiResult getFlowContractChangeList(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取项目ID
        List<TrainScheduleVo> trainScheduleVoList;
        if (ObjectUtils.isEmpty(ids)) {

            trainScheduleVoList = trainScheduleService.selectFlowTrainScheduleList(mapWhere);
        } else {
            trainScheduleVoList = trainScheduleService.selectFlowTrainScheduleList(mapWhere);
        }
        return ApiResult.success(trainScheduleVoList);
    }



    /**
     *  //是否存在被引用的   true 存在 ，false 不存在
     * @param mapWhere  line  scheduleCode
     * @return
     */
    @GetMapping(value = "/checkDeleteScheduleCode")
    public ApiResult checkDeleteScheduleCode(@RequestParam Map<String, Object> mapWhere)
    {
        String line=mapWhere.get("line").toString();
        if(ObjectUtils.isEmpty(mapWhere.get("scheduleCode")))
        {
            return ApiResult.result(1001, "scheduleCode");
        }
        List<TrainDailyVo> trainDailyVoList = trainDailyService.selectByParams(mapWhere);
        //是否存在被引用的  true 存在 ，false 不存在
        if(!ObjectUtils.isEmpty(trainDailyVoList))
        {
            return ApiResult.success("true");
        }
        else
        {
            return ApiResult.success("false");
        }
    }

    /**
     * 获取站点对象
     * @param mapWhere  scheduleName    checkScheduleName  line
     * @return
     */
    @GetMapping(value = "/getStationList")
    public ApiResult getStationList(@RequestParam Map<String, Object> mapWhere)
    {
        List<TrainScheduleVo> queryTrainScheduleList=trainScheduleService.selectByParams(mapWhere);
        return ApiResult.success(queryTrainScheduleList);
    }


}
