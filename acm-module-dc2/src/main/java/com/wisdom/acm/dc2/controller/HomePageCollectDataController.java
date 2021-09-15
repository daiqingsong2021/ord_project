package com.wisdom.acm.dc2.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyAddForm;
import com.wisdom.acm.dc2.form.TrainDailyUpdateForm;
import com.wisdom.acm.dc2.service.TrainDailyService;
import com.wisdom.acm.dc2.service.homePage.HomePageCollectDataService;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.acm.dc2.vo.homePage.HomePageCollectDataVo;
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
 *首页汇总数据多个模块
 */
@Controller
@RestController
@RequestMapping("sy/homePageCollectDataVo")
public class HomePageCollectDataController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HomePageCollectDataService homePageCollectDataService;


    /**
     * 查询首页汇总数据
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getTrainDailyList(@RequestParam Map<String, Object> mapWhere)
    {

        HomePageCollectDataVo homePageCollectDataVo=homePageCollectDataService.selectCollectDataList(mapWhere);
        return ApiResult.success(homePageCollectDataVo);
    }



}
