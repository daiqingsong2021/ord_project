package com.wisdom.webservice.token;

import java.security.MessageDigest;

public class Md5Util {

    /**
     * 将普通字符串用md5加密，并转化为32进制字符串 大写输出
     * @param s
     * @return
     */
    public static String StringInMd5(String s) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };

        return md5Encrypt32(s, hexDigits);
    }

    /**
     * 将普通字符串用md5加密，并转化为32进制字符串 小写输出
     * @param s
     * @return
     */
    public static String StringInMd5Lower(String s) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5',
               '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        return md5Encrypt32(s, hexDigits);
    }

    private static String md5Encrypt32(String s, char[] hexDigits) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
