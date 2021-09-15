package com.wisdom.base.common.service.impl;

import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.feign.*;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.LogService;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.vo.CalendarVo;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.log.LogContentsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class LogServiceImpl implements LogService {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    CommDictService dictService;

    @Autowired
    CommUserService userService;

    @Autowired
    CommOrgService orgService;

    @Autowired
    CommCalendarService calendarService;

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
    @Override
    public void addAcmLogger(AcmLogger log, boolean isSucess, String error) {

        if("add".equals(log.getOptType())){
            // 解析日志
            this.resolveAddLogger(log);
        }else{
            // 解析日志
            this.resolveLogger(log);
        }

        String logstr = "日志内容："+log.getContent() + ";"+ (isSucess ? "成功" : "失败") + ";"+error;
        System.out.println(logstr);
        // 记录操作日志
        loggerService.addAcmLoggerInfo(log,isSucess,error);
    }
}
