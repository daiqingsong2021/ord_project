package com.wisdom.encrypt.algorithm;

import com.wisdom.encrypt.util.AesEncryptUtils;

/**
 * Aes加密算法实现
 * 

 * 
 * @date 2019-01-12
 * 
 * @about
 *
 */
public class AesEncryptAlgorithm implements EncryptAlgorithm {

	@Override
	public String encrypt(String content, String encryptKey) throws Exception {
		return AesEncryptUtils.aesEncrypt(content, encryptKey);
	}

	@Override
	public String decrypt(String encryptStr, String decryptKey) throws Exception {
		return AesEncryptUtils.aesDecrypt(encryptStr, decryptKey);
	}

//	public static void main(String[] args) throws Exception {
//		AesEncryptAlgorithm aesEncryptAlgorithm =new AesEncryptAlgorithm();
//		String aa = aesEncryptAlgorithm.encrypt("{id:1,name:\"尹吉欢\"}","wisdom!QAZ!@#123");
//		System.out.println(aa);
//	}

}
