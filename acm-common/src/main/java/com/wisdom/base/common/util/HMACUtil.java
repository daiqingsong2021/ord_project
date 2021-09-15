package com.wisdom.base.common.util;



import com.wisdom.base.common.enums.HMACEnum;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//密码加密
public class HMACUtil {

    public static String getHMAC(String data, String key) {
        String result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMACEnum.HMAC_SHA1_ALGORITHM.getType());
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance( HMACEnum.HMAC_SHA1_ALGORITHM.getType());
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = HMACUtil.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return result;
        } else {
            return null;
        }
    }


    private static String encodeBase64(byte[] binaryData) {
        return Base64.getEncoder().encodeToString(binaryData);
    }

    private static byte[] decodeBase64(String encoded){
        return Base64.getDecoder().decode(encoded);
    }

}
