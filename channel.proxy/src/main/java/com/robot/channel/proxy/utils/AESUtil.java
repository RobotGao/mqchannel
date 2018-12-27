package com.robot.channel.proxy.utils;

import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @version V1.0
 * @desc AES 加密工具类
 */
public class AESUtil {

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

	/**
	 * AES 加密操作
	 *
	 * @param content
	 *            待加密内容
	 * @param password
	 *            加密密码
	 * @return 返回Base64转码后的加密数据
	 */
	public static byte[] encrypt(byte[] content, String password) {
		try {
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
			byte[] result = cipher.doFinal(content);// 加密
			return result;
		} catch (Exception ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	/**
	 * AES 解密操作
	 *
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
			// 执行操作
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception ex) {
			Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * 生成加密秘钥
	 *
	 * @return
	 * @throws Exception 
	 */
	 /**
	private static SecretKeySpec getSecretKey(final String key) throws Exception {
		  if (null == key || key.length() == 0) {
	            throw new NullPointerException("key not is null");
	        }
	        SecretKeySpec key2 = null;
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	        random.setSeed(key.getBytes());
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");
	            kgen.init(128, random);
	            SecretKey secretKey = kgen.generateKey();
	            byte[] enCodeFormat = secretKey.getEncoded();
	            key2 = new SecretKeySpec(enCodeFormat, "AES");
	        } catch (NoSuchAlgorithmException ex) {
	            throw new NoSuchAlgorithmException();
	        }
	        return key2;

	}
	*/
	
	/**
     * 生成加密秘钥
     *
     * @return
     * @throws Exception
     */
	private static SecretKeySpec getSecretKey(final String password) throws Exception {
        if (null == password || password.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key2 = null;
        int iterationCount = 1000;
        int keyLength = 128;
        byte[] salt = password.getBytes();
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        key2 = new SecretKeySpec(keyBytes, "AES");
        return key2;
    }
	
	

//	public static void main(String[] args) {
//		String s = "hello,您好";
//		String password = "123";
//		System.out.println("s:" + s);
//		byte[] s1 = AESUtil.encrypt(s.getBytes(), password);
//		System.out.println("s1:" + new String(s1));
//		byte[] s2 = AESUtil.decrypt(s1, password);
//		System.out.println("s2:" + new String(s2));
//	}

}
