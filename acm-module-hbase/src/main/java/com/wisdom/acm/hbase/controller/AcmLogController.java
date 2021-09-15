package com.wisdom.acm.hbase.controller;

import com.wisdom.acm.hbase.form.*;
import com.wisdom.acm.hbase.service.*;
import com.wisdom.acm.hbase.vo.*;
import com.wisdom.base.common.aspect.LogAspect;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "acmlog")
public class AcmLogController {

    @Autowired
    private AcmLogService logService;

    @Autowired
    private LeafService leafService;

    private static Logger logger = LoggerFactory.getLogger(AcmLogController.class);
    /**
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResult add(@RequestBody AcmLogger acmLogger){
        logger.info("Hbase 记录日志 开始！！！！！！" + "line="+acmLogger.getLine() +"time="+acmLogger.getRecordTime() + "id=" +acmLogger.getRecordId() +"status="+acmLogger.getRecordStatus());
         acmLogger.setId(String.valueOf(leafService.getId()));
        Integer instrNum= logService.addLogger(acmLogger);
        logger.info("Hbase 记录日志 结束！！！！！！" + instrNum);
         return ApiResult.success();
    }


    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Object query(){
        return logService.query();
    }


    /**
     * 操作审计列表
     *
     * @param searchMap
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/audit/list/{pageSize}/{currentPageNum}")
    public ApiResult queryOperationAudit(SysSearchAuditForm searchMap, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) throws ParseException {
        List<SysOperationAuditVo> retList = logService.queryOperationAudit(searchMap, pageSize, currentPageNum);
        return ApiResult.success(retList);
    }

}
