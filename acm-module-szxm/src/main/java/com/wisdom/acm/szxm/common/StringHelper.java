package com.wisdom.acm.szxm.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.springframework.util.ObjectUtils;

public class  StringHelper
{

    public static Boolean isNullAndEmpty(String str)
    {
        return !(isNotNullAndEmpty(str));
    }

    public static Boolean isNotNullAndEmpty(String str)
    {
        Boolean b = false;
        if (str != null && !"".equals(str) && !"null".equals(str) && !"undefined".equals(str))
        {
            b = true;
        }

        return b;
    }
   
    /**
     * 
     * getFromIndex:(子字符串modelStr在字符串str中第count次出现时的下标). <br/>
     *
     * @author wyf
     * @param str 字符串str
     * @param modelStr 子字符串
     * @param count 第count次出现
     * @return  子字符串modelStr在字符串str中第count次出现时的下标
     * @since JDK 1.6
     */
    public static int getFromIndex(String str, String modelStr, Integer count)
    {
        // 对子字符串进行匹配
        java.util.regex.Matcher slashMatcher = java.util.regex.Pattern.compile(modelStr).matcher(str);
        int index = 0;
        while (slashMatcher.find())
        {
            index++;
            // 当modelStr字符第count次出现的位置
            if (index == count)
            {
                break;
            }
        }
        return slashMatcher.start();

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

    public static String formattString(Object str)
    {
        if(ObjectUtils.isEmpty(str))
            return "";
        if(isNullAndEmpty(String.valueOf(str)))
            return "";
        else return String.valueOf(str);
    }

}
