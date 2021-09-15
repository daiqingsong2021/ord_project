package com.wisdom.acm.base.vo.calendar;

import com.wisdom.base.common.vo.CalendarVo;
import lombok.Data;
@Data
public class BaseCalendarVo extends CalendarVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 日历名称
     */
    private String calName;

    /**
     * 全局默认
     */
    private Integer isDefault;

    /**
     * 是否内置
     */
    private Integer builtIn;

}