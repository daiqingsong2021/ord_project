package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCalendarPo;
import com.wisdom.acm.base.vo.calendar.BaseCalendarVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseCalendarMapper  extends CommMapper<BaseCalendarPo> {

    /**
     * 日历设置列表
     * @return
     */
    public List<BaseCalendarVo> selectCalendarList();

    /**
     * 获取默认日历
     * @return
     */
    public BaseCalendarVo selectDefaultCalendar();

    /**
     * 日历基本信息
     * @param calendarId
     * @return
     */
    public BaseCalendarVo selectCalendarById(Integer calendarId);

    /**
     * 通过id查BaseCalendarVo
     * @param calendarId
     * @return
     */
    public BaseCalendarVo selectOneBaseCalendarVo(Integer calendarId);


    List<Map<String,Object>> selectOneCalendarFromPropject( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarFromDefine( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarFromTask( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarFromPrepa( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarRsrcMaterial( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarRsrcEquip( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarRsrcUser( @Param("calendarIds") List<Integer> calendarIds);

    List<Map<String,Object>> selectOneCalendarRsrcRole( @Param("calendarIds") List<Integer> calendarIds);


}