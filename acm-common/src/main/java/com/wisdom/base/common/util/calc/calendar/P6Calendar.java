package com.wisdom.base.common.util.calc.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P6Calendar extends PmCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日历内容
	 */
	private String clndrData;

	/**
	 * @param clndrData 要设置的 clndrData
	 */
	public void setClndrData(final String clndrData) {
		if(!Tools.isEmpty(clndrData)) {
			this.setExceptMap(this.getExceptDayMap(clndrData)); // 返回特例工作日工作时间的map
			this.setWorkDayOfWeekMap(this.getWeekWorkTimeMap(clndrData)); // 解析标准工作周的工作时间
		}
	}

	/**
	 * @return clndrData
	 */
	public String getClndrData() {
		return this.clndrData;
	}

	/**
	 * 返回特例工作日工作时间的map
	 * 
	 * @param dateRule 日历字串
	 * @return map 例外工作配置
	 * {
	 * 20190401=null,##不上班
	 * 20190402=[{"fromTime": "08:00:00","toTime": "12:00:00"},{"fromTime": "13:00:00","toTime": "17:00:00"}] ##上班时间段
	 * }
	 */
	private Map<Integer, PmDay> getExceptDayMap(final String dateRule) {
		Map<Integer, PmDay> map = new HashMap<Integer, PmDay>();
		Pattern day = Pattern.compile("\\d{5,}");
		Pattern hour = Pattern.compile("\\d{2}\\:\\d{2}");
		List<String> exceptList = this.findExceptDaysList(dateRule);
		for (String s : exceptList) {
			Matcher md = day.matcher(s);
			Matcher mh = hour.matcher(s);
			if (md.find()) {
				int dateInt = Tools.parseInt(md.group());
				PmDay pmDay = this.getDayWorkTimes(mh);
				pmDay.setDate(dateInt);
				map.put(dateInt, pmDay);
			}
		}
		return map;
	}

	/**
	 * 获取特例的工作日工作时间的List对象
	 * 
	 * @param dateRule 日历字串
	 * @return list of except day info
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
	private List<String> findExceptDaysList(final String dateRule) {
		List<String> l = new ArrayList<String>();
		// the pattern like (0||6(d|40581)())
		Pattern pa = Pattern.compile(".[0-9]..[0-9]{1,}.d.[0-9]{5,}..(\\(\\d..\\d{1,}\\(s.\\d{2}.\\d{2}.f.\\d{2}.\\d{2}....){0,}\\)\\)");
		Matcher ma = pa.matcher(dateRule);
		while (ma.find()) {
			l.add(ma.group());
		}
		return l;
	}

	/**
	 * 解析标准工作周的工作时间
	 * 
	 * @param dateRule 日历字串
	 * @return Map 标准工作周配置
	 * {
	 * 1=0,
	 * 2=08:00,12:00,13:00,17:00,
	 * 3=08:00,12:00,13:00,17:00,
	 * 4=08:00,12:00,13:00,17:00,
	 * 5=08:00,12:00,13:00,17:00,
	 * 6=08:00,12:00,13:00,17:00,
	 * 7=0
	 * }
	 */
	private Map<Integer, PmDay> getWeekWorkTimeMap(final String dateRule) {
		Map<Integer, PmDay> map = new HashMap<Integer, PmDay>();
		Pattern hour = Pattern.compile("\\d{2}\\:\\d{2}");
		List<String> list = this.findWeekWorkTimeList(dateRule);
		for (String s : list) {
			int weekInt = Integer.parseInt(s.substring(4, 5));
			Matcher mh = hour.matcher(s);
			PmDay pmDay = this.getDayWorkTimes(mh);
			pmDay.setDate(weekInt);
			map.put(weekInt, pmDay);
		}
		return map;
	}

	/**
	 * 获取周的工作时间的List对象
	 * 
	 * @param dateRule 日历信息
	 * @return list of week info
	 */
	private List<String> findWeekWorkTimeList(final String dateRule) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\\(\\d\\|\\|\\d\\(\\)\\((.\\d..\\d{1,}.s.\\d{2}.\\d{2}.f.\\d{2}.\\d{2}\\)\\(\\)\\)){0,}\\)\\)");
		Matcher m = p.matcher(dateRule);
		while (m.find()) {
			list.add(m.group());
		}
		return list;
	}

	/**
	 * @param mh 标准一天的上班配置
	 * @return 标准一天的上班配置
	 */
	private PmDay getDayWorkTimes(final Matcher mh) {
		List<PmTime> times = new ArrayList<PmTime>(); // 一天上班时间段集合
		PmDay pmDay = new PmDay(); // 一天
		while (mh.find()) {
			PmTime pmHour = new PmTime(); // 上班时间段
			pmHour.setFromTime(Tools.getDayTimes(mh.group()));
			if (mh.find()) {
				pmHour.setToTime(Tools.getDayTimes(mh.group()));
			}
			times.add(pmHour);
		}
		pmDay.setTimes(times);
		return pmDay;
	}

}
