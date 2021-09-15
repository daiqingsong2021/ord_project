package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.basisrc.DateUtils;
import com.wisdom.acm.sys.service.UUVService;
import com.wisdom.base.common.msg.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Author：wqd
 * Date：2019-09-16 15:06
 * Description：<描述>
 */
@RestController
@RequestMapping(value = "org")
public class UUVOrgUserController {
    private static Logger logger = LoggerFactory.getLogger(UUVOrgUserController.class);

    @Autowired
    private UUVService uuvService;

    @PostMapping(value = "/syncUUVOrg")
    public ApiResult syncUUVOrg() {
        logger.info("全量同步组织");
        Calendar mingTianCalendar = new GregorianCalendar();
        mingTianCalendar.setTime(new Date());
        mingTianCalendar.add(Calendar.DATE, 1);
        uuvService.syncUUVOrg("1970-01-01", DateUtils.format(mingTianCalendar.getTime()));
        return ApiResult.success();
    }

    @PostMapping(value = "/syncUUVUser")
    public ApiResult syncUUVUser() {
        logger.info("全量同步人员");
        Calendar mingTianCalendar = new GregorianCalendar();
        mingTianCalendar.setTime(new Date());
        mingTianCalendar.add(Calendar.DATE, 1);
        uuvService.syncUUVUser("1970-01-01", DateUtils.format(mingTianCalendar.getTime()));
        return ApiResult.success();
    }

    @PostMapping(value = "/syncIncrUUVOrg")
    public ApiResult syncIncrUUVOrg() {
        logger.info("增量同步组织");
        Calendar zuoTianCalendar = new GregorianCalendar();
        zuoTianCalendar.setTime(new Date());
        zuoTianCalendar.add(Calendar.DATE, -1);
        Calendar mingTianCalendar = new GregorianCalendar();
        mingTianCalendar.setTime(new Date());
        mingTianCalendar.add(Calendar.DATE, 1);
        //增量同步取最后更新时间在 昨天--今天 有更新的组织数据（外部人员）
        uuvService.syncUUVOrg(DateUtils.format(zuoTianCalendar.getTime()), DateUtils.format(mingTianCalendar.getTime()));
        return ApiResult.success();
    }

    @PostMapping(value = "/syncIncrUUVUser")
    public ApiResult syncIncrUUVUser() {
        logger.info("增量同步人员");
        Calendar zuoTianCalendar = new GregorianCalendar();
        zuoTianCalendar.setTime(new Date());
        zuoTianCalendar.add(Calendar.DATE, -1);
        Calendar mingTianCalendar = new GregorianCalendar();
        mingTianCalendar.setTime(new Date());
        mingTianCalendar.add(Calendar.DATE, 1);
        //增量同步取最后更新时间在 昨天--今天 有更新的人员数据（外部人员）
        uuvService.syncUUVUser(DateUtils.format(zuoTianCalendar.getTime()), DateUtils.format(mingTianCalendar.getTime()));
        return ApiResult.success();
    }

}
