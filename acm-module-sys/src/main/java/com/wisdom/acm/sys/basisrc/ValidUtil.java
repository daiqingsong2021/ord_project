package com.wisdom.acm.sys.basisrc;

import java.util.Collection;
import java.util.List;

public class ValidUtil {
    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(Iterable<?> list) {
        return list == null || !list.iterator().hasNext();
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(Number[] list) {
        return list == null || list.length == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(String[] list) {
        return list == null || list.length == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(int[] list) {
        return list == null || list.length == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(float[] list) {
        return list == null || list.length == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(double[] list) {
        return list == null || list.length == 0;
    }

    /**
     * @param list list
     * @return the list is Empty
     */
    public static boolean isEmpty(long[] list) {
        return list == null || list.length == 0;
    }

    /**
     *
     * @param os
     * @return boolean
     */
    public static boolean isEmpty(Object[] os) {
        return os == null || os.length == 0;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 为空返回true
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str)  ? true : false;
    }

//    /**
//     * 判断对象是否为NULL
//     *
//     * @param obj
//     * @return boolean
//     */
//    public static boolean isEmpty(Object obj) {
//        return obj == null || isEmpty(FormatUtil.toString(obj));
//    }

}
