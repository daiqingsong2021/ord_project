package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@FeignClient(value = "acm-module-hbase", configuration = FeignConfiguration.class)
@EnableAsync
public interface LoggerService {
    Logger logger = LoggerFactory.getLogger(LoggerService.class);
    /**
     * @param
     * @return
     */
    @Async
    default void addAcmLoggerInfo(String module, String title, String content) {

        AcmLogger acmLogger = new AcmLogger();
        acmLogger.setTitle(title);
        acmLogger.setModule(module);
        acmLogger.setContent(content);
        this.addAcmLoggerInfo(acmLogger, true, null);
    }

    /**
     * @param
     * @return
     */
    default void addAcmLoggerInfo(String module, String title, String content, boolean isSucess, String error) {

        AcmLogger acmLogger = new AcmLogger();
        acmLogger.setTitle(title);
        acmLogger.setModule(module);
        acmLogger.setContent(content);
        this.addAcmLoggerInfo(acmLogger, isSucess, error);
    }

    /**
     * @param
     * @return
     */
    default void addAcmLoggerInfo(AcmLogger acmLogger, boolean isSucess, String error) {

        logger.info("日志Hbase addAcmLoggerInfo1 ！！！！！！！！！！");
        acmLogger.setStatus(isSucess ? "成功" : "失败");
        acmLogger.setError(error);
        // 如果title中包含uuv 则调用uuv记录日志方法
        if (acmLogger.getTitle().contains("UUV")) {
            this.addAcmLoggerInfoForUUV(acmLogger);
            return;
        }
        //判断是否为定时器生成日报数据
        if ("日报管理".equals(acmLogger.getModule())) {
            if(acmLogger.getTitle().contains("日报日志记录")){
                this.addAcmLoggerInfoForReport(acmLogger);
                return;
            }
        }
        this.addAcmLoggerInfo(acmLogger);
    }


    /**
     * @param
     * @return
     */
    default void addAcmLoggerInfo(AcmLogger acmLogger) {

        logger.info("日志Hbase addAcmLoggerInfo ！！！！！！！！！！");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdate = sdf.format(date);
        String userName = request.getHeader("userName");
        String userType = request.getHeader("userType");
        String actuName = "";
        try {
            actuName = URLDecoder.decode(request.getHeader("actuName"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String hostIp = StringUtils.isNotBlank(request.getHeader("userHost")) ? request.getHeader("userHost") : ClientUtil.getClientIp(request);
        String cteator = userName + "(" + actuName + ")";

        acmLogger.setCreatTime(createdate);
        acmLogger.setIpaddress(hostIp);
        acmLogger.setCreator(cteator);
        acmLogger.setUserType(userType);
        logger.info("日志Hbase 录入 ！！！！！！！！！！");
        this.add(acmLogger);
    }

    default void addAcmLoggerInfoForReport(AcmLogger acmLogger) {
        //给定时器用
        logger.info("日志Hbase addAcmLoggerInfo ！！！！！！！！！！");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdate = sdf.format(date);
        String userName = "weihuyuan";
        String userType = "1";
        String actuName = "维护员";
        String creator = userName + "(" + actuName + ")";
        acmLogger.setCreatTime(createdate);
        acmLogger.setCreator(creator);
        acmLogger.setUserType(userType);
        acmLogger.setIpaddress("定时器");
        logger.info("定时器生成日志Hbase 录入 ！！！！！！！！！！");
        this.add(acmLogger);
    }

    /**
     * 同步uuv数据调用该增加日志方法（定时任务无请求）
     *
     * @param
     * @return
     */
    default void addAcmLoggerInfoForUUV(AcmLogger acmLogger) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdate = sdf.format(date);

        acmLogger.setCreatTime(createdate);
        acmLogger.setCreator("admin(超级管理员)");
        acmLogger.setIpaddress(" ");
        this.add(acmLogger);
    }


    /**
     * 添加日志记录
     *
     * @param acmLogger
     * @return
     */
    @PostMapping(value = "/acmlog/add")
    ApiResult add(@RequestBody AcmLogger acmLogger);
}
