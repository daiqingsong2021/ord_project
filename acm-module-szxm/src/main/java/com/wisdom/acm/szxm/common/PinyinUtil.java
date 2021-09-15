package com.wisdom.acm.szxm.common;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinyinUtil
{

    // 将汉字转换为拼音的全拼的大写，非汉字的原样输出
    public static StringBuilder getQuanPin(String str)
    {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++)
        {
            char s = str.charAt(i);

            /**
             * PinyinHelper.toHanyuPinyinStringArray说明：
             * 1、参数传中文，返回值是这个汉字的小写拼音+这个汉字是几声。比如：参数传 "汉 "，return的String[ ] 就是
             * [han4]
             * 2、当传字母时，返回值是null。
             * 注意：toHanyuPinyinStringArray接收的参数是char，意思就是说一次只能转换一个，
             * 比如“美”是string，toHanyuPinyinStringArray不能直接接收，
             * 每次只能传一个，返回的那个String数组里肯定只有一个元素。
             */
            String[] s1 = PinyinHelper.toHanyuPinyinStringArray(s);

            if (null == s1)
            {
                sb.append(s);
            }
            else
            {
                String up = s1[0].substring(0, s1[0].length() - 1);
                up = up.toLowerCase();
                sb.append(up);
            }
        }
        return sb;
    }

    public static void main(String args[])
    {
        System.out.println(PinyinUtil.getQuanPin("小鸡鸡").toString().compareTo(PinyinUtil.getQuanPin("王你好").toString()));;
    }

}
