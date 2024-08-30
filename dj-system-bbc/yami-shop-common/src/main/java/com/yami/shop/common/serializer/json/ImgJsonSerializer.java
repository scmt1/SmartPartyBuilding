/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.serializer.json;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yami.shop.common.bean.Domain;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.util.CacheManagerUtil;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.SpringContextUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片加上前缀
 * @author yami
 */
@Component
public class ImgJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        CacheManagerUtil cacheManagerUtil = SpringContextUtils.getBean(CacheManagerUtil.class);
        if (StrUtil.isBlank(value)) {
            gen.writeString(StrUtil.EMPTY);
            return;
        }
        String[] imgs = value.split(StrUtil.COMMA);
        StringBuilder sb = new StringBuilder();
        if (ArrayUtil.isEmpty(imgs)) {
            gen.writeString(sb.toString());
            return;
        }

        String rule="^((http[s]{0,1})://)";
        Pattern pattern= Pattern.compile(rule);

        Domain domain = cacheManagerUtil.getCache("SysConfigObject", Constant.DOMAIN_CONFIG);
        if (domain == null) {
            SysConfig config = SpringContextUtils.getBean(SqlSession.class).selectOne("com.yami.shop.dao.SysConfigMapper.queryByKey", Constant.DOMAIN_CONFIG);
            domain = Json.parseObject(config.getParamValue(),Domain.class);
            cacheManagerUtil.putCache("SysConfigObject", Constant.DOMAIN_CONFIG, domain);
        }

        for (String img : imgs) {
            Matcher matcher=pattern.matcher(img);
            //若图片以http或https开头，直接返回
            if (matcher.find()){
                sb.append(img).append(StrUtil.COMMA);
            }else {
                sb.append(domain.getResourcesDomainName()).append("/").append(img).append(StrUtil.COMMA);
            }
        }
        sb.deleteCharAt(sb.length()-1);
        gen.writeString(sb.toString());
    }
}
