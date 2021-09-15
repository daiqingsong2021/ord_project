package com.wisdom.base.common.controller;

import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    // 日志
    private ThreadLocal<AcmLogger> threadLocalLogger = new ThreadLocal<>();

    public void setAcmLogger (AcmLogger logger ){
        this.threadLocalLogger.set(logger);
    }

    public AcmLogger getAcmLogger(){
        return this.threadLocalLogger.get();
    }

}
