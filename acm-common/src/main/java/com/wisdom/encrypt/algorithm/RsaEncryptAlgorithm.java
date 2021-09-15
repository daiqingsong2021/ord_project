package com.wisdom.encrypt.algorithm;


import com.wisdom.encrypt.util.RSAUtils;
import org.springframework.stereotype.Component;


/**
 * 自定义RSA算法
 *
 * @date 2019-01-12
 * 
 * @about
 *
 */
//@Component
public class RsaEncryptAlgorithm implements EncryptAlgorithm {

	public String encrypt(String content, String encryptKey) throws Exception {
		return RSAUtils.encryptByPublicKey(content);
	}

	public String decrypt(String encryptStr, String decryptKey) throws Exception {
		return RSAUtils.decryptByPrivateKey(encryptStr);
	}

//	public static void main(String[] args) throws Exception {
//		EncryptAlgorithm encryptAlgorithm = new AesEncryptAlgorithm();
//		String aa = encryptAlgorithm.encrypt("{\"id\":1,\"name\":\"加密实体对象\"}","abcdef0123456789");
//		System.out.println(aa);
//	}

}
