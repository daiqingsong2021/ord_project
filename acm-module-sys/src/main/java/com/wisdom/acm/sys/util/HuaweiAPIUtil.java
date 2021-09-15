package com.wisdom.acm.sys.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
public class HuaweiAPIUtil {

    public static boolean verifyRequestParams(javax.servlet.http.HttpServletRequest
                                                      request,
                                              String accessKey, int encryptLength) {
//解析出url内容
        Map<String, String[]> paramsMap = Maps.newHashMap(request.getParameterMap());
        String timeStamp = null;
        String authToken = null;
        String[] timeStampArray = paramsMap.get("timeStamp");
        if (null != timeStampArray && timeStampArray.length > 0) {
            timeStamp = timeStampArray[0];
        }
        String[] authTokenArray = paramsMap.remove("authToken");
        if (null != authTokenArray && authTokenArray.length > 0) {
            authToken = authTokenArray[0];
        }
//对剩下的参数进行排序，拼接成加密内容
        Map<String, String[]> sortedMap = new TreeMap<String, String[]>();
        sortedMap.putAll(paramsMap);
        StringBuffer strBuffer = new StringBuffer();
        Set<String> keySet = sortedMap.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = sortedMap.get(key)[0];
            strBuffer.append("&").append(key).append("=").append(value);
        }
//修正消息体,去除第一个参数前面的&
        String reqParams = strBuffer.toString().substring(1);
        String key = accessKey + timeStamp;
        String signature = null;
        try {
            signature = generateResponseBodySignature(key, reqParams);
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | IllegalStateException | UnsupportedEncodingException e) {
// TODO Auto-generated catch block
        }
        return authToken.equals(signature);
    }

    /**
     * 生成http响应消息体签名示例Demo
     *
     * @param key  用户在isv console分配的accessKey，请登录后查看
     * @param body http响应的报文
     * @return 加密结果
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     */
    public static String generateResponseBodySignature(String key, String body)
            throws InvalidKeyException, NoSuchAlgorithmException,
            IllegalStateException, UnsupportedEncodingException {
        return base_64(hmacSHA256(key, body));
    }

    /**
     * hamcSHA256加密算法
     *
     * @param macKey  秘钥key
     * @param macData 加密内容-响应消息体
     * @return 加密密文
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     */
    public static byte[] hmacSHA256(String macKey, String macData)
            throws NoSuchAlgorithmException, InvalidKeyException,
            IllegalStateException, UnsupportedEncodingException {
        SecretKeySpec secret =
                new SecretKeySpec(macKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secret);
        byte[] doFinal = mac.doFinal(macData.getBytes("UTF-8"));
        return doFinal;
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String base_64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * 对资源开通后，返回的用户名和密码进行加密
     *
     * @param key           秘钥
     * @param str           原文
     * @param encryptLength 加密长度
     * @return 加密结果
     */
    public static String generateSaaSUsernameOrPwd(String key, String str, int
            encryptLength) {
        String iv = getRandomChars(16);
        String afterEncryptStr = "";
        try {
            log.error(str+" "+key+" "+iv+" "+encryptLength);
            afterEncryptStr = encryptAESCBCEncode(str, key, iv, encryptLength);
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage());
        }
        System.out
                .println(afterEncryptStr);
        return iv + afterEncryptStr;
    }

    /**
     * 随机生成字符串
     *
     * @param length 随机字符串的长度
     * @return 随机字符串
     */
    public static String getRandomChars(int length) {
        String randomChars = "";
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
//字母和数字中随机
            if (random.nextInt(2) % 2 == 0) {
//输出是大写字母还是小写字母
                int letterIndex = random.nextInt(2) % 2 == 0 ? 65 : 97;
                randomChars += (char) (random.nextInt(26) + letterIndex);
            } else {
                randomChars += String.valueOf(random.nextInt(10));
            }
        }
        return randomChars;
    }

    /**
     * AES CBC 位加密
     *
     * @param content       加密内容
     * @param key           加密秘钥
     * @param iv            向量iv
     * @param encryptLength 仅支持128、 256长度
     * @return 加密结果
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String encryptAESCBCEncode(String content, String key,
                                             String iv, int encryptLength)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(key)
                || StringUtils.isEmpty(iv)) {
            return null;
        }
        return base_64(
                encryptAESCBC(content.getBytes(), key.getBytes(), iv.getBytes(), encryptLength));
    }

    /**
     * AES CBC 256位加密
     *
     * @param content       加密内容字节数组
     * @param keyBytes      加密字节数组
     * @param iv            加密向量字节数组
     * @param encryptLength 仅支持128、 256长度
     * @return 解密后字节内容
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] encryptAESCBC(byte[] content, byte[] keyBytes,
                                       byte[] iv, int encryptLength)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keyBytes);
        keyGenerator.init(encryptLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] result = cipher.doFinal(content);
        return result;
    }
}
