package com.wisdom.base.common.util.calc.calendar;

import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.JsonUtil;
import com.wisdom.base.common.vo.CalendarVo;

import java.util.*;

public class PcCalendar extends PmCalendar {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 默认构造函数
	 */
	public PcCalendar(){

	}

	/**
	 *
	 * @param baseCalendar 日历配置对象
	 */
	public PcCalendar(final CalendarVo baseCalendar) {
		List<Map<String,Object>> exceptions = Tools.isEmpty(baseCalendar.getExceptions()) ? null : JsonUtil.readValue(baseCalendar.getExceptions(), List.class);
		List<Map<String,Object>> weekDays = JsonUtil.readValue(baseCalendar.getWeekDays(), List.class);
		this.setId(FormatUtil.toString(baseCalendar.getId()));
		this.setClndrName(baseCalendar.getCalName());
		this.setDayHrCnt(Tools.toBigDecimal(baseCalendar.getDayHrCnt()));
		this.setWeekHrCnt(Tools.toBigDecimal(baseCalendar.getWeekHrCnt()));
		this.setMonthHrCnt(Tools.toBigDecimal(baseCalendar.getMonthHrCnt()));
		this.setYearHrCnt(Tools.toBigDecimal(baseCalendar.getYearHrCnt()));
		this.setExceptMap(this.getExceptDayMap(exceptions)); // 返回特例工作日工作时间的map
		this.setWorkDayOfWeekMap(this.getWeekWorkTimeMap(weekDays)); // 解析标准工作周的工作时间
	}

	/**
	 * 日期数据
	 */
	private String weekDays;

	/**
	 * 例外日期数据
	 */
	private String exceptions;

	/**
	 *  日期数据
	 * @return weekDays
	 */
	public String getWeekDays() {
		return weekDays;
	}

	/**
	 * 日期数据
	 * @param weekDays
	 */
	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
		if(!Tools.isEmpty(weekDays)){
			List<Map<String,Object>> _weekDays = JsonUtil.readValue(weekDays, List.class);
			this.setWorkDayOfWeekMap(this.getWeekWorkTimeMap(_weekDays)); // 解析标准工作周的工作时间
		}
	}

	/**
	 * 例外日期数据
	 * @return
	 */
	public String getExceptions() {
		return exceptions;
	}

	/**
	 * 例外日期数据
	 * @param exceptions exceptions
	 */
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
		if(!Tools.isEmpty(exceptions)) {
			List<Map<String,Object>> _exceptions = JsonUtil.readValue(exceptions, List.class);
			this.setExceptMap(this.getExceptDayMap(_exceptions)); // 返回特例工作日工作时间的map
		}
	}

	/**
	 * 返回特例工作日工作时间段
	 *
	 * @param exceptions 例外时间配置
	 * @return 例外上班时间配置
	 * {
	 * 20190401=null,##不上班
	 * 20190402=[{"fromTime": "08:00:00","toTime": "12:00:00"},{"fromTime": "13:00:00","toTime": "17:00:00"}] ##上班时间段
	 * }
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, PmDay> getExceptDayMap(final List<Map<String,Object>> exceptions) {
		Map<Integer, PmDay> map = new HashMap<>();
		if(!Tools.isEmpty(exceptions)) {
			for (Map<String, Object> s : exceptions) {
				String dayWorking = Tools.toString(s.get("dayWorking")); // 是否工作
				List<Map<String, Object>> workingTimes = (List<Map<String, Object>>) s.get("workingTimes");
				Date fromDate = Tools.toDate(s.get("fromDate"));
				Date toDate = Tools.toDate(s.get("toDate"));
				if(!Tools.isEmpty(fromDate) && !Tools.isEmpty(toDate)) {
					int fromInt = CalendarUtil.getDayIntByDate(fromDate);
					int toInt = CalendarUtil.getDayIntByDate(toDate);
					for (int i = fromInt; i <= toInt; i++) {
						PmDay pmDay = this.getDayWorkTimes(dayWorking, workingTimes);
						pmDay.setDate(i);
						map.put(i, pmDay);
					}
				}
			}
		}
		return map;
	}

	/**
	 * 解析标准工作周的工作时间
	 *
	 * @param weekDays 标准工作周配置
	 * @return 标准工作周配置
	 * {
	 * 1=null,
	 * 2=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 3=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 4=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 5=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 6=[{"fromTime": "08:00:00","toTime": "12:00:00"}, {"fromTime": "13:00:00","toTime": "17:00:00"}],
	 * 7=null
	 * }
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, PmDay> getWeekWorkTimeMap(final List<Map<String,Object>> weekDays) {
		Map<Integer, PmDay> map = new HashMap<>();
		if(!Tools.isEmpty(weekDays)) {
			for (Map<String, Object> s : weekDays) {
				String dayWorking = Tools.toString(s.get("dayWorking"));
				int dayType = Tools.parseInt(s.get("dayType"));
				List<Map<String, Object>> workingTimes = (List<Map<String, Object>>) s.get("workingTimes");
				PmDay pmDay = this.getDayWorkTimes(dayWorking, workingTimes);
				pmDay.setDate(dayType);
				map.put(dayType, pmDay);
			}
		}
		return map;
	}

	/**
	 * 按规则解析时间
	 *
	 * @param workingTimes 标准工作周上班时间配置
	 * @return 标准工作周上班时间配置
	 */
	private PmDay getDayWorkTimes(final String dayWorking, final List<Map<String, Object>> workingTimes) {
		PmDay pmDay = new PmDay(); // 一天
		if (!dayWorking.equals("0") && !Tools.isEmpty(workingTimes)) {
			List<PmTime> pmHours = new ArrayList<>(); // 一天上班时间段集合
			for (Map<String, Object> wt : workingTimes) {
				PmTime pmHour = new PmTime(); // 上班时间段
				pmHour.setFromTime(Tools.getDayTimes(Tools.toString(wt.get("fromTime"))));
				pmHour.setToTime(Tools.getDayTimes(Tools.toString(wt.get("toTime"))));
				pmHours.add(pmHour);
			}
			pmDay.setTimes(pmHours);
		}
		return pmDay;
	}
}
