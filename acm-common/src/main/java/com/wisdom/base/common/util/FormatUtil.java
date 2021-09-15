package com.wisdom.base.common.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 工具箱 处理字符串、分隔符等相关方法
 * @author D.L.
 */
public final class FormatUtil {
	/**
	 * 定义日志输出器
	 */
	private static final Logger log = LoggerFactory.getLogger(FormatUtil.class);

	/**
	 * the constructor
	 */
	private FormatUtil() {
	}

	/**
	 * 转换XML中的<与>号为代码标记
	 * @param desc xml字符串对象
	 * @return 转换xml后的字符对象
	 */
	public static String removeXMLKeywords(String desc) {
		String xml = "";
		if (desc != null) {
			xml = desc.replace("<", "&lt;").replace(">", "&gt;");
		}

		return xml;
	}

	/**
	 * 将参数全部转换为小写
	 * @param arg 要转换的参数
	 * @return 小写字符
	 */
	public static String toLowerCase(String arg) {
		String str = "";
		if (arg != null) {
			// log.debug("未传入转换为小写的字符！");
			str = arg.toLowerCase();
		}

		return str;
	}

	/**
	 * 将参数全部转换为小写
	 * @param arg 要转换的参数
	 * @return 小写字符
	 */
	public static String toUpperCase(String arg) {
		String str = "";
		if (arg != null) {
			// log.debug("未传入转换为大写的字符！");
			str = arg.toUpperCase();
		}

		return str;
	}

	/**
	 * 将对象转换为字符串
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			if (obj.getClass().getName().equals("java.sql.Timestamp") || obj.getClass().getName().equals("java.util.Date") || obj.getClass().getName().equals("java.sql.Date")) {
				return stringFromDate(obj);
			} else if (obj.getClass().getName().equals("java.lang.Double") || obj.getClass().getName().equals("java.lang.Float")) {
				return doubleFormat(parseDouble(obj));
			} else {
				return obj.toString();
			}
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
	 * 将对象转换为整形
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Integer toInteger(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? null : Integer.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为整形
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static int parseInt(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? 0 : Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为Float
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Float toFloat(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? null : Float.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为Float
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static float parseFloat(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? 0 : Float.parseFloat(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为Double
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Double toDouble(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? null : Double.valueOf(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为Double
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static double parseDouble(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? 0 : Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将对象转换为Double
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static BigDecimal toBigDecimal(Object obj) {
		try {
			return obj == null || obj.toString().length() == 0 ? null : new BigDecimal(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 将对象转换为Boolean
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static boolean parseBoolean(Object obj) {
		try {
			String text = FormatUtil.toString(obj);
			return text.equalsIgnoreCase("Y") || text.equalsIgnoreCase("1") || text.equalsIgnoreCase("true");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将对象转换为Date
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Date parseDate(Object obj) {
		try {
			if (obj != null) {
				if (obj.getClass().getName().equals("java.lang.String")) {
					return FormatUtil.stringToDate(obj.toString());
				} else if (obj.getClass().getName().equals("java.sql.Timestamp") || obj.getClass().getName().equals("java.util.Date") || obj.getClass().getName().equals("java.sql.Date")) {
					return (Date) obj;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将对象转换为Date
	 * @param obj 需要转换的对象
	 * @return 字符串
	 */
	public static Date toDate(Object obj) {
		return parseDate(obj);
	}

	/**
	 * 数组转成list
	 * @param ids
	 * @return List<String>
	 */
	public static List<String> parseList(String[] ids) {
		List<String> list = new ArrayList<String>();
		if (!StringUtils.isEmpty(ids)) {
			for (String t : ids) {
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * 数组转成list
	 * @param ids
	 * @return List<String>
	 */
	public static List<String> parseList(Object[] ids) {
		List<String> list = new ArrayList<String>();
		if (!StringUtils.isEmpty(ids)) {
			for (Object t : ids) {
				list.add(toString(t));
			}
		}
		return list;
	}

	/**
	 * 数组转成list
	 * @param ids
	 * @return List<String>
	 */
	public static List<String> parseList(List<Object> ids) {
		List<String> list = new ArrayList<String>();
		if (!StringUtils.isEmpty(ids)) {
			for (Object t : ids) {
				list.add(toString(t));
			}
		}
		return list;
	}

	/**
	 * 生成带图标的列
	 * @param icon 图标的样式名称
	 * @param column 列值
	 * @return 带图标的列值
	 */
	public static String iconColumn(String icon, String column) {
		return iconColumn(icon, column, 16);
	}

	/**
	 * 生成带图标的列
	 * @param icon 图标的样式名称
	 * @param column 列值
	 * @return 带图标的列值
	 */
	public static String iconColumn(String icon, String column, double width) {
		return "<div class=\"" + icon + "\" style='width:" + width + "px;height:16px;margin-right:2px;margin-top:-1px;float:left'></div>" + column;
	}

	/**
	 * 生成带图标的列
	 * @param icon 图标的样式名称
	 * @param column 列值
	 * @return 带图标的列值
	 */
	public static String iconColumnLast(String icon, String column) {
		return iconColumnLast(icon, column, 16);
	}

	/**
	 * 生成带图标的列
	 * @param icon 图标的样式名称
	 * @param column 列值
	 * @return 带图标的列值
	 */
	public static String iconColumnLast(String icon, String column, double with) {
		return column + "<div class=\"" + icon + "\" style='width:" + with + "px;height:16px;margin-right:2px;margin-top:-1px;float:right'></div>";
	}

	/**
	 * 生成带图标的列
	 * @param icon 图标的样式名称
	 * @param column 列值
	 * @param function the onclick function
	 * @return 带图标的列值
	 */
	public static String iconColumn(String icon, String column, String function) {
		return "<div class='" + icon + "' onclick=\"" + function + "\" style='width:16px;height:16px;margin-right:2px;margin-top:-1px;float:left;cursor:pointer;'></div>" + column;
	}

	/**
	 * 将字符串转换为日期
	 * @param time 日期字符
	 * @return 日期
	 */
	public static Date stringToDate(String time) {
		Date date = null;
		if (!StringUtils.isEmpty(time) && time.trim().length() >= 10) {
			time = time.trim();
			String format = "";
			if (time.indexOf("-") > -1) {
				format = "yyyy-MM-dd" + appendTime(time);
			} else if (time.indexOf("/") > -1) {
				format = "yyyy/MM/dd" + appendTime(time);
			} else {
				return date;
			}
			try {
				date = new SimpleDateFormat(format).parse(time, new ParsePosition(0));
			} catch (Exception e) {
				log.error("格式日期{}异常。", time);
			}
		}

		return date;
	}

	/**
	 * 将日期转换为字符串
	 * @param time the date
	 * @return the date String
	 */
	public static String stringFromDate(Object time) {
		if (time != null) {
			return stringFromDate(time, "yyyy-MM-dd");
		} else {
			return "";
		}
	}

	/**
	 * 将日期转换为字符串
	 * @param time time
	 * @return String of time
	 */
	public static String stringFromTime(Object time) {
		if (time != null) {
			return stringFromDate(time, "yyyy-MM-dd HH:mm:ss");
		} else {
			return "";
		}
	}

	/**
	 * 将日期转换为字符串
	 * @param time 时间
	 * @param pattern 时间格式
	 * @return the date String of the Pattern
	 */
	public static String stringFromDate(Object time, String pattern) {
		if (time != null) {
			return new SimpleDateFormat(pattern).format(time);
		} else {
			return "";
		}
	}

	/**
	 * 格式化数字
	 * @param d the double numbel
	 * @param pattern the pattern
	 * @return 格式化后的数字
	 */
	public static String decimalToString(double d, String pattern) {
		return new DecimalFormat(pattern).format(d);
	}

	/**
	 * 默认格式化double 类型的数字
	 * @param number the double type number
	 * @return 格式化后的数据
	 */
	public static String doubleFormat(double number) {

		return doubleFormat(number, "#0.00");
	}
	
	
	/**
	 * 格式化double后去除最后的0*
	 * @param number
	 * @return
	 */
	public static String doubleFormatShort(double number) {

		String str = doubleFormat(number, "#0.00000000");
		Pattern pattern = Pattern.compile("\\.{0,1}0*$");
		str = pattern.matcher(str).replaceAll("");
		if(str.length()== 0){
			str = "0";
		}
		return str;
	}

	/**
	 * 格式化数字
	 * @param number the double numbel
	 * @param fmt the pattern
	 * @return 格式化后的数字
	 */
	public static String doubleFormat(double number, String fmt) {
		return decimalToString(number, fmt);
	}

	/**
	 * 判断日期字符串的格式
	 * @param time time
	 * @return time
	 */
	private static String appendTime(String time) {
		String fmt = "";
		if (time.indexOf(":") > -1) {
			String[] ts = time.split(":");
			switch (ts.length) {
			case 1:
				fmt = " HH";
				break;
			case 2:
				fmt = " HH:mm";
				break;
			case 3:
				fmt = " HH:mm:ss";
				break;
			default:
				break;
			}
		} else if (time.indexOf(" ") > -1) {
			fmt = " HH";
		}

		return fmt;
	}

	/**
	 * 获取日期的年
	 * @param date date
	 * @return date of year
	 */
	public static String dateOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return toString(calendar.get(Calendar.YEAR));
	}

	/**
	 * 格式化日期到月
	 * @param date date
	 * @return yyyy.MM
	 */
	public static String dateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 格式化日期到季度
	 * @param date date
	 * @return yyyy.XX QT
	 */
	public static String dateOfQuarter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) / 3 + 1 + " Qt");
	}

	/**
	 * 获取当前日期是一年中的第几周
	 * @param date 判断日期
	 * @return 数值型字符，一年最后不满一周的日期算为今年最大周
	 */
	public static String weekOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = 0;
		if (calendar.get(Calendar.WEEK_OF_YEAR) == 1 & calendar.get(Calendar.MONTH) == 11) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
			week = calendar.get(Calendar.WEEK_OF_YEAR) + 1;
		} else {
			week = calendar.get(Calendar.WEEK_OF_YEAR);
		}
		return FormatUtil.toString(week);
	}

	/**
	 * 把集合按指定字段值分组返回成map
	 * @param list
	 * @param field
	 * @return childMap
	 */
	public static Map<Object, List<Map<String, Object>>> groupListMap(List<Map<String, Object>> list, String field) {
		Map<Object, List<Map<String, Object>>> childMap = new HashMap<Object, List<Map<String, Object>>>();
		List<Map<String, Object>> fieldList = null;
		if (!ObjectUtils.isEmpty(list) && !StringUtils.isEmpty(field)) {
			for (Map<String, Object> map : list) {
				Object value = map.get(field);
				fieldList = childMap.get(value);
				if (fieldList == null) {
					fieldList = new ArrayList<Map<String, Object>>();
					childMap.put(value, fieldList);
				}
				fieldList.add(map);
			}
		}
		return childMap;
	}

	/**
	 * 将对象集合转化为 以field作为键，对象作为值的 Map集合
	 * @param list
	 * @param field
	 * @return childMap
	 */
	public static Map<Object, Map<String, Object>> listToMap(List<Map<String, Object>> list, String field) {
		Map<Object, Map<String, Object>> childMap = new HashMap<Object, Map<String, Object>>();
		if (!ObjectUtils.isEmpty(list) && !StringUtils.isEmpty(field)) {
			for (Map<String, Object> map : list) {
				Object value = map.get(field);
				childMap.put(value, map);
			}
		}
		return childMap;
	}
	/**
	 * 获得随机数
	 * @return UUID
	 */
	public static String UUID() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * 移除Xss攻击字符
	 * @param value
	 * @return String
	 */
	public static String cleanXSS(String value) {
		if (!StringUtils.isEmpty(value)) {
			value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
			value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
			value = value.replaceAll("'", "& #39;");
			value = value.replaceAll("eval\\((.*)\\)", "");
			value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
			value = value.replaceAll("script", "");
		}
		return value;
	}

	/**
	 * 还原Xss攻击字符
	 * @param value
	 * @return String
	 */
	public static String restoreXSS(String value) {
		if (!StringUtils.isEmpty(value)) {
			value = value.replaceAll("& lt;", "<").replaceAll("& gt;", ">");
			value = value.replaceAll("& #40;", "(").replaceAll("& #41;", ")");
			value = value.replaceAll("& #39;", "'");
		}
		return value;
	}

	/**
	 * URL转码
	 *
	 * @param value
	 * @return
	 */
	public static String encode(String value) {
		return encode(value, "UTF-8");
	}

	/**
	 * URL转码
	 * @param value
	 * @param enc
	 * @return
	 */
	public static String encode(String value, String enc) {
		if (!StringUtils.isEmpty(value)) {
			try {
				value = URLEncoder.encode(value, enc);
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
		return value;
	}

	/**
	 * URL解码
	 * @param value
	 * @return
	 */
	public static String decode(String value) {
		return decode(value, "UTF-8");
	}

	/**
	 * URL解码
	 * @param value
	 * @param enc
	 * @return
	 */
	public static String decode(String value, String enc) {
		if (!StringUtils.isEmpty(value)) {
			try {
				value = URLDecoder.decode(value, enc);
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
		return value;
	}

	/**
	 * 将数字字符转化为数字，消除科学计数法，返回字符串
	 *
	 * @param numstr
	 *            数字字符串
	 * @return
	 */
	public static String BigDecToPlainString(String numstr)
	{
		BigDecimal dec = new BigDecimal(numstr);
		numstr = dec.toPlainString();
		return numstr;
	}

	/**
	 * 将数字字符转化为数字，保留scale位有效数字，并且消除科学计数法，返回字符串
	 *
	 * @param numstr
	 *            数字字符串
	 * @param scale
	 *            保留的有效数字位数
	 * @return
	 */
	public static String BigDecToPlainString(String numstr, int scale)
	{
		BigDecimal dec = new BigDecimal(numstr);
		numstr = dec.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
		return numstr;
	}
	
}
