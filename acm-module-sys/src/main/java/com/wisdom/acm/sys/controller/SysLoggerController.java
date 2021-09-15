package com.wisdom.acm.sys.controller;

import com.wisdom.base.common.feign.LoggerService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("logger")
@RestController
public class SysLoggerController {


    @Autowired
    private LoggerService loggerService;

    /**
     * 记录日志
     * @param acmLogger
     * @return
     */
    @PostMapping(value = "/add")
    public ApiResult addOperationLogger(@RequestBody AcmLogger acmLogger){
        loggerService.addAcmLoggerInfo(acmLogger.getModule(),acmLogger.getTitle(),acmLogger.getContent());
        return ApiResult.success();
    }
}
