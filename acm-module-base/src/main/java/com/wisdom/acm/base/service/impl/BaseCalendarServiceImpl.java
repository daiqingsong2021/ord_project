package com.wisdom.acm.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.wisdom.acm.base.form.calendar.BaseCalendarExceptionsUpdateForm;
import com.wisdom.acm.base.form.calendar.BaseCalendarUpdateForm;

import com.wisdom.acm.base.form.calendar.BaseCalendarWeekDaysUpdateForm;
import com.wisdom.acm.base.mapper.BaseCalendarMapper;

import com.wisdom.acm.base.po.BaseCalendarPo;

import com.wisdom.acm.base.service.BaseCalendarService;
import com.wisdom.acm.base.vo.calendar.BaseCalendarInfoVo;
import com.wisdom.acm.base.vo.calendar.BaseCalendarVo;
import com.wisdom.acm.base.vo.calendar.BaseExceptionDayVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.JsonUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.vo.CalendarVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class BaseCalendarServiceImpl extends BaseService<BaseCalendarMapper, BaseCalendarPo> implements BaseCalendarService {


    @Override
    public List<BaseCalendarVo> queryCalendarList() {
        List<BaseCalendarVo> allCalendarList = this.mapper.selectCalendarList();
        return allCalendarList;
    }

    @Override
    public BaseCalendarPo getDefaultCalendarPo() {
        Example example = new Example(BaseCalendarPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDefault", 1);
        return this.selectOneByExample(example);
    }

    @Override
    public BaseCalendarInfoVo getDefaultCalendarInfo() {
        BaseCalendarPo calendarPo = this.getDefaultCalendarPo();
        BaseCalendarInfoVo calendarInfoVo = null;
        if (calendarPo != null) {
            //
            calendarInfoVo = this.dozerMapper.map(calendarPo, BaseCalendarInfoVo.class);
            calendarInfoVo.setExceptionsString(calendarPo.getExceptions());
            calendarInfoVo.setWeekDaysString(calendarPo.getWeekDays());
        }
        return calendarInfoVo;
    }

    @Override
    public BaseCalendarVo getDefaultCalendar(){
        BaseCalendarPo calendarPo = this.getDefaultCalendarPo();
        BaseCalendarVo calendarVo = null;
        if(calendarPo != null){
            calendarVo = this.dozerMapper.map(calendarPo,BaseCalendarVo.class);
        }
        return calendarVo;
    }

    @Override
    public void deleteCalendarByIds(List<Integer> ids) {

       if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarFromPropject(ids))){
           throw new BaseException("??????????????????????????????????????????");
       }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarFromDefine(ids))){
            throw new BaseException("????????????????????????????????????????????????");
       }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarFromTask(ids))){
            throw new BaseException("??????????????????????????????????????????");
        }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarFromPrepa(ids))){
            throw new BaseException("??????????????????????????????????????????");
        }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarRsrcMaterial(ids))){
            throw new BaseException("????????????????????????????????????????????????");
        }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarRsrcEquip(ids))){
            throw new BaseException("????????????????????????????????????????????????");
        }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarRsrcUser(ids))){
            throw new BaseException("????????????????????????????????????????????????");
        }
        if(!ObjectUtils.isEmpty(this.mapper.selectOneCalendarRsrcRole(ids))){
            throw new BaseException("????????????????????????????????????????????????");
        }
        super.deleteByIds(ids);
    }

    @Override
    public BaseCalendarPo addCalendar() {

        BaseCalendarPo baseCalendarPo = new BaseCalendarPo();

        List<Map<String, Object>> weekDays = new ArrayList<>();
        weekDays.add(this.getDayWorkingTime(0,1)); //?????????
        weekDays.add(this.getDayWorkingTime(1,2)); //?????????
        weekDays.add(this.getDayWorkingTime(1,3)); //?????????
        weekDays.add(this.getDayWorkingTime(1,4)); //?????????
        weekDays.add(this.getDayWorkingTime(1,5)); //?????????
        weekDays.add(this.getDayWorkingTime(1,6)); //?????????
        weekDays.add(this.getDayWorkingTime(0,7)); //?????????

        baseCalendarPo.setCalName("????????????");
        baseCalendarPo.setExceptions(null);
        baseCalendarPo.setWeekDays(JsonUtil.toJson(weekDays));
        baseCalendarPo.setIsDefault(0);
        baseCalendarPo.setDayHrCnt(8.00);
        baseCalendarPo.setWeekHrCnt(40.00);
        baseCalendarPo.setMonthHrCnt(172.00);
        baseCalendarPo.setYearHrCnt(2000.00);
        //??????????????????
        baseCalendarPo.setBuiltIn(0);

        super.insert(baseCalendarPo);
        return baseCalendarPo;
    }

    private Map<String, Object> getDayWorkingTime(int dayWorking, int dayType){
        Map<String, Object> weekDay = new LinkedHashMap<>();
        weekDay.put("dayWorking", dayWorking);
        weekDay.put("dayType", dayType);
        if(dayWorking != 0) {
            List<Map<String, Object>> workingTimes = new ArrayList<>();
            Map<String, Object> one = new LinkedHashMap<>();
            one.put("fromTime", "08:30:00");
            one.put("toTime", "12:00:00");
            workingTimes.add(one);
            one = new LinkedHashMap<>();
            one.put("fromTime", "13:00:00");
            one.put("toTime", "17:30:00");
            workingTimes.add(one);
            weekDay.put("workingTimes", workingTimes);
        }
        return weekDay;
    }

    @Override
    public BaseCalendarInfoVo getCalendarInfoVo(Integer calendarId) {

        // ????????????????????????
        BaseCalendarPo calendarPo = this.selectById(calendarId);
        if(ObjectUtils.isEmpty(calendarPo)){
            throw new BaseException("????????????????????????!");
        }
        //
        BaseCalendarInfoVo calendarInfoVo = this.dozerMapper.map(calendarPo, BaseCalendarInfoVo.class);
        calendarInfoVo.setExceptionsString(calendarPo.getExceptions());
        calendarInfoVo.setWeekDaysString(calendarPo.getWeekDays());

        return calendarInfoVo;
    }

    @Override
    @AddLog(title = "????????????????????????",module = LoggerModuleEnum.BM_CALENDAR)
    public BaseCalendarPo updateCalendar(BaseCalendarUpdateForm baseCalendarUpdateForm) {

        // ?????????????????????????????????
        BaseCalendarPo baseCalendarPo = this.selectById(baseCalendarUpdateForm.getId());

        if (baseCalendarPo == null) {
            throw new BaseException("????????????????????????!");
        }

        // ??????????????????
        this.addChangeLogger(baseCalendarUpdateForm,baseCalendarPo);
        // ????????????marge???taskPo
        this.dozerMapper.map(baseCalendarUpdateForm, baseCalendarPo);
        // ????????????
        this.updateById(baseCalendarPo);

        return baseCalendarPo;
    }

    @Override
    public BaseCalendarVo getBaseCalendarVo(Integer id) {
        BaseCalendarVo baseCalendarVo = this.mapper.selectOneBaseCalendarVo(id);
        return baseCalendarVo;
    }

    /**
     * ??????ids??????????????????
     * @param ids
     * @return
     */
    @Override
    public List<BaseCalendarPo> getBaseCalendarPos(List<Integer> ids){
        List<BaseCalendarPo> baseCalendarPos = new ArrayList<>();
        if(!ObjectUtils.isEmpty(ids)){
            baseCalendarPos = this.selectByIds(ids);
        }
        return baseCalendarPos;
    }
    @Override
    public List<CalendarVo> getCalendarVoInfos(List<BaseCalendarPo> baseCalendarPos){
        List<CalendarVo> CalendarVos = new ArrayList<>();
        if(!ObjectUtils.isEmpty(baseCalendarPos)){
            CalendarVo CalendarVo = null;
            for(BaseCalendarPo baseCalendarPo : baseCalendarPos){
                CalendarVo = new CalendarVo();
                CalendarVo = this.dozerMapper.map(baseCalendarPo, CalendarVo.class);
                CalendarVos.add(CalendarVo);
            }
        }
        return CalendarVos;
    }
    @Override
    public BaseCalendarPo getBaseClndrData(Integer id) {
        BaseCalendarPo baseCalendarPo = this.mapper.selectByPrimaryKey(id);
        return baseCalendarPo;
    }

    @Override
    public BaseCalendarPo copyCalendar(Integer id) {
        BaseCalendarPo baseCalendarPo = this.mapper.selectByPrimaryKey(id);
        baseCalendarPo.setCalName(baseCalendarPo.getCalName() + "-copy");
        baseCalendarPo.setIsDefault(0);
        baseCalendarPo.setId(null);
        super.insert(baseCalendarPo);
        return baseCalendarPo;
    }

    @Override
    public void updateDefaultCalendar(Integer id) {
        BaseCalendarPo calendarPo = this.selectById(id);
        if (calendarPo == null) {
            throw new BaseException("???????????????,??????????????????!");
        }
        this.clearDefaultCalendar();
        calendarPo.setIsDefault(1);
        this.updateById(calendarPo);
    }


    /**
     * ??????????????????
     *
     * @return
     */
    private int clearDefaultCalendar() {

        BaseCalendarPo calendarPo = new BaseCalendarPo();
        calendarPo.setIsDefault(0);

        Example example = new Example(BaseCalendarPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDefault", 1);

        return this.updateByExampleSelective(calendarPo, example);
    }


    @Override
    public BaseCalendarPo updateWeekDays(BaseCalendarWeekDaysUpdateForm updateForm) {

        BaseCalendarPo calendarPo = this.selectById(updateForm.getId());
        if (calendarPo == null) {
            throw new BaseException("???????????????!");
        }
        this.dozerMapper.map(updateForm, calendarPo);
        String json = JsonUtil.toJson(updateForm.getWeekDays());
        calendarPo.setWeekDays(json);
        this.updateById(calendarPo);
        return calendarPo;
    }

    @Override
    public BaseCalendarPo updateExceptions(BaseCalendarExceptionsUpdateForm updateForm) {

        BaseCalendarPo calendarPo = this.selectById(updateForm.getId());
        String oldjson = calendarPo.getExceptions();
        if (calendarPo == null) {
            throw new BaseException("??????????????????");
        }
        this.dozerMapper.map(updateForm, calendarPo);
        String json = JsonUtil.toJson(updateForm.getExceptions());
        List<BaseExceptionDayVo> oldlist = new ArrayList<BaseExceptionDayVo>();
        List<BaseExceptionDayVo> newlist = new ArrayList<>();
        if (!StringUtils.isEmpty(oldjson))
            oldlist = JSON.parseArray(oldjson, BaseExceptionDayVo.class);
        if (!StringUtils.isEmpty(json))
            newlist = JSON.parseArray(json, BaseExceptionDayVo.class);
        if (!ObjectUtils.isEmpty(newlist)) {
            oldlist.addAll(newlist);
        }

        String upjson = JsonUtil.toJson(oldlist);
        calendarPo.setExceptions(upjson);
        this.updateById(calendarPo);

        return calendarPo;
    }

    @Override
    public BaseCalendarPo cancleExceptions(Integer id ,String time){
        BaseCalendarPo calendarPo = this.selectById(id);
        String oldjson = calendarPo.getExceptions();
        if (calendarPo == null) {
            throw new BaseException("??????????????????");
        }

        List<BaseExceptionDayVo> oldlist = new ArrayList<BaseExceptionDayVo>();
        if (!StringUtils.isEmpty(oldjson))
            oldlist = JSON.parseArray(oldjson, BaseExceptionDayVo.class);

        if(!ObjectUtils.isEmpty(oldlist)){
            for(int i = oldlist.size() - 1;i >= 0;i--){
                BaseExceptionDayVo baseExceptionDayVo = oldlist.get(i);
                String oldTime = baseExceptionDayVo.getName();
                if(oldTime.equals(time)){
                    oldlist.remove(i);
                }
            }
        }
        String upjson = JsonUtil.toJson(oldlist);
        calendarPo.setExceptions(upjson);
        this.updateById(calendarPo);

        return calendarPo;
    }
}
