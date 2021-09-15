package com.wisdom.acm.szxm.controller.quartz;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.quartz.TimeTaskAddForm;
import com.wisdom.acm.szxm.form.quartz.TimeTaskUpdateForm;
import com.wisdom.acm.szxm.service.quartz.TimeTaskService;
import com.wisdom.acm.szxm.vo.quartz.TimeTaskVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TimeTaskController
 */
@Controller
@RestController
@RequestMapping("timeTask")
public class TimeTaskController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TimeTaskService timeTaskService;

    @GetMapping(value = "/getTimeTaskList/{pageSize}/{currentPageNum}")
    public ApiResult getTimeTaskList(@RequestParam Map<String, Object> mapWhere,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<TimeTaskVo> timeTaskVoList=timeTaskService.selectJobList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(timeTaskVoList);
    }

    @PostMapping(value = "/addTimeTask")
    public ApiResult addTimeTask(@RequestBody @Valid TimeTaskAddForm timeTaskAddForm)
    {
        TimeTaskVo timeTaskVo= timeTaskService.addTimeTask(timeTaskAddForm);
        return ApiResult.success(timeTaskVo);
    }

    @PostMapping(value = "/updateTimeTask")
    public ApiResult updateTimeTask(@RequestBody @Valid TimeTaskUpdateForm timeTaskUpdateForm)
    {
        TimeTaskVo timeTaskVo= timeTaskService.updateTimeTask(timeTaskUpdateForm);
        return ApiResult.success(timeTaskVo);
    }

    @DeleteMapping(value = "/deleteTimeTask")
    public ApiResult deleteTimeTask(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        timeTaskService.deleteTimeTask(ids);
        return ApiResult.success();
    }

    @PostMapping(value = "/triggerTimeTask/{id}")
    public ApiResult triggerTimeTask(@PathVariable("id")Integer id)
    {
        timeTaskService.triggerTimeTask(id);
        return ApiResult.success();
    }

    @PostMapping(value = "/pauseTimeTask/{id}")
    public ApiResult pauseTimeTask(@PathVariable("id")Integer id)
    {
        timeTaskService.pauseTimeTask(id);
        return ApiResult.success();
    }

    @PostMapping(value = "/resumeTimeTask/{id}")
    public ApiResult resumeTimeTask(@PathVariable("id")Integer id)
    {
        timeTaskService.resumeTimeTask(id);
        return ApiResult.success();
    }



}
