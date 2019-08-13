package com.example.administrator.alshow.util;

import android.util.Log;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.logging.Logger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class ACDEncodeUtil {
	static int SALT_SIZE=8;

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.===========================
	 */
	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String entryptPassword(String plainPassword) {
		byte[] salt = generateSalt(SALT_SIZE);
		byte[] hashPassword = sha1(plainPassword.getBytes(),"SHA-1", salt, 1);
		return Hex.encodeHexString(salt)+ Hex.encodeHexString(hashPassword);
	}
	
	private static byte[] generateSalt(int numBytes) {
		byte[] bytes = new byte[numBytes];
		SecureRandom random=new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
	}
	
	private static byte[] sha1(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt;
		try {
			salt = Hex.decodeHex(password.substring(0,16).toCharArray());
			byte[] hashPassword = sha1(plainPassword.getBytes(),"SHA-1", salt, 1024);
			return password.equals(new String(Hex.encodeHex(salt))+new String(Hex.encodeHex(hashPassword)));
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
