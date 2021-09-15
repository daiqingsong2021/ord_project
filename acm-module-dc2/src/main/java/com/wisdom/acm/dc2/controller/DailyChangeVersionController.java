package com.wisdom.acm.dc2.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.DailyChangeVersionAddForm;
import com.wisdom.acm.dc2.service.DailyChangeVersionService;
import com.wisdom.acm.dc2.vo.DailyChangeVersionVo;
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

/**
 *版本控制
 */
@Controller
@RestController
@RequestMapping("bbgl/DailyChangeVersion")
public class DailyChangeVersionController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DailyChangeVersionService dailyChangeVersionService;


    /**
     * 新增版本控制
     * @param dailyChangeVersionAddForm
     * @return
     */
    @PostMapping(value = "/add")
    public ApiResult addDailyChangeVersion(@RequestBody @Valid DailyChangeVersionAddForm dailyChangeVersionAddForm)
    {
        DailyChangeVersionVo DailyChangeVersionVo = dailyChangeVersionService.addDailyChangeVersion(dailyChangeVersionAddForm);
        return ApiResult.success(DailyChangeVersionVo);
    }


   
    @DeleteMapping(value = "/delete")
    public ApiResult deleteDailyChangeVersion(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        //为了添加多条删除日志
        for(Integer id:ids)
        {
            dailyChangeVersionService.deleteDailyChangeVersion(id);
        }
        return ApiResult.success();
    }


    @GetMapping(value = "/{id}")
    public ApiResult getDailyChangeVersionById(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(dailyChangeVersionService.selectById(id));
    }

    /**
     * 查询版本控制
     * @param mapWhere  moudleRecordId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getDailyChangeVersionListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("moudleRecordId")))
        {
            mapWhere.put("moudleRecordId", mapWhere.get("moudleRecordId"));
        }

        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        PageInfo<DailyChangeVersionVo> queryDailyChangeVersionList=dailyChangeVersionService.selectDailyChangeVersionList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryDailyChangeVersionList);
    }

    /**
     * 查询版本控制
     * @param mapWhere  moudleRecordId
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getDailyChangeVersionList(@RequestParam Map<String, Object> mapWhere)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("moudleRecordId")))
        {
            mapWhere.put("moudleRecordId", mapWhere.get("moudleRecordId"));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        List<DailyChangeVersionVo> queryDailyChangeVersionList=dailyChangeVersionService.selectByParams(mapWhere);
        return ApiResult.success(queryDailyChangeVersionList);
    }




}
