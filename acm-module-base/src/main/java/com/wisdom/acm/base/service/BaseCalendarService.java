package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.calendar.BaseCalendarExceptionsUpdateForm;
import com.wisdom.acm.base.form.calendar.BaseCalendarUpdateForm;
import com.wisdom.acm.base.form.calendar.BaseCalendarWeekDaysUpdateForm;
import com.wisdom.acm.base.po.BaseCalendarPo;
import com.wisdom.acm.base.vo.calendar.BaseCalendarInfoVo;
import com.wisdom.acm.base.vo.calendar.BaseCalendarVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.CalendarVo;

import java.util.List;

public interface BaseCalendarService extends CommService<BaseCalendarPo> {

    List<BaseCalendarVo> queryCalendarList();

    BaseCalendarInfoVo getDefaultCalendarInfo();

    BaseCalendarVo getDefaultCalendar();

    BaseCalendarPo getDefaultCalendarPo();

    void deleteCalendarByIds(List<Integer> ids);

    BaseCalendarPo addCalendar();
    /**
     * 获取日历基本信息
     *
     * @param calendarId
     * @return
     */
    BaseCalendarInfoVo getCalendarInfoVo(Integer calendarId);

    BaseCalendarPo updateCalendar(BaseCalendarUpdateForm baseCalendarUpdateForm);

    BaseCalendarVo getBaseCalendarVo(Integer id);

    List<BaseCalendarPo> getBaseCalendarPos(List<Integer> ids);

    List<CalendarVo> getCalendarVoInfos(List<BaseCalendarPo> baseCalendarPos);

    /**
     * 查询日历个性设置
     * @param id
     * @return
     */
    public BaseCalendarPo getBaseClndrData(Integer id);

    /**
     * 复制日历
     * @param id
     * @return
     */
    BaseCalendarPo copyCalendar(Integer id);

    /**
     * 修改为默认日历
     *
     * @param id
     * @return
     */
    void updateDefaultCalendar(Integer id);

    /**
     * 保存标准周期
     *
     * @param updateForm
     * @return
     */
    BaseCalendarPo updateWeekDays(BaseCalendarWeekDaysUpdateForm updateForm);

    /**
     * 保存标准周期
     *
     * @param updateForm
     * @return
     */
    BaseCalendarPo updateExceptions(BaseCalendarExceptionsUpdateForm updateForm);

    BaseCalendarPo cancleExceptions(Integer id,String time);
}
