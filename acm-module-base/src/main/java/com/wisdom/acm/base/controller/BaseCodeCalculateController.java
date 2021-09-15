package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.service.BaseCodeCalculateService;
import com.wisdom.acm.base.vo.BaseCodeResultVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BaseCodeCalculateController {

    @Autowired
    private BaseCodeCalculateService codeCalculateService;

    /**
     * 计算编码
     * @param map
     * @return
     */
    @GetMapping("/code/calculate")
    public ApiResult<BaseCodeResultVo> calculateCode(@RequestParam Map<String,Object> map){
        BaseCodeResultVo vo = codeCalculateService.calculateCode(map);
        return ApiResult.success(vo);
    }

    /**
     * 校验编码是否重复
     * @param map
     * @return
     */
    @GetMapping("/code/repeat/check")
    public ApiResult<Boolean> checkCodeIsRepeat(Map<String,Object> map){
        boolean flag = codeCalculateService.checkCodeIsRepeat(map);
        return ApiResult.success(flag);
    }

}
