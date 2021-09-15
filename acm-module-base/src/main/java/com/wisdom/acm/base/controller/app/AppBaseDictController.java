package com.wisdom.acm.base.controller.app;

import com.wisdom.acm.base.service.AppBaseDictService;
import com.wisdom.acm.base.vo.app.AppBaseDictVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppBaseDictController {

    @Autowired
    private AppBaseDictService appBaseService;

    @GetMapping("/dictionary/{dictType}/list")
    public ApiResult queryAppBaseDictByTypeCode(@PathVariable("dictType") String dictType){
        List<AppBaseDictVo> list = appBaseService.queryAppBaseDictByTypeCode(dictType);
        return ApiResult.success(list);
    }

}
