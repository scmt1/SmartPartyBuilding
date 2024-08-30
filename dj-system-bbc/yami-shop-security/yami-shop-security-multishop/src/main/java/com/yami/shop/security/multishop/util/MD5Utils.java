package com.yami.shop.security.multishop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {


    /** * 加密 * @param context */
    public static String encrypByMd5(String context) {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(context.getBytes());//update处理
            byte [] encryContext = md.digest();//调用该方法完成计算

            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < encryContext.length; offset++) {
                //做相应的转化（十六进制）
                i = encryContext[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
           }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            return  null;
        }
    }

}
