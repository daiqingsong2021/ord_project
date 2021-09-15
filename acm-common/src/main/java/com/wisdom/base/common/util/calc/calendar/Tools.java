package com.wisdom.base.common.util.calc.calendar;

import com.wisdom.base.common.exception.BaseException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tools {

	/**
	 * 得到时间的时分秒
	 * 
	 * @param time time 08:30:00
	 * @return 得到时间段
	 */
	public static int[] getDayTimes(final String time) {
		int[] times = new int[] { 0, 0, 0 };
		if (!isEmpty(time)) {
			String[] _times = time.split(":");
			times[0] = (_times.length >= 1) && !isEmpty(_times[0]) ? Integer.parseInt(_times[0]) : 0;
			times[1] = (_times.length >= 2) && !isEmpty(_times[1]) ? Integer.parseInt(_times[1]) : 0;
			times[2] = (_times.length >= 3) && !isEmpty(_times[2]) ? Integer.parseInt(_times[2]) : 0;
		}
		return times;
	}

	/**
	 * 为空(NUll)
	 * @param map map
	 * @return boolean
	 */
	public static boolean isEmpty(final Map<?, ?> map) {
		return (map == null) || map.isEmpty();
	}

	/**
	 * 为空(NUll)
	 * @param list list
	 * @return the list is Empty
	 */
	public static boolean isEmpty(final List<?> list) {
		return (list == null) || (list.size() == 0);
	}

	/**
	 * 为空(NUll)
	 * @param list list
	 * @return the list is Empty
	 */
	public static boolean isEmpty(final Collection<?> list) {
		return (list == null) || (list.size() == 0);
	}

	/**
	 * 为空(NUll)
	 * @param list list
	 * @return the list is Empty
	 */
	public static boolean isEmpty(final Iterable<?> list) {
		return (list == null) || !list.iterator().hasNext();
	}

	/**
	 * 为空(NUll)
	 * @param os
	 * @return boolean
	 */
	public static boolean isEmpty(final Object[] os) {
		return (os == null) || (os.length == 0);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str 字符串
	 * @return 为空返回true
	 */
	public static boolean isEmpty(final String str) {
		return (str == null) || "".equals(str) ? true : false;
	}

	/**
	 * 判断对象是否为NULL
	 * 
	 * @param obj 对象
	 * @return boolean
	 */
	public static boolean isEmpty(final Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof Optional) {
			return !((Optional)obj).isPresent();
		} else if (obj instanceof CharSequence) {
			return ((CharSequence)obj).length() == 0;
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		} else if (obj instanceof Collection) {
			return ((Collection)obj).isEmpty();
		} else {
			return obj instanceof Map ? ((Map)obj).isEmpty() : false;
		}
	}

	/**
	 * 将对象转换为字符串
	 * @param objs
	 * @return String
	 */
	public static String toString(Collection<?> objs) {
		StringBuilder sb = new StringBuilder();
		if (objs == null || objs.size() == 0) {
			return "";
		} else {
			for (Object object : objs) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(toString(object));
			}
		}
		return sb.toString();
	}

	/**
	 * 将对象转换为字符串
	 * @param objs
	 * @return String
	 */
	public static String toString(Object[] objs) {
		StringBuilder sb = new StringBuilder();
		if (objs == null || objs.length == 0) {
			return "";
		} else {
			for (Object object : objs) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(toString(object));
			}
		}
		return sb.toString();
	}

	/**
	 * toString
	 * 
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String toString(final Object obj) {
		if (obj == null) {
			return "";
		} else {
			if (obj.getClass().getName().equals("java.sql.Timestamp") || obj.getClass().getName().equals("java.util.Date")
				|| obj.getClass().getName().equals("java.sql.Date") || obj.getClass().getName().equals("java.util.GregorianCalendar")) {
				return toDateTimeString(obj);
			} else {
				return obj.toString();
			}
		}
	}

	/**
	 * toString
	 * 
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String toDateString(final Object obj) {
		Date date = toDate(obj);
		if (!isEmpty(date)) {
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		return "";
	}

	/**
	 * toString
	 * 
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String toDateTimeString(final Object obj) {
		Date date = toDate(obj);
		if (!isEmpty(date)) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		}
		return "";
	}

	/**
	 * 将对象转换为整形
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Integer toInteger(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? null : Integer.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为整形
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static int parseInt(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? 0 : Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为长整形
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Long toLong(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? null : Long.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为长整形
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static long parseLong(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? 0 : Long.parseLong(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为Float
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Float toFloat(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? null : Float.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为Float
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static float parseFloat(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? 0 : Float.parseFloat(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为Double
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Double toDouble(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? null : Double.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为Double
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static double parseDouble(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? 0 : Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为Boolean
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static boolean parseBoolean(final Object obj) {
		try {
			String text = toString(obj);
			return text.equalsIgnoreCase("Y") || text.equalsIgnoreCase("1") || text.equalsIgnoreCase("true");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将对象转换为Double
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static BigDecimal toBigDecimal(final Object obj) {
		try {
			return (obj == null) || (obj.toString().length() == 0) ? null : new BigDecimal(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为Date
	 * 
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Date toDate(final Object obj) {
		try {
			if (obj != null) {
				if (obj.getClass().getName().equals("java.lang.Long")) {
					Long l = toLong(obj);
					if(!Tools.isEmpty(l)){
						return new Date(l);
					}
				} else if (obj.getClass().getName().equals("java.lang.String")) {
					return toDate(obj.toString());
				} else if (obj.getClass().getName().equals("java.sql.Timestamp") || obj.getClass().getName().equals("java.util.Date") || obj.getClass().getName().equals("java.sql.Date")) {
					return (Date) obj;
				} else if (obj.getClass().getName().equals("java.util.GregorianCalendar")) {
					return ((Calendar) obj).getTime();
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * 将字符串转换为日期
	 * 
	 * @param time 日期字符
	 * @return 日期
	 */
	public static Date toDate(String time) {
		Date date = null;
		try {
			if (!isEmpty(time) && (time.trim().length() >= 10)) {
				time = time.trim();
				if (time.contains("-")) {
					String format = "yyyy-MM-dd" + appendTime(time);
					date = new SimpleDateFormat(format).parse(time, new ParsePosition(0));
				} else if (time.contains("/")) {
					String format = "yyyy/MM/dd" + appendTime(time);
					date = new SimpleDateFormat(format).parse(time, new ParsePosition(0));
				}
			}
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * 判断日期字符串的格式
	 * 
	 * @param time 时间
	 * @return time 时间格式化
	 */
	private static String appendTime(final String time) {
		String fmt = "";
		if (time.contains(":")) {
			String[] ts = time.split(":");
			if(ts.length == 1) {
				fmt = " HH";
			}else if(ts.length == 2) {
				fmt = " HH:mm";
			}else if(ts.length == 3) {
				fmt = " HH:mm:ss";
			}
		} else if (time.contains(" ")) {
			fmt = " HH";
		}
		return fmt;
	}

	/**
	 * 找出找最小的
	 * @param one 日期1
	 * @param two 日期2
	 * @return 最早的日期
	 */
	public static Date getBeforeDate(final Date one, final Date two){
		if (Tools.isEmpty(one) || (!Tools.isEmpty(two) && two.before(one))) {
			return two;
		}else{
			return one;
		}
	}

	/**
	 * 找出找最大的
	 * @param one 日期1
	 * @param two 日期2
	 * @return 最大的日期
	 */
	public static Date getAfterDate(final Date one, final Date two){
		if (Tools.isEmpty(one) || (!Tools.isEmpty(two) && two.after(one))) {
			return two;
		}else{
			return one;
		}
	}

	/**
	 * 得到对象的属性值，通过调用get方法
	 * @param obj 对象
	 * @param propertyName 属性名称
	 * @return value
	 */
	public static Object getValue(Object obj, String propertyName) {
		Object value;
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, obj.getClass());
			Method get = pd.getReadMethod();// 获得get方法
			value = get.invoke(obj);
		} catch (Exception e) {
			throw new BaseException(e);
		}
		return value;
	}

	/**
	 * 设置对象的属性值，通过调用set方法
	 * @param obj 对象
	 * @param propertyName 属性名称
	 * @param params 设置的值
	 */
	public static void setValue(Object obj, String propertyName, Object... params)  {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, obj.getClass());
			Method set = pd.getWriteMethod(); // 获得set方法
			set.invoke(obj, params);
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}
}
