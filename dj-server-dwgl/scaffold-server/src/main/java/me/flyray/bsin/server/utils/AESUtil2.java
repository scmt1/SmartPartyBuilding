package me.flyray.bsin.server.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.flyray.bsin.server.BsinScaffoldApplication;
import org.springframework.boot.SpringApplication;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil2 {
    private static final String defaultV = "6859505890402435";
    private static final String key = "1061697007556132";

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;
    }

    /**
     * 加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) {
        try {
            if (StringUtils.isNotBlank(content)) {
                final Base64.Encoder encoder = Base64.getEncoder();
                SecretKeySpec skeySpec = getKey(key);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(defaultV.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                byte[] encrypted = cipher.doFinal(content.getBytes());
                String encodedText = encoder.encodeToString(encrypted);
                return encodedText;
            }
            return content;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(String content) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            SecretKeySpec skeySpec = getKey(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(defaultV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] base64 = decoder.decode(content);
            byte[] original = cipher.doFinal(base64);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            return content;
        }
    }
}
