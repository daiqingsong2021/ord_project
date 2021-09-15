package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.po.BaseBoPo;
import com.wisdom.acm.base.service.BaseBoService;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.base.common.enums.ResultEnum;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/digitDirBo")
public class BaseDictBoController {

    @Autowired
    private BaseBoService digitDirBoService;


    /**
     * 获取数字字典列表
     *
     * @return
     */
    @GetMapping(value = "/{boType}/list")
    public ApiResult queryDigitdirBoList(@PathVariable("boType")Integer boType) {
        List<BaseBoVo> retList = digitDirBoService.queryBoList(boType);
        return ApiResult.success(retList);
    }
}
