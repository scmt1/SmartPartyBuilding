/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.config;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取IP
 * @author yami
 */
@Slf4j
@Configuration
public class Ip2RegionConfig {

    @Bean
    public DbSearcher dbSearcher() throws DbMakerConfigException, IOException {
        DbConfig dbConfig = new DbConfig();

        InputStream inputStream = Ip2RegionConfig.class.getResourceAsStream("/ip2region.db");

        return new DbSearcher(dbConfig, toByteArray(inputStream));
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (Exception e) {
            log.error("Exception:", e);
        } finally {
            input.close();
        }
        return output.toByteArray();
    }
}
