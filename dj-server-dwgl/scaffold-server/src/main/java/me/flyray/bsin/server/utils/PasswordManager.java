/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.utils;

import cn.hutool.crypto.symmetric.AES;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.common.exception.YamiShopBindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author 菠萝凤梨
 * @date 2022/1/19 16:02
 */
@Component
public class PasswordManager {
    private static final Logger logger = LoggerFactory.getLogger(PasswordManager.class);

    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.password.signKey:-mall4j-password}")
    public String passwordSignKey;

    public String decryptPassword(String data) {
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptPassword;
        try {
            decryptStr = aes.decryptStr(data);
            // decryptPassword = decryptStr.substring(13);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"解密错误！");
        }
        return decryptStr;
    }


    public String encryptPassword(String data) {
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptPassword;
        try {
            //byte[] encrypt = aes.encrypt(data);
            //decryptPassword=Base64Utils.encodeToString(encrypt);
            decryptPassword = aes.encryptBase64(data);
        } catch (Exception e) {
            logger.error("Exception:", e);
//            throw new YamiShopBindException("AES加密错误", e);
            throw e;

        }

        return decryptPassword;
    }
}
