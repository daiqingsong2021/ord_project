package com.wisdom.webservice.token;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Author：wqd
 * Date：2019-11-11 14:50
 * Description：<描述>
 */
public class RSASecurityUtils {
    /** 指定加密算法为RSA */
    private static final String ALGORITHM = "RSA";
    /** 密钥长度，注：只加密关键信息即可（过长字符串RSA加解密性能较差） */
    private static final int KEYSIZE = 1024;//1024、2048、3072、4096...长度无限制，越长性能越差安全性越高

    /**
     * 生成密钥对
     * @throws Exception
     */
    public static void generateKeyPair() throws Exception {
        // /**可信任的随机数源 */
        // SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        // keyPairGenerator.initialize(KEYSIZE, secureRandom);
        keyPairGenerator.initialize(KEYSIZE);//默认随机数源
        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();
        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();
        try {
            /** 密钥转字符串 */
            byte[] privateData=privateKey.getEncoded();
            String privateStr= Base64.encodeBase64String(privateData);
            byte[] publicData=publicKey.getEncoded();
            String publicStr=Base64.encodeBase64String(publicData);
            System.out.println("私钥："+privateStr);
            System.out.println("公钥："+publicStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 公钥加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String publicKey,String source) throws Exception {
        Key key=stringToPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return Base64.encodeBase64String(b1);
    }

    /**
     * 私钥加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String encryptPriKey(String privateKey,String source) throws Exception {
        Key key=stringToPrivateKey(privateKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return Base64.encodeBase64String(b1);
    }

    /**
     * 私钥解密算法
     * @param cryptograph 密文
     * @return
     * @throws Exception
     */
    public static String decrypt(String privateKey,String cryptograph) throws Exception {
        Key key=stringToPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 公钥解密算法
     * @param cryptograph 密文
     * @return
     * @throws Exception
     */
    public static String decryptPubKey(String publicKey,String cryptograph) throws Exception {
        Key key=stringToPublicKey(publicKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 密钥串转privateKey
     * @param keyStr
     * @return
     * @throws Exception
     */
    public static Key stringToPrivateKey(String keyStr) throws Exception {
        byte[] keyBytes=Base64.decodeBase64(keyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 密钥串转publicKey
     * @param keyStr
     * @return
     * @throws Exception
     */
    public static PublicKey stringToPublicKey(String keyStr) throws Exception {
        byte[] m = Base64.decodeBase64(keyStr);
        byte[] e = Base64.decodeBase64("AQAB");
        BigInteger b1 = new BigInteger(1, m);
        BigInteger b2 = new BigInteger(1, e);
        //公钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
        RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return pubKey;

    }

    public static void main(String[] args) throws Exception {
        String publicKey="phBFwcKZKtzH+gZJnjo57/qcMjWFtCIarXRE3p6+p66oHDBDR9sd5OwcygWolrifT/WOq298rzBrKZeuIulXVSSXKCtHRbCgOhkHra3LVJP0WVPHJYo0sX6r0EZ+wMGo/OWCwCcz0apa2irUTNKjHpsQWqsQx8o9ttNFI5Ttzfk=";
        System.out.println(encrypt(publicKey,"1111你好"));

    }
}
