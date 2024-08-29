package me.flyray.bsin.server.utils.jcxf;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;


import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class EncryptUtils {

//	public static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
//			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//
//	public static String toHexString(final byte[] bs) {
//		final int len;
//		if (bs != null && (len = bs.length) != 0) {
//			final char[] cs = new char[len << 1];
//			final char[] myDigits = DIGITS;
//			byte b;
//			for (int i = 0, j = 0; i < len; i++) {
//				cs[j++] = myDigits[((b = bs[i]) >>> 4) & 0xF];
//				cs[j++] = myDigits[b & 0xF];
//			}
//			return String.valueOf(cs);
//		}
//		return null;
//	}
//
//	private static String digest(String name, String source)
//			throws NoSuchAlgorithmException {
//		if (source != null) {
//			final MessageDigest md = MessageDigest.getInstance(name);
//			md.update(source.getBytes());
//			return toHexString(md.digest());
//		}
//		return null;
//	}
//
//	private static String digest(String name, byte[] bytes)
//			throws NoSuchAlgorithmException {
//		if (bytes != null) {
//			final MessageDigest md = MessageDigest.getInstance(name);
//			md.update(bytes);
//			return toHexString(md.digest());
//		}
//		return null;
//	}
//
//	public static String SHA1_HEX(String source) {
//		try {
//			return digest("SHA-1", source);
//		} catch (NoSuchAlgorithmException ignore) {
//		}
//		return null;
//	}
//
//	public static String SHA1_HEX(byte[] bytes) {
//		try {
//			return digest("SHA-1", bytes);
//		} catch (NoSuchAlgorithmException bytesz) {
//		}
//		return null;
//	}
//
//	public static String MD5_HEX(String source) {
//		try {
//			return digest("MD5", source);
//		} catch (NoSuchAlgorithmException ignore) {
//		}
//		return null;
//	}
//
//	public static String MD5_HEX(byte[] bytes) {
//		try {
//			return digest("MD5", bytes);
//		} catch (NoSuchAlgorithmException ignore) {
//		}
//		return null;
//	}
//
//	public static String uuid(){
//		String uuid = UUID.randomUUID().toString();
//		return uuid.replaceAll("-", "");
//	}
//
//	/**
//     * base64进制加密
//     *
//     * @param password
//     * @return
//     */
//    public static String encrytBase64(String password) {
//        byte[] bytes = password.getBytes();
//        return Base64.encodeToString(bytes);
//    }
//    /**
//     * base64进制解密
//     * @param cipherText
//     * @return
//     */
//    public static String decryptBase64(String cipherText) {
//        return Base64.decodeToString(cipherText);
//    }
//    /**
//     * 16进制加密
//     *
//     * @param password
//     * @return
//     */
//    public static String encrytHex(String password) {
//        byte[] bytes = password.getBytes();
//        return Hex.encodeToString(bytes);
//    }
//    /**
//     * 16进制解密
//     * @param cipherText
//     * @return
//     */
//    public static String decryptHex(String cipherText) {
//        return new String(Hex.decode(cipherText));
//    }
//
//    public static String generateKey()
//    {
//        AesCipherService aesCipherService=new AesCipherService();
//        Key key=aesCipherService.generateNewKey();
//        return Base64.encodeToString(key.getEncoded());
//    }
//    /**
//     *
//     * @param password 密码
//     * @param username 用户名
//     * @param hashIterations 迭代次数
//     * @return
//     */
//    public static String md5(String password,String username,int hashIterations){
//    	return new Md5Hash(password, username, hashIterations).toBase64();
//    }
//
//	/**
//	 * SM4对称加密
//	 * 转换16进制
//	 * @param sourceData
//	 * @return
//	 */
//	public static String Sm4Encrypt(String key, String sourceData){
//		SM4 sm4 = SmUtil.sm4(key.getBytes());
//		return HexUtil.encodeHexStr(sm4.encrypt(sourceData));
//	}
//
//	/**
//	 * SM4对称解密
//	 * @param encryptStr
//	 * @return
//	 */
//	public static String Sm4Decrypt(String key, String encryptStr){
//		SM4 sm4 = SmUtil.sm4(key.getBytes());
//		return sm4.decryptStr(encryptStr);
//	}

}
