package com.wisdom.base.common.aspect;

import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.feign.*;
import com.wisdom.base.common.form.BaseForm;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.vo.CalendarVo;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.log.LogContentsVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableAsync
//@Aspect
public class LogAspect {

    @Autowired
    CommDictService dictService;

    @Autowired
    CommUserService userService;

    @Autowired
    CommOrgService orgService;

    @Autowired
    CommCalendarService calendarService;

    @Value(value = "${sys.log.enable}")
    private String logEnable;

    //定义一个日志的全局的静态
    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired(required = false)
    private LoggerService loggerService;

    static ThreadLocal<AcmLogger> acmLogger = new ThreadLocal<>();

    @Pointcut("@annotation(com.wisdom.base.common.aspect.AddLog)")
    public void logPointcut() {

    }

    //设置全局模块
    LoggerModuleEnum module=null;

    @Before("logPointcut()&&@annotation(addLog)")
    public void doBefore(JoinPoint joinPoint, AddLog addLog) {

        System.out.println("doBefore start");
        logger.info("日志切面 doBefore start ！！！！！！！！！！");
        AcmLogger acmLog = new AcmLogger();
        String title = addLog.title();
        module = addLog.module();
        System.out.println("LoggerModuleEnum");
        boolean initContent = addLog.initContent();
        acmLog.setModule(module.getName());
        acmLog.setTitle(title);

        // 开头内容
        String startContent = "";
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            System.out.println("requestAttributes");
            HttpServletRequest request = requestAttributes.getRequest();
            startContent = request.getParameter("startContent");
            // 去空
            boolean b = startContent == null || "null".equals(startContent);
            if(b){
                startContent = "";
            }
            acmLog.setStartContent(startContent);
        }

        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if(BaseForm.class.isAssignableFrom(args[i].getClass())){
                System.out.println("isAssignableFrom");
                BaseForm baseForm = (BaseForm)args[i];
                if(initContent){
                    acmLog.setLogContentsVos(LogUtil.getAddLogContent(baseForm));
                    acmLog.setOptType("add");
                    AcmLogger tempAcmLog =LogUtil.addLogger(baseForm);
                    acmLog.setRecordTime(tempAcmLog.getRecordTime());
                    acmLog.setLine(tempAcmLog.getLine());
                    acmLog.setRecordStatus(tempAcmLog.getRecordStatus());
                }else{
                    String logContent = baseForm.getLogContent();
                    acmLog.setContent(logContent);
                }
                System.out.println("end");
                break;
            }
        }
        System.out.println("doBefore end");
        if(acmLog != null){
            System.out.println("acmLog is not null...");
        }
        acmLogger.set(acmLog);
        logger.info("日志切面 doBefore end ！！！！！！！！！！"+module.getName() +title);
    }

    @AfterReturning(value = "logPointcut()",returning = "object")
    public void after(JoinPoint joinPoint,Object object) {
        AcmLogger log = acmLogger.get();
        logger.info("日志切面 after start ！！！！！！！！！！"+log);
        if(log != null && (log.getLoaded() == null || !log.getLoaded())){
            AcmLogger logger = getRunAcmLogger(joinPoint);
            // logger = getRunAcmLogger(joinPoint);
            this.cover(logger,log);
        }
        // 增加日志
        this.addAcmLogger(log,true ,null);
        System.out.println("日志增加结束!");
        logger.info("日志切面 after end ！！！！！！！！！！"+log);
    }

    private AcmLogger getRunAcmLogger(JoinPoint joinPoint){
        System.out.println("getRunAcmLogger start...");
        AcmLogger logger = null;
        if(BaseController.class.isAssignableFrom(joinPoint.getSignature().getDeclaringType())){
            BaseController baseController = (BaseController) joinPoint.getTarget();
            logger = baseController.getAcmLogger();
        }else if(BaseService.class.isAssignableFrom(joinPoint.getSignature().getDeclaringType())){
            BaseService baseService = (BaseService)joinPoint.getTarget();
            logger =baseService.getAcmLogger();
        }
        System.out.println("getRunAcmLogger end...");
        return logger;
    }

    @AfterThrowing(value = "logPointcut()",throwing="ex")
    public void afterThrowing(JoinPoint joinPoint,Exception ex) {
        System.out.println("afterThrowing start...");
        logger.info("日志切面 afterThrowing start ！！！！！！！！！！");
        AcmLogger log = acmLogger.get();
        if(log != null && (log.getLoaded() == null || !log.getLoaded())) {
            AcmLogger logger = getRunAcmLogger(joinPoint);
            this.cover(logger, log);
        }
        System.out.println("afterThrowing ...");
        // 增加日志
        this.addAcmLogger(log,ex == null,ex == null ? null : ex.getMessage());
        System.out.println("日志增加结束!");
        logger.info("日志切面 afterThrowing 日志增加结束 end ！！！！！！！！！！");
    }
    /**
     * 覆盖
     * @param newLog
     * @param baseLog
     */
    private void cover(AcmLogger newLog,AcmLogger baseLog){
        if(newLog != null){
            if(!ObjectUtils.isEmpty(newLog.getContent())){
                baseLog.setContent(newLog.getContent());
            }
            if(!ObjectUtils.isEmpty(newLog.getTitle())){
                baseLog.setTitle(newLog.getTitle());
            }
            if(!ObjectUtils.isEmpty(newLog.getModule())){
                baseLog.setModule(newLog.getModule());
            }
            if(!ObjectUtils.isEmpty(newLog.getLogContentsVos())){
                baseLog.setLogContentsVos(newLog.getLogContentsVos());
            }
            if(!ObjectUtils.isEmpty(newLog.getRecordTime())){
                baseLog.setRecordTime(newLog.getRecordTime());
            }
            if(!ObjectUtils.isEmpty(newLog.getLine())){
                baseLog.setLine(newLog.getLine());
            }
            if(!ObjectUtils.isEmpty(newLog.getRecordId())){
                baseLog.setRecordId(newLog.getRecordId());
            }
            if(!ObjectUtils.isEmpty(newLog.getRecordStatus())){
                baseLog.setRecordStatus(newLog.getRecordStatus());
            }
        }
    }

    public void resolveLogger(AcmLogger acmLogger) {

        if(!ObjectUtils.isEmpty(acmLogger.getLogContentsVos())){

            StringBuffer content = new StringBuffer();
            List<String> dictCodes = new ArrayList<>();
            List<Integer> userIds = new ArrayList<>();
            List<Integer> orgIds = new ArrayList<>();
            List<Integer> calendarIds = new ArrayList<>();

            for(LogContentsVo contentsVo : acmLogger.getLogContentsVos()){
                if(ParamEnum.DICT.getCode().equals(contentsVo.getType())){
                    dictCodes.add(FormatUtil.toString(contentsVo.getTypeValue()));
                }else if(ParamEnum.USER.getCode().equals(contentsVo.getType())){
                    if(!ObjectUtils.isEmpty(contentsVo.getNewValue())){
                        userIds.add(FormatUtil.parseInt(contentsVo.getNewValue()));
                    }
                    if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                        userIds.add(FormatUtil.parseInt(contentsVo.getOldValue()));
                    }
                }else if(ParamEnum.ORG.getCode().equals(contentsVo.getType())){
                    if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                        orgIds.add(FormatUtil.parseInt(contentsVo.getNewValue()));
                    }
                    if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                        orgIds.add(FormatUtil.parseInt(contentsVo.getOldValue()));
                    }
                }else if(ParamEnum.CALENDAR.getCode().equals(contentsVo.getType())){
                    if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                        calendarIds.add(FormatUtil.parseInt(contentsVo.getNewValue()));
                    }
                    if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                        calendarIds.add(FormatUtil.parseInt(contentsVo.getOldValue()));
                    }
                }
            }

            Map<Integer, UserVo> userVoMap = null;
            Map<Integer, OrgVo> orgVoMap = null;
            Map<Integer, CalendarVo> calendarVoMap = null;
            DictionarysMap dictionarysMap = null;
            if(!ObjectUtils.isEmpty(userIds)){
                userVoMap = userService.getUserVoMapByUserIds(userIds);
            }

            if(!ObjectUtils.isEmpty(orgIds)){
                orgVoMap = orgService.getOrgVoMapByOrgs(orgIds);
            }

            if(!ObjectUtils.isEmpty(calendarIds)){
                calendarVoMap = calendarService.getCalendarVoMap(calendarIds);
            }

            if(!ObjectUtils.isEmpty(dictCodes)){
                dictionarysMap = dictService.getDictMapByTypeCodes(dictCodes);
            }

            for(LogContentsVo contentsVo : acmLogger.getLogContentsVos()){

                String title  = contentsVo.getTitle();
                String newValue = null,oldValue = null;
                if(ParamEnum.DICT.getCode().equals(contentsVo.getType())){
                    if(dictionarysMap != null ){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())){
                            newValue = dictionarysMap.getDictionaryName(FormatUtil.toString(contentsVo.getTypeValue()),contentsVo.getNewValue());
                        }
                        if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                            oldValue = dictionarysMap.getDictionaryName(FormatUtil.toString(contentsVo.getTypeValue()),contentsVo.getOldValue());
                        }
                    }
                }else if(ParamEnum.USER.getCode().equals(contentsVo.getType())){
                    if(userVoMap != null){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())){
                            UserVo userVo = userVoMap.get(FormatUtil.parseInt(contentsVo.getNewValue()));
                            if(userVo != null){
                                newValue = userVo.getName();
                            }
                        }
                        if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                            UserVo userVo = userVoMap.get(FormatUtil.parseInt(contentsVo.getOldValue()));
                            if(userVo != null){
                                oldValue = userVo.getName();
                            }
                        }
                    }
                }else if(ParamEnum.ORG.getCode().equals(contentsVo.getType())){

                    if(orgVoMap != null){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                            OrgVo orgVo = orgVoMap.get(FormatUtil.parseInt(contentsVo.getNewValue()));
                            if(orgVo != null){
                                newValue = orgVo.getName();
                            }
                        }
                        if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                            OrgVo orgVo = orgVoMap.get(FormatUtil.parseInt(contentsVo.getOldValue()));
                            if(orgVo != null){
                                oldValue = orgVo.getName();
                            }
                        }
                    }
                }else if(ParamEnum.CALENDAR.getCode().equals(contentsVo.getType())){
                    if(calendarVoMap != null){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                            CalendarVo calendarVo = calendarVoMap.get(FormatUtil.parseInt(contentsVo.getNewValue()));
                            if(calendarVo != null){
                                newValue = calendarVo.getCalName();
                            }
                        }
                        if(!ObjectUtils.isEmpty(contentsVo.getOldValue())) {
                            CalendarVo calendarVo = calendarVoMap.get(FormatUtil.parseInt(contentsVo.getOldValue()));
                            if(calendarVo != null){
                                oldValue = calendarVo.getCalName();
                            }
                        }
                    }
                }else if(ParamEnum.NONE.getCode().equals(contentsVo.getType())){
                    newValue = contentsVo.getNewValue();
                    oldValue = contentsVo.getOldValue();
                }

                if (content.length() > 0) {
                    content.append(",");
                }

                if(!ObjectUtils.isEmpty(newValue) && ObjectUtils.isEmpty(oldValue)){
                    content.append(title).append("由空修改为【").append(oldValue).append("】");
                }else if(ObjectUtils.isEmpty(newValue) && !ObjectUtils.isEmpty(oldValue)){
                    content.append(title).append("由【").append(newValue).append("】修改为空");
                }else if(!ObjectUtils.isEmpty(newValue) && !ObjectUtils.isEmpty(oldValue)){
                    content.append(title).append("由【").append(oldValue).append("】修改为【"+ newValue+"】");
                }
            }

            String logTitle = acmLogger.getTitle();
            acmLogger.setContent( !ObjectUtils.isEmpty(logTitle) ? logTitle + "," + content.toString() : content.toString());
        }
    }

    public void resolveAddLogger(AcmLogger acmLogger) {

        if(!ObjectUtils.isEmpty(acmLogger.getLogContentsVos())){

            StringBuffer content = new StringBuffer();
            List<String> dictCodes = new ArrayList<>();
            List<Integer> userIds = new ArrayList<>();
            List<Integer> orgIds = new ArrayList<>();
            List<Integer> calendarIds = new ArrayList<>();

            for(LogContentsVo contentsVo : acmLogger.getLogContentsVos()){
                if(ParamEnum.DICT.getCode().equals(contentsVo.getType())){
                    dictCodes.add(FormatUtil.toString(contentsVo.getTypeValue()));
                }else if(ParamEnum.USER.getCode().equals(contentsVo.getType())){
                    if(!ObjectUtils.isEmpty(contentsVo.getNewValue())){
                        userIds.add(FormatUtil.parseInt(contentsVo.getNewValue()));
                    }
                }else if(ParamEnum.ORG.getCode().equals(contentsVo.getType())){
                    if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                        orgIds.add(FormatUtil.parseInt(contentsVo.getNewValue()));
                    }
                }else if(ParamEnum.CALENDAR.getCode().equals(contentsVo.getType())){
                    if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                        calendarIds.add(FormatUtil.parseInt(contentsVo.getNewValue()));
                    }
                }
            }

            Map<Integer, UserVo> userVoMap = null;
            Map<Integer, OrgVo> orgVoMap = null;
            Map<Integer, CalendarVo> calendarVoMap = null;
            DictionarysMap dictionarysMap = null;
            if(!ObjectUtils.isEmpty(userIds)){
                userVoMap = userService.getUserVoMapByUserIds(userIds);
            }

            if(!ObjectUtils.isEmpty(orgIds)){
                orgVoMap = orgService.getOrgVoMapByOrgs(orgIds);
            }

            if(!ObjectUtils.isEmpty(calendarIds)){
                calendarVoMap = calendarService.getCalendarVoMap(calendarIds);
            }

            if(!ObjectUtils.isEmpty(dictCodes)){
                dictionarysMap = dictService.getDictMapByTypeCodes(dictCodes);
            }

            for(LogContentsVo contentsVo : acmLogger.getLogContentsVos()){

                String title  = contentsVo.getTitle();
                String newValue = null;
                if(ParamEnum.DICT.getCode().equals(contentsVo.getType())){
                    if(dictionarysMap != null ){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())){
                            newValue = dictionarysMap.getDictionaryName(FormatUtil.toString(contentsVo.getTypeValue()),contentsVo.getNewValue());
                        }
                    }
                }else if(ParamEnum.USER.getCode().equals(contentsVo.getType())){
                    if(userVoMap != null){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())){
                            UserVo userVo = userVoMap.get(FormatUtil.parseInt(contentsVo.getNewValue()));
                            if(userVo != null){
                                newValue = userVo.getName();
                            }
                        }
                    }
                }else if(ParamEnum.ORG.getCode().equals(contentsVo.getType())){

                    if(orgVoMap != null){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                            OrgVo orgVo = orgVoMap.get(FormatUtil.parseInt(contentsVo.getNewValue()));
                            if(orgVo != null){
                                newValue = orgVo.getName();
                            }
                        }
                    }
                }else if(ParamEnum.CALENDAR.getCode().equals(contentsVo.getType())){
                    if(calendarVoMap != null){
                        if(!ObjectUtils.isEmpty(contentsVo.getNewValue())) {
                            CalendarVo calendarVo = calendarVoMap.get(FormatUtil.parseInt(contentsVo.getNewValue()));
                            if(calendarVo != null){
                                newValue = calendarVo.getCalName();
                            }
                        }
                    }
                }else if(ParamEnum.NONE.getCode().equals(contentsVo.getType())){
                    newValue = contentsVo.getNewValue();
                }

                if (content.length() > 0) {
                    content.append(",");
                }

                if(!ObjectUtils.isEmpty(newValue)){
                    content.append(title).append("【").append(newValue).append("】");
                }
            }

            String logTitle = acmLogger.getTitle();
            acmLogger.setContent( !ObjectUtils.isEmpty(logTitle) ? logTitle + "," + content.toString() : content.toString());
        }
    }

    @Async
    public void addAcmLogger(AcmLogger log,boolean isSucess,String error) {

        if(log == null || logEnable == null || "0".equals(logEnable)){
            return;
        }
        logger.info("日志切面 addAcmLogger start ！！！！！！！！！！");

        //判断是否为日报数据
        if("ODR_REPORT".equals(module.getCode())&&log.getContent().contains("定时器生成日报")){
            log.setOptType("add");
            this.resolveAddLogger(log);
            loggerService.addAcmLoggerInfo(log,isSucess,error);
            return;
        }

        if("add".equals(log.getOptType())){
            // 解析日志
            this.resolveAddLogger(log);
            logger.info("日志切面 addAcmLogger  解析add 日志 ！！！！！！！！！！");
        }else{
            // 解析日志
            this.resolveLogger(log);
            logger.info("日志切面 addAcmLogger  解析非add 日志 ！！！！！！！！！！");
        }

        String logstr = "日志内容："+log.getContent() + ";"+ (isSucess ? "成功" : "失败") + ";"+error;
        System.out.println(logstr);
        logger.info("日志切面 addAcmLogger  记录操作日志 ！！！！！！！！！！"+logstr);
        // 记录操作日志
        logger.info("日志Hbase 调用addAcmLoggerInfo 录入 ！！！！！！！！！！");
        loggerService.addAcmLoggerInfo(log,isSucess,error);
    }

}
