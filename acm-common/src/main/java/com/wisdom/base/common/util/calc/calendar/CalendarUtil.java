package com.wisdom.base.common.util.calc.calendar;

import java.io.Serializable;
import java.util.*;

public class CalendarUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否为工作天
	 *
	 * @param date 当前日期
	 * @param pmCalendar 项目日历
	 * @return 是/否
	 */
	public static boolean isWorkDate(final Date date, final PmCalendar pmCalendar) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		int dayInt = getDayIntByDate(date); // 得到日数的整型天数
		PmDay pmDay = pmCalendar.getExceptMap().get(dayInt);
		if (!Tools.isEmpty(pmDay)) {
			return pmDay.getOnDayMillis() > 0;
		} else {
			int week = getWeekByDate(date); // 得到指定日期是星期几
			pmDay = pmCalendar.getWorkDayOfWeekMap().get(getWeek(week));
			return pmDay.getOnDayMillis() > 0;
		}
	}

	/**
	 * 是否为工作时间
	 *
	 * @param date 当前日期
	 * @param pmCalendar 项目日历
	 * @return 是/否
	 */
	public static boolean isWorkTime(final Date date, final PmCalendar pmCalendar) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		int dayInt = getDayIntByDate(date); // 得到日数的整型天数
		PmDay pmDay = pmCalendar.getExceptMap().get(dayInt);
		if (!Tools.isEmpty(pmDay)) {
			return isWorkTime(date, pmDay);
		} else {
			int week = getWeekByDate(date); // 得到指定日期是星期几
			pmDay = pmCalendar.getWorkDayOfWeekMap().get(getWeek(week));
			return isWorkTime(date, pmDay);
		}
	}

	/**
	 * 是否为工作时间
	 * @param date 当前日期
	 * @param pmDay 当前日期
	 * @return 是/否
	 */
	@SuppressWarnings("deprecation")
	private static boolean isWorkTime(final Date date, final PmDay pmDay) {
		if (!Tools.isEmpty(pmDay)) {
			if(Tools.isEmpty(pmDay.getTimes())){
				return false;
			}else{
				List<PmTime> times = pmDay.getTimes();
				for (PmTime time : times) {
					long as = (date.getHours() * 60 * 60 * 1000L) + (date.getMinutes() * 60 * 1000L);
					if(as >= time.getFromTimeMillis() && as <= time.getToTimeMillis()){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 更具日历返回当天的开始时间
	 * 
	 * @param date 当前日期
	 * @param pmCalendar 项目日历
	 * @return 当天开始工作的时间
	 */
	public static Date getStartWorkTime(final Date date, final PmCalendar pmCalendar) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		int dayInt = getDayIntByDate(date); // 得到日数的整型天数
		int week = getWeekByDate(date); // 得到指定日期是星期几
		boolean isFind = false; // 是否找到有效的上班时间
		while (!isFind) {
			PmDay pmDay = pmCalendar.getExceptMap().get(dayInt);
			if (!Tools.isEmpty(pmDay)) {
				isFind = setWorkStartTime(start, pmDay); // 有没有例外不上班的情况
			} else {
				pmDay = pmCalendar.getWorkDayOfWeekMap().get(getWeek(week));
				isFind = setWorkStartTime(start, pmDay);
			}
			if(!isFind){
				start.add(Calendar.DATE, 1);
			}
			++dayInt;
			++week;
		}
		return start.getTime();
	}

	/**
	 * 设置时间
	 * 
	 * @param start 开始日期
	 * @param pmDay 日历当天设置
	 * @return 是否存在上班时间设置
	 */
	private static boolean setWorkStartTime(final Calendar start, final PmDay pmDay) {
		if (!Tools.isEmpty(pmDay) && !Tools.isEmpty(pmDay.getTimes())) { // 上班的情况
			List<PmTime> times = pmDay.getTimes();
			PmTime time = times.get(0);
			setCalendarHourOrMinute(start, time.getFromTime()[0], time.getFromTime()[1]);
			return true;
		}
		return false;
	}

	/**
	 * 更具日历返回当天的完成时间
	 * 
	 * @param date 当天日期
	 * @param pmCalendar 日期字串
	 * @return 当天完成工作的时间
	 */
	public static Date getEndWorkTime(final Date date, final PmCalendar pmCalendar) {
		Calendar end = Calendar.getInstance();
		end.setTime(date);
		int dayInt = getDayIntByDate(date); // 得到日数的整型天数
		int week = getWeekByDate(date); // 得到指定日期是星期几
		boolean isFind = false; // 是否找到有效的下班时间
		while (!isFind) {
			PmDay pmDay = pmCalendar.getExceptMap().get(dayInt);
			if (!Tools.isEmpty(pmDay)) {
				isFind = setWorkEndTime(end, pmDay); // 有没有例外不上班的情况
			} else {
				pmDay = pmCalendar.getWorkDayOfWeekMap().get(getWeek(week));
				isFind = setWorkEndTime(end, pmDay);
			}
			if(!isFind){
				end.add(Calendar.DATE, -1);
			}
			--dayInt;
			--week;
		}
		return end.getTime();
	}

	/**
	 * 设置时间
	 * 
	 * @param end 完成日期
	 * @param pmDay 完成日期当天的配置
	 * @return 返回是否找到配置
	 */
	private static boolean setWorkEndTime(final Calendar end, final PmDay pmDay) {
		if (!Tools.isEmpty(pmDay) && !Tools.isEmpty(pmDay.getTimes())) { // 上班的情况
			List<PmTime> times = pmDay.getTimes();
			PmTime time = times.get(times.size() - 1);
			setCalendarHourOrMinute(end, time.getToTime()[0], time.getToTime()[1]);
			return true;
		}
		return false;
	}

	/**
	 * 设置日期的时分秒
	 * @param calendar 日期
	 * @param hour 时
	 * @param minute 分
	 */
	private static void setCalendarHourOrMinute(final Calendar calendar, final int hour, final int minute) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
	}

	/**
	 * 设置日期的时分秒
	 * @param date 日期
	 * @param hour 时
	 * @param minute 分
	 * @return 日期
	 */
	private static Calendar setCalendarHourOrMinute(final Date date, final int hour, final int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		setCalendarHourOrMinute(calendar, hour, minute);
		return calendar;
	}

	/**
	 * 返回一天的工作小时数
	 *
	 * @param date the date
	 * @param pmCalendar 日历信息
	 * @return 当天的工作小时数(小时)
	 */
	public static double getWorkHour(final Date date, final PmCalendar pmCalendar) {
		long workTime = 0;
		int dayInt = getDayIntByDate(date); // 得到日数的整型天数
		int week = getWeekByDate(date); // 得到指定日期是星期几
		PmDay exceptDay = pmCalendar.getExceptMap().get(dayInt);
		PmDay workDay = pmCalendar.getWorkDayOfWeekMap().get(week);
		if (!Tools.isEmpty(exceptDay)) {
			workTime = exceptDay.getOnDayMillis();
		} else if (!Tools.isEmpty(workDay)) {
			workTime = workDay.getOnDayMillis();
		}
		return workTime / 1000.0 / 60 / 60;
	}

	/**
	 * 计算开始日期
	 * 
	 * @param date 开始日期
	 * @param workHour 延时工作小时(小时)0小时也要更正日期
	 * @param pmCalendar 日历字符串
	 * @return 计算完成日期
	 */
	public static Date caculateStartDate(final Date date, final Double workHour, final PmCalendar pmCalendar) {
		Date _date = date;
		if (workHour >= 0) {// 正延时
			_date = _caculateEndDate(_date, workHour, pmCalendar, true);
		} else {// 负延时
			_date = _caculateEndDate(_date, workHour, pmCalendar, true);
		}
		return _date;
	}

	/**
	 * 计算完成日期
	 * 
	 * @param date 开始日期
	 * @param workHour 延时工作小时(小时)0小时也要更正日期
	 * @param pmCalendar 日历字符串
	 * @return 计算完成日期
	 */
	public static Date caculateEndDate(final Date date, final Double workHour, final PmCalendar pmCalendar) {
		Date _date = date;
		if (workHour >= 0) {// 正延时
			_date = _caculateEndDate(_date, workHour, pmCalendar, false);
		} else {// 负延时
			_date = _caculateEndDate(_date, workHour, pmCalendar, false);
		}
		return _date;
	}

	/**
	 * 计算完成日期
	 * 
	 * @param start 开始日期
	 * @param workHour 延时工作小时(小时)
	 * @param pmCalendar 日历字符串
	 * @param isStart 是否求开始日期，比如开始时间如是当天完成时间=第二天开始时间，完成时间如果是开始时间=头一天完成时间
	 * @return 计算完成日期
	 */
	private static Date _caculateEndDate(final Date start, final double workHour, final PmCalendar pmCalendar, final boolean isStart) {
		long workMillis = ((int) (workHour * 60)) * 60L * 1000L; // 需要工作的毫秒数
		if (Math.abs(workMillis * 1.0 / pmCalendar.getWorkHourOfWeek()) > 1) { // 如果所跨周数大于1
			return _caculateEndDateWeekByStart(start, workMillis, pmCalendar, isStart); //按周计算
		}else{
			return _caculateEndByStart(start, workHour, pmCalendar, isStart); //按日历计算
		}
	}

	/**
	 * 按周计算完成日期
	 * @param start 开始
	 * @param workMillis 工作毫秒
	 * @param pmCalendar 日历
	 * @param isStart 是否求开始日期，比如开始时间如是当天完成时间=第二天开始时间，完成时间如果是开始时间=头一天完成时间
	 * @return 完成日期
	 */
	private static Date _caculateEndDateWeekByStart(final Date start, final long workMillis, final PmCalendar pmCalendar, final boolean isStart) {
		Calendar second = setCalendarHourOrMinute(start, 0, 0);
		if(workMillis >= 0){
			second.add(Calendar.DATE, 1); //第二天
			long todaymillis = getWorkHourBetweenTwoDate(start, second.getTime(), pmCalendar); // 获取当天实际工作量
			long _workMillis = workMillis - todaymillis;
			long[] endTimes = getWorkHoursBetweenWeeks(second.getTime(), _workMillis, pmCalendar);
			Date end = new Date(endTimes[0]);
			double workRemain = endTimes[1] / 1000.0 / 60.0 / 60.0;
			return __caculateEndDateWeekByStart(end, workRemain, pmCalendar, isStart);
		} else {
			Date sd = getStartWorkTime(start, pmCalendar);
			long todaymillis = getWorkHourBetweenTwoDate(sd, start, pmCalendar); // 获取当天实际工作量
			long _workMillis = Math.abs(workMillis) - todaymillis;
			second.add(Calendar.DATE, -1); //上一天
			long[] endTimes = getWorkHoursBetweenWeeksSub(second.getTime(), _workMillis, pmCalendar);
			Date end = new Date(endTimes[0]);
			double workRemain = - endTimes[1] / 1000.0 / 60.0 / 60.0;
			return __caculateEndDateWeekByStart(end, workRemain, pmCalendar, isStart);
		}
	}

	/**
	 * 按周计算完成日期
	 * @param date 开始
	 * @param workHour 工作毫秒
	 * @param pmCalendar 日历
	 * @param isStart 是否求开始日期，比如开始时间如是当天完成时间=第二天开始时间，完成时间如果是开始时间=头一天完成时间
	 * @return 完成日期
	 */
	@SuppressWarnings("deprecation")
	private static Date __caculateEndDateWeekByStart(final Date date, final double workHour, final PmCalendar pmCalendar, final boolean isStart) {
		if (0 == workHour) {
			if(isStart){
				return getStartWorkTime(date, pmCalendar);
			}else{
				date.setDate(date.getDate() - 1);
				return getEndWorkTime(date, pmCalendar);
			}
		}else {
			return _caculateEndByStart(date, workHour, pmCalendar, isStart);
		}
	}

	/**
	 * 返回周之间的工作时间，且考虑except时间
	 * 
	 * @param start 开始日期
	 * @param workMillis 工作时间
	 * @param pmCalendar 日历信息
	 * @return 年；月；日；工作毫秒数
	 */
	private static long[] getWorkHoursBetweenWeeks(final Date start, final long workMillis, final PmCalendar pmCalendar) {
		int week = (int)(workMillis / pmCalendar.getWorkHourOfWeek()); // 获取此工作时间段跨度多少星期
		long surplus = workMillis - (pmCalendar.getWorkHourOfWeek() * week); // 获取減去星期工作时间后剩于的工作小时数
		if (week < 1) {
			return new long[]{start.getTime(), surplus};
		} else {
			int dayStartInt = getDayIntByDate(start); // 获取日期对应的天dayInt
			int weekEndDay = dayStartInt + (week * 7);
			Date end = getDateByDayInt(weekEndDay); //获取日期增加整数周后的结束日期
			for(int day : pmCalendar.getExceptMap().keySet()){
				if ((dayStartInt <= day) && (day < weekEndDay)) {
					surplus += getMillisAddOfThisExceptBetweenWeek(day, pmCalendar);
				}
			}
			getWorkHoursBetweenWeeks(end, surplus, pmCalendar);
			return new long[]{end.getTime(), surplus};
		}
	}

	/**
	 * 返回周之间的工作时间，且考虑except时间
	 *
	 * @param end 开始日期
	 * @param workMillis 工作时间
	 * @param pmCalendar 日历信息
	 * @return 年；月；日；工作毫秒数
	 */
	private static long[] getWorkHoursBetweenWeeksSub(final Date end, final long workMillis, final PmCalendar pmCalendar) {
		int week = (int)(workMillis / pmCalendar.getWorkHourOfWeek()); // 获取此工作时间段跨度多少星期
		long surplus = workMillis - (pmCalendar.getWorkHourOfWeek() * week); // 获取減去星期工作时间后剩于的工作小时数
		if (week < 1) {
			return new long[]{end.getTime(), surplus};
		} else {
			int dayEndInt = getDayIntByDate(end); // 获取日期对应的天dayInt
			int weekStartDay = dayEndInt - (week * 7);
			Date start = getDateByDayInt(weekStartDay); //获取日期减少整数周后的开始日期
			for(int day : pmCalendar.getExceptMap().keySet()){
				if ((weekStartDay < day) && (day <= dayEndInt)) {
					surplus += getMillisAddOfThisExceptBetweenWeek(day, pmCalendar);
				}
			}
			getWorkHoursBetweenWeeksSub(start, surplus, pmCalendar);
			return new long[]{start.getTime(), surplus};
		}
	}

	/**
	 * 返回星期的标准工作时间-例外的工作时间
	 * 
	 * @param day the dayNum
	 * @param pmCalendar 日历信息
	 * @return the mills of except
	 */
	private static long getMillisAddOfThisExceptBetweenWeek(final int day, final PmCalendar pmCalendar) {
		long weekWorkTime = 0; //工作毫秒
		long exceptWorkTime = 0; //例外毫秒
		PmDay dayExcept = pmCalendar.getExceptMap().get(day);
		int week = getWeekByDate(getDateByDayInt(day));
		PmDay dayWork = pmCalendar.getWorkDayOfWeekMap().get(week);
		if (!Tools.isEmpty(dayExcept) && !Tools.isEmpty(dayExcept.getTimes())) {
			for (PmTime time : dayExcept.getTimes()) {
				exceptWorkTime += time.getTimeDifference();
			}
		}
		if (!Tools.isEmpty(dayWork) && !Tools.isEmpty(dayWork.getTimes())) {
			for (PmTime time : dayWork.getTimes()) {
				weekWorkTime += time.getTimeDifference();
			}
		}
		return weekWorkTime - exceptWorkTime;
	}
	
	/**
	 * 计算完成日期
	 * 
	 * @param start 开始日期
	 * @param workHour 延时工作小时(小时)
	 * @param pmCalendar 日历字符串
	 * @param isStart 是否求开始日期，比如开始时间如是当天完成时间=第二天开始时间，完成时间如果是开始时间=头一天完成时间
	 * @return 计算完成日期
	 */
	@SuppressWarnings("deprecation")
	private static Date _caculateEndByStart(final Date start, final Double workHour, final PmCalendar pmCalendar, final boolean isStart) {
		if (workHour >= 0) {
			long[] endTimes = calendarEndTime(start, workHour, pmCalendar);
			Date end = getDateByDayInt((int) endTimes[0]);
			if(isStart && endTimes[3] == 1){ //始果计算开始时间并且是当天的完成时间，则自动推到下一个工作日期
				end.setDate(end.getDate() + 1);
				end = getStartWorkTime(end, pmCalendar);
			}else{
				end.setTime(end.getTime() + endTimes[1] + endTimes[2]);
			}
			return end;
		} else {
			long[] endTimes = calendarEndTimeSUB(start, Math.abs(workHour), pmCalendar);
			Date end = getDateByDayInt((int) endTimes[0]);
			if(!isStart && endTimes[3] == 1) { //始果计算开始时间并且是当天的完成时间，则自动推到下一个工作日期
				end.setDate(end.getDate() - 1);
				end = getEndWorkTime(end, pmCalendar);
			}else {
				end.setTime((end.getTime() + endTimes[1]) - endTimes[2]);
			}
			return end;
		}
	}

	/**
	 * 根据开始时间和工作小时数求完成时间
	 * 
	 * @param start 开始日期
	 * @param workHour 工作小时数
	 * @param pmCalendar 日历信息
	 * @return i+","+s[1]+","+workMillis;
	 */
	private static long[] calendarEndTime(final Date start, final Double workHour, final PmCalendar pmCalendar) {
		long workMillis = ((int) (workHour * 60)) * 60 * 1000L; // 需要工作的毫秒数
		Map<Integer, PmDay> mapExcept = pmCalendar.getExceptMap();
		Map<Integer, PmDay> mapWorkDay = pmCalendar.getWorkDayOfWeekMap();
		int startInt = getDayIntByDate(start); // 得到日数的整型天数
		int week = getWeekByDate(start); // 得到指定日期是星期几
		for (int i = startInt; workMillis >= 0; i++, week++) { // 根据天数循环
			PmDay exceptDay = mapExcept.get(i);
			if (!Tools.isEmpty(exceptDay)) {// 判断这一天是否在except当中
				if (exceptDay.getOnDayMillis() > 0) { // 如果这一天的工时不为零则计算
					long[] workTimes = subDayWorkByStart(start, workMillis, startInt,  i, exceptDay);
					if(workTimes.length > 1){
						return workTimes;
					}else{
						workMillis = workTimes[0];
					}
				}
			} else {
				PmDay weekDay = mapWorkDay.get(getWeek(week)); // 得到日期对应的星期几的工作时间
				if (!Tools.isEmpty(weekDay) && (weekDay.getOnDayMillis() > 0)) { // 根据当前天数获取当前天所在这周的星期几
					long[] workTimes = subDayWorkByStart(start, workMillis, startInt,  i, weekDay);
					if(workTimes.length > 1){
						return workTimes;
					}else{
						workMillis = workTimes[0];
					}
				}
			}
		}
		return new long[] { startInt, 0, workMillis, 0 };
	}

	/**
	 * 减去一天的工作量
	 * @param start 开始
	 * @param workTime 工作时间
	 * @param dayInt 当天
	 * @param day 当天开作时间配置
	 * @return 天数，起始日期，剩余工作时间, 是否刚好最后一天工作量
	 */
	private static long[] subDayWorkByStart(final Date start, final long workTime, final int startInt, final int dayInt, final PmDay day){
		long workMillis = workTime;
		if (workMillis > day.getOnDayMillis()) { // 是否大于一天的工作量
			workMillis -= day.getOnDayMillis();
		} else {
			for (PmTime time : day.getTimes()) { // 分割字符串，去除开始时间和完成时间
				if (startInt == dayInt) { // 获取第一天的正确开始时间
					long[] s = getFirstDayActual(start, time);
					if (workMillis <= s[0]) {
						return new long[] { dayInt, s[1], workMillis, (workMillis == s[0] ? 1 : 0) }; // 天的整型，开始的时间，剩于的毫秒數
					} else {
						workMillis -= s[0];
					}
				} else {
					if (workMillis <= time.getTimeDifference()) { // 判断剩余的时间毫秒数是否小于某一时段的毫秒数，如果小于则此毫秒数，天数，时间极为完成时间
						return new long[] { dayInt, time.getFromTimeMillis(), workMillis, (workMillis == time.getTimeDifference() ? 1 : 0) }; // 返回天数，这一时段的开始时间，剩余毫秒数
					} else {
						workMillis -= time.getTimeDifference(); // 如果不成立则剩余的毫秒数减去这一时间段的毫秒数
					}
				}
			}
		}
		return new long[] { workMillis };
	}

	/**
	 * 根据开始时间和工作小时数求完成时间
	 * 
	 * @param end 结束日期
	 * @param workHour 工作小时数 workHour 為負數的情況
	 * @param pmCalendar 日历信息
	 * @return 天数，起始日期，剩余工作时间, 是否刚好最后一天工作量
	 */
	private static long[] calendarEndTimeSUB(final Date end, final Double workHour, final PmCalendar pmCalendar) {
		long workMillis = ((int) (workHour * 60)) * 60 * 1000L; // 分钟要整型，所以要取整，需要工作的毫秒数
		Map<Integer, PmDay> mapExcept = pmCalendar.getExceptMap();
		Map<Integer, PmDay> mapWorkDay = pmCalendar.getWorkDayOfWeekMap();
		int endInt = getDayIntByDate(end); // 得到日数的整型天数
		int week = getWeekByDate(end); // 得到指定日期是星期几
		for (int i = endInt; workMillis >= 0; i--, week--) { // 根据天数循环
			PmDay exceptDay = mapExcept.get(endInt);
			if (!Tools.isEmpty(exceptDay)) {// 判断这一天是否在except当中
				if (exceptDay.getOnDayMillis() > 0) { // 如果这一天的工时为零则跳过
					long[] workTimes = subDayWorkByEnd(end, workMillis, endInt,  i, exceptDay);
					if(workTimes.length > 1){
						return workTimes;
					}else{
						workMillis = workTimes[0];
					}
				}
			} else {
				PmDay weekDay = mapWorkDay.get(getWeek(week)); // 得到日期对应的星期几
				if (!Tools.isEmpty(weekDay) && (weekDay.getOnDayMillis() > 0)) { // 根据当前天数获取当前天所在这周的星期几
					long[] workTimes = subDayWorkByEnd(end, workMillis, endInt,  i, weekDay);
					if(workTimes.length > 1){
						return workTimes;
					}else{
						workMillis = workTimes[0];
					}
				}
			}
		}
		return new long[] { endInt, 0, workMillis };
	}

	/**
	 * 减去一天的工作量
	 * @param end 结束
	 * @param workTime 工作时间
	 * @param dayInt 当天
	 * @param day 当天工作配置
	 * @return 天数，起始日期，剩余工作时间, 是否刚好最后一天工作量
	 */
	private static long[] subDayWorkByEnd(final Date end, final long workTime, final int endInt, final int dayInt, final PmDay day){
		long workMillis = workTime;
		if (endInt != dayInt && workMillis > day.getOnDayMillis()) { // 是否大于一天的工作量
			workMillis -= day.getOnDayMillis();
		} else {
			List<PmTime> times = day.getTimes();
			for (int n = times.size() - 1; n >= 0; n--) { // 分割字符串，去除开始时间和完成时间
				PmTime time = times.get(n);
				if (endInt == dayInt) { // 获取第一天的正确开始时间
					long[] s = getFirstDayActualSUB(end, time);
					if (workMillis <= s[0]) {
						return new long[] { dayInt, s[1], workMillis, (workMillis == s[0] ?1 : 0)}; // 天的整型，开始的时间，剩于的毫秒數
					} else {
						workMillis -= s[0];
					}
				} else {
					if (workMillis <= time.getTimeDifference()) { // 判断剩余的时间毫秒数是否小于某一时段的毫秒数，如果小于则此毫秒数，天数，时间极为完成时间
						return new long[] { dayInt, time.getToTimeMillis(), workMillis, (workMillis == time.getTimeDifference() ? 1 : 0)}; // 返回天数，这一时段的开始时间，剩余毫秒数
					} else {
						workMillis -= time.getTimeDifference(); // 如果不成立则剩余的毫秒数减去这一时间段的毫秒数
					}
				}
			}
		}
		return new long[] { workMillis };
	}

	/**
	 * 计算工期
	 * 
	 * @param start 开始时间
	 * @param end 完成时间
	 * @param pmCalendar 日历字符串
	 * @return 工期(小时)
	 */
	public static double caculateDuration(final Date start, final Date end, final PmCalendar pmCalendar) {
		if (start.after(end)) {
			long workMillis = getWorkHourBetweenTwoDateJumpWeek(end, start, pmCalendar);
			return -(workMillis / 1000.0 / 60.0 / 60.0);
		} else {
			long workMillis = getWorkHourBetweenTwoDateJumpWeek(start, end, pmCalendar);
			return workMillis / 1000.0 / 60.0 / 60.0;
		}
	}

	/**
	 * 获取开始工作时间到完成工作时间工作的分钟数
	 *
	 * @param start 开始日期
	 * @param end 结束日期
	 * @param pmCalendar 日历信息
	 * @return the work minit between to date
	 */
	private static long getWorkHourBetweenTwoDateJumpWeek(final Date start, final Date end, final PmCalendar pmCalendar) {
		double day = (end.getTime() - start.getTime()) / (24.0 * 60 * 60 * 1000); //相差多少天
		if (day == 0) {
			return 0; //日期相同
		} else if (day < 7) {
			return getWorkHourBetweenTwoDate(start, end, pmCalendar); //小于一周
		} else {
			return _getWorkHourBetweenTwoDateJumpWeek(start, end, pmCalendar);
		}
	}

	/**
	 * 获取开始工作时间到完成工作时间工作的分钟数
	 *
	 * @param start 开始日期
	 * @param end 结束日期
	 * @param pmCalendar 日历信息
	 * @return the work minit between to date
	 */
	private static long _getWorkHourBetweenTwoDateJumpWeek(final Date start, final Date end, final PmCalendar pmCalendar) {
			Calendar second = setCalendarHourOrMinute(start, 0, 0);
			second.add(Calendar.DATE, 1); //第二天
			long workMillis = getWorkHourBetweenTwoDate(start, second.getTime(), pmCalendar); //得到开始时间当天的实际工期
			int startDay = getDayIntByDate(second.getTime()); // 从第二天开始算
			int endDay = getDayIntByDate(end);
			int week = (endDay - startDay) / 7; // 获取所跨周的数量
			int _startDay = startDay + (week * 7);
			workMillis += pmCalendar.getWorkHourOfWeek() * week;
			for(int dayInt : pmCalendar.getExceptMap().keySet()){ // 循环mapExcept，减去特例日的工作时间
				if ((startDay <= dayInt) && (dayInt < _startDay)) {
					workMillis -= getMillisAddOfThisExceptBetweenWeek(dayInt, pmCalendar);
				}
			}
			Date _start = getDateByDayInt(_startDay);
			workMillis += getWorkHourBetweenTwoDate(_start, end, pmCalendar);
			return workMillis;
	}

	/**
	 * 返回两个工作时间之间的工作毫秒数
	 * 
	 * @param start 开始日期
	 * @param end 结束日期
	 * @param pmCalendar 日历信息
	 * @return the hour between to date
	 */
	private static long getWorkHourBetweenTwoDate(final Date start, final Date end, final PmCalendar pmCalendar) {
		long workMillis = 0;
		Map<Integer, PmDay> mapExcept = pmCalendar.getExceptMap(); // 例外日期
		Map<Integer, PmDay> mapWorkDay = pmCalendar.getWorkDayOfWeekMap(); // 标准周日期
		int startInt = getDayIntByDate(start); // 得到日数的整型天数
		int week = getWeekByDate(start); // 得到指定日期是星期几
		int endInt = getDayIntByDate(end); // 得到日数的整型天数
		for (int i = startInt; i <= endInt; i++, week++) {
			PmDay excptDay = mapExcept.get(i);
			if (!Tools.isEmpty(excptDay)) { //存在例外
				workMillis += getWorkHourBetweenTwoDate(i, start, startInt, end, endInt, excptDay);
			} else { //标准工作周
				PmDay weekDay = mapWorkDay.get(getWeek(week)); // 得到日期对应的星期几
				workMillis += getWorkHourBetweenTwoDate(i, start, startInt, end, endInt, weekDay);
			}
		}
		return workMillis;
	}

	/**
	 * 返回两个工作时间之间的工作毫秒数
	 * @param dayInt 当天日期天数
	 * @param start 开始
	 * @param startDayInt 开始天数
	 * @param end 结束
	 * @param endDayInt 结束开数
	 * @param day 当天时间配置
	 * @return 工作时间
	 */
	private static long getWorkHourBetweenTwoDate(final int dayInt, final Date start, final int startDayInt, final Date end, final int endDayInt, final PmDay day){
		long workMillis = 0;
		if(!Tools.isEmpty(day) && (day.getOnDayMillis() > 0)) {
			if(startDayInt == endDayInt){ //开始结束同一天
				workMillis += getWorkHourBetweenTwoDate(start, end, day);
			}else if ((dayInt != startDayInt) && (dayInt != endDayInt)) { //开始结束中间的某一天
				workMillis += day.getOnDayMillis();
			} else {
				for (PmTime time : day.getTimes()) { // 分割字符串，去除开始时间和完成时间
					if (dayInt == startDayInt) { //等于开始
						workMillis += getFirstDayActual(start, time)[0];
					} else { //等于结束
						workMillis += getLastDayActual(end, time);
					}
				}
			}
		}
		return workMillis;
	}

	/**
	 * 返回两个工作时间之间的工作毫秒数
	 * @param start 开始
	 * @param end 结束
	 * @param day 当天工作时间配置
	 * @return 工作时间
	 */
	private static long getWorkHourBetweenTwoDate(final Date start, final Date end, final PmDay day){
		long workMillis = 0;
		if(!Tools.isEmpty(day) && (day.getOnDayMillis() > 0)) {
			for (PmTime time : day.getTimes()) {
				workMillis += getActualWorkMillis(start, time, end);
			}
		}
		return workMillis;
	}

	/**
	 * 返回最后一天实际的工作毫秒数
	 * 
	 * @param start 实际开始时间
	 * @param time 日历上工作时间
	 * @return long型日期，此段时间的开始时间
	 */
	@SuppressWarnings("deprecation")
	private static long getLastDayActual(final Date start, final PmTime time) {
		long as = (start.getHours() * 60 * 60 * 1000L) + (start.getMinutes() * 60 * 1000L);
		long s = time.getFromTimeMillis();
		long e = time.getToTimeMillis();
		if (as > e) {
			return e - s;
		} else if (as > s) {
			return as - s;
		} else {
			return 0;
		}
	}

	/**
	 * 获取实际开始到实际完成的时间差的毫秒数
	 * 
	 * @param start 实际时间
	 * @param time 开始时间
	 * @param end 完成时间
	 * @return the millis of actual work time
	 */
	@SuppressWarnings("deprecation")
	private static long getActualWorkMillis(final Date start, final PmTime time, final Date end) {
		long as = (start.getHours() * 60 * 60 * 1000L) + (start.getMinutes() * 60 * 1000L);
		long ae = (end.getHours() * 60 * 60 * 1000L) + (end.getMinutes() * 60 * 1000L);
		long s = time.getFromTimeMillis();
		long e = time.getToTimeMillis();
		if ((as >= e) || (ae <= s) || (as >= ae)) {
			return 0;
		} else if (as >= s){
			if(ae <= e) {
				return ae - as;
			} else {
				return e - as;
			}
		} else {
			if (ae >= e) {
				return e - s;
			} else {
				return ae - s;
			}
		}
	}

	/**
	 * 返回第一天实际的工作毫秒数
	 * 
	 * @param end 实际完成时间
	 * @param times 日历上工作时间
	 * @return long型日期，此段时间的开始时间
	 */
	@SuppressWarnings("deprecation")
	private static long[] getFirstDayActualSUB(final Date end, final PmTime times) {
		long ae = (end.getHours() * 60 * 60 * 1000L) + (end.getMinutes() * 60 * 1000L);
		long s = times.getFromTimeMillis();
		long e = times.getToTimeMillis();
		if (ae < s) {
			return new long[] { 0, s };
		} else if (ae < e) {
			return new long[] { (ae - s), ae };
		} else {
			return new long[] { (e - s), e };
		}
	}

	/**
	 * 返回第一天实际的工作毫秒数
	 * 
	 * @param start 实际开始时间
	 * @param time 日历上工作时间
	 * @return long型日期，此段时间的开始时间
	 */
	@SuppressWarnings("deprecation")
	private static long[] getFirstDayActual(final Date start, final PmTime time) {
		long as = (start.getHours() * 60L * 60L * 1000L) + (start.getMinutes() * 60L * 1000L);
		long s = time.getFromTimeMillis();
		long e = time.getToTimeMillis();
		if (as > e) {
			return new long[] { 0, s };
		} else if (as > s) {
			return new long[] { (e - as), as };
		} else {
			return new long[] { (e - s), s};
		}
	}

	/**
	 * 获取当前时区的毫秒偏移量 ，由于时区原因当前时间距格林乔治起始时间的毫秒数会有时区上的偏差
	 * 
	 * @return the offset of timezone
	 */
	private static int getTimeZoneOffSet() {
		Calendar cal = Calendar.getInstance();
		TimeZone off = cal.getTimeZone();
		return off.getOffset(cal.getTimeInMillis());
	}

	/**
	 * 得到日历起始(1899-12-30)日期距指定日期的天数
	 * 
	 * @param date 日期
	 * @return 距指定日期的天数
	 */
	public static int getDayIntByDate(final Date date) {
		return (int) (((date.getTime() + getTimeZoneOffSet()) / 24 / 60 / 60 / 1000) + 25569);
	}

	/**
	 * 得到根据日历起始(1899-12-30)日期距多少天后的日期
	 * 
	 * @param dayInt 日期整型
	 * @return 日期
	 */
	private static Date getDateByDayInt(final int dayInt) {
		long time = ((dayInt - 25569) * 24L * 60 * 60 * 1000) - getTimeZoneOffSet();
		return new Date(time);
	}

	/**
	 * 得到日期的星期数
	 * 
	 * @param date 日期
	 * @return 星期几
	 */
	private static int getWeekByDate(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getWeekByDate(calendar);
	}

	/**
	 * 得到日期的星期数
	 *
	 * @param calendar 日期
	 * @return 星期几
	 */
	private static int getWeekByDate(final Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 得到日期的星期数
	 * 
	 * @param week 星期数
	 * @return 星期几
	 */
	private static int getWeek(final int week) {
		int _week = (week % 7 + 7) % 7;
		_week = _week == 0 ? 7 : _week;
		return Math.abs(_week);
	}
}
